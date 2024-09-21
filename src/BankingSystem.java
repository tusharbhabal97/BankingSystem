import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class  BankingSystem{
    private static final String url="jdbc:mysql://localhost:3306/banking_system";
    private static final String userName="root";
    private static final String password="tushar123456";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            Connection connection= DriverManager.getConnection(url,userName,password);
            System.out.println("Connection Successful");
            Scanner scanner=new Scanner(System.in);
            User user=new User(connection,scanner);
            Accounts accounts=new Accounts(connection,scanner);
            AccountManager accountManager=new AccountManager(connection,scanner);

            String email;
            long account_number;

            while (true){
                System.out.println("Welcome to Banking System ");
                System.out.println();
                System.out.println("1. Register ");
                System.out.println("2. Login ");
                System.out.println("3. Exit ");
                System.out.println("Enter your choice: ");
                int choice1=scanner.nextInt();
                switch (choice1){
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email=user.login();
                        if (email!=null){
                            System.out.println();
                            System.out.println("User Logged In ");
                            if (!accounts.account_exist(email)){
                                System.out.println();
                                System.out.println("1.Open a new bank Account ");
                                System.out.println("2.Exit ");
                                if (scanner.nextInt()==1){
                                    account_number=accounts.open_account(email);
                                    System.out.println("Account Created Successfully ");
                                    System.out.println("Your Account Number is "+account_number);
                                }else {
                                    break;
                                }
                            }account_number=accounts.get_account_number(email);
                            int choice2=0;
                            while (choice2!=5){
                                System.out.println();
                                System.out.println("1.Debit Money ");
                                System.out.println("2.Credit Money ");
                                System.out.println("3.Transfer Money ");
                                System.out.println("4.Check Balance ");
                                System.out.println("5. Log Out ");
                                choice2=scanner.nextInt();
                                switch (choice2){
                                    case 1:
                                        accountManager.debit_money(account_number);
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.get_balance(account_number);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter Valid Choice ");
                                        break;
                                }
                            }
                        }else {
                            System.out.println("Incorrect email or password ");
                        }

                    case 3:
                        System.out.println("Thank You for using our Banking System ");
                        System.out.println("Exiting System ");
                        return;
                    default:
                        System.out.println("Enter Valid Choice ");
                        break;
                }
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
