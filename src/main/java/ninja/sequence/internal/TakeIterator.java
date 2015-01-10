package ninja.sequence.internal;

import java.util.Iterator;

public class TakeIterator<TSource> extends AbstractIterator<TSource, TSource> {
	private long remaining;

	public TakeIterator(Iterator<TSource> parent, long limit) {
		super(parent);

		this.remaining = limit;
	}

	@Override
	protected TSource computeNext() {
		if (super.parent.hasNext() && this.remaining-- > 0) {
			return this.parent.next();
		}

		return computationEnd();
	}
}