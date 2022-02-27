package GayeonMall;

import java.util.HashMap;

@SuppressWarnings("serial")
public class ProductHashMap extends HashMap<String, Product> {

	public String toTxt() {
		String txt = "";
		
		for (Product product : this.values()) 
			txt += product.toTxt();
		
		return txt;
	}
}