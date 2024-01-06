package BankingManagementSystem;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Accounts{
    private Connection connection;
    private Scanner scanner;
    public Accounts(Connection con, Scanner sc) {
        this.connection = con;
        this.scanner = sc;
    }

    public long openAccount(String email){
        if(!account_exist(email)){
            String query = "INSERT INTO accounts (acc_no,name,email,balance,security_pin) VALUES (?,?,?,?,?);";
            scanner.nextLine();
            System.out.print("Enter name :- ");
            String name = scanner.nextLine();
            System.out.print("Enter Initial amount :- ");
            double balance = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Enter security pin :- ");
            String pin = scanner.nextLine();
            try {
                long acc_no = generateAccountNumber();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1,acc_no);
                preparedStatement.setString(2,name);
                preparedStatement.setString(3,email);
                preparedStatement.setDouble(4,balance);
                preparedStatement.setString(5,pin);
                int rowsAffected = preparedStatement.executeUpdate();
                if(rowsAffected>0){
                    return acc_no;
                }
                else {
                    throw new RuntimeException("Account creation failed!!");
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Account already exist!!!");
    }

    public long getAccountNumber(String email){
        String query = "SELECT acc_no FROM accounts WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getLong("acc_no");
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Account number doesn't exist!!");
    }

    public long generateAccountNumber(){
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT acc_no FROM accounts ORDER BY acc_no DESC LIMIT 1;");
            if(resultSet.next()){
                long last_acc_no = resultSet.getLong("acc_no");
                return last_acc_no+1;
            } else {
                return 1000100;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return 1000100;
    }

    public boolean account_exist(String email){
        String query = "SELECT acc_no FROM accounts WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return true;
            }
            else {
                return false;
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}