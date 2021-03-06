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
		System.out.println("start");
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
				Sort<String> sort = new Sort<String>();
				int score;
				for (int intervalInDays = 1; intervalInDays < 31 * 2; intervalInDays++) {
					for (int i = 0; i < stock.length; i++) {
						score = ca.make(i, true, intervalInDays);
						if (score != 0) {
							sort.addItem(stock[i].name + " interval in days:" + intervalInDays + " buy score:", score);
						}
					}
					for (int i = 0; i < stock.length; i++) {
						score = ca.make(i, false, intervalInDays);
						if (score != 0) {
							sort.addItem(stock[i].name + " interval in days:" + intervalInDays + " short score:", score);
						}
					}
				}
				sort.sortElements();
				ArrayList<Sort.Ele<String>> sortList = sort.getReverseList();
				ArrayList<String> sits = new ArrayList<String>();
				for (int i = 0; i < sortList.size(); i++) {
					String osits = (String) sortList.get(i).e + sortList.get(i).nr;
					System.out.println(osits);
					sits.add(osits);
				}
				writeFile("metadata/IntervalValues.txt", sits);
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
