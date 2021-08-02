package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import common.pojo.Customer;
import common.util.DBUtil;
import common.pojo.Account;

public class AccountDAO {
	
	Scanner scan = new Scanner(System.in);
	private static final Logger logger = LogManager.getLogger(AccountDAO.class);
	
	public void transfer(Customer c) throws Exception {
		
		//connect to the database
		Connection dbaccess = DBUtil.getInstance().getConnection();
		//initialize hashmap
		Map<Integer,Account> view = new HashMap<Integer,Account>();
		//bank database query w/ user input
		PreparedStatement viewAccounts = dbaccess.prepareStatement("select * from bankdb.account where customer_id =?");
		viewAccounts.setInt(1, c.getId());
		ResultSet accounts = viewAccounts.executeQuery();
		//storing the accounts in the hashmap
		while(accounts.next()) {
			Account a = new Account(accounts.getInt(1),accounts.getString(2),accounts.getDouble(3),accounts.getInt(4));
			view.put(a.getId(),a);
		}
		//printing the user's accounts
		Set<Integer> keys = view.keySet();
		System.out.println("Accounts:\n");
		for(Integer a : keys) {
			System.out.println(view.get(a));
		}
		//getting user input to select an account to transfer from and what type of transfer
		System.out.println("\nPlease type the Account Number you would like to transfer from:\n");
		int userInput = scan.nextInt();
		double balance = view.get(userInput).getBalance();
		System.out.println(view.get(userInput) + "\n");
		System.out.println("1. Transfer to my own account\n2. Transfer to another customer's account\n");
		int transferInput = scan.nextInt();
		
		switch(transferInput) {
			//transfer to own account
			case 1:{
				transferToOwnAccount(c,keys,view,balance,userInput,dbaccess);
				break;
			}
			//transfer to another customer's account
			case 2:{
				transferBetweenCustomers(c,view,balance,userInput,dbaccess);
				break;
			}
		}
		
		
	}

	public void transaction(Customer c) throws Exception {
		
		Connection dbaccess = DBUtil.getInstance().getConnection();
		
		Map<Integer,Account> view = new HashMap<Integer,Account>();
		
		PreparedStatement viewAccounts = dbaccess.prepareStatement("select * from bankdb.account where customer_id =?");
		viewAccounts.setInt(1, c.getId());
		
		ResultSet accounts = viewAccounts.executeQuery();
		
		
		while(accounts.next()) {
			Account a = new Account(accounts.getInt(1),accounts.getString(2),accounts.getDouble(3),accounts.getInt(4));
			view.put(a.getId(),a);
		}
		Set<Integer> keys = view.keySet();
		System.out.println("Accounts:\n");
		for(Integer a : keys) {
			System.out.println(view.get(a));
		}
		
		System.out.println("\nPlease type the Account Number you would like to access:\n");
		int userInput = scan.nextInt();
		
		System.out.println(view.get(userInput) + "\n1. Deposit\n2. Withdrawal");
		int choice = scan.nextInt();
		
		switch(choice) {
			case 1:{
				deposit(view,userInput,dbaccess);
				break;
			}
			case 2:{
				withdraw(view,userInput,dbaccess);
				break;
				}
			}
		}
		
		
		
	

	public void viewAccounts(Customer c) throws Exception{
		
		Connection dbaccess = DBUtil.getInstance().getConnection();
		
		List<Account> view = new ArrayList<Account>();
		
		PreparedStatement viewAccounts = dbaccess.prepareStatement("select * from bankdb.account where customer_id =? order by id");
		viewAccounts.setInt(1, c.getId());
		
		ResultSet accounts = viewAccounts.executeQuery();
		
		
		while(accounts.next()) {
			Account a = new Account(accounts.getInt(1),accounts.getString(2),accounts.getDouble(3),accounts.getInt(4));
			view.add(a);
		}
		for(Account a : view) {
			
			System.out.println(a);
		}
		if(view.isEmpty()) {
			System.out.println("You do not currently have any accounts with us. Please create an account to view it here.");
			
		}
		
		
	}

