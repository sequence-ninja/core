package ninja.sequence.internal;

import java.util.Iterator;

import ninja.sequence.contract.Check;
import ninja.sequence.delegate.Func;

public class MapIterator<TSource, TResult> extends AbstractIterator<TSource, TResult> {
	private final Func<? super TSource, ? extends TResult> resultSelector;

	public MapIterator(Iterator<? extends TSource> parent, Func<? super TSource, ? extends TResult> resultSelector) {
		super(parent);

		this.resultSelector = Check.argumentNotNull(resultSelector, "resultSelector must not be null.");
	}

	@Override
	protected TResult computeNext() {
		if (this.parent.hasNext()) {
			return this.resultSelector.invoke(
				this.parent.next()
			);
		}

		return computationEnd();
	}
}
