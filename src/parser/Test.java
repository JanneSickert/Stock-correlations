package parser;

public class Test extends Pictures{

	public void parseWebsite() {
	}
	
	public void printAllURLs() {
	}
	
	public Website getWebsite() {
		return null;
	}
	
	private void test_findStartIndex() {
		final String
		TEXT = "eins zwei drei vier",
		FIND = "drei";
		int in = findStartIndex(TEXT, FIND);
		System.out.println(TEXT.substring(in));
	}
	
	Test() {
		super(null);
		test_findStartIndex();
		test_deleteAMP();
	}
	
	private void test_deleteAMP() {
		final String testURL = "https://traderfox.de/charts/finance-chart.php?width=935&amp;height=328&amp;stock_id=386713&amp;time_range=360&amp;time_unit=d&amp;chart_type=CandleStick&amp;volume=1&amp;show_extremes=1&amp;class=stock";
		System.out.println(deleteAMP(testURL));
	}
	
	public static void main(String[] args) {
		new Test();
	}

	@Override
	public void writeDataToStorage() {
	}
}
