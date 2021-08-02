package manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import common.pojo.Customer;
import common.pojo.Employee;
import dao.UserDAO;

public class UserMgr {
	
	UserDAO dao = new UserDAO();
	private static final Logger logger = LogManager.getLogger(UserMgr.class);
	
	public void register() {
		try {
			dao.register();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.warn("Could not fulfill register request",e);
		}
	}
	
	
	public Customer login() throws Exception{
		
			Customer c = null;
			try {
				c = dao.login();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.warn("Could not fulfill login request",e);
			}
			return c;
	}

	public Employee employeeLogin() {
		
		Employee e = null;
		try {
			e = dao.employeeLogin();
		}catch (Exception err) {
			logger.warn("Could not login employee.",e);
		}
		return e;
	}


	public void approveRejectPending() {
		// TODO Auto-generated method stub
		try {
			dao.approveRejectPending();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.warn("Could not access pending customer list.");
		}
	}


	public void viewAccounts() {
		// TODO Auto-generated method stub
		try {
			dao.viewAccounts();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.warn("Could not access customer accounts.");
		}
	}


	public void viewLog() {
		// TODO Auto-generated method stub
		try {
			dao.viewLog();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.warn("Log File could not be found.");
		}
	}
	
	
}
