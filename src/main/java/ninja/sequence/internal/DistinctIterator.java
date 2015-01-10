package ninja.sequence.internal;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ninja.sequence.delegate.EqualityComparator;
import ninja.sequence.internal.util.Key;

public class DistinctIterator<TSource> extends AbstractIterator<TSource, TSource> {
	private final EqualityComparator<? super TSource> comparator;
	private final Set<Key<TSource>> set = new HashSet<Key<TSource>>();

	public DistinctIterator(Iterator<? extends TSource> parent, EqualityComparator<? super TSource> comparator) {
		super(parent);

		this.comparator = comparator;
	}

	@Override
	protected TSource computeNext() {
		while (super.parent.hasNext()) {
			Key<TSource> key = new Key<TSource>(super.parent.next(), this.comparator);

			if (!this.set.add(key)) {
				continue;
			}

			return key.value;
		}

		return computationEnd();
	}
}
