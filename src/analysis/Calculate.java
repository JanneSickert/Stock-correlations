package analysis;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * The most important data structure is the matrix object. Is have three Dimensions:
 * Dimensions 1: blocks
 * Dimensions 2: Difference of candle in selected block.
 * Dimensions 3: Difference of all other stocks in blocks.
 * @author janne
 *
 */
public class Calculate {

	final static Scanner sc = new Scanner(System.in);
	static Difference[] diffrence = new Difference[MakeData.stock.length];

	public static void calculate() {
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
		switch (buyOrShort) {
		case 0:
			buy(inputNr);
			break;
		case 1:
			sshort(inputNr);
			break;
		default:
			System.out.println("ERROR: invalid input!");
		}
		for (int i = 0; i < MakeData.stock.length; i++) {
			diffrence[i] = new Difference(MakeData.stock[i]);
		}
	}

	public static class Difference {

		ArrayList<ArrayList<Integer>> dif = new ArrayList<ArrayList<Integer>>();

		Difference(Stock stock) {
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

		public ArrayList<ArrayList<Integer>> calc(Calculate.Difference d) {
			ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
			for (int i = 0; i < d.dif.size(); i++) {
				list.add(new ArrayList<Integer>());
				for (int k = 0; k < list.get(i).size(); k++) {
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

	/**
	 * Make a matrix of difference from all stocks.
	 */
	private static Matrix<Integer> makeMatrix(Deduction d, int nr) {
		ArrayList<ArrayList<Integer>> deduction = d.calc(diffrence[nr]);
		Matrix<Integer> matrix = new Matrix<Integer>();
		for (int x = 0; x < deduction.size(); x++) {
			matrix.add(new ArrayList<ArrayList<Integer>>());
			for (int y = 0; y < deduction.get(x).size(); x++) {
				matrix.get(x).add(new ArrayList<Integer>());
				for (int z = 0; z < diffrence.length; z++) {
					if (z != nr) {
						matrix.get(x, y).add(diffrence[z].dif.get(x).get(deduction.get(x).get(y)));
					}
				}
			}
		}
		return matrix;
	}

	private static void sshort(int nr) {
		Matrix<Integer> matrix = makeMatrix(new Deduction() {
			@Override
			public boolean check(Integer i) {
				return (i > 0);
			}
		}, nr);
	}

	private static void buy(int nr) {
		Matrix<Integer> matrix = makeMatrix(new Deduction() {
			@Override
			public boolean check(Integer i) {
				return (i < 0);
			}
		}, nr);
	}
}