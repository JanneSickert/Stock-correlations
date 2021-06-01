package analysis;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import parser.Comment;

public class Stock {

	private String picURL;
	private String name;
	private ArrayList<Block> graph = new ArrayList<Block>();

	class Block {
		ArrayList<Candle> candle = new ArrayList<Candle>();
	}

	class Candle {
		int lastPriceInPixel, maxValueInPixel, minValueInPixel, firstPriceInPixel;
	}

	@Comment(includeTest = true, exampleInput = {
			"https://traderfox.de/charts/finance-chart.php?width=935&height=328&stock_id=386713&time_range=360&time_unit=d&chart_type=CandleStick&volume=1&",
			"drillisch-ag-on" })
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
		while (-1 != (n = in.read(buf))) {
			out.write(buf, 0, n);
		}
		out.close();
		in.close();
		byte[] response = out.toByteArray();
		FileOutputStream fos = new FileOutputStream(getStoragePath());
		fos.write(response);
		fos.close();
		System.out.println("Download: " + name + " from " + picURL);
	}

	private String getStoragePath() {
		return ("metadata/pics/" + name + ".png");
	}

	class Point {
		int x, y;

		Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	public void loadData() {
		final Point START_UP = new Point(55, 25);
		final Point START_DOWN = new Point(55, 303);
		final Point END_UP = new Point(862, 25);
		final int CANDLE_UP = new Color(102, 170, 34).getRGB();
		final int CANDLE_DOWN = new Color(204, 0, 0).getRGB();
		final int WHITE_COLOR = new Color(255, 255, 255).getRGB();
		BufferedImage img = null;
		URL location = getClass().getClassLoader().getResource(getStoragePath());
		try {
			img = ImageIO.read(location);
		} catch (Exception ex) {
			System.out.println("ERROR: Cannot load image -> " + getStoragePath());
		}
		int modi = 0, blockIndex = -1, candleIndex = -1, lastPixel = -1, firstPixel = -1;
		for (int x = START_UP.x; x < END_UP.x; x++) {
			boolean inCandle = false, up = false, down = false, emptyLine = true;
			for (int y = START_UP.y; y < START_DOWN.y; y++) {
				if (img.getRGB(x, y) == CANDLE_UP) {
					if (!(inCandle)) {
						inCandle = true;
						lastPixel = y;
						up = true;
						emptyLine = false;
					}
				} else if (img.getRGB(x, y) == CANDLE_DOWN) {
					if (!(inCandle)) {
						inCandle = true;
						firstPixel = y;
						down = true;
						emptyLine = false;
					}
				} else if (img.getRGB(x, y) == WHITE_COLOR) {
					if (inCandle) {
						inCandle = false;
						if (up) {
							firstPixel = y - 1;
						}
						if (down) {
							lastPixel = y - 1;
						}
					}
				}
				if (emptyLine) {
					modi = 0;
				} else {
					if (modi == 4) {
						modi = 1;
					}
				}
				switch (modi) {
				case 0:
					graph.add(new Block());
					blockIndex++;
					candleIndex = -1;
				case 1:
					graph.get(blockIndex).candle.add(new Candle());
					candleIndex++;
				case 2:
					graph.get(blockIndex).candle.get(candleIndex).lastPriceInPixel = lastPixel;
					graph.get(blockIndex).candle.get(candleIndex).firstPriceInPixel = firstPixel;
					modi = 3;
					break;
				case 3:
					if (up) {
						graph.get(blockIndex).candle.get(candleIndex).maxValueInPixel = lastPixel;
						graph.get(blockIndex).candle.get(candleIndex).minValueInPixel = firstPixel;
					}
					if (down) {
						graph.get(blockIndex).candle.get(candleIndex).maxValueInPixel = firstPixel;
						graph.get(blockIndex).candle.get(candleIndex).minValueInPixel = lastPixel;
					}
					modi = 4;
					x++;
					break;
				}
			}
		}
	}
}