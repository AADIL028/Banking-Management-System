package BankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    private Connection  connection;
    private Scanner scanner;
    public AccountManager(Connection con, Scanner sc) {
        this.connection = con;
        this.scanner = sc;
    }

    public void DebitMoney(long acc_no) throws SQLException {
        //get amount to debit and security pin from user.
        scanner.nextLine();
        System.out.print("Enter amount :- ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security pin :- ");
        String pin = scanner.nextLine();

        try{
            //set auto commit false to handle transaction control.
            connection.setAutoCommit(false);
            //check for valid account no.
            if(acc_no!=0) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE acc_no = ? AND security_pin=?");
                preparedStatement.setLong(1,acc_no);
                preparedStatement.setString(2,pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                //verify that account number and security pin is valid or not.
                if(resultSet.next()){
                    double balance = resultSet.getLong("balance");
                    //check for sufficient balance is available to debit money.
                    if(amount<=balance){
                        //perform update operation to update balance in db.
                        String debitQuery = "UPDATE accounts SET balance = balance - ? where acc_no = ?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(debitQuery);
                        preparedStatement1.setDouble(1,amount);
                        preparedStatement1.setLong(2,acc_no);
                        int rowsAffected = preparedStatement1.executeUpdate();
                        //check if query executed successfully or not.
                        if(rowsAffected>0){
                            System.out.println("Amount Rs."+amount+" debited successfully!!!");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed!!!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Insufficient balance!!!");
                    }
                } else {
                    System.out.println("Security Pin is invalid!!!");
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }


    public void CreditMoney(long acc_no) throws SQLException {
        //get amount to credit and security pin from user.
        scanner.nextLine();
        System.out.print("Enter amount :- ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security pin :- ");
        String pin = scanner.nextLine();

        try{
            connection.setAutoCommit(false);
            //check for valid account no.
            if(acc_no!=0) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE acc_no = ? AND security_pin=?");
                preparedStatement.setLong(1,acc_no);
                preparedStatement.setString(2,pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                //verify that account number and security pin is valid or not.
                if(resultSet.next()){
                    //perform update operation to update balance in db.
                    String creditQuery = "UPDATE accounts SET balance = balance + ? where acc_no = ?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(creditQuery);
                    preparedStatement1.setDouble(1,amount);
                    preparedStatement1.setLong(2,acc_no);
                    int rowsAffected = preparedStatement1.executeUpdate();
                    //check if query executed successfully or not.
                    if(rowsAffected>0){
                        System.out.println("Amount Rs."+amount+" credited successfully!!!");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transaction Failed!!!");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                } else {
                    System.out.println("Security Pin is invalid!!!");
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }


    public void transferMoney(long senderAccNo) throws SQLException{
        //get receiver account number, amount to transfer and security pin from user.
        scanner.nextLine();
        System.out.print("Enter Receiver Account number :- ");
        long receiverAccNo = scanner.nextLong();
        System.out.print("Enter Amount :- ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security pin :- ");
        String pin = scanner.nextLine();
        try {
            connection.setAutoCommit(false);
            if(senderAccNo!=0 && receiverAccNo!=0){
                //verify senders account no and pin.
                String verifyQuery = "SELECT * FROM accounts WHERE acc_no = ? AND security_pin = ?;";
                PreparedStatement preparedStatement = connection.prepareStatement(verifyQuery);
                preparedStatement.setLong(1,senderAccNo);
                preparedStatement.setString(2,pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    //update sender and receiver balance in db.
                    double senderBalance = resultSet.getDouble("balance");
                    //check for sufficient balance is available in sender account to debit money.
                    if(amount<=senderBalance){
                        //debit amount from sender account balance.
                        String debitQuery = "UPDATE accounts SET balance = balance - ? where acc_no = ?";
                        //credit amount in receiver account balance.
                        String creditQuery = "UPDATE accounts SET balance = balance + ? where acc_no = ?";
                        PreparedStatement debitPreparedStatement = connection.prepareStatement(debitQuery);
                        PreparedStatement creditPreparedStatement = connection.prepareStatement(creditQuery);
                        debitPreparedStatement.setDouble(1,amount);
                        debitPreparedStatement.setLong(2,senderAccNo);
                        creditPreparedStatement.setDouble(1,amount);
                        creditPreparedStatement.setLong(2,receiverAccNo);
                        int creditedAffected = creditPreparedStatement.executeUpdate();
                        int debitedAffected = debitPreparedStatement.executeUpdate();
                        //check if query executed successfully or not.
                        if(creditedAffected>0 && debitedAffected>0){
                            System.out.println("Transaction successful!!!");
                            System.out.println("Rs."+amount+" transferred successfully.");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed!!!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Insufficient Balance!!!!");
                    }
                } else {
                    System.out.println("Invalid Security pin!!!!");
                }
            } else {
                System.out.println("Invalid Account number!!!");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void checkBalance(long acc_no){
        //get security pin from user and verify.
        scanner.nextLine();
        System.out.print("Enter security pin :- ");
        String pin = scanner.nextLine();
        String query = "SELECT balance FROM accounts WHERE acc_no = ? AND security_pin = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1,acc_no);
            preparedStatement.setString(2,pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            //if account no and pin is valid then get balance from db and print it.
            if(resultSet.next()){
                System.out.println("Balance :- "+resultSet.getDouble("balance"));
            }
            else {
                System.out.println("Invalid Security pin!!!");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
