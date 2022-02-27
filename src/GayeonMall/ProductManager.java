package GayeonMall;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class ProductManager {
	private static ProductManager singleObj = null;

	public static ProductManager getInstance() throws IOException {
		if (singleObj == null) {
			singleObj = new ProductManager();
		}
		return singleObj;
	}

	static final String fileProductListPath = "src/GayeonMall/productList.txt";
	static final String fileCartPath = "src/GayeonMall/cart.txt";
	UiClass uiClass = new UiClass();

	ProductHashMap productMap = new ProductHashMap();
	CartHashMap cartMap = new CartHashMap();
	HashMap<Integer, String> productIndexMap = new HashMap<Integer, String>();

	public void loadProductInfo() throws FileNotFoundException {
		Scanner productSc = new Scanner(new File(fileProductListPath));
		this.productMap.clear(); // 초기화 해주기
		int i = 1;

		while (productSc.hasNext()) {
			String tmpStr = productSc.nextLine();
			String[] shoppingItem = tmpStr.split(",", -1);
			Product product = new Product(shoppingItem[0], Integer.parseInt(shoppingItem[1]),
					Integer.parseInt(shoppingItem[2]));

			this.productMap.put(shoppingItem[0], product);
			this.productIndexMap.put(i, shoppingItem[0]);
			i++;
		}
	}

	public void loadCartInfo() throws FileNotFoundException {
		Scanner cartSc = new Scanner(new File(fileCartPath));
		this.cartMap.clear();

		while (cartSc.hasNext()) {
			String cartStr = cartSc.nextLine();
			String[] cartItem = cartStr.split(",", -1);
			Cart cart = new Cart(cartItem[0], cartItem[1], Integer.parseInt(cartItem[2]),
					Integer.parseInt(cartItem[3]));

			if (!this.cartMap.containsKey(cartItem[0]))
				this.cartMap.put(cartItem[0], new CartMap());

			this.cartMap.get(cartItem[0]).put(cartItem[1], cart);
		}
	}

	// 상품 List 출력
	public void viewProduct() throws IOException {
		this.loadProductInfo();
		int num = 0;
		System.out.println("-----------  상품 List------------");
		for (String str : this.productMap.keySet()) {
			num++;
			System.out.print(num + " : " + this.productMap.get(str).toTxt());
		}
		System.out.println("-----------------------------------");
		return;
	}

	public void viewCart() throws IOException {
		this.loadCartInfo();
		String currentId = UserManager.getInstance().getLoginUserId();
		if (this.cartMap.containsKey(currentId)) {
			System.out.println(this.cartMap.get(currentId).toTxt());
		} else {
			this.cartMap.put(currentId, new CartMap());
		}
	}

	public void writeProductMap() throws IOException { // product 파일에 다시쓰는 메서드.
		BufferedWriter productWriter = new BufferedWriter(new FileWriter(fileProductListPath));
		productWriter.write(this.productMap.toTxt());
		productWriter.close();
	}

	public void writeCartMap() throws IOException {
		BufferedWriter cartWriter = new BufferedWriter(new FileWriter(fileCartPath));
		cartWriter.write(this.cartMap.toTxt());
		cartWriter.close();
	}

	// 상품 구매하기
	public void purchaseProduct() throws IOException {
		this.loadProductInfo();
		this.viewProduct();

		Scanner sc = new Scanner(System.in);
//		String productName = null;
		int productIndex = 0;
		int productQty = 0, changeCash = 0, changeQty = 0, inputNum = 0, inputNum2 = 0;
		int userCash = UserManager.getInstance().getUserMap().get(UserManager.getInstance().getLoginUserId()).getCash();

		System.out.println("구매 원하는 상품의 번호를 입력하세요.");
		productIndex = Integer.parseInt(sc.nextLine());

		if (this.productIndexMap.containsKey(productIndex)) { // 상품이 있으면 구매가능.
			System.out.println("구매원하는 " + this.productIndexMap.get(productIndex) + "의 수량을 입력하세요. ");
			productQty = Integer.parseInt(sc.nextLine());

			if (this.productMap.get(this.productIndexMap.get(productIndex)).getQty() >= productQty) { // 입력한 상품의 수량이
																										// map에 저장된 수량보다
																										// 같거나 적아야만
																										// 구매가능.
				System.out.println("총 상품의 가격은 "
						+ (this.productMap.get(this.productIndexMap.get(productIndex)).getPrice() * productQty)
						+ "원 입니다.");

				System.out.println("나의 현재 잔액은 " + userCash + "원 입니다.");

				if (userCash >= (this.productMap.get(this.productIndexMap.get(productIndex)).getPrice() * productQty)) { // 내가가진금액과
																															// 구매할
																															// 상품금액
																															// 가격비교
					changeCash = userCash
							- (this.productMap.get(this.productIndexMap.get(productIndex)).getPrice() * productQty);

					UserManager.getInstance().getUserMap().get(UserManager.getInstance().getLoginUserId())
							.setCash(changeCash);
					UserManager.getInstance().saveUserData();

					// product의 바뀐 수량도 바꿔주기.
					changeQty = this.productMap.get(this.productIndexMap.get(productIndex)).getQty() - productQty;
					this.productMap.get(this.productIndexMap.get(productIndex)).setQty(changeQty);
					this.writeProductMap();

					System.out.println("현재 구매가능한 상품 수량은 " + changeQty + "개 입니다.");

					System.out.println("'" + this.productIndexMap.get(productIndex) + "'" + " 구매 성공!");
					System.out.println("현재 " + UserManager.getInstance().getLoginUserId() + "님의 잔액은 " + changeCash
							+ "원 입니다." + "\n");
					return;
				}

				else { // 잔액부족시
					System.out.println(UserManager.getInstance().getLoginUserId() + "님의 잔액이 부족합니다. 충전하시겠습니까? ");
					System.out.println("1. 충전하기   2. 충전안해. 돌아갈래");
					inputNum = sc.nextInt();
					if (inputNum == 1) {
						UserManager.getInstance().chargeCash();
						userCash = UserManager.getInstance().getUserMap()
								.get(UserManager.getInstance().getLoginUserId()).getCash();

						System.out.println("현재 잔액은 " + userCash + "원 입니다.");
						System.out.println("다시 상품구매로 돌아까용? ");
						System.out.println("1. 네  2. 아니오");
						inputNum2 = sc.nextInt();

						if (inputNum2 == 1) {
							this.loadProductInfo();
							this.purchaseProduct();
							return;
						} else {
							return;
						}

					} else if (inputNum == 2) {
						return;
					}
				}

			} else if (this.productMap.get(this.productIndexMap.get(productIndex)).getQty() < productQty) {
				System.out.println("상품 수량부족. 구매 불가합니다. ");
				System.out.println("현재 구매가능한 상품의 수량은 "
						+ this.productMap.get(this.productIndexMap.get(productIndex)).getQty() + "개 입니다." + "\n");
				return;
			}

		} else {
			System.out.println("입력하신 상품이 존재하지않습니다. 다시한번 확인하시오");
			ProductManager.getInstance().viewProduct();
			return;
		}
		return;
	}

	// 1. 내 장바구니 제품 추가
	public void doProductCartIn() throws IOException {
		Scanner sc = new Scanner(System.in);
		String currentId = UserManager.getInstance().getLoginUserId();
		String productName;
		int productQuantity, summaryProductQuantity = 0, summaryProductPrice = 0;

		this.viewProduct();
		System.out.println("*-*-*-*-*-* My Cart *-*-*-*-*-*");
		this.viewCart();
		System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");

		System.out.print("구매 하고자 하는 제품명 :      > 뒤로가기 9 \n");
		productName = sc.nextLine();

		if (productName.equals("9")) {
			this.writeCartMap();
		} else if (!this.cartMap.get(currentId).containsKey(productName)) { // 현재 아이디의 카트내에 그 물품이 존재 하지 않을때.
			this.cartMap.get(currentId).put(productName, new Cart());
			this.cartMap.get(currentId).get(productName).setUserId(currentId);
			this.cartMap.get(currentId).get(productName).setProductName(productName);

			System.out.println("제품 수량 : ");
			productQuantity = Integer.parseInt(sc.nextLine());

			if (productQuantity > 0 && productMap.get(productName).getQty() >= productQuantity) {
				summaryProductQuantity = productQuantity;
				summaryProductPrice = (productMap.get(productName).getPrice()) * productQuantity;
				this.cartMap.get(currentId).get(productName).setProductQty(summaryProductQuantity);
				this.cartMap.get(currentId).get(productName).setProductTotalPrice(summaryProductPrice);

				System.out.println("장바구니에 추가 되었습니다.");
				System.out.println("*-*-*-*-*-* My Cart *-*-*-*-*-*");
				System.out.println(this.cartMap.get(currentId).toTxt());
				System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
				this.writeCartMap();
			} else {
				System.out.println("구매하고자 하는 제품 수량이 현재 제품 재고보다 많습니다.");
				this.cartMap.get(currentId).remove(productName);
				this.writeCartMap();
			}
		} else {
			if (this.productMap.containsKey(productName)) { // 물품이 존재할 때
				System.out.println("제품 수량 : ");
				productQuantity = Integer.parseInt(sc.nextLine());

				if (productQuantity > 0 && this.productMap.get(productName)
						.getQty() >= (this.cartMap.get(currentId).get(productName).getProductQty() + productQuantity)) {
					summaryProductQuantity += this.cartMap.get(currentId).get(productName).getProductQty()
							+ productQuantity;
					summaryProductPrice += this.cartMap.get(currentId).get(productName).getProductTotalPrice()
							+ ((this.productMap.get(productName).getPrice()) * productQuantity);

					this.cartMap.get(currentId).get(productName).setProductQty(summaryProductQuantity);
					this.cartMap.get(currentId).get(productName).setProductTotalPrice(summaryProductPrice);

					System.out.println("장바구니에 추가 되었습니다.");
					System.out.println("*-*-*-*-*-* My Cart *-*-*-*-*-*");
					System.out.println(this.cartMap.get(currentId).toTxt());
					System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
					this.writeCartMap();
				} else {
					System.out.println("구매하고자 하는 제품 수량이 현재 제품 재고보다 많습니다.");
					this.writeCartMap();
				}
			} else {
				System.out.println("다시 입력해 주세요.");
				this.writeCartMap();
			}
		}
	}

	// 2. 내 장바구니 상품 삭제
	public void doProductCartOut() throws IOException {
		String currentId = UserManager.getInstance().getLoginUserId();
		System.out.println("*-*-*-*-*-* My Cart *-*-*-*-*-*");
		this.viewCart();
		System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");

		System.out.println("장바구니에서 삭제 원하는 상품명을 입력하세요.");
		Scanner sc = new Scanner(System.in);
		String inputProductName = sc.nextLine();

		if (this.cartMap.get(currentId).containsKey(inputProductName)) {

			// 수량 변경도 가능하게 해쥬기
			this.cartMap.get(currentId).remove(inputProductName);
			// this.cartMap.remove(currentUserId); => 이러면 다 삭제돼

			System.out.println("장바구니에서 삭제 완료되었습니다. ");
			this.writeCartMap();

		} else if (!this.cartMap.get(currentId).containsKey(inputProductName)) {
			System.out.println("삭제할 상품이 없습니다. \n");
		}

	}

	// 3. 내 장바구니 모두 삭제
	public void doProductCartAllRemove() throws IOException {
		String currentId = UserManager.getInstance().getLoginUserId();
		System.out.println("*-*-*-*-*-* My Cart *-*-*-*-*-*");
		this.viewCart();
		System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
		System.out.println("장바구니에 담긴 상품 모두 삭제하시겠습니까?  \n 1. 네    2. 아니오 ");
		Scanner sc = new Scanner(System.in);
		int tmpNum = sc.nextInt();

		if (tmpNum == 1) {
			this.cartMap.remove(currentId);
			this.writeCartMap();
			System.out.println("장바구니의 내역이 모두 삭제되었습니다. ");
		} else if (tmpNum == 2) {
			return;
		}
	}

	// 4. 내 장바구니 상품 모두 구매
	public void doPurchaseAllCartProduct() throws IOException {
		ProductManager.getInstance().loadProductInfo();
		String currentId = UserManager.getInstance().getLoginUserId();
		int userCash = UserManager.getInstance().getUserMap().get(currentId).getCash();
		int cartMapSumTotalPrice = 0;
		int summaryPrice = 0;
		int tmpNum = 0;

		System.out.println("*-*-*-*-*-* My Cart *-*-*-*-*-*");
		this.viewCart();
		System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");

		System.out.println("Cart에 있는 상품을 모두 구매하시겠습니까?");
		System.out.println("1. 네 2. 아니오");

		Scanner sc = new Scanner(System.in);
		tmpNum = sc.nextInt();

		if (tmpNum == 1) { // 모두 사

			CartMap valueOfCartMap = this.cartMap.get(currentId);

			for (Cart cart : valueOfCartMap.values()) {
				summaryPrice += cart.getProductTotalPrice();
			}

			// 총액 구함, 각 내 프로덕트 의 각 수량이 모든 프로덕트(프로덕트맵) 의 수량의 차이가 항상 0보다 크거나 같아야만 모두구매가
			// 가능해야겠다.

			for (Cart cart : valueOfCartMap.values()) { // 재고만 체크
				if (this.productMap.containsKey(cart.getProductName())) { // 상품이 있는지 확인
					int myCartInProductQty = cart.getProductQty();
					if (myCartInProductQty <= this.productMap.get(cart.getProductName()).getQty()) { // 수량체크. 전체 상품 수량보다
																										// 내가 카트에넣어둔 수량이
																										// 적을때만 가능.
						int summaryQty = (this.productMap.get(cart.getProductName()).getQty()) - myCartInProductQty;
						this.productMap.get(cart.getProductName()).setQty(summaryQty);

						this.writeProductMap();

					} else {
						System.out.println(this.productMap.get(cart.getProductName()).getName() + " 의 재고가 부족합니다.   ");
						System.out.println("현재" + this.productMap.get(cart.getProductName()).getName() + "구매 가능 수량 :  "
								+ this.productMap.get(cart.getProductName()).getQty() + "\n");
						return;
					}
				} else {
					System.out.println("장바구니 상품이 존재하지 않습니다.  ");
					this.writeCartMap();
					return;
				}
			}

			System.out.println("현재 나의 잔액 :  " + userCash + "원 ");
			System.out.println("카트의 총 상품 가격 : " + summaryPrice + "원 ");

			if (userCash >= summaryPrice) { // 구매가능해. . 내가가진 돈보다 장바구니안의총금액이 적을때만 구매가능.,
				int afterPurchaseCash = userCash - summaryPrice;
				UserManager.getInstance().getUserMap().get(currentId).setCash(afterPurchaseCash);
				UserManager.getInstance().saveUserData();
				System.out.println(
						"나의 현재 잔액 " + UserManager.getInstance().getUserMap().get(currentId).getCash() + "원 입니다. ");

			} else { // 돈 부족하니까 충전하고 사게끔 유도.
				System.out.println("돈이 부족합니다. 충전하고 오세용");
				UserManager.getInstance().chargeCash();
			}

			this.cartMap.remove(currentId); // cart 에서 내 상품 모두 없애기
			this.writeCartMap();

		} else if (tmpNum == 2) { // 안사
			return;
		}

	}

	// 상품 수량 변경하기
	public boolean changeProductQty() throws IOException {
		String productName = null;
		int changeQty = 0;
		this.viewProduct();
		ProductManager.getInstance().loadProductInfo();
		System.out.println("변경 원하는 상품의 이름을 입력하세요.");
		Scanner inputProductName = new Scanner(System.in);

		productName = inputProductName.nextLine();
		if (ProductManager.getInstance().productMap.containsKey(productName)) {
			System.out.println("변경할 수량을 입력하세요.");
			changeQty = inputProductName.nextInt();

			if (changeQty < 0) {
				System.out.println("음수는 입력할 수 없습니다.");
				return true;
			}

			ProductManager.getInstance().productMap.get(productName).setQty(changeQty);
			ProductManager.getInstance().writeProductMap();

			System.out.println(productName + "의 상품 수량이 " + changeQty + "개로 변경되었습니다. " + "\n");
			return false;
		} else {
			System.out.println("해당 상품이 존재하지 않습니다. ");
			return false;
		}
	}

	// 상품 가격 변경 ===> 장바구니에 영향 줌
	public boolean changeProductPrice() throws FileNotFoundException, IOException {
		ProductManager.getInstance().loadProductInfo();
		ProductManager.getInstance().loadCartInfo();
		String currentId = UserManager.getInstance().getLoginUserId();
		String productName = null;
		int changePrice = 0;
		this.viewProduct();

		Scanner inputProductName = new Scanner(System.in);
		System.out.println("변경 원하는 상품의 이름을 입력하세요.");
		productName = inputProductName.nextLine();

		if (ProductManager.getInstance().productMap.containsKey(productName)) {
			System.out.println("변경할 가격을 입력하세요.");
			changePrice = inputProductName.nextInt();

			if (changePrice < 0) {
				System.out.println("음수는 입력할 수 없습니다.");
				return true;
			}

			ProductManager.getInstance().productMap.get(productName).setPrice(changePrice);
			ProductManager.getInstance().writeProductMap();

			for (CartMap cartMap : this.cartMap.values()) {
				if (cartMap.containsKey(productName)) {
					int totalCartMapPrice = cartMap.get(productName).getProductQty() * changePrice;
					cartMap.get(productName).setProductTotalPrice(totalCartMapPrice);
				}
			}
			this.writeCartMap();

			System.out.println(productName + "의 상품가격이 " + changePrice + "원 으로 변경되었습니다. " + "\n");
			return true;
		} else {
			System.out.println("해당 상품이 존재하지 않습니다. ");
			return false;
		}
	}

	// 새로운 상품 추가
	public boolean addNewProduct() throws FileNotFoundException, IOException {
		ProductManager.getInstance().loadProductInfo();
		BufferedWriter userWriter = new BufferedWriter(new FileWriter(fileProductListPath, true));
		String productName = "";
		int productPrice, productQty = 0;
		this.viewProduct();
		Scanner inputProductName = new Scanner(System.in);

		System.out.println("추가할 상품 이름을 입력하세요.");
		productName = inputProductName.nextLine();

		System.out.println("상품 가격을 입력하세요. ");
		productPrice = inputProductName.nextInt();
		if (productPrice < 0) {
			System.out.println("음수는 입력할 수 없습니다.");
			return true;
		}

		System.out.println("상품 개수를 입력하세요.");
		productQty = inputProductName.nextInt();
		if (productQty < 0) {
			System.out.println("음수는 입력할 수 없습니다.");
			return true;
		}

		userWriter.write(productName + "," + productPrice + "," + productQty + "\n");
		System.out.println("상품 추가가 완료되었습니다. ");
		userWriter.close();

		return false;
	}

	// 상품 삭제 ===> 장바구니에 영향 줌
	public boolean deleteProduct() throws IOException {
		this.loadCartInfo();
		this.loadProductInfo();
		String productName = "";
		int tmpNum = 0;
		this.viewProduct();
		System.out.println("삭제할 상품이름을 입력하시오.");
		Scanner inputProductName = new Scanner(System.in);
		productName = inputProductName.nextLine();

		if (ProductManager.getInstance().productMap.containsKey(productName)) {
			System.out.println("정말 삭제할건가요..?   ");
			System.out.println("1. 네!!!  		2. 아니요..");

			tmpNum = inputProductName.nextInt();
			if (tmpNum == 1) {
				ProductManager.getInstance().productMap.remove(productName);
				ProductManager.getInstance().writeProductMap();
				System.out.println("상품 삭제가 완료되었습니다. ");

				for (CartMap cartMap : this.cartMap.values()) {
					if (cartMap.containsKey(productName)) {
						cartMap.remove(productName);
					}
				}
				this.writeCartMap();
				return true;
				
			} else if (tmpNum == 2) {
				System.out.println("취소되었습니다. ");
				return true;
			}

		} else {
			System.out.println("입력하신 상품이름이 없습니다. ");
			return true;
		}
		return false;
	}
}
