package common.pojo;


public class Account {

	
	private int id;
	private String type;
	private double balance;
	private int customerid;
	
	
	
	public Account(int id, String type, double balance,int customerid) {
		super();
		this.id = id;
		this.type = type;
		this.balance = balance;
		this.customerid = customerid;
	}
	
//	public Account(Customer c, ResultSet account) {
//		super();
//		this.id = account.getInt(1);
//		this.type = account.getString(2);
//		this.balance = account.getDouble(3);
//		this.customerid = c.getId();
//	}
	
	public String toString() {
		return "Account No.: " + id + " | Account Type: " + type + " | Balance: $" + balance;
	}
	public String transfer() {
		return "Account No.: " + id + " | Account Type: " + type;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public int getCustomerid() {
		return customerid;
	}
	public void setCustomerid(int customerid) {
		this.customerid = customerid;
	}
	
}
