package parser;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import analysis.Stock;
import analysis.Calculate;

public class Test {

	public static void main(String[] args) {
		new Test_Pictures();
		new Test_Stock();
		Test_Calculate.test();
	}

	public static class Test_Pictures extends Pictures {
		private void test_deleteAMP() {
			String testURL = null, ref = null;
			Method[] methoden = Pictures.class.getDeclaredMethods();
			for (Method m : methoden) {
				Comment c = m.getAnnotation(Comment.class);
				if (c != null) {
					if (c.includeTest()) {
						testURL = c.exampleInput()[0];
						ref = c.exampleOutput();
					}
				}
			}
			if (deleteAMP(testURL).equals(ref)) {
				System.out.println("test_deleteAMP() complete");
			} else {
				System.out.println("ERROR: test_deleteAMP() return a false value");
				System.out.println("method: " + deleteAMP(testURL));
				System.out.println("testVa: " + ref);
			}
		}

		Test_Pictures() {
			super(null);
			test_deleteAMP();
		}
	}

	public static class Test_Stock {
		Test_Stock() {
			@SuppressWarnings("rawtypes")
			Constructor[] konstruktoren = Stock.class.getDeclaredConstructors();
			for (@SuppressWarnings("rawtypes")
			Constructor k : konstruktoren) {
				@SuppressWarnings("unchecked")
				Comment ab = (Comment) k.getAnnotation(Comment.class);
				if (ab != null) {
					if (ab.includeTest()) {
						Stock stock = new Stock(ab.exampleInput()[0], ab.exampleInput()[1]);
						try {
							stock.downloadImage();
							System.out.println("test_downloadImage() complete");
						} catch (IOException e) {
							System.out.println("ERROR: Cannot download image");
						}
						stock.loadData();
					}
				}
			}
		}
	}
	
	public static void p(String str) {
		System.out.println(str);
	}

	public static class Test_Calculate extends Calculate {
		public static void test() {
			Method[] methoden = Calculate.class.getDeclaredMethods();
			for (Method m : methoden) {
				Comment c = m.getAnnotation(Comment.class);
				if (c != null) {
					if (c.includeTest()) {
						switch (c.testId()) {
						case 0:
							
						}
					}
				}
			}
		}
	}
}