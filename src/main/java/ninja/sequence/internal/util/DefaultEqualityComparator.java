package ninja.sequence.internal.util;

import ninja.sequence.delegate.EqualityComparator;

public class DefaultEqualityComparator<T> implements EqualityComparator<T> {
	@Override
	public boolean equals(T first, T second) {
		return first == second || first != null && first.equals(second);
	}
}
