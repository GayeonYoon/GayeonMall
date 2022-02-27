package GayeonMall;

import java.util.HashMap;

public class CartMap extends HashMap<String, Cart> {
	
	public String toTxt() {
		String txt = "";
		for(Cart cart : this.values()) {
			txt += cart.toTxt();
		}
		return txt;
	}
}
