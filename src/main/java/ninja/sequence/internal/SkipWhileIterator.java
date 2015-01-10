package ninja.sequence.internal;

import java.util.Iterator;

import ninja.sequence.contract.Check;
import ninja.sequence.delegate.Predicate;

public class SkipWhileIterator<TSource> extends AbstractIterator<TSource, TSource> {
	private boolean skip = true;
	private final Predicate<? super TSource> predicate;

	public SkipWhileIterator(Iterator<? extends TSource> parent, Predicate<? super TSource> predicate) {
		super(parent);

		this.predicate = Check.argumentNotNull(predicate, "predicate must not be null.");
	}

	@Override
	protected TSource computeNext() {
		while (super.parent.hasNext()) {
			TSource next = this.parent.next();

			if (!this.skip) {
				return next;
			}

			this.skip = !this.predicate.invoke(next);
		}

		return computationEnd();
	}
}
