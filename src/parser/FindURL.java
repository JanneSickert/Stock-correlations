package parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public interface FindURL {

	public void parseWebsite();
	
	public void printAllURLs();
	
	public Website getWebsite();
	
	@WasTested
	@Comment(ret = "the HTML code line by line")
	default public ArrayList<String> download(String strURL) {
		ArrayList<String> list = new ArrayList<String>();
		URL url;
		InputStream is = null;
		BufferedReader br;
		String line;

		try {
			url = new URL(strURL);
			is = url.openStream(); // throws an IOException
			br = new BufferedReader(new InputStreamReader(is));

			while ((line = br.readLine()) != null) {
				list.add(line);
			}
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return list;
	}
	
	@WasTested
	@Comment(
			exampleInput = "https://traderfox.de/aktien/387372-zooplus-ag\" title=\"EI: ZOOPLUS AG\">ZOOPLUS AG</a></td>",
			i_exampleOutput = 44)
	default public int getIndexFrom(String str, char delimiter) {
		int res = -1;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == delimiter) {
				res = i;
				break;
			}
		}
		return res;
	}
	
	@Comment(
			exampleInput = {"<td data-id=\"name\" class=\"name\"><a href=\"/aktien/2079583-2g-energy-ag\" title=\"EI: 2G ENERGY AG\">2G ENERGY AG</a></td>"},
			exampleOutput = "2079583-2g-energy-ag")
	default public String insulateRestURL(String line, @Comment(make = "the first index of exampleOutput") int startIndex, String CONTENT) {
		// find end of string by <">
		int end = -1;
		for (int i = startIndex; i < line.length(); i++) {
			if (line.charAt(i) == '\"') {
				end = i;
				break;
			}
		}
		if (end == -1) {
			System.out.println("ERROR: while parsing in insulateRestURL");
			System.exit(3);
		}
		// insulate substring
		int len = end - CONTENT.length();
		char[] arr = new char[len];
		int a = 0, l = CONTENT.length();
		while (a < len) {
			arr[a] = line.charAt(l);
			a++;
			l++;
		}
		return (new String(arr));
	}
	
	@WasTested
	@Comment(ret = "The first index of the string to be found.")
	default public int findStartIndex(@Comment(contain = "the HTML source code") char[] text, @Comment(contain = "The string to be found.") char[] content) {
		int resultIndex = 0, checkSum = 0;
		for (int i = 0; i < (text.length - content.length); i++) {
			for (int k = 0; k < content.length; k++) {
				if (text[i + k] == content[k]) {
					checkSum++;
				} else {
					checkSum = 0;
					break;
				}
			}
			if (content.length == checkSum) {
				resultIndex = i;
				break;
			}
		}
		return resultIndex;
	}
	
	@WasTested
	@Comment(ret = "The first index of the string to be found.")
	default public int findStartIndex(@Comment(contain = "the HTML source code") String text, @Comment(contain = "The string to be found.") String content) {
		char[] c_text = new char[text.length()];
		char[] c_content = new char[content.length()];
		int i;
		for (i = 0; i < c_text.length; i++) {
			c_text[i] = text.charAt(i);
		}
		for (i = 0; i < c_content.length; i++) {
			c_content[i] = content.charAt(i);
		}
		return (findStartIndex(c_text, c_content));
	}
}