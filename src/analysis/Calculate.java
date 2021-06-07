package analysis;

import java.util.ArrayList;

/**
 * Step 1: Make difference of start price and end price of every candle from every stock. I will call this data strut cl.
 * Step 2: Select a stock and watch for + or - differences in cl. The indexes will be return in a array.
 * Step 3: Look for the next index behind the current index of step 2. And get the difference of start price and end price from cl. The differences also will be return in a array.
 * Step 4: Make step 3 for every stock. Create a array called bi.
 * Step 5: Get the current market situation by the current pixel of every stock.
 * Step 6: Look for the current market situation of every stock except the selected stock.
 * Step 7: Subtract the current difference of every stock with the differences of the past.
 * Step 8: The results of step 7 will be count together.
 * @author janne
 *
 */
public class Calculate {
	
	private final int NR_OF_STOCKS = 413;
	private final int MAX_PIXEL = 862;
	private final int STOP = 9;
	private final int EN = Integer.MAX_VALUE;

	private int[][] cl = new int[NR_OF_STOCKS][MAX_PIXEL];
	private int[][] bi = new int[NR_OF_STOCKS][MAX_PIXEL];
	
	Calculate(ArrayList<MakeData.ForCal> list) {
		for (int i = 0; i < NR_OF_STOCKS; i++) {
			for (int k = 0; k < MAX_PIXEL; k++) {
				cl[i][k] = EN;
			}
		}
		for (MakeData.ForCal mfc : list) {
			cl[mfc.stock][mfc.index] = mfc.first - mfc.last;
		}
	}
	
	
	private abstract class Difference{
		
		abstract boolean check(int nr);
		
		/**
		 * 
		 * @param stock index
		 * @return index array not values
		 */
		int[] step2(int stock) {
			int[] res = new int[MAX_PIXEL];
			for (int k = 0; k < MAX_PIXEL; k++) {
				res[k] = EN;
			}
			int d = 0;
			for (int i = 0; i < MAX_PIXEL; i++) {
				int v = cl[stock][i];
				if (!(v == EN)) {
					if (check(v)) {
						res[d] = i;
						d++;
					}
				}
			}
			return res;
		}
	}
	
	
	private int getNextValueBehind(int stock, int candle) {
		int ret;
		int i = 1;
		while (candle - i >= 0 && cl[stock][candle - i] == EN) {
			if (i == STOP) {
				break;
			}
			i++;
		}
		if (i == STOP) {
			ret = EN;
		} else {
			ret = cl[stock][candle - i];
		}
		return ret;
	}
	
	private int[] getCurrentMarketSituation() {
		int[] res = new int[NR_OF_STOCKS];
		for (int i = 0; i < NR_OF_STOCKS; i++) {
			res[i] = 0;
		}
		for (int i = 0; i < NR_OF_STOCKS; i++) {
			int ende = MAX_PIXEL - 1 - STOP;
			for (int k = MAX_PIXEL - 1; k >= ende; k--) {
				if (cl[i][k] != EN) {
					res[i] = cl[i][k];
					break;
				}
			}
		}
		return res;
	}
	
	private int dis(int v1, int v2) {
		int result = v1 - v2;
		if (result < 0) {
			result = result * (-1);
		}
		return result;
	}
	
	public int make(int stock, boolean buy) {
		int[] arr = null;// This is a index array.
		if (buy) {
			arr = new Difference() {
				@Override
				boolean check(int nr) {
					return (nr > 0);
				}
			}.step2(stock);
		} else {
			arr = new Difference() {
				@Override
				boolean check(int nr) {
					return (nr < 0);
				}
			}.step2(stock);
		}
		for (int i = 0; i < NR_OF_STOCKS; i++) {
			for (int k = 0; k < MAX_PIXEL; k++) {
				bi[i][k] = EN;
			}
		}
		for (int i = 0; i < NR_OF_STOCKS; i++) {
			for (int k = 0; arr[k] != EN; k++) {
				bi[i][k] = getNextValueBehind(i, arr[k]);
			}
		}
		int[] currentMarketSituation = getCurrentMarketSituation();
		int[] resultPerStock = new int[NR_OF_STOCKS];
		int v1, v2;
		for (int i = 0; i < NR_OF_STOCKS; i++) {
			v2 = currentMarketSituation[i];
			for (int k = 0; k < MAX_PIXEL; k++) {
				v1 = bi[i][k];
				if (v1 != EN) {
					resultPerStock[i] = resultPerStock[i] + dis(v1, v2);
				}
			}
		}
		int result = 0;
		for (int i = 0; i < NR_OF_STOCKS; i++) {
			if (resultPerStock[i] != EN) {
				result += resultPerStock[i];
			}
		}
		return result;
	}
}