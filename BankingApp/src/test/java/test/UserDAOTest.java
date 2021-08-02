package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
//import common.util.*;
import org.junit.Test;

import dao.UserDAO;

public class UserDAOTest {

	//@ContextConfiguration(location = AppConstants.CONFIG_FILE)
	@Test
	public void testLogin() throws Exception{
		
		UserDAO dao = new UserDAO();
		
		try {
			InputStream file = new FileInputStream("C:\\Users\\marcu\\OneDrive\\Desktop\\Revature\\Github\\project-0\\BankingApp\\src\\test\\java\\simulated-user-input");
			System.setIn(file);
			dao.login();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
