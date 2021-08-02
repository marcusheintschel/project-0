package manager;

import dao.AccountDAO;
import common.pojo.Customer;


public class AccountMgr {

	AccountDAO dao = new AccountDAO();

	public void transfer(Customer c) {
		
		try {
			dao.transfer(c);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void transaction(Customer c) {
		
		try {
			dao.transaction(c);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void viewAccounts(Customer c) {
		try {
			dao.viewAccounts(c);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void createAccount(Customer c) {
		try {
			dao.createAccount(c);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
