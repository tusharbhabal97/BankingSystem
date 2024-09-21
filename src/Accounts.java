import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private Connection connection;
    private Scanner scanner;
    public Accounts(Connection connection, Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }

    public long open_account(String email){
        if (!account_exist(email)){
            String open_account_query="insert into accounts(account_number,full_name,email,balance,security_pin) values(?,?,?,?,?)";
            scanner.nextLine();
            System.out.println("Enter full name: ");
            String full_name=scanner.nextLine();
            System.out.println("Enter Initial amount: ");
            double balance=scanner.nextDouble();
            scanner.nextLine();
            System.out.println("Enter Security Pin: ");
            String security_pin=scanner.nextLine();
            try {
                long account_number=generate_account_number();
                PreparedStatement preparedStatement=connection.prepareStatement(open_account_query);
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,full_name);
                preparedStatement.setString(3,email);
                preparedStatement.setDouble(4,balance);
                preparedStatement.setString(5,security_pin);
                int affectedRows=preparedStatement.executeUpdate();
                if (affectedRows>0){
                    return account_number;
                }else {
                    throw new RuntimeException("Account creation failed ");
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Account already exist ");
    }


    private long generate_account_number(){
        try {
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery("select account_number from accounts order by account_number desc limit 1");
            if (resultSet.next()){
                long last_account_number=resultSet.getLong("account_number");
                return last_account_number+1;
            }else {
                return 10000100;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 10000100;
    }


    public long get_account_number(String email){
        String query="select account_number from accounts where email=?";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getLong("account_number");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        throw new RuntimeException("Account number doesn't exist ");
    }



    public boolean account_exist(String email){
        String query="select account_number from accounts where email=?";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next()){
                return true;
            }else {
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
