package ninja.sequence.internal;

import java.util.Iterator;

import ninja.sequence.contract.Check;
import ninja.sequence.delegate.Predicate;

public class SelectIterator<TSource> extends AbstractIterator<TSource, TSource> {
	private final Predicate<? super TSource> predicate;

	public SelectIterator(Iterator<? extends TSource> parent, Predicate<? super TSource> predicate) {
		super(parent);

		this.predicate = Check.argumentNotNull(predicate, "predicate must not be null.");
	}

	@Override
	protected TSource computeNext() {
		while (this.parent.hasNext()) {
			TSource element = this.parent.next();

			if (this.predicate.invoke(element)) {
				return element;
			}
		}

		return computationEnd();
	}
}
