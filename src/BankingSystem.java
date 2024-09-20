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

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
