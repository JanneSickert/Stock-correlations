package parser;

public class Test implements FindURL{

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
		test_findStartIndex();
	}
	
	public static void main(String[] args) {
		new Test();
	}
}
