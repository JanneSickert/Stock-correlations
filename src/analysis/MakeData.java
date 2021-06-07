package analysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import parser.ExportData;

public class MakeData {

	static Stock[] stock;
	static ArrayList<ArrayList<Stock.Candle>> stock_list = new ArrayList<ArrayList<Stock.Candle>>();
	
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
		for (int a = 0; a < stock.length; a++) {
			stock_list.add(new ArrayList<Stock.Candle>());
			for (int b = 0; b < stock[a].graph.size(); b++) {
				for (int c = 0; c < stock[a].graph.get(b).candle.size(); c++) {
					if (!(stock[a].graph.get(b).candle.get(c).lastPriceInPixel == -1)) {
						stock_list.get(a).add(stock[a].graph.get(b).candle.get(c));
					}
				}
			}
		}
		new ExportData() {
			@Override
			public void write() {
				ArrayList<ForCal> lfc = new ArrayList<ForCal>();
				ArrayList<String> listForC = new ArrayList<String>();
				for (int a = 0; a < stock_list.size(); a++) {
					for (int b = 0; b < stock_list.get(a).size(); b++) {
						Stock.Candle c = stock_list.get(a).get(b);
						listForC.add("" + a + ":" + c.indexInPixel + ":" + c.firstPriceInPixel + ":" + c.lastPriceInPixel);
						ForCal fc = new ForCal();
						fc.stock = a;
						fc.index = c.indexInPixel;
						fc.first = c.firstPriceInPixel;
						fc.last = c.lastPriceInPixel;
						lfc.add(fc);
					}
				}
				writeFile("metadata/listForC.txt", listForC);
				Calculate ca = new Calculate(lfc);
				int[][] score = new int[2][stock.length];//index 0 is for buy.
				for (int i = 0; i < stock.length; i++) {
					score[0][i] = ca.make(i, true);
					System.out.println(stock[i].name + " buy score: " + score[0][i]);
				}
				for (int i = 0; i < stock.length; i++) {
					score[1][i] = ca.make(i, false);
					System.out.println(stock[i].name + " short score: " + score[1][i]);
				}
			}
		}.write();
	}
	
	public static class ForCal{
		int stock;
		int index;
		int first;
		int last;
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
