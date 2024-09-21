import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    private Connection connection;
    private Scanner scanner;

    AccountManager(Connection connection, Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }


    public void debit_money(long account_number) throws SQLException {
        scanner.nextLine();
        System.out.println("Enter Amount: ");
        double amount=scanner.nextDouble();
        System.out.println("Enter Security Pin: ");
        String security_pin=scanner.nextLine();
        try {
            connection.setAutoCommit(false);
            if (account_number!=0){
                String query="select * from accounts where account_number=? and security_pin=?";
                PreparedStatement preparedStatement=connection.prepareStatement(query);
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    double current_balance=resultSet.getDouble("balance");
                    if (amount<=current_balance){
                        String debit_query="update accounts set balance= balance - ? where account_number=?";
                        PreparedStatement preparedStatement1=connection.prepareStatement(debit_query);
                        preparedStatement1.setDouble(1,amount);
                        preparedStatement1.setLong(2,account_number);
                        int affected_rows=preparedStatement.executeUpdate();
                        if (affected_rows>0){
                            connection.commit();
                            System.out.println("Rs. "+amount+" debited successfully");
                            connection.setAutoCommit(true);
                            return;
                        }else {
                            System.out.println("Transaction Failed ");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else {
                        System.out.println("Insufficient Balance ");
                    }
                }else {
                    System.out.println("Invalid PIN ");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }


    public void credit_money(long account_number) throws SQLException {
        scanner.nextLine();
        System.out.println("Enter Amount: ");
        double amount=scanner.nextDouble();
        System.out.println("Enter Security Pin: ");
        String security_pin=scanner.nextLine();
        try {
            connection.setAutoCommit(false);
            if (account_number!=0){
                String query="select * from accounts where account_number=? and security_pin=?";
                PreparedStatement preparedStatement=connection.prepareStatement(query);
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    double current_balance=resultSet.getDouble("balance");
                    if (resultSet.next()){
                        String credit_query="update accounts set balance= balance + ? where account_number=?";
                        PreparedStatement preparedStatement1=connection.prepareStatement(credit_query);
                        preparedStatement1.setDouble(1,amount);
                        preparedStatement1.setLong(2,account_number);
                        int affected_rows=preparedStatement.executeUpdate();
                        if (affected_rows>0){
                            connection.commit();
                            System.out.println("Rs. "+amount+" credited successfully");
                            connection.setAutoCommit(true);
                            return;
                        }else {
                            System.out.println("Transaction Failed ");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else {
                        System.out.println("Insufficient Balance ");
                    }
                }else {
                    System.out.println("Invalid PIN ");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }



    public void transfer_money(long sender_account_number){
        scanner.nextLine();
        System.out.println("Enter Receiver account number: ");
        long receiver_account_number=scanner.nextLong();
        System.out.println("Enter Amount: ");
        double amount=scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter security pin: ");
        String security_pin=scanner.nextLine();
        try {
            connection.setAutoCommit(false);
            if (sender_account_number!=0 && receiver_account_number!=0){
                String check_pin_query="select * from accounts where account_number=? and security_pin=?";
                PreparedStatement preparedStatement=connection.prepareStatement(check_pin_query);
                preparedStatement.setLong(1,sender_account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet=preparedStatement.executeQuery();
                if (resultSet.next()){
                    double current_balance=resultSet.getDouble("balance");
                    if (amount<=current_balance){
                        String debit_query="update accounts set balance=balance-? where account_number=?";
                        String credit_query="update accounts set balance=balance+? where account_number=?";
                        PreparedStatement debitPreparedStatement=connection.prepareStatement(debit_query);
                        PreparedStatement creditPreparedStatement=connection.prepareStatement(credit_query);

                        preparedStatement.setDouble(1,amount);
                        preparedStatement.setLong(2,sender_account_number);
                        preparedStatement.setDouble(1,amount);
                        preparedStatement.setLong(2,receiver_account_number);

                        int affectedRows1=preparedStatement.executeUpdate();
                        int affectedRows2=preparedStatement.executeUpdate();
                        if (affectedRows1>0 && affectedRows2>0){
                            connection.commit();
                            System.out.println("Transaction Successful ");
                            System.out.println("Rs. "+amount+ " Transferred Successfully ");
                            connection.setAutoCommit(true);
                            return;
                        }else {
                            System.out.println("Transaction Failed ");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else {
                        System.out.println("Insufficient Balance ");
                    }
                }else {
                    System.out.println("Invalid PIN ");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
//        connection.setAutoCommit(true);
    }

    public void get_balance(long account_number){
        scanner.nextLine();
        System.out.println("Enter Security PIN: ");
        String security_pin=scanner.nextLine();
        try {
            String query = "select balance from accounts where account_number=? and security_pin=?";
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setLong(1,account_number);
            preparedStatement.setString(2,security_pin);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next()){
                double balance=resultSet.getDouble("balance");
                System.out.println("Balance "+balance);
            }else {
                System.out.println("Invalid PIN ");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }



}




