package parser;

import java.util.ArrayList;

public class Main {

	private static FindURL[] fu = {new TraderFox()};
	private static ArrayList<String> list = new ArrayList<String>();
	
	public static void main(String[] args) {
			startParsing();
			System.out.println("\n" + "\n");
			Pictures pic = new Pictures(list);
			pic.parseWebsite();
			// fu[0].writeDataToStorage();
			// pic.writeDataToStorage();
	}

	private static void startParsing() {
		for (FindURL current : fu) {
			current.parseWebsite();
			current.printAllURLs();
			Website w = current.getWebsite();
			for (int i = 0; i < w.getSize(); i++) {
				list.add(w.getURL(i));
			}
		}
	}
}