package GayeonMall;

import java.util.HashMap;

public class CartHashMap extends HashMap<String, CartMap> {

	public String toTxt() {
		String txt = "";
		for (CartMap cartMap : this.values()) {
			txt += cartMap.toTxt();
		}
		return txt;
	}

}
