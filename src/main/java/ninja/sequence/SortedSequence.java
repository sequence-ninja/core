package ninja.sequence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import ninja.sequence.contract.Check;
import ninja.sequence.delegate.Func;
import ninja.sequence.internal.MapIterator;
import ninja.sequence.internal.util.DefaultComparator;
import ninja.sequence.monad.Indexed;

public class SortedSequence<T, K> extends Sequence<T> {
	private final List<Comparator<? super T>> comparators;

	public static <T, K> SortedSequence<T, K> create(Iterable<T> source, Func<? super T, ? extends K> keySelector,
		Comparator<? super K> comparator, boolean descending) {

		return new SortedSequence<T, K>(
			new OrderedIterable<T, K>(source, keySelector, comparator, descending)
		);
	}

	private SortedSequence(OrderedIterable<T, K> source) {
		super(source);

		this.comparators = source.comparators;
	}

	public final SortedSequence<T, K> thenBy(Func<? super T, ? extends K> keySelector) {
		return thenBy(keySelector, null);
	}

	public final SortedSequence<T, K> thenBy(Func<? super T, ? extends K> keySelector, Comparator<? super K> comparator) {
		return new SortedSequence<T, K>(
			new OrderedIterable<T, K>(this, keySelector, comparator, false, this.comparators)
		);
	}

	public final SortedSequence<T, K> thenByDescending(Func<? super T, ? extends K> keySelector) {
		return thenByDescending(keySelector, null);
	}

	public final SortedSequence<T, K> thenByDescending(Func<? super T, ? extends K> keySelector, Comparator<? super K> comparator) {
		return new SortedSequence<T, K>(
			new OrderedIterable<T, K>(this, keySelector, comparator, true, this.comparators)
		);
	}

	private static class OrderedIterable<TSource, TKey> implements Iterable<TSource> {
		private final Iterable<TSource> source;
		private final List<Comparator<? super TSource>> comparators;

		public OrderedIterable(Iterable<TSource> source, final Func<? super TSource, ? extends TKey> keySelector,
			final Comparator<? super TKey> comparator, final boolean descending) {

			this(source, keySelector, comparator, descending, new ArrayList<Comparator<? super TSource>>());
		}

		private Comparator<? super TKey> ensureComparator(Comparator<? super TKey> comparator) {
			if (comparator == null) {
				return new DefaultComparator<TKey>();
			}

			return comparator;
		}

		public OrderedIterable(Iterable<TSource> source, final Func<? super TSource, ? extends TKey> keySelector,
			final Comparator<? super TKey> comparator, final boolean descending, List<Comparator<? super TSource>> comparators) {

			Check.argumentNotNull(source, "source must not be null.");
			Check.argumentNotNull(keySelector, "keySelector must not be null.");

			this.source = source;

			final Comparator<? super TKey> comparer = ensureComparator(comparator);

			comparators.add(
				new Comparator<TSource>() {
					@Override
					public int compare(TSource a, TSource b) {
						return (descending ? -1 : 1) * comparer.compare(
							keySelector.invoke(a), keySelector.invoke(b)
						);
					}
				}
			);

			this.comparators = comparators;
		}

		@Override
		public Iterator<TSource> iterator() {
			List<Indexed<TSource>> items = new ArrayList<Indexed<TSource>>();

			int index = 0;
			for (TSource item : this.source) {
				items.add(new Indexed<TSource>(index++, item));
			}

			Collections.sort(items,
				new Comparator<Indexed<TSource>>() {
					@Override
					public int compare(Indexed<TSource> a, Indexed<TSource> b) {
						for (Comparator<? super TSource> comparator : comparators) {
							int result = comparator.compare(a.getValue(), b.getValue());

							if (result != 0) {
								return result;
							}
						}

						// preserve initial order
						return Long.valueOf(a.getIndex()).compareTo(b.getIndex());
					}
				}
			);

			return new MapIterator<Indexed<TSource>, TSource>(items.iterator(),
				new Func<Indexed<TSource>, TSource>() {
					@Override
					public TSource invoke(Indexed<TSource> item) {
						return item.getValue();
					}
				}
			);
		}
	}
}
