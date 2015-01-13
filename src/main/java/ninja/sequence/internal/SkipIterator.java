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
			TSource next = this.parent.next();

			if (this.remaining == 0) {
				return next;
			}

			this.remaining--;
		}

		return computationEnd();
	}
}
