package BankingManagementSystem;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingApp {
    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String uname = "root";
    private static final String pass = "Admin@820";
    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            Connection con = DriverManager.getConnection(url,uname,pass);
            Scanner sc = new Scanner(System.in);
            User user = new User(con,sc);
            Accounts accounts = new Accounts(con,sc);
            AccountManager accountManager = new AccountManager(con,sc);

            String email;
            long acc_no;
            System.out.println("-------------------------------------");
            System.out.println("-----|WELCOME TO BANKING SYSTEM|-----");
            System.out.println("-------------------------------------");
            while (true){
                System.out.println("1. Register.");
                System.out.println("2. Login.");
                System.out.println("3. Exit.");
                System.out.print("Enter your choice :- ");
                int ch1 = sc.nextInt();
                switch (ch1){
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email = user.login();
                        if(email!=null){
                            System.out.println("User Logged In.");
                            if(!accounts.account_exist(email)){
                                System.out.println();
                                System.out.println("1. Open a new Bank Account.");
                                System.out.println("2. Exit.");
                                if(sc.nextInt() == 1){
                                    acc_no = accounts.openAccount(email);
                                    System.out.println("Account Created Successfully!!");
                                    System.out.println("Your account number is :- "+acc_no);
                                } else {
                                    break;
                                }
                            }
                            acc_no = accounts.getAccountNumber(email);
                            int ch2 = 0;
                            while (ch2!=5){
                                System.out.println("1. Debit Money.");
                                System.out.println("2. Credit Money.");
                                System.out.println("3. Transfer Money.");
                                System.out.println("4. Check Balance.");
                                System.out.println("5. Logout.");
                                System.out.print("Enter your choice :- ");
                                ch2 = sc.nextInt();
                                switch (ch2){
                                    case 1:
                                        accountManager.DebitMoney(acc_no);
                                        break;
                                    case 2:
                                        accountManager.CreditMoney(acc_no);
                                        break;
                                    case 3:
                                        accountManager.transferMoney(acc_no);
                                        break;
                                    case 4:
                                        accountManager.checkBalance(acc_no);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Invalid Choice!!!");
                                        break;
                                }
                            }
                        } else {
                            System.out.println("Invalid Email or Password!!!");
                        }
                        break;
                    case 3:
                        System.out.println("Thank you for using BANKING SYSTEM!:)....");
                        System.out.println("Exiting system.....");
                        return;
                    default:
                        System.out.println("Invalid Choice!!!");
                }
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}