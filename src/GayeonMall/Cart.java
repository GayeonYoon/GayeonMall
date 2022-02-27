package GayeonMall;

public class Cart {
	String userId;
	String productName;
	int productQty;
	int productTotalPrice;

	public Cart() {
	}

	public Cart(String userId, String productName, int productQty, int productTotalPrice) {
		this.userId = userId;
		this.productName = productName;
		this.productQty = productQty;
		this.productTotalPrice = productTotalPrice;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductQty() {
		return productQty;
	}

	public void setProductQty(int productQty) {
		this.productQty = productQty;
	}

	public int getProductTotalPrice() {
		return productTotalPrice;
	}

	public void setProductTotalPrice(int productTotalPrice) {
		this.productTotalPrice = productTotalPrice;
	}

	public String toTxt() {
		return this.userId + "," + this.productName + "," + this.productQty + "," + this.productTotalPrice + "\n";
	}

}
