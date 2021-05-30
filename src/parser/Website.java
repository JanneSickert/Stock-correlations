package parser;

import java.util.ArrayList;

public class Website {

	private String mainDomain;
	private ArrayList<String> link;
	private int size = 0;
	
	public Website(String mainDomain) {
		this.mainDomain = mainDomain;
		this.link = new ArrayList<String>();
	}
	
	public String getURL(int index) {
		return (mainDomain + link.get(index));
	}
	
	public void addItem(String item) {
		link.add(item);
		size++;
	}
	
	public int getSize() {
		return size;
	}
}