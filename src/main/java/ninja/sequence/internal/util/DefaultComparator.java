package ninja.sequence.internal.util;

import java.util.Comparator;

public class DefaultComparator<T> implements Comparator<T> {
	@SuppressWarnings("unchecked")
	public int compare(T first, T second){
		if (first == second) {
			return 0;
		}

		if (first == null) {
			return -1;
		}

		if (second == null) {
			return 1;
		}

		if (first instanceof Comparable) {
			Comparable<T> comparable = (Comparable<T>) first;
			return comparable.compareTo(second);
		}

		throw new UnsupportedOperationException("not comparable");
	}
}
