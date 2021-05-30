package parser;

import java.util.ArrayList;

public class TraderFox implements FindURL{
	
	private Website web;
	private final int START_LINE = 400, STEPP = 7, LEN = 58, END_LINE = 3291;
	private ArrayList<String> htmlCode;
	
	@Override
	public void parseWebsite() {
		final String URL_TO_DOWNLOAD = "https://traderfox.de/aktien/alle-deutschen-aktien-bestandteile";
		web = new Website("https://traderfox.de/aktien/");
		htmlCode = download(URL_TO_DOWNLOAD);
		for (int i = START_LINE; i <= END_LINE; i+= STEPP) {
			String s = htmlCode.get(i).substring(LEN);
			int in = getIndexFrom(s, '\"');
			web.addItem(s.substring(0, in));
		}
	}

	@Override
	public void printAllURLs() {
		for (int i = 0; i < web.getSize(); i++) {
			System.out.println(web.getURL(i));
		}
	}

	@Override
	public Website getWebsite() {
		return web;
	}
}