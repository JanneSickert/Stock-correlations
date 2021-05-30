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
					String link = deleteAMP(getPictureLink(s));
					System.out.println(link);
					picturURLs.add(link);
				}
			}
		}
	}
	
	@Comment(
			exampleInput = {"https://traderfox.de/charts/finance-chart.php?width=935&amp;height=328&amp;stock_id=386713&amp;time_range=360&amp;time_unit=d&amp;chart_type=CandleStick&amp;volume=1&amp;show_extremes=1&amp;class=stock"},
			exampleOutput = "https://traderfox.de/charts/finance-chart.php?width=935&height=328&stock_id=386713&time_range=360&time_unit=d&chart_type=CandleStick&volume=1&show_extremes=1&class=stock"
			)
	protected String deleteAMP(String url) {
		final char[] DEL = {'a', 'm', 'p', ';'};
		int nrOfAmp = 0;
		for (int i = 0; i < (url.length() - DEL.length); i++) {
			if (url.charAt(i) == '&') {
				if (url.charAt(i + 1) == DEL[0] && url.charAt(i + 2) == DEL[1] && url.charAt(i + 3) == DEL[2] && url.charAt(i + 4) == DEL[3]) {
					nrOfAmp++;
				}
			}
		}
		char[] resultArr = new char[url.length() - (nrOfAmp * DEL.length)];
		char[] urlArr = new char[url.length()];
		for (int i = 0; i < url.length(); i++) {
			urlArr[i] = url.charAt(i);
		}
		int i = 0, k = 0;
		while (i < resultArr.length) {
			if (urlArr[i] == '&') {
				if (url.charAt(i + 1) == DEL[0] && url.charAt(i + 2) == DEL[1] && url.charAt(i + 3) == DEL[2] && url.charAt(i + 4) == DEL[3]) {
					resultArr[k] = '&';
					i += DEL.length;
				} else {
					resultArr[k] = '&';
				}
			} else {
				resultArr[k] = urlArr[i];
			}
			i++;
			k++;
		}
		return (new String(resultArr));
	}
	
	@Comment(
			exampleInput = {"			<img src=\"/charts/finance-chart.php?width=935&amp;height=328&amp;stock_id=386713&amp;time_range=360&amp;time_unit=d&amp;chart_type=CandleStick&amp;volume=1&amp;show_extremes=1&amp;class=stock\" alt=\"1+1 DRILLISCH AG O.N. Chart\" title=\"1+1 DRILLISCH AG O.N. Chart\" id=\"chart-element\" data-id=\"chart-element\">"},
			exampleOutput = "https://traderfox.de/charts/finance-chart.php?width=935&amp;height=328&amp;stock_id=386713&amp;time_range=360&amp;time_unit=d&amp;chart_type=CandleStick&amp;volume=1&amp;show_extremes=1&amp;class=stock"
			)
	private String getPictureLink(String rawText) {
		final String DOMAIN = "https://traderfox.de";
		int a = getIndexFrom(rawText, '\"') + 1;
		String next = rawText.substring(a, rawText.length() - 1);
		a = getIndexFrom(next, '\"');
		return (DOMAIN + next.substring(0, a));
	}
	
	public void printAllURLs() {
		for (String s : picturURLs) {
			System.out.println(s);
		}
	}
	
	public Website getWebsite() {
		return web;
	}

	@Override
	public void writeDataToStorage() {
		writeFile("metadata/pictureLinks.txt", picturURLs);
	}
}