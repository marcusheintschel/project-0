package common.pojo;

import java.sql.ResultSet;

public class Customer {

	private int id;
	private String first_name;
	private String last_name;
	private String email;
	private String dob;
	private String username;
	private String password;
	private boolean isApproved;

	public Customer(int id, String first_name, String last_name, 
					String email, String dob, String username, String password,boolean isApproved) {
		super();
		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.dob = dob;
		this.username = username;
		this.password = password;
		this.isApproved = isApproved;
	}
	
	public Customer(ResultSet rs) throws Exception {
		this.id = rs.getInt(1);
		this.first_name = rs.getString(2);
		this.last_name = rs.getString(3);
		this.email = rs.getString(4);
		this.dob = rs.getString(5);
		this.username = rs.getString(6);
		this.password = rs.getString(7);
		this.isApproved = rs.getBoolean(8);
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "id: | " + id + " | first: | " + first_name + " | last: | " + last_name + " | email: | " + email + " | dob: | " + dob + " | username: | " + username + " | password | " + password + " |";
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getFirst_name() {
		return first_name;
	}


	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}


	public String getLast_name() {
		return last_name;
	}


	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getDob() {
		return dob;
	}


	public void setDob(String dob) {
		this.dob = dob;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}
}
