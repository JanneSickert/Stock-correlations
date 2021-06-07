package analysis;

import java.util.ArrayList;

public class Sort<T> {
	
	public static class Ele<T> {
		int nr;
		T e;
	}
	
	private ArrayList<Ele<T>> list = new ArrayList<Ele<T>>();
	private ArrayList<Ele<T>> sort_list = new ArrayList<Ele<T>>();
	private int size = 0;
	
	public void addItem(T e, int i) {
		Ele<T> element = new Ele<T>();
		element.nr = i;
		element.e = e;
		list.add(element);
		size++;
	}
	
	public void sortElements() {
		for (int q = 0; q < size; q++) {
			int i = 0, ci = 0;
			Ele<T> ls = list.get(0);
			do {
				if (ls.nr > list.get(i).nr) {
					ls = list.get(i);
					ci = i;
				}
				i++;
			} while (i < size - q);
			sort_list.add(ls);
			list.remove(ci);
		}
	}
	
	public int getSize() {
		return size;
	}
	
	public ArrayList<Ele<T>> getReverseList() {
		ArrayList<Ele<T>> cl = new ArrayList<Ele<T>>();
		int k = sort_list.size() - 1;
		while (k >= 0) {
			cl.add(sort_list.get(k));
			k--;
		}
		return cl;
	}
}