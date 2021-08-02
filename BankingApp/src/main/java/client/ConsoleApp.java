package client;
import java.sql.SQLException;
import java.util.Scanner;
import common.pojo.Customer;
import manager.AccountMgr;
import manager.UserMgr;

public class ConsoleApp {
	 
	public static boolean logout = false;
	public static boolean quit = false;
	UserMgr um = new UserMgr();
	AccountMgr am = new AccountMgr();
	Scanner scan = new Scanner(System.in);
	private boolean loggedIn = false;
	private Customer logged = null;
	
	
	public void start(){
		
		while(quit == false) {
			//show menu
			/*
			 * 1.login
			 * 2.register
			 */
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
				
				
			
			
			}else if(logged != null) {
				
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
			}
		}
	}

	private void employeeLogin() {
		// TODO Auto-generated method stub
		
	}

	private void transfer(Customer c) {
		try {
			am.transfer(c);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void transaction(Customer c) {
		try {
			am.transaction(c);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void viewAccounts(Customer c) {
		
		try {
			am.viewAccounts(c);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void createAccount(Customer c) {
		
		try {
			am.createAccount(c);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setLogged(c);
		loggedIn = true;
		
	}
	private void logout() {
		setLogged(null);
		setLoggedIn(false);
	}
	
	

}
