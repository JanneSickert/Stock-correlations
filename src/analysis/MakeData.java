package analysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MakeData {

	public static void main(String[] args) {
		ArrayList<String> names = readFile("metadata/StockNames.txt");
		Stock[] stock = new Stock[names.size()];
		for (int i = 0; i < stock.length; i++) {
			stock[0] = new Stock(names.get(i));
			stock[0].loadData();
		}
		Stock.exportStockData();
	}

	public static ArrayList<String> readFile(String FileUrl) {
		ArrayList<String> list = new ArrayList<String>();
		FileReader fr;
		try {
			fr = new FileReader(FileUrl);
			BufferedReader br = new BufferedReader(fr);
			String zeile = "";
			while ((zeile = br.readLine()) != null) {
				list.add(zeile);
			}
			br.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return list;
	}
}
