package ninja.sequence.internal;

import java.util.Iterator;

import ninja.sequence.contract.Check;
import ninja.sequence.delegate.Predicate;

public class TakeWhileIterator<TSource> extends AbstractIterator<TSource, TSource> {
	private final Predicate<? super TSource> predicate;

	public TakeWhileIterator(Iterator<TSource> iterator, Predicate<? super TSource> predicate) {
		super(iterator);

		this.predicate = Check.argumentNotNull(predicate, "predicate must not be null.");
	}

	@Override
	protected TSource computeNext() {
		if (super.parent.hasNext()) {
			TSource next = this.parent.next();

			if (this.predicate.invoke(next)) {
				return this.parent.next();
			}
		}

		return computationEnd();
	}
}