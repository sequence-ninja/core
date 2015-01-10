package ninja.sequence.internal;

import java.util.Iterator;

public class SkipIterator<TSource> extends AbstractIterator<TSource, TSource> {
	private long remaining = 0;

	public SkipIterator(Iterator<? extends TSource> parent, long count) {
		super(parent);

		this.remaining = count;
	}

	@Override
	protected TSource computeNext() {
		while (super.parent.hasNext()) {
			if (this.remaining == 0) {
				return this.parent.next();
			}

			this.remaining--;
		}

		return computationEnd();
	}
}
