package analysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import parser.ExportData;

public class MakeData {

	static Stock[] stock;
	
	public static void main(String[] args) {
		ArrayList<String> names = readFile("metadata/StockNames.txt");
		stock = new Stock[names.size()];
		ArrayList<Stock> normalStock = new ArrayList<Stock>();
		ArrayList<String> meta = new ArrayList<String>(), normalMeta = new ArrayList<String>(), strEmtyLines = new ArrayList<String>();
		ArrayList<ArrayList<Integer>> emptyLines = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < stock.length; i++) {
			stock[i] = new Stock(names.get(i));
			stock[i].loadData();
			meta.add(stock[i].getMeta() + ",");
			emptyLines.add(stock[i].emptyLinesInGraph);
			if (stock[i].getMetaAsObject().isNormal()) {
				normalStock.add(stock[i]);
			}
		}
		Stock.exportStockData("StockValues");
		new ExportData() {
			@Override
			public void write() {
				writeFile("metadata/SizeStockNames.txt", meta);
			}
		}.write();
		Stock.textToFile.clear();
		for (int a = 0; a < normalStock.size(); a++) {
			normalStock.get(a).addGraphToExportData();
			normalMeta.add(normalStock.get(a).getMeta() + ",");
		}
		Stock.exportStockData("NormalStockData");
		new ExportData() {
			@Override
			public void write() {
				writeFile("metadata/NormalSizeStockNames.txt", normalMeta);
			}
		}.write();
		new ExportData() {
			@Override
			public void write() {
				for (int i = 0; i < emptyLines.size(); i++) {
					String s = "[";
					for (int k = 0; k < emptyLines.get(i).size(); k++) {
						s = s + emptyLines.get(i).get(k) + ",";
					}
					s = s + "]," + "\n";
					strEmtyLines.add(s);
				}
				writeFile("metadata/Space.txt", strEmtyLines);
			}
		}.write();
		Calculate.calculate();
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
