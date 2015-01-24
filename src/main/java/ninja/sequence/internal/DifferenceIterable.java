package ninja.sequence.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ninja.sequence.delegate.EqualityComparator;

public class DifferenceIterable<T> implements Iterable<T> {
	private final Iterable<? extends T> parent;
	private final Iterable<? extends T> other;

	private final EqualityComparator<? super T> comparator;

	public DifferenceIterable(Iterable<? extends T> parent, Iterable<? extends T> other, EqualityComparator<? super T> comparator) {
		this.parent = parent;
		this.other = other;
		this.comparator = comparator;
	}

	@Override
	public Iterator<T> iterator() {
		return new DifferenceIterator(this.parent.iterator(), this.other.iterator());
	}

	private class DifferenceIterator extends AbstractIterator<T, T> {
		private final Iterator<? extends T> other;

		private final List<T> others = new ArrayList<T>();

		public DifferenceIterator(Iterator<? extends T> parent, Iterator<? extends T> other) {
			super(parent);

			this.other = other;
		}

		@Override
		protected T computeNext() {
			while (super.parent.hasNext()) {
				T parentElement = super.parent.next();

				if (!otherContains(parentElement)) {
					return parentElement;
				}
			}

			return computationEnd();
		}

		private boolean otherContains(T parentElement) {
			while (this.other.hasNext()) {
				this.others.add(this.other.next());
			}

			for (T otherElement : this.others) {
				if (comparator.equals(otherElement, parentElement)) {
					return true;
				}
			}

			return false;
		}
	}
}
