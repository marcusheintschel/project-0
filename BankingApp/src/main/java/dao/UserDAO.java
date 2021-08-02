package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import common.pojo.Customer;
import common.pojo.Employee;
import common.util.DBUtil;

public class UserDAO {

	Scanner scan = new Scanner(System.in);
	private static final Logger logger = LogManager.getLogger(UserDAO.class);
	Customer log = null;
	Employee emp = null;
	
	public void register() throws Exception {
		//connecting to the database utility
		Connection dbaccess = DBUtil.getInstance().getConnection();

			try{	//taking customer input to create new customer account
					System.out.println("Please enter a username to login with");
					String username = scan.next();
					//check to make sure the username is unique
					PreparedStatement userQuery = dbaccess.prepareStatement("select username from bankdb.customer where username = ?");
					userQuery.setString(1, username);
					ResultSet taken = userQuery.executeQuery();
					if(taken.next()) {
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
						logger.info("New customer registered with username: " + username);
					}	
			}catch (Exception e) {
				logger.warn("Customer attempted to create an account with a taken username.");
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
				log = new Customer(customer);
				System.out.println("Please enter your password:");
				String password = scan.next();
				//checking password against the sql table
				if(password.equals(log.getPassword())) {
					System.out.println("You are now logged in.\n");
					logger.info("Customer " + log.getFirst_name() + " " + log.getLast_name() + " has logged in");
					return log;
				}else {
					System.out.println("Your password was incorrect.\n");
					logger.info("User with username: |" + log.getUsername() + "| Entered an incorrect password");
					return null;
				}
			//if username does not exist
			}else {
				System.out.println("There was no customer with that username.\n");
				logger.info("User entered a username that does not exist.");
			}
		}catch (Exception e){
			logger.warn("Could not connect to the database.", e);
		}
		return log;
	}
	
	public Employee employeeLogin() throws Exception {
		//connecting to the database utility
		Connection dbaccess = DBUtil.getInstance().getConnection();
		try{
			
			System.out.println("Please enter your username: ");
			String username = scan.next();
			//sql query with user input to select a customer
			PreparedStatement login = dbaccess.prepareStatement("select * from bankdb.employee where username = ?;");
			login.setString(1, username);
			ResultSet employee = login.executeQuery();
			//checking to see if username exists on database
			if(employee.next()) {
				//creating a customer object with the user's values
				emp = new Employee(employee.getString(1),employee.getString(2));
				System.out.println("Please enter your password:");
				String password = scan.next();
				//checking password against the sql table
				if(password.equals(log.getPassword())) {
					System.out.println("You are now logged in.\n");
					logger.info("Customer " + log.getFirst_name() + " " + log.getLast_name() + " has logged in");
					return emp;
				}else {
					System.out.println("Your password was incorrect.\n");
					logger.info("Admin has logged in.");
					return emp;
				}
			//if username does not exist
			}else {
				System.out.println("There was no employee with that username.\n");
				logger.info("Employee entered a username that does not exist.");
			}
		}catch (Exception e){
			logger.warn("Could not connect to the database.", e);
		}
		return emp;
	}



	public void approveRejectPending() throws Exception{
		//connecting to the database
		Connection dbaccess = DBUtil.getInstance().getConnection();
		//initialize hashmap of pending customers
		Map<Integer,Customer> pending = new HashMap<Integer,Customer>();
		PreparedStatement pendingQuery = dbaccess.prepareStatement("select * from bankdb.customer where isApproved = false");
		ResultSet pendingCustomers = pendingQuery.executeQuery();
		
		while(pendingCustomers.next()) {
			Customer c = new Customer(pendingCustomers);
			pending.put(c.getId(), c);
		}
		Set<Integer> customerId = pending.keySet();
		System.out.println("Pending Accounts:\n");
		for(Integer a : customerId) {
			System.out.println(pending.get(a));
		}
		System.out.println("Please enter the ID number of the customer to approve/reject.\n");
		int selectApproveReject = scan.nextInt();
		
		System.out.println("\n\n" + pending.get(selectApproveReject));
		
		System.out.println("\n1. Approve\n2. Reject");
		int approveReject = scan.nextInt();
		
		switch(approveReject) {
		case 1:{
			approve(dbaccess,pending.get(selectApproveReject));
			break;
		}
		case 2:{
			reject(dbaccess,pending.get(selectApproveReject));
			break;
		}
		default:{
			System.out.println("Invalid Input");
			break;
		}
		}
		
	}



	private void reject(Connection dbaccess,Customer customer) throws Exception {
		
		PreparedStatement reject = dbaccess.prepareStatement("delete from bankdb.customer where id = ?");
		reject.setInt(1, customer.getId());
		int status = reject.executeUpdate();
		if(status == 1) {
			System.out.println("Customer rejected. Account deleted.");
			logger.info("Customer " + customer.getFirst_name() + " " + customer.getLast_name() + " deleted successfully.");
		}
	}



	private void approve(Connection dbaccess, Customer customer) throws Exception{
		
		PreparedStatement approve = dbaccess.prepareStatement("update bankdb.customer set isApproved = true where id = ?");
		approve.setInt(1, customer.getId());
		int status = approve.executeUpdate();
		if(status == 1) {
			System.out.println("Customer has been approved. Full banking privileges now accessible.");
			logger.info("Customer " + customer.getFirst_name() + " " + customer.getLast_name() + " approved successfully.");
		}
	}



	public void viewAccounts() throws Exception{
		
		Connection dbaccess = DBUtil.getInstance().getConnection();
		
		PreparedStatement accountView = dbaccess.prepareStatement("select c.first_name,c.last_name,a.id,a.\"type\",a.balance "
																+ "from bankdb.customer c inner join bankdb.account a on a.customer_id = c.id order by c.id");
		ResultSet accountSet = accountView.executeQuery();
		while(accountSet.next()) {
			System.out.println(accountSet.getString(1) + " " + accountSet.getString(2) + " Account No. " 
							+ accountSet.getInt(3) + ": Type | " + accountSet.getString(4) + "| Balance: $" + accountSet.getDouble(5));
		}

	}



	public void viewLog() throws Exception{
		// TODO Auto-generated method stub
		File log = new File("C:/Users/marcu/OneDrive/Desktop/Revature/Github/project-0/BankingApp/src/main/resources/log.txt");
		BufferedReader in = new BufferedReader(new FileReader(log));
		String line = in.readLine();
		while(line != null)
		{
		  System.out.println(line);
		  line = in.readLine();
		}
		in.close();
	}
	
}