	public void createAccount(Customer c) throws Exception{
		
		Connection dbaccess = DBUtil.getInstance().getConnection();
		
		System.out.println("Please enter the type of account you would like to make:");
		String type = scan.nextLine();
		System.out.println("Please enter your starting balance:");
		double balance = scan.nextDouble();
		PreparedStatement insert = dbaccess.prepareStatement("insert into bankdb.account (type,balance,customer_id) values (?,?::numeric::money,?);");
		
		insert.setString(1,type);
		insert.setDouble(2, balance);
		insert.setInt(3, c.getId());
		
		int inserted = insert.executeUpdate();
		if(inserted == 1) {
			System.out.println("Your account was successfully created!\n");
			logger.info(c.getFirst_name() + " " + c.getLast_name() + " created a new " + type + " account.");
		}
		
	}
	public void deposit(Map<Integer,Account> view, int userInput, Connection dbaccess) {
		
		System.out.println("How much would you like to deposit in Account No.: "+ view.get(userInput).getId());
		double deposit = scan.nextDouble();
		double balance = view.get(userInput).getBalance();
		double amount = deposit + balance;
		
		PreparedStatement depositIntoAccount;
		try {
			depositIntoAccount = dbaccess.prepareStatement("update bankdb.account set balance = ? where id = ?");
		
		depositIntoAccount.setDouble(1, amount);
		depositIntoAccount.setInt(2, view.get(userInput).getId());
		
		int update = depositIntoAccount.executeUpdate();
		if(update == 1){
			System.out.println("Deposit Successful!\n");
			logger.info("Deposit into Account No.: " + view.get(userInput).getId() + " in the amount of $" + deposit);
		}
		System.out.println("Old Balance: $" + balance + "\n");
		System.out.println("Deposit Amount: $" + deposit + "\n");
		System.out.println("New Balance: $" + amount + "\n");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void withdraw(Map<Integer,Account> view, int userInput, Connection dbaccess) {
		
		System.out.println("How much would you like to withdraw from Account No.: "+ view.get(userInput).getId());
		double withdrawal = scan.nextDouble();
		double balance = view.get(userInput).getBalance();
		double amount = balance - withdrawal;
		try {
		if(amount >= 0) {
			PreparedStatement depositIntoAccount = dbaccess.prepareStatement("update bankdb.account set balance = ? where id = ?");
			depositIntoAccount.setDouble(1, amount);
			depositIntoAccount.setInt(2, view.get(userInput).getId());
			
			int update = depositIntoAccount.executeUpdate();
			
			if(update == 1){
				System.out.println("Deposit Successful!\n");
				logger.info("Withdraw from Account No.: " + view.get(userInput).getId() + " in the amount of " + withdrawal);
			}
			System.out.println("Old Balance: $" + balance + "\n");
			System.out.println("Withdrawn Amount: $" + withdrawal + "\n");
			System.out.println("New Balance: $" + amount + "\n");
		
		}else {
			System.out.println("You don't have that much money in Account No.:" + view.get(userInput).getId() + "!\n");
			System.out.println("Balance for Account No.: " + view.get(userInput).getId() + " = $" + view.get(userInput).getBalance() + "\n\n");
		}
		}catch (SQLException e){
			logger.warn("Could not connect to database.", e);
		}
		
	}
	
	public void transferBetweenCustomers(Customer c, Map<Integer,Account> view, double balance,int userInput,Connection dbaccess){
		int custId = 0;
		try {
		Map<Integer,Account> view2 = new HashMap<Integer,Account>();
		System.out.println("Please enter the email of the customer you wish to send money to.");
		String custEmail = scan.next();
		PreparedStatement emailSearch = dbaccess.prepareStatement("select id from bankdb.customer where email = ?");
		emailSearch.setString(1, custEmail);
		ResultSet idset = emailSearch.executeQuery();
		
		while(idset.next()) {
			custId = idset.getInt(1);
		}
		
		PreparedStatement idSearch = dbaccess.prepareStatement("select * from bankdb.account where customer_id = ?");
		idSearch.setInt(1, custId);
		
		ResultSet custAccounts = idSearch.executeQuery();
		
		while(custAccounts.next()) {
			Account b = new Account(custAccounts.getInt(1),custAccounts.getString(2),custAccounts.getDouble(3),custAccounts.getInt(4));
			view2.put(b.getId(), b);
		}
		Set<Integer> keys2 = view2.keySet();
		System.out.println("\n" + custEmail + "'s Accounts:\n");
		
		for(Integer a : keys2) {
			System.out.println(view2.get(a).transfer());
		}
		System.out.println("\nPlease select the Account No. you wish to transfer to:");
		int transferNo = scan.nextInt();
		
		System.out.println("You've selected: \n" + view2.get(transferNo).transfer());
		System.out.println("Enter the amount you wish to transfer");
		double transfer = scan.nextDouble();
		
		if((balance - transfer) >= 0) {
			
			double amountFrom = balance - transfer;
			double amountTo = (view2.get(transferNo).getBalance()) + transfer;
			PreparedStatement transferFrom = dbaccess.prepareStatement("update bankdb.account set balance = ? where id = ?");
			PreparedStatement transferTo = dbaccess.prepareStatement("update bankdb.account set balance = ? where id = ?");
			transferFrom.setDouble(1, amountFrom);
			transferFrom.setInt(2, view.get(userInput).getId());
			int statusFrom = transferFrom.executeUpdate();
			transferTo.setDouble(1, amountTo);
			transferTo.setInt(2, view2.get(transferNo).getId());
			int statusTo = transferTo.executeUpdate();
			if(statusTo == 1 && statusFrom == 1) {
				System.out.println("Transfer successful.\n\nNew Balances:");
				try {
					viewAccounts(c);
					logger.info("Transfer to Customer ID - " + view2.get(transferNo).getCustomerid() +": $" + transfer 
							+ " from Account No. " + view.get(userInput).getId() 
							+ " to Account No. " + view2.get(transferNo).getId());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.warn("Unable to show account view", e);
				}
			}
			
		}else {
			System.out.println("You don't have that much money in Account No.:" + view.get(userInput).getId() + "!\n");
			System.out.println("Balance for Account No.: " + view.get(userInput).getId() + " = $" + view.get(userInput).getBalance() + "\n\n");
		}
		}catch (SQLException e){
			
		}
	}
	//keys, view, balance, dbaccess, userInput
	public void transferToOwnAccount(Customer c, Set<Integer> keys, Map<Integer,Account> view, double balance, int userInput, Connection dbaccess) {
		//display accounts
		System.out.println("Accounts: \n");
		for(Integer a : keys) {
			System.out.println(view.get(a));
		}
		System.out.println("\nEnter the Account No. of the Account you'd like to transfer to:\n");
		int account = scan.nextInt();
		
		System.out.println("Enter the amount you wish to transfer:\n");
		double transfer = scan.nextDouble();
		try {
		if((balance - transfer) >= 0) {
			
			double amountFrom = balance - transfer;
			double amountTo = (view.get(account).getBalance()) + transfer;
			PreparedStatement transferFrom = dbaccess.prepareStatement("update bankdb.account set balance = ? where id = ?");
			PreparedStatement transferTo = dbaccess.prepareStatement("update bankdb.account set balance = ? where id = ?");
			transferFrom.setDouble(1, amountFrom);
			transferFrom.setInt(2, view.get(userInput).getId());
			int statusFrom = transferFrom.executeUpdate();
			transferTo.setDouble(1, amountTo);
			transferTo.setInt(2, view.get(account).getId());
			int statusTo = transferTo.executeUpdate();
			if(statusTo == 1 && statusFrom == 1) {
				System.out.println("Transfer successful.\n\nNew Balances:");
				try {
					viewAccounts(c);
					logger.info("Transfer to own account: $" + transfer 
								+ " from Account No. " + view.get(userInput).getId() 
								+ " to Account No. " + view.get(account).getId());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.warn("Unable to show account view", e);
				}
			}
			
		}else {
			System.out.println("You don't have that much money in Account No.:" + view.get(userInput).getId() + "!\n");
			System.out.println("Balance for Account No.: " + view.get(userInput).getId() + " = $" + view.get(userInput).getBalance() + "\n\n");
		}
		}catch (SQLException e) {
			logger.warn("Could not connect to the database.",e);
		}
	}
}

