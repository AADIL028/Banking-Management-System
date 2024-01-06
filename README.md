# 💰 Banking Management System 💼

## Overview

This Java application, the Banking Management System, employs a dual-table structure with 'user' and 'accounts,' offering a robust and secure banking experience. Users can register with essential details, and each account is intricately managed with details such as account number, balance, and a security PIN. Leveraging MySQL for data storage, this system ensures efficient and reliable data management.

## Features

📝 **User Registration**: Users can register securely with their name, email, and password.

💼 **Account Management**: Seamlessly manage bank accounts, each uniquely identified by an account number. Retrieve account details, including balance and security PIN.

💳 **Transaction Handling**: Conduct financial transactions, including debiting, crediting, and transferring money between accounts.

⚖️ **Balance Inquiry**: Check account balance to keep track of financial status.

## Technology Stack

- **Java**: Core programming language for application logic.

- **MySQL Database**: Backend storage with two tables, 'user' and 'accounts,' ensuring efficient data management.

- **JDBC (Java Database Connectivity)**: Utilizing JDBC for seamless interaction with the MySQL database, ensuring secure and reliable data retrieval and manipulation.

## Database Structure

### `user` Table

| Field    | Type         | Key | 
|----------|--------------|-----| 
| name     | varchar(255) |     | 
| email    | varchar(255) | PRI | 
| password | varchar(255) |     | 

### `accounts` Table

| Field        | Type          | Key | 
|--------------|---------------|-----| 
| acc_no       | bigint        | PRI | 
| name         | varchar(255)  |     | 
| email        | varchar(255)  | UNI | 
| balance      | decimal(10,2) |     | 
| security_pin | char(4)       |     | 


## How to Use

1. **Clone Repository**: `git clone https://github.com/AADIL028/banking-management-system.git`
   
2. **Set Up Database**: Create a MySQL database named `banking_system` with 'user' and 'accounts' tables.

3. **Run Application**: Execute the `BankingApp` class to start the Banking Management System.

## Contributing

🚀 Contributions are welcome! If you have ideas for improvements or want to report issues, please create an [issue](https://github.com/AADIL028/banking-management-system/issues) on GitHub.

---
