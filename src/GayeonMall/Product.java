package GayeonMall;

public class Product {

	private String name;
	private int price;
	private int qty;

	Product() {
	}

	Product(String name, int price, int qty) {
		this.name = name;
		this.price = price;
		this.qty = qty;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}
	
	public String toTxt() {
		return this.name + "," + this.price + "," + this.qty + "\n";
	}

	@Override
	public String toString() {
		return "Product [name=" + name + ", price=" + price + ", qty=" + qty + "]";
	}
	 

}
