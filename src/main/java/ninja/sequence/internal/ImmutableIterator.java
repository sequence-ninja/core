package ninja.sequence.internal;

import java.util.Iterator;

public abstract class ImmutableIterator<T> implements Iterator<T> {
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
