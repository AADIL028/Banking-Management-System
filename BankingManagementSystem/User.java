package BankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Scanner;

public class User {
    private Connection connection;
    private Scanner scanner;
    public User(Connection con, Scanner sc) {
        this.connection = con;
        this.scanner = sc;
    }

    public void register(){
        // get details from user to register as new user.
        scanner.nextLine();
        System.out.print("Enter the name :- ");
        String name = scanner.next();
        System.out.print("Enter Email :- ");
        String email = scanner.next();
        System.out.print("Enter password :- ");
        String password = scanner.next();
        //encrypt the password to store in database.
        byte[] enc_pass = Base64.getEncoder().encode(password.getBytes());
        //check for the email is already exist ?...
        if(user_exist(email)){
            System.out.println("User already exist for this email address!!");
            return;
        }
        try {
            //insert details of user in user table of database.
            String redQuery = "INSERT INTO user (name,email,password) VALUES (?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(redQuery);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,new String(enc_pass));
            int regResult = preparedStatement.executeUpdate();
            //check if registration query is executed successful or not.
            if(regResult>0){
                System.out.println("Registration successful.");
            }
            else {
                System.out.println("Registration failed.");
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public String login(){
        // get email and password from user to register as new user.
        scanner.nextLine();
        System.out.print("Enter email :- ");
        String email = scanner.next();
        System.out.print("Enter password :- ");
        String password = scanner.next();
        try {
            //verify email and password from db.
            String loginQuery = "SELECT * FROM user WHERE email = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(loginQuery);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            //check email
            if(resultSet.next()){
                String dbPass = resultSet.getString("password");
                //decrypt password and check for valid password.
                byte[] byte_dec_pass = Base64.getDecoder().decode(dbPass);
                String dec_pass = new String(byte_dec_pass);
                //check password.
                if(dec_pass.equals(password)){
                    return email;
                } else {
                    return null;
                }
            }
            else {
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public boolean user_exist(String email){
        try {
            //get details from db to verify email is exist or not.
            String query = "SELECT * FROM user WHERE email = ?";
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
