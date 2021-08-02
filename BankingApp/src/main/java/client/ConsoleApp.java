package client;

import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import common.pojo.Customer;
import common.pojo.Employee;
import manager.AccountMgr;
import manager.UserMgr;

public class ConsoleApp {
	 
	public static boolean logout = false;
	public static boolean quit = false;
	UserMgr um = new UserMgr();
	AccountMgr am = new AccountMgr();
	Scanner scan = new Scanner(System.in);
	private Customer logged = null;
	private Employee admin = null;
	private static final Logger logger = LogManager.getLogger(ConsoleApp.class);
	
	public void start(){
		
		while(quit == false) {
			//show menu
			/*
			 * 1.login
			 * 2.register
			 */
			
			while(admin != null) {
				System.out.println("Please select an option:\n\n1. Approve/Reject Pending Account\n2. View Accounts\n3. View Transaction Log\n4. Logout");
				int userInput = scan.nextInt();
				
				switch(userInput) {
					case 1:{
						approveRejectPending();
						break;
					}
					case 2:{
						viewAccounts();
						break;
					}
					case 3:{
						viewLog();
						break;
					}
					case 4:{
						employeeLogout();
						break;
					}
				}
			}
			
			if(logged == null) {
					
				System.out.println("Please select an option:\n\n1. Login\n2. Register\n3. Employee Login\n4. Quit");
				int userInput = scan.nextInt();
				
				switch(userInput) {
					case 1:{
						login();
						break;
					}
					case 2:{
						register();
						break;
					}
					case 3:{
						employeeLogin();
						break;
					}
					case 4:{
						quit = true;
						scan.close();
						System.out.println("Goodbye! Thanks for banking with us!");
						break;
					}
					
				}
				
				
			
			
			}else if(logged != null && logged.isApproved()) {
				
				//customer has logged in
				int userInput;
				System.out.println("Please select an option:\n1. Create a new Account\n2. View Existing Accounts\n3. Deposit/Withdrawal\n4. Transfer Money\n5. Logout");
				userInput = scan.nextInt();
				
				switch(userInput) {
					case 1:{
						createAccount(logged);
						break;
					}
					case 2:{
						viewAccounts(logged);
						break;
					}
					case 3:{
						transaction(logged);
						break;
					}
					case 4:{
						transfer(logged);
						break;
					}
					case 5:{
						logout();
						break;
					}
				}
			}else if(logged != null && logged.isApproved() == false) {
				System.out.println("Your user account is currently pending approval.\n\n");
				System.out.println("You will now be redirected to the Login Page.\n\n");
				logout();
			}
		}
	}

	private void viewLog() {
		// TODO Auto-generated method stub
		um.viewLog();
	}

	private void viewAccounts() {
		// TODO Auto-generated method stub
		um.viewAccounts();
	}

	private void approveRejectPending() {
		// TODO Auto-generated method stub
		um.approveRejectPending();
	}

	private void employeeLogin() {
		Employee e = null;
		try{
			e = um.employeeLogin();
		}catch (Exception err) {
			logger.warn("Could not login admin.",err);
		}
		setAdmin(e);
	}
	
	private void employeeLogout() {
		setAdmin(null);
		
	}

	public Employee getAdmin() {
		return admin;
	}

	public void setAdmin(Employee admin) {
		this.admin = admin;
	}

	private void transfer(Customer c) {
		try {
			am.transfer(c);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.warn("Could not fulfill transfer request",e);
		}
	}

	private void transaction(Customer c) {
		try {
			am.transaction(c);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.warn("Could not fulfill transaction request",e);;
		}
	}

	private void viewAccounts(Customer c) {
		
		try {
			am.viewAccounts(c);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.warn("Could not fulfill view account request",e);
		}
		
	}

	private void createAccount(Customer c) {
		
		try {
			am.createAccount(c);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.warn("Could not fulfill create account request",e);
		}
	}



	public Customer getLogged() {
		return logged;
	}

	public void setLogged(Customer logged) {
		this.logged = logged;
	}

	private void register() {
		um.register();
	}

	private void login(){
		
		Customer c = null;
		try {
			c = um.login();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.warn("Could not fulfill login request",e);
		}
		setLogged(c);
		
	}
	private void logout() {
		logger.info("Customer has logged out.");
		setLogged(null);
	}
	
	

}
