package GayeonMall;

import java.io.IOException;

public class User {

	private String id, pw, name, age;
	private int cash;
	private static User singleObj = null;

	public static User getInstance() throws IOException { // 싱글톤
		if (singleObj == null) {
			singleObj = new User();
		}
		return singleObj;
	}

	User(String id, String pw, String name, String age, int cash) {
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.age = age;
		this.cash = cash;
	}

	User() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public int getCash() {
		return cash;
	}

	public void setCash(int cash) {
		this.cash = cash;
	}

	public String toTxt() {
		return this.id + "," + this.pw + "," + this.name + "," + this.age + "," + this.cash + "\n";
	}

}
