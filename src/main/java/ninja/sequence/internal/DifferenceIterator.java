package ninja.sequence.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ninja.sequence.contract.Check;
import ninja.sequence.delegate.EqualityComparator;

public class DifferenceIterator<TSource> extends AbstractIterator<TSource, TSource> {
	private final Iterator<? extends TSource> other;
	private final EqualityComparator<? super TSource> comparator;
	private final List<TSource> others = new ArrayList<TSource>();

	public DifferenceIterator(Iterator<? extends TSource> parent, Iterator<? extends TSource> other, EqualityComparator<? super TSource> comparator) {
		super(parent);

		this.other = Check.argumentNotNull(other, "other must not be null.");
		this.comparator = Check.argumentNotNull(comparator, "comparator must not be null.");
	}

	@Override
	protected TSource computeNext() {
		while (this.parent.hasNext()) {
			TSource parentElement = this.parent.next();

			if (!otherContains(parentElement)) {
				return parentElement;
			}
		}

		return computationEnd();
	}

	private boolean otherContains(TSource parentElement) {
		while (this.other.hasNext()) {
			this.others.add(this.other.next());
		}

		for (TSource otherElement : this.others) {
			if (this.comparator.equals(otherElement, parentElement)) {
				return true;
			}
		}

		return false;
	}
}
