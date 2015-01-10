package ninja.sequence.internal.util;

import ninja.sequence.delegate.EqualityComparator;

public class Key<T> {
	private final T value;
	private final EqualityComparator<? super T> comparator;

	public Key(T value, EqualityComparator<? super T> comparator) {
		this.value = value;
		this.comparator = comparator;
	}

	public T getValue() {
		return this.value;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || getClass() != other.getClass()) {
			return false;
		}

		Key<T> key = (Key<T>)other;

		if (this.comparator != null) {
			return this.comparator.equals(this.value, key.value);
		}

		return !(value != null ? !value.equals(key.value) : key.value != null);
	}

	@Override
	public int hashCode() {
		return 0;
	}
}
