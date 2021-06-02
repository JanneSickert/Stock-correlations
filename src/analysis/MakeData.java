package analysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import parser.ExportData;

public class MakeData {

	private static Stock[] stock;
	
	public static void main(String[] args) {
		ArrayList<String> names = readFile("metadata/StockNames.txt");
		stock = new Stock[names.size()];
		ArrayList<String> meta = new ArrayList<String>();
		for (int i = 0; i < stock.length; i++) {
			stock[i] = new Stock(names.get(i));
			stock[i].loadData();
			meta.add(stock[i].getMeta() + ",");
		}
		Stock.exportStockData();
		new ExportData() {
			@Override
			public void write() {
				writeFile("metadata/SizeStockNames.txt", meta);
			}
		}.write();
	}

	private static ArrayList<String> readFile(String FileUrl) {
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
