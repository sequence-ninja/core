package ninja.sequence.internal;

import java.util.Iterator;
import java.util.Map;

import ninja.sequence.GroupedSequence;
import ninja.sequence.contract.Check;
import ninja.sequence.delegate.Func;
import ninja.sequence.delegate.Func2;

public class JoinIterator<TSource, TInner, TKey, TResult> extends AbstractIterator<TSource, TResult> {
	private final Map<TKey, GroupedSequence<TKey, TInner>> map;

	private final Func<? super TSource, ? extends TKey> outerKeySelector;
	private final Func2<? super TSource, ? super TInner, ? extends TResult> resultSelector;

	private TSource outerElement;
	private Iterator<TInner> iterator;

	public JoinIterator(Iterator<? extends TSource> parent, Map<TKey, GroupedSequence<TKey, TInner>> map,
		Func<? super TSource, ? extends TKey> outerKeySelector, Func2<? super TSource, ? super TInner, ? extends TResult> resultSelector) {

		super(parent);

		this.outerKeySelector = Check.argumentNotNull(outerKeySelector, "outerKeySelector must not be null.");
		this.resultSelector = Check.argumentNotNull(resultSelector, "resultSelector must not be null.");
		this.map = Check.argumentNotNull(map, "map must not be null.");
	}

	@Override
	protected TResult computeNext() {
		if (this.outerElement != null && this.iterator != null && this.iterator.hasNext()) {
			return this.resultSelector.invoke(this.outerElement, this.iterator.next());
		}

		while (super.parent.hasNext()) {
			this.outerElement = super.parent.next();
			TKey outerKey = this.outerKeySelector.invoke(this.outerElement);

			if (outerKey != null) {
				GroupedSequence<TKey, TInner> groupedTraversable = this.map.get(outerKey);

				if (groupedTraversable != null) {
					this.iterator = groupedTraversable.iterator();

					return this.resultSelector.invoke(this.outerElement, this.iterator.next());
				}
			}
		}

		return computationEnd();
	}
}
