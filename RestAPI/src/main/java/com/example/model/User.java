package com.example.model;
/**
 * @author Nagesh.Chauhan
 *
 */
public class User {
	private int id;
	private String name;
	private String password;
	private String money;

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(int id, String name, String password, String money) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.money = money;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public int GiveMoney(User user, Float money)
	{
		Float money_Customer = Float.parseFloat(this.money);
		
		if(money_Customer < money)
			return 0;
		else
		{
			money_Customer -= money;
			this.money = money_Customer.toString();// Set lai tien cua khach hang sau khi tra
			///////
			Float money_Shoper = Float.parseFloat(user.money);
			money_Shoper += money;
			user.setMoney(money_Shoper.toString());
			return 1;
		}
	}

	
}
