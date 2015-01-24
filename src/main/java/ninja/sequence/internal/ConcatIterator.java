package ninja.sequence.internal;

import java.util.Iterator;

import ninja.sequence.contract.Check;

public class ConcatIterator<T> extends AbstractIterator<T, T> {
	private final Iterator<? extends T> other;

	public ConcatIterator(Iterator<? extends T> first, Iterator<? extends T> second) {
		super(first);

		this.other = Check.argumentNotNull(second, "second must not be null.");
	}

	@Override
	protected T computeNext() {
		if (super.parent.hasNext()) {
			return super.parent.next();
		}

		if (this.other.hasNext()) {
			return this.other.next();
		}

		return computationEnd();
	}
}
