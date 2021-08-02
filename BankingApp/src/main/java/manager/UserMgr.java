package manager;

import java.sql.SQLException;

import common.pojo.Customer;
import dao.UserDAO;

public class UserMgr {
	
	UserDAO dao = new UserDAO();
	public void register() {
		try {
			dao.register();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public Customer login() throws SQLException{
		
			Customer c = null;
			try {
				c = dao.login();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return c;
	}

	public void employeeLogin() {
		
	}
	
	
}
