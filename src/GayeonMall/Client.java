package GayeonMall;

import java.io.IOException;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) throws IOException {
		UiClass uiclass = new UiClass();
		Scanner sc = new Scanner(System.in);
		boolean isWithDwaral = true;
		boolean istrue = true;
		boolean isLogin = true;
		int loginMenuNum, loginNum, loginNum2;

		while (istrue) {
			uiclass.mainConsole(); // 메인 콘솔
			loginMenuNum = sc.nextInt();
			switch (loginMenuNum) {
			case 1: // 회원가입 메서드
				UserManager.getInstance().signUp();
				break;
			case 2: // 로그인
				uiclass.loginConsole();
				loginNum = sc.nextInt();
				if (loginNum == 1) {
					isLogin = UserManager.getInstance().login();
					while (isLogin) { // login 상태 유지
						uiclass.productInfoConsole();
						loginNum2 = sc.nextInt();

						if (loginNum2 == 1) { // 1. 전체상품 조회
							ProductManager.getInstance().viewProduct();
						} else if (loginNum2 == 2) { // 2. Cash 충전
							UserManager.getInstance().chargeCash();
						} else if (loginNum2 == 3) { // 3. 상품 구매
							ProductManager.getInstance().purchaseProduct();
						} else if (loginNum2 == 4) { // 4. 장바구니
							while (true) {
								uiclass.cartManageDisplay();
								int cartManageNum = 0;
								cartManageNum = sc.nextInt();
								if (cartManageNum == 1) {
									ProductManager.getInstance().doProductCartIn();
								} else if (cartManageNum == 2) {
									ProductManager.getInstance().doProductCartOut();
								} else if (cartManageNum == 3) {
									ProductManager.getInstance().doProductCartAllRemove();
								} else if (cartManageNum == 4) {
									ProductManager.getInstance().doPurchaseAllCartProduct();
								} else if (cartManageNum == 0) {
									break;
								} else {
									System.out.println("잘못 입력 하였습니다.");
									break;
								}
							}
						} else if (loginNum2 == 5 && UserManager.getInstance().getLoginUserId().equals("admin")) { // 관리자Menu 
							while (true) {
								uiclass.productReviseConsole();
								int adminMenuNum = 0;
								adminMenuNum = sc.nextInt();
								if (adminMenuNum == 1) {
									ProductManager.getInstance().changeProductQty();
								} else if (adminMenuNum == 2) {
									ProductManager.getInstance().changeProductPrice();
								} else if (adminMenuNum == 3) {
									ProductManager.getInstance().addNewProduct();
								} else if (adminMenuNum == 4) {
									ProductManager.getInstance().deleteProduct();
								} else if (adminMenuNum == 0) {
									break;
								}
							}
						}  else  {
							// 관리자메뉴
							break;
							
						}
					}
				}
				break;
			case 3:
				// 탈퇴하기
				while (isWithDwaral) {
					isWithDwaral = UserManager.getInstance().withdrawal();
					break;
				}

			case 9:
				System.out.println("System 종료");
				System.exit(0);
			}
		}

	}
}
