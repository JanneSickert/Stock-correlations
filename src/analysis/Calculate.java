package analysis;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * The most important data structure is the matrix object. Is have three
 * Dimensions: Dimensions 1: blocks Dimensions 2: Difference of candle in
 * selected block. Dimensions 3: Difference of all other stocks in blocks.
 * 
 * @author janne
 *
 */
public class Calculate {

	private final static Scanner sc = new Scanner(System.in);
	private static Difference[] diffrence = new Difference[MakeData.stock.length];

	public static void calculate() {
		for (int i = 0; i < MakeData.stock.length; i++) {
			diffrence[i] = new Difference(MakeData.stock[i]);
		}
		select();
	}

	private static void select() {
		for (int i = 0; i < MakeData.stock.length; i++) {
			System.out.print("" + i + ":" + MakeData.stock[i].name + "\n");
		}
		System.out.print("Nr:");
		int inputNr = sc.nextInt();
		System.out.print("press 0 to buy 1 to short:");
		int buyOrShort = sc.nextInt();
		int score = -10000000;
		switch (buyOrShort) {
		case 0:
			score = buy(inputNr);
			break;
		case 1:
			score = sshort(inputNr);
			break;
		default:
			System.out.println("ERROR: invalid input!");
			System.exit(11);
		}
		System.out.println(MakeData.stock[inputNr].name + " have the score: " + score);
	}

	public static class Difference {

		ArrayList<ArrayList<Integer>> dif = new ArrayList<ArrayList<Integer>>();
		String name;

		Difference(Stock stock) {
			name = stock.name;
			for (int i = 0; i < stock.graph.size(); i++) {
				dif.add(new ArrayList<Integer>());
				for (int k = 0; k < stock.graph.get(i).candle.size() - 1; k++) {
					Integer a = stock.graph.get(i).candle.get(k + 1).lastPriceInPixel;
					Integer b = stock.graph.get(i).candle.get(k).lastPriceInPixel;
					Integer c = a - b;
					dif.get(i).add(c);
				}
			}
		}
	}

	public static abstract class Deduction {

		public abstract boolean check(Integer i);

		/**
		 * 
		 * @return [Block, index array of up or down]
		 */
		public ArrayList<ArrayList<Integer>> calc(Calculate.Difference d) {
			ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
			for (int i = 0; i < d.dif.size(); i++) {
				list.add(new ArrayList<Integer>());
				for (int k = 0; k < d.dif.get(i).size(); k++) {
					if (check(d.dif.get(i).get(k))) {
						list.get(i).add(k);
					}
				}
			}
			return list;
		}
	}

	public static class Matrix<T> extends ArrayList<ArrayList<ArrayList<T>>> {

		private static final long serialVersionUID = 6650960829060570814L;

		public T get(int x, int y, int z) {
			return (super.get(x).get(y).get(z));
		}

		public ArrayList<T> get(int x, int y) {
			return (super.get(x).get(y));
		}
	}

	private static ArrayList<Integer> makeCurrentDifference() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (Difference d : diffrence) {
			int i = 1;
			boolean dontHaveValue = true;
			do {
				if (!(d.dif.get(d.dif.size() - i).size() == 0)) {
					list.add(d.dif.get(d.dif.size() - i).get(d.dif.get(d.dif.size() - i).size() - 1));
					dontHaveValue = false;
				}
				i++;
				if (i == d.dif.size()) {
					list.add(0);// the stock have no value.
					dontHaveValue = false;
				}
			} while (dontHaveValue);
		}
		return list;
	}

	private static ArrayList<Integer> makeMarketDifferenceArray(Matrix<Integer> matrix, ArrayList<Integer> current) {
		ArrayList<Integer> resultList = new ArrayList<Integer>();
		for (int x = 0; x < matrix.size(); x++) {
			for (int y = 0; y < matrix.get(x).size(); y++) {
				for (int z = 0; z < current.size(); z++) {
					Integer ires = (Integer) matrix.get(x, y, z) - current.get(z);
					resultList.add(ires);
				}
			}
		}
		return resultList;
	}

	private static int makeSumOfList(ArrayList<Integer> list) {
		int res = 0;
		for (Integer i : list) {
			int a = (int) i;
			res = res + a;
		}
		return res;
	}

	/**
	 * Make a matrix of difference from all stocks.
	 */
	private static Matrix<Integer> makeMatrix(Deduction d, int nr, int sub) {
		ArrayList<ArrayList<Integer>> deduction = d.calc(diffrence[nr]);
		Matrix<Integer> matrix = new Matrix<Integer>();
		for (int x = 0; x < deduction.size(); x++) {
			matrix.add(new ArrayList<ArrayList<Integer>>());
			for (int y = 0; y < deduction.get(x).size(); x++) {
				matrix.get(x).add(new ArrayList<Integer>());
				for (int z = 0; z < diffrence.length; z++) {
					if (z != nr) {
						Integer dgg = deduction.get(x).get(y);
						if (dgg > 0) {
							Integer v = diffrence[z].dif.get(x).get(dgg - sub);
							matrix.get(x, y).add(v);
						}
					}
				}
			}
		}
		return matrix;
	}

	private static int sshort(int nr) {
		Matrix<Integer> matrix = makeMatrix(new Deduction() {
			@Override
			public boolean check(Integer i) {
				return (i > 0);
			}
		}, nr, 1);
		int score = makeSumOfList(makeMarketDifferenceArray(matrix, makeCurrentDifference()));
		return score;
	}

	private static int buy(int nr) {
		Matrix<Integer> matrix = makeMatrix(new Deduction() {
			@Override
			public boolean check(Integer i) {
				return (i < 0);
			}
		}, nr, 1);
		int score = makeSumOfList(makeMarketDifferenceArray(matrix, makeCurrentDifference()));
		return score;
	}
}