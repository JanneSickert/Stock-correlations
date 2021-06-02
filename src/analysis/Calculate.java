package analysis;

import java.util.ArrayList;
import java.util.Scanner;

public class Calculate {

	final static Scanner sc = new Scanner(System.in);
	static Diffrence[] diffrence = new Diffrence[MakeData.stock.length];

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
			diffrence[i] = new Diffrence(MakeData.stock[i]);
		}
	}

	
	public static class Diffrence {
		
		ArrayList<ArrayList<Integer>> dif = new ArrayList<ArrayList<Integer>>();

		Diffrence(Stock stock) {
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

	private static void sshort(int nr) {

	}

	private static void buy(int nr) {

	}
}