package analysis;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import parser.Comment;

public class Stock {
	
	private String picURL;
	private String name;
	
	@Comment(
			includeTest = true,
			exampleInput = {
					"https://traderfox.de/charts/finance-chart.php?width=935&height=328&stock_id=386713&time_range=360&time_unit=d&chart_type=CandleStick&volume=1&",
					"drillisch-ag-on"
				}
			)
	public Stock(String picURL, String name) {
		this.picURL = picURL;
		this.name = name;
	}
	
	public void downloadImage() throws IOException {
		 URL url = new URL(picURL);
		 InputStream in = new BufferedInputStream(url.openStream());
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 byte[] buf = new byte[1024];
		 int n = 0;
		 while (-1!=(n=in.read(buf)))
		 {
		    out.write(buf, 0, n);
		 }
		 out.close();
		 in.close();
		 byte[] response = out.toByteArray();
		 FileOutputStream fos = new FileOutputStream(getStoragePath());
		 fos.write(response);
		 fos.close();
	}
	
	private String getStoragePath() {
		String path = "metadata/pics/" + name + ".png";
		return path;
	}
	
	public void loadData() {
		
	}
}