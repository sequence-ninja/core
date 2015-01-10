package ninja.sequence.internal;

import java.util.Iterator;

import ninja.sequence.contract.Check;

public class ConcatIterator<TSource> extends AbstractIterator<TSource, TSource> {
	private final Iterator<? extends TSource> other;

	public ConcatIterator(Iterator<? extends TSource> first, Iterator<? extends TSource> second) {
		super(first);

		this.other = Check.argumentNotNull(second, "second must not be null.");
	}

	@Override
	protected TSource computeNext() {
		if (super.parent.hasNext()) {
			return super.parent.next();
		}

		if (this.other.hasNext()) {
			return this.other.next();
		}

		return computationEnd();
	}
}
