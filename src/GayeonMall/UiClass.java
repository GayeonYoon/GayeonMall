package GayeonMall;

import java.io.IOException;

public class UiClass {

	public void mainConsole() { 			// 실행하면 나오는 main 화면
		System.out.println(" ****** ShoppingMall에 온걸 환영합니다 ***** \n");
		System.out.println("1. 회원가입 ");
		System.out.println("2. 로그인");
		System.out.println("3. 회원탈퇴");
		System.out.println("9. Exit");
	}

	public void loginConsole() { 			// 로그인메뉴
		System.out.println("1. 로그인 하기");
		System.out.println("0. 뒤로가기");
	}

	public void adminConsole() { 		// 관리자 화면
		System.out.println("1.  관리자 신규 등록");
		System.out.println("2. 회원 관리");
		System.out.println("3. 상품 관리 ");
	}

	public void productInfoConsole() throws IOException { 			// 회원 로그인상태에서 실행 될 메서드.
		System.out.println("*** 회원 Menu *** \n");
		System.out.println("1. 전체상품 조회");
		System.out.println("2. Cash 충전");
		System.out.println("3. 상품 구매 ");
		System.out.println("4. 장바구니");
		if (UserManager.getInstance().getLoginUserId().equals("admin")) { // 관리자 상품관리 화면
			System.out.println("5. 관리자 Menu");
		} else {
			return;
		}
		
		System.out.println("0. 뒤로가기");


	}

	public void cartManageDisplay() {
		System.out.println("1. 내 장바구니 상품 추가");
		System.out.println("2. 내 장바구니 상품 삭제");
		System.out.println("3. 내 장바구니 모두 삭제");
		System.out.println("4. 내 장바구니 상품 모두 구매");
		System.out.println("0. 뒤로가기");
	}

	public void productReviseConsole() { // 관리자 상품관리 화면
		System.out.println("1. 상품 수량 변경");
		System.out.println("2. 상품 가격 변경");
		System.out.println("3. 새로운 상품 추가");
		System.out.println("4. 상품 삭제");
		System.out.println("0. 뒤로가기");
	}
}
