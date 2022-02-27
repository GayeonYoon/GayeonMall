package GayeonMall;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class UserManager {

	static final String fileUserManagePath = "src/GayeonMall/user.txt";
	private HashMap<String, User> userMap = new HashMap<String, User>();
	private static UserManager singleObj = null;
	private String loginUserId = "";

	public static UserManager getInstance() throws IOException {
		if (singleObj == null) {
			singleObj = new UserManager();
		}
		return singleObj;
	}

	private UserManager() throws FileNotFoundException {
		this.loadUserInfos();
	}

	public HashMap<String, User> getUserMap() {
		return this.userMap;
	}

	public String getLoginUserId() {
		return this.loginUserId;
	}

	public void saveUserData() throws IOException { // user File에 다시쓰는 method.
		BufferedWriter userWriter = new BufferedWriter(new FileWriter(fileUserManagePath));

		for (String str : this.userMap.keySet()) {
			userWriter.write(this.userMap.get(str).toTxt());
		}
		userWriter.close();

	}

	public void loadUserInfos() throws FileNotFoundException {
		this.userMap.clear(); // 초기화 해주기

		Scanner userSc = new Scanner(new File(fileUserManagePath));

		while (userSc.hasNext()) {
			String tmpStr = userSc.nextLine();
			String[] userItem = tmpStr.split(",", -1);
			User user = new User(userItem[0], userItem[1], userItem[2], userItem[3], Integer.parseInt(userItem[4]));
			this.userMap.put(userItem[0], user);
		}
	}

	// 회원가입
	public void signUp() throws IOException {
		this.loadUserInfos();
		BufferedWriter userWriter = new BufferedWriter(new FileWriter(fileUserManagePath, true));

		Scanner sc = new Scanner(System.in);
		String id, pw, name, age;
		int cash;

		System.out.println("아이디를 입력하세요.");
		id = sc.nextLine();

		if (this.userMap.containsKey(id)) {
			System.out.println("이미 존재하는 아이디입니다.");
			userWriter.close();
			return;
		}

		System.out.println("비밀번호를 입력하세요.");
		pw = sc.nextLine();

		System.out.println("이름을 입력하세요.");
		name = sc.nextLine();

		System.out.println("나이를 입력하세요.");
		age = sc.nextLine();

		System.out.println("충전하실 금액을입력하세요. ");
		cash = sc.nextInt();
		userWriter.write(id + "," + pw + "," + name + "," + age + "," + cash + "\n");

		System.out.println(" \n ***** 환영합니다! ***** ");
		System.out.println("회원가입이 완료되었습니다.  \n");
		userWriter.close();

		return;
	}

	// 회원 로그인
	public boolean login() throws IOException {
		this.loadUserInfos();

		Scanner sc = new Scanner(System.in);
		String id = null;
		String pw = null;

		System.out.println("ID를 입력하세요");
		id = sc.nextLine();

		if (this.userMap.containsKey(id)) {
			System.out.println("PW를 입력하세요");
			pw = sc.nextLine();

			if (this.userMap.get(id).getPw().equals(pw)) {
				System.out.println("로그인이 완료되었습니다. ");
				this.loginUserId = id;
				System.out.println(id + "님의 현재 사용 가능한 금액은 " + this.userMap.get(id).getCash() + "원 입니다. " + "\n");

				return true;
			} else {
				System.out.println("비밀번호가 틀립니다.");
				return false;
			}
		} else {
			System.out.println("입력하신 아이디가 존재하지않습니다. ");
			return false;
		}
	}

	// 회원탈퇴

	public boolean withdrawal() throws IOException {
		this.loadUserInfos();

		Scanner sc = new Scanner(System.in);

		String inputId = null;
		String inputPw = null;

		System.out.println("삭제할 ID를 입력하세요.");
		inputId = sc.nextLine();

		if (this.userMap.containsKey(inputId)) {
			System.out.println("삭제할 PW를 입력하세요.");
			inputPw = sc.nextLine();

			if (this.userMap.get(inputId).getPw().equals(inputPw)) {
				this.userMap.remove(inputId);
				this.saveUserData();
				System.out.println("회원탈퇴가 완료되었습니다.");
				return false;

			} else {
				System.out.println("입력하신 PW가 틀립니다. ");
				return true;
			}
		} else {
			System.out.println("입력하신 ID가 존재하지 않습니다.");
			return true;
		}
	}

	// 충전 메서드 만들기.
	public void chargeCash() throws IOException {
		this.loadUserInfos();
		
		int inputNum;
		int inputChargeCash = 0;

		Scanner scan = new Scanner(System.in);

		System.out.println("충전할 금액을 입력하세요.");
		System.out.println("1. 10,000원    2. 30,000원   3. 50,000원");
		inputNum = scan.nextInt();

		switch (inputNum) {
		case 1:
			inputChargeCash = this.userMap.get(this.loginUserId).getCash() + 10000;
			this.userMap.get(this.loginUserId).setCash(inputChargeCash);
			System.out.println("10000원이 충전되었습니다. ");
			System.out.println("현재 사용 가능한 금액은 " + inputChargeCash + "원 입니다.  " + "\n");
			break;

		case 2:
			inputChargeCash = this.userMap.get(this.loginUserId).getCash() + 30000;
			this.userMap.get(this.loginUserId).setCash(inputChargeCash);
			System.out.println("30000원이 충전되었습니다. ");
			System.out.println("현재 사용 가능한 금액은 " + inputChargeCash + "원 입니다. " + "\n");
			break;

		case 3:
			inputChargeCash = this.userMap.get(this.loginUserId).getCash() + 50000;
			this.userMap.get(loginUserId).setCash(inputChargeCash);
			System.out.println("50000원이 충전되었습니다. ");
			System.out.println("현재 사용 가능한 금액은 " + inputChargeCash + "원 입니다. " + "\n");
			break;
		}
		this.saveUserData();
		
		return;
	}

}