package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import common.pojo.Customer;
import common.util.DBUtil;

public class UserDAO {

	Scanner scan = new Scanner(System.in);
	private static final Logger logger = LogManager.getLogger(UserDAO.class);
	Customer log = null;
	
	
	public void register() throws Exception {
		//connecting to the database utility
		Connection dbaccess = DBUtil.getInstance().getConnection();
		//get a resultset of the username column in the bankdb.customer table - to check later upon username entry
		ArrayList<String> users = new ArrayList<String>();
		//use 'where' in sql query below to find records with specified username
		PreparedStatement userQuery = dbaccess.prepareStatement("select username from bankdb.customer");
		ResultSet userlist = userQuery.executeQuery();
		//add usernames to users list for uniqueness checking
		while(userlist.next()) {
			users.add(userlist.getString(1));
		}
			try{	//taking customer input to create new customer account
					System.out.println("Please enter a username to login with");
					String username = scan.next();
					//check to make sure the username is unique
					if(users.contains(username)) {
						System.out.println("Username already exists.");
						throw new Exception();
					}
					System.out.println("Please enter your password");
					String password = scan.next();
					System.out.println("Please enter your first name");
					String first_name = scan.next();
					System.out.println("Please enter your last name");
					String last_name = scan.next();
					System.out.println("Please enter your email address");
					String email = scan.next();
					System.out.println("Please enter your date of birth in the format yyyy-mm-dd");
					String dob = scan.next();
					Date date = Date.valueOf(dob);
					//creating sql insert statement
					PreparedStatement insert = dbaccess.prepareStatement("insert into bankdb.customer (first_name,last_name,email,dob,username,pass) values (?,?,?,?,?,?);");
					insert.setString(1, first_name);
					insert.setString(2, last_name);
					insert.setString(3, email);
					insert.setDate(4, date);
					insert.setString(5, username);
					insert.setString(6, password);
					
					int inserted = insert.executeUpdate();
					//confirming the customer account's creation
					if(inserted == 1) {
						System.out.println("Your user account was successfully created. You may now login with your username and password.\n");
					}	
			}catch (Exception e) {
				logger.warn("Username already exists.",e);
			}
			
		}
	
	
	
	public Customer login() throws Exception {
		//connecting to the database utility
		Connection dbaccess = DBUtil.getInstance().getConnection();
		try{
			
			System.out.println("Please enter your username: ");
			String username = scan.next();
			//sql query with user input to select a customer
			PreparedStatement login = dbaccess.prepareStatement("select * from bankdb.customer c where username = ?;");
			login.setString(1, username);
			ResultSet customer = login.executeQuery();
			//checking to see if username exists on database
			if(customer.next()) {
				//creating a customer object with the user's values
				log = new Customer(customer.getInt(1),customer.getString(2),customer.getString(3),customer.getString(4),customer.getString(5),customer.getString(6),customer.getString(7));
				System.out.println("Please enter your password:");
				String password = scan.next();
				//checking password against the sql table
				if(password.equals(log.getPassword())) {
					System.out.println("You are now logged in.\n");
					return log;
				}else {
					System.out.println("Your password was incorrect.\n");
					return null;
				}
			//if username does not exist
			}else {
				System.out.println("There was no customer with that username.\n");
			}
		}catch (Exception e){
			logger.warn("Could not connect to the database.", e);
		}
		return log;
	}
	
	public void employeeLogin() {
		
	}
}


