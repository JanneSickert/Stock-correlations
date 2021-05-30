package parser;

import java.util.ArrayList;

/**
 * This class find the URL for the stock-chart
 * @author janne
 *
 */
public class Pictures implements FindURL{
	
	private Website web = null;
	private final String KEY_STRING = "<img src=\"/charts/finance-chart.php";
	ArrayList<String> urls, picturURLs;
	
	Pictures(ArrayList<String> urls) {
		this.urls = urls;
	}
	
	public void parseWebsite() {
		picturURLs = new ArrayList<String>();
		for (int i = 0; i < urls.size(); i++) {
			ArrayList<String> code = download(urls.get(i));
			System.out.println("Load picture at: " + urls.get(i));
			for (String s : code) {
				if (s.contains(KEY_STRING)) {
					System.out.println("\n" + s + "\n");
					String link = getPictureLink(s);
					System.out.println(link);
					picturURLs.add(link);
				}
			}
		}
	}
	
	@Comment(
			exampleInput = {"			<img src=\"/charts/finance-chart.php?width=935&amp;height=328&amp;stock_id=386713&amp;time_range=360&amp;time_unit=d&amp;chart_type=CandleStick&amp;volume=1&amp;show_extremes=1&amp;class=stock\" alt=\"1+1 DRILLISCH AG O.N. Chart\" title=\"1+1 DRILLISCH AG O.N. Chart\" id=\"chart-element\" data-id=\"chart-element\">"},
			exampleOutput = "\"/charts/finance-chart.php?width=935&amp;height=328&amp;stock_id=386713&amp;time_range=360&amp;time_unit=d&amp;chart_type=CandleStick&amp;volume=1&amp;show_extremes=1&amp;class=stock"
			)
	private String getPictureLink(String rawText) {
		int a = getIndexFrom(rawText, '\"') + 1;
		String next = rawText.substring(a, rawText.length() - 1);
		a = getIndexFrom(next, '\"') - 1;
		return (next.substring(0, a));
	}
	
	public void printAllURLs() {
		for (String s : picturURLs) {
			System.out.println(s);
		}
	}
	
	public Website getWebsite() {
		return web;
	}
}