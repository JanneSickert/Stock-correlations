package parser;

import java.io.File;
import java.util.ArrayList;

public class Main {

	private static FindURL[] fu = { new TraderFox() };
	private static ArrayList<String> list = new ArrayList<String>();

	public static void main(String[] args) {
		createMissingFolders();
		startParsing();
		System.out.println("\n" + "\n");
		Pictures pic = new Pictures(list);
		pic.parseWebsite();
		fu[0].writeDataToStorage();
		pic.writeDataToStorage();
	}

	private static void createMissingFolders() {
		final String[] FN = {"metadata", "pics"};
		File[] folder = {new File(FN[0]), new File(FN[0] + "/" + FN[1])};
		for (File f : folder) {
			if (!(f.exists())) {
				f.mkdir();
			}
		}
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