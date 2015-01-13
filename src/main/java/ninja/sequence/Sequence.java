package ninja.sequence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NavigableSet;

import ninja.sequence.contract.Check;
import ninja.sequence.datastructure.Tuple;
import ninja.sequence.delegate.Accumulator;
import ninja.sequence.delegate.EqualityComparator;
import ninja.sequence.delegate.Func;
import ninja.sequence.delegate.Func2;
import ninja.sequence.delegate.Predicate;
import ninja.sequence.internal.ArrayIterable;
import ninja.sequence.internal.ArrayIterator;
import ninja.sequence.internal.BindIterator;
import ninja.sequence.internal.BooleanArrayIterable;
import ninja.sequence.internal.ByteArrayIterable;
import ninja.sequence.internal.CharArrayIterable;
import ninja.sequence.internal.ConcatIterator;
import ninja.sequence.internal.DifferenceIterator;
import ninja.sequence.internal.DistinctIterator;
import ninja.sequence.internal.DoubleArrayIterable;
import ninja.sequence.internal.FloatArrayIterable;
import ninja.sequence.internal.IntArrayIterable;
import ninja.sequence.internal.InvertedListIterator;
import ninja.sequence.internal.JoinIterator;
import ninja.sequence.internal.LongArrayIterable;
import ninja.sequence.internal.MapIterator;
import ninja.sequence.internal.RangeIterator;
import ninja.sequence.internal.RepeatIterator;
import ninja.sequence.internal.SelectIterator;
import ninja.sequence.internal.ShortArrayIterable;
import ninja.sequence.internal.SkipIterator;
import ninja.sequence.internal.SkipWhileIterator;
import ninja.sequence.internal.TakeIterator;
import ninja.sequence.internal.TakeWhileIterator;
import ninja.sequence.internal.ZipAllIterator;
import ninja.sequence.internal.ZipIterator;
import ninja.sequence.internal.util.DefaultComparator;
import ninja.sequence.internal.util.DefaultEqualityComparator;
import ninja.sequence.internal.util.Key;
import ninja.sequence.monad.Option;
import ninja.sequence.util.Funcs;

// document what operation the function is doing, eager or lazy

public class Sequence<T> implements Iterable<T> {
	private static final Sequence<Object> EMPTY = new Sequence<Object>(
		new Iterable<Object>() {
			@Override
			public Iterator<Object> iterator() {
				return Collections.emptyIterator();
			}
		}
	);

	private final Iterable<T> source;

	protected Sequence(Iterable<T> source) {
		this.source = source;
	}

	/**
	 * Returns an empty sequence.
	 *
	 * @param <T> the type of the sequence elements
	 * @return an empty sequence
	 */
	@SuppressWarnings("unchecked")
	public static <T> Sequence<T> empty() {
		return (Sequence<T>)EMPTY;
	}

	/**
	 * Creates a new Sequence out of an iterable with the elements in the same
	 * order as when iterating over the iterable.
	 *
	 * @param iterable the iterable to create the new sequence from
	 * @param <T> the type of the sequence elements
	 * @return the new sequence
	 * @throws IllegalArgumentException if {@code iterable} is {@code null}
	 */
	public static <T> Sequence<T> of(Iterable<T> iterable) {
		if (iterable == null) {
			throw new IllegalArgumentException("iterable must not be null.");
		}

		return new Sequence<T>(iterable);
	}

	/**
	 * Creates a new Sequence out of a map with the elements in the same order
	 * as when iterating over the entry set of the supplied map.
	 *
	 * @param map the map to create the new sequence from
	 * @param <K> the key type of the map
	 * @param <V> the element type of the map
	 * @return the new sequence
	 * @throws IllegalArgumentException if {@code map} is {@code null}
	 */
	public static <K, V> Sequence<Map.Entry<K, V>> of(Map<K, V> map) {
		if (map == null) {
			throw new IllegalArgumentException("map must not be null.");
		}

		return new Sequence<Map.Entry<K, V>>(map.entrySet());
	}

	/**
	 * Creates a new Sequence whose elements are the specified values in order.
	 *
	 * @param values the elements of the new sequence
	 * @param <T> the type of the sequence elements
	 * @return the new sequence
	 */
	@SafeVarargs
	@SuppressWarnings("varargs")
	public static <T> Sequence<T> of(T... values) {
		return new Sequence<T>(new ArrayIterable<T>(values));
	}

	/**
	 * Creates a new Sequence out of an array of primitive booleans in the same order.
	 *
	 * @param array the boolean array to create the new sequence from
	 * @return the new sequence
	 * @throws IllegalArgumentException if {@code array} is {@code null}
	 */
	public static Sequence<Boolean> of(final boolean[] array) {
		if (array == null) {
			throw new IllegalArgumentException("array must not be null.");
		}

		return new Sequence<Boolean>(new BooleanArrayIterable(array));
	}

	/**
	 * Creates a new Sequence out of an array of primitive bytes in the same order.
	 *
	 * @param array the byte array to create the new sequence from
	 * @return the new sequence
	 * @throws IllegalArgumentException if {@code array} is {@code null}
	 */
	public static Sequence<Byte> of(final byte[] array) {
		if (array == null) {
			throw new IllegalArgumentException("array must not be null.");
		}

		return new Sequence<Byte>(new ByteArrayIterable(array));
	}

	/**
	 * Creates a new Sequence out of an array of primitive chars in the same order.
	 *
	 * @param array the char array to create the new sequence from
	 * @return the new sequence
	 * @throws IllegalArgumentException if {@code array} is {@code null}
	 */
	public static Sequence<Character> of(final char[] array) {
		if (array == null) {
			throw new IllegalArgumentException("array must not be null.");
		}

		return new Sequence<Character>(new CharArrayIterable(array));
	}

	/**
	 * Creates a new Sequence out of an array of primitive doubles in the same order.
	 *
	 * @param array the double array to create the new sequence from
	 * @return the new sequence
	 * @throws IllegalArgumentException if {@code array} is {@code null}
	 */
	public static Sequence<Double> of(final double[] array) {
		if (array == null) {
			throw new IllegalArgumentException("array must not be null.");
		}

		return new Sequence<Double>(new DoubleArrayIterable(array));
	}

	/**
	 * Creates a new Sequence out of an array of primitive floats in the same order.
	 *
	 * @param array the float array to create the new sequence from
	 * @return the new sequence
	 * @throws IllegalArgumentException if {@code array} is {@code null}
	 */
	public static Sequence<Float> of(final float[] array) {
		if (array == null) {
			throw new IllegalArgumentException("array must not be null.");
		}

		return new Sequence<Float>(new FloatArrayIterable(array));
	}

	/**
	 * Creates a new Sequence out of an array of primitive ints in the same order.
	 *
	 * @param array the int array to create the new sequence from
	 * @return the new sequence
	 * @throws IllegalArgumentException if {@code array} is {@code null}
	 */
	public static Sequence<Integer> of(final int[] array) {
		if (array == null) {
			throw new IllegalArgumentException("array must not be null.");
		}

		return new Sequence<Integer>(new IntArrayIterable(array));
	}

	/**
	 * Creates a new Sequence out of an array of primitive longs in the same order.
	 *
	 * @param array the long array to create the new sequence from
	 * @return the new sequence
	 * @throws IllegalArgumentException if {@code array} is {@code null}
	 */
	public static Sequence<Long> of(final long[] array) {
		if (array == null) {
			throw new IllegalArgumentException("array must not be null.");
		}

		return new Sequence<Long>(new LongArrayIterable(array));
	}

	/**
	 * Creates a new Sequence out of an array of primitive shorts in the same order.
	 *
	 * @param array the short array to create the new sequence from
	 * @return the new sequence
	 * @throws IllegalArgumentException if {@code array} is {@code null}
	 */
	public static Sequence<Short> of(final short[] array) {
		if (array == null) {
			throw new IllegalArgumentException("array must not be null.");
		}

		return new Sequence<Short>(new ShortArrayIterable(array));
	}

	/**
	 *
	 * @param from
	 * @param count
	 * @return
	 */
	public static Sequence<Integer> range(final int from, final int count) {
		if (count < 0) {
			throw new IllegalArgumentException("count must be greater or equal 0.");
		}

		if ((long)from + count - 1> Integer.MAX_VALUE) {
			throw new IllegalArgumentException("The total numbers to process exceeds the maximum possible value.");
		}

		return new Sequence<Integer>(
			new Iterable<Integer>() {
				@Override
				public Iterator<Integer> iterator() {
					return new RangeIterator(count, from);
				}
			}
		);
	}

	/**
	 *
	 * @param value
	 * @param count
	 * @param <T>
	 * @return
	 */
	public static <T> Sequence<T> repeat(final T value, final int count) {
		if (count < 0) {
			throw new IllegalArgumentException("count must be greater or equal 0.");
		}

		return new Sequence<T>(
			new Iterable<T>() {
				@Override
				public Iterator<T> iterator() {
					return new RepeatIterator<T>(value, count);
				}
			}
		);
	}

	/**
	 * Aggregates all the elements of this sequence by calling the Accumulator one time for each element.
	 * Passes both the element of the traversable and the aggregated value of the previous invocation of the
	 * Accumultator. The first .... initial value
	 *
	 * A.k.a. reduce left
	 *
	 * @param accumulator
	 * @throws java.lang.IllegalArgumentException if the specified accumulator is null
	 * @return
	 */
	public final Option<T> aggregate(Accumulator<T, ? super T> accumulator) {
		Check.argumentNotNull(accumulator, "accumulator must not be null.");

		Iterator<? extends T> iterator = this.source.iterator();

		if (!iterator.hasNext()) {
			return Option.none();
		}

		return Option.some(aggregate(iterator, iterator.next(), accumulator, Funcs.<T>forward()));
	}

	/**
	 *
	 *
	 * A.k.a. fold left
	 *
	 * @param identity
	 * @param accumulator
	 * @param <TIdentity>
	 * @throws java.lang.IllegalArgumentException if the specified accumulator is null
	 * @return
	 */
	public final <TIdentity> TIdentity aggregate(TIdentity identity, Accumulator<TIdentity, ? super T> accumulator) {
		return aggregate(this.source.iterator(), identity, accumulator, Funcs.<TIdentity>forward());
	}

	/**
	 *
	 *
	 * A.k.a. fold left
	 *
	 * @param identity
	 * @param accumulator
	 * @param map
	 * @param <TIdentity>
	 * @param <TResult>
	 * @throws java.lang.IllegalArgumentException if the specified accumulator is null
	 * @return
	 */
	public final <TIdentity, TResult> TResult aggregate(TIdentity identity, Accumulator<TIdentity, ? super T> accumulator, Func<TIdentity, ? extends TResult> map) {
		return aggregate(this.source.iterator(), identity, accumulator, map);
	}

	private <TSeed, TResult> TResult aggregate(Iterator<? extends T> source, TSeed seed, Accumulator<TSeed, ? super T> accumulator, Func<TSeed, ? extends TResult> map) {
		Check.argumentNotNull(accumulator, "accumulator must not be null.");
		Check.argumentNotNull(map, "map must not be null.");

		TSeed result = seed;

		while (source.hasNext()) {
			result = accumulator.accumulate(result, source.next());
		}

		return map.invoke(result);
	}

	/**
	 * Determines whether all elements of this traversable satisfy a condition.
	 * The iteration is stopped as soon as the result can be determined, e.g. first
	 * element found that does not satsify the condition.
	 *
	 * @param predicate A function to test each element for a condition.
	 * @throws java.lang.IllegalArgumentException if the specified predicate is null
	 * @return {@code true} if every element of this sequence passes the test
	 * in the specified predicate, or if this traversable does not contain any
	 * elements; otherwise, {@code false}
	 */
	public final boolean all(Predicate<? super T> predicate) {
		Check.argumentNotNull(predicate, "predicate must not be null.");

		for (T element : this.source) {
			if (!predicate.invoke(element)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Determines whether this traversable contains any elements. The iteration
	 * is stopped as soon as the result can be determined.
	 *
	 * A.k.a. contains
	 *
	 * @return true if this sequence contains any elements; otherwise, false.
	 */
	public final boolean any() {
		return this.source.iterator().hasNext();
	}

	/**
	 * Determines whether a sequence contains any elements. The iteration is
	 * stopped as soon as the result can be determined, e.g. first element
	 * found that does satisfy the condition.
	 *
	 * @param predicate A function to test each element for a condition.
	 * @throws java.lang.IllegalArgumentException if the specified predicate is null
	 * @return true if this traversable contains any elements; otherwise, false.
	 */
	public final boolean any(Predicate<? super T> predicate) {
		return select(predicate).any();
	}

	/**
	 * Returns an array containing all the elements of this traversable in proper
	 * sequence. The query is immediatly evaluated.
	 *
	 * @param allocator A function that provides the length of this sequence, to produce a new
	 *                  array of the given type.
	 * @throws java.lang.IllegalArgumentException if the specified function is null
	 * @throws java.lang.NullPointerException if the resulting array of the function
	 * is null
	 * @throws java.lang.UnsupportedOperationException if the length of the resulting
	 * array of the function is less then the amount elements of this traversable
	 * @return an array containing the elements of this traversable
	 */
	public final T[] asArray(Func<Integer, T[]> allocator) {
		Check.argumentNotNull(allocator, "allocator must not be null.");

		ArrayList<T> list = asArrayList();

		T[] result = allocator.invoke(list.size());

		if (result == null) {
			throw new NullPointerException("The result of the allocator must not be null.");
		}

		if (result.length < list.size()) {
			throw new UnsupportedOperationException(
				"Length of the returned array must be equal or greater than: " + list.size());
		}

		return list.toArray(result);
	}

	/**
	 * Returns an {@code ArrayList} containing all the elements of this traversable
	 * in proper sequence. The query is immediatly evaluated.
	 *
	 * @return an {@code ArrayList} containing the elements of this traversable
	 */
	public final ArrayList<T> asArrayList() {
		return asCollection(new ArrayList<T>());
	}

	/**
	 *
	 * @param collection
	 * @param <R>
	 * @throws java.lang.IllegalArgumentException if the specified collection is null
	 * @return
	 */
	public final <R extends Collection<T>> R asCollection(R collection) {
		Check.argumentNotNull(collection, "collection must not be null.");

		for (T element : this.source) {
			collection.add(element);
		}

		return collection;
	}

	/**
	 * Returns a {@code HashMap} according to the specified key selector function.
	 *
	 * @param keySelector A function to extract the key of an element
	 * @param <K> The type of the key
	 * @throws java.lang.IllegalArgumentException if the specified keySelector is null
	 * @return a {@code HashMap} containing ... key and its element....
	 */
	public final <K> HashMap<K, T> asHashMap(Func<? super T, ? extends K> keySelector) {
		return asHashMap(keySelector, Funcs.<T>forward());
	}

	/**
	 *
	 * @param keySelector
	 * @param elementSelector
	 * @param <K>
	 * @param <V>
	 * @throws java.lang.IllegalArgumentException if the specified keySelector or elementSelector is null
	 * @return
	 */
	public final <K, V> HashMap<K, V> asHashMap(Func<? super T, ? extends K> keySelector, Func<? super T, ? extends V> elementSelector) {
		return asMap(new HashMap<K, V>(), keySelector, elementSelector);
	}

	/**
	 *
	 * @return
	 */
	public final HashSet<T> asHashSet() {
		return asHashSet(Funcs.<T>forward());
	}

	/**
	 *
	 * @param valueSelector
	 * @param <E>
	 * @throws java.lang.IllegalArgumentException if the specified keySelector is null
	 * @return
	 */
	public final <E> HashSet<E> asHashSet(Func<? super T, ? extends E> valueSelector) {
		Check.argumentNotNull(valueSelector, "valueSelector must not be null.");

		return map(valueSelector).asCollection(new HashSet<E>());
	}

	/**
	 *
	 * @param map
	 * @param keySelector
	 * @param <K>
	 * @param <R>
	 * @throws java.lang.IllegalArgumentException if the specified map or keySelector is null
	 * @return
	 */
	public final <K, R extends Map<K, T>> R asMap(R map, Func<? super T, ? extends K> keySelector) {
		Check.argumentNotNull(map, "map must not be null.");
		Check.argumentNotNull(keySelector, "keySelector must not be null.");

		for (T element : source) {
			map.put(keySelector.invoke(element), element);
		}

		return map;
	}

	/**
	 *
	 * @param map
	 * @param keySelector
	 * @param elementSelector
	 * @param <K>
	 * @param <V>
	 * @param <R>
	 * @throws java.lang.IllegalArgumentException if the specified map, keySelector or elementSelector is null
	 * @return
	 */
	public final <K, V, R extends Map<K, V>> R asMap(R map, Func<? super T, ? extends K> keySelector, Func<? super T, ? extends V> elementSelector) {
		Check.argumentNotNull(map, "map must not be null.");
		Check.argumentNotNull(keySelector, "keySelector must not be null.");
		Check.argumentNotNull(elementSelector, "elementSelector must not be null.");

		for (T element : source) {
			map.put(keySelector.invoke(element), elementSelector.invoke(element));
		}

		return map;
	}

	/**
	 * Projects each element of a sequence to an IEnumerable<T> and flattens the resulting sequences into one sequence.
	 * The SelectMany<TSource, TResult>(IEnumerable<TSource>, Func<TSource, IEnumerable<TResult>>) method enumerates
	 * the input sequence, uses a transform function to map each element to an IEnumerable<T>, and then enumerates and
	 * yields the elements of each such IEnumerable<T> object. That is, for each element of source, selector is invoked
	 * and a sequence of values is returned. SelectMany<TSource, TResult>(IEnumerable<TSource>, Func<TSource, IEnumerable<TResult>>)
	 * then flattens this two-dimensional collection of collections into a one-dimensional IEnumerable<T> and returns it.
	 * For example, if a query uses SelectMany<TSource, TResult>(IEnumerable<TSource>, Func<TSource, IEnumerable<TResult>>)
	 * to obtain the orders (of type Order) for each customer in a database, the result is of type IEnumerable<Order> in
	 * C# or IEnumerable(Of Order) in Visual Basic. If instead the query uses Select to obtain the orders, the collection
	 * of collections of orders is not combined and the result is of type IEnumerable<List<Order>> in C# or I
	 * Enumerable(Of List(Of Order)) in Visual Basic.
	 * A.k.a. flat, flatMap, flatten
	 * @param collectionSelector
	 * @param <R>
	 * @throws java.lang.IllegalArgumentException if the specified resultSelector is null
	 * @return
	 */
	public final <R> Sequence<R> bind(final Func<? super T, ? extends Sequence<? extends R>> collectionSelector) {
		Check.argumentNotNull(collectionSelector, "collectionSelector must not be null.");

		return new Sequence<R>(
			new Iterable<R>() {
				@Override
				public Iterator<R> iterator() {
					return new BindIterator<T, R>(source.iterator(), collectionSelector);
				}
			}
		);
	}

	/**
	 * Concatenates two sequences. The Concat<TSource>(IEnumerable<TSource>, IEnumerable<TSource>) method differs of
	 * the Union method because the Concat<TSource>(IEnumerable<TSource>, IEnumerable<TSource>) method returns all the
	 * original elements in the input sequences. The Union method returns only unique elements.
	 * @param other
	 * @throws java.lang.IllegalArgumentException if other is null
	 * @return
	 */
	public final Sequence<T> concat(T... other) {
		Check.argumentNotNull(other, "other must not be null.");

		return concat(array(other));
	}

	/**
	 *
	 * @param other
	 * @throws java.lang.IllegalArgumentException if other is null
	 * @return
	 */
	public final Sequence<T> concat(final Iterable<? extends T> other) {
		Check.argumentNotNull(other, "other must not be null.");

		return new Sequence<T>(
			new Iterable<T>() {
				@Override
				public Iterator<T> iterator() {
					return new ConcatIterator<T>(source.iterator(), other.iterator());
				}
			}
		);
	}

	/**
	 * Returns the number of elements in this traversable.
	 *
	 * @return the number of elements in this traversable
	 */
	public final long count() {
		if (this.source instanceof Collection) {
			return ((Collection)this.source).size();
		}

		return aggregate(0,
			new Accumulator<Integer, T>() {
				@Override
				public Integer accumulate(Integer count, T item) {
					return count + 1;
				}
			}
		);
	}

	/**
	 * Returns the number of elements in this traversable that satisifes a condition.
	 *
	 * @param predicate A function to test each element for a condition
	 * @throws java.lang.IllegalArgumentException if the specified predicate is null
	 * @return the number of elements in this traversable that satisifes a condition
	 */
	public final long count(final Predicate<? super T> predicate) {
		return select(predicate).count();
	}

	/**
	 * The set difference of two sets is defined as the members of the first set that do not appear in the second set.
	 * @param other An IEnumerable<T> whose elements that also occur in the first sequence will cause those elements to
	 *              be removed of the returned sequence.
	 * @throws java.lang.IllegalArgumentException if other is null
	 * @return
	 */
	public final Sequence<T> difference(T... other) {
		Check.argumentNotNull(other, "other must not be null.");

		return difference(array(other));
	}

	/**
	 *
	 * @param other
	 * @throws java.lang.IllegalArgumentException if other is null
	 * @return
	 */
	public final Sequence<T> difference(Iterable<? extends T> other) {
		return difference(other, new DefaultEqualityComparator<T>());
	}

	/**
	 *
	 * @param other
	 * @param comparator
	 * @throws java.lang.IllegalArgumentException if other or the comparator is null
	 * @return
	 */
	public final Sequence<T> difference(final Iterable<? extends T> other, final EqualityComparator<? super T> comparator) {
		Check.argumentNotNull(other, "other must not be null.");
		Check.argumentNotNull(comparator, "comparator must not be null.");

		return new Sequence<T>(
			new Iterable<T>() {
				@Override
				public Iterator<T> iterator() {
					return new DifferenceIterator<T>(source.iterator(), other.iterator(), comparator);
				}
			}
		);
	}

	/**
	 * Returns distinct elements of a sequence by using the default equality comparer to compare values.
	 * The Distinct<TSource>(IEnumerable<TSource>) method returns an unordered sequence that contains no duplicate values.
	 * It uses the default equality comparer, Default, to compare values.
	 *
	 * The default equality comparer, Default, is used to compare values of the types that implement the IEquatable<T>
	 * generic interface. To compare a custom data type, you must implement this interface and provide your own
	 * GetHashCode and Equals methods for the type.
	 * @return
	 */
	public final Sequence<T> distinct() {
		return distinct(new DefaultEqualityComparator<T>());
	}

	/**
	 *
	 * @param comparator
	 * @throws java.lang.IllegalArgumentException if the comparator is null
	 * @return
	 */
	public final Sequence<T> distinct(final EqualityComparator<? super T> comparator) {
		Check.argumentNotNull(comparator, "comparator must not be null.");

		return new Sequence<T>(
			new Iterable<T>() {
				@Override
				public Iterator<T> iterator() {
					return new DistinctIterator<T>(source.iterator(), comparator);
				}
			}
		);
	}

	/**
	 * Returns an {@link ninja.sequence.monad.Option} with the first element of
	 * this sequence or an {@code Option} of none if this sequence does not have
	 * any elements.
	 *
	 * @return an {@code Option} with the first element of this sequence or an
	 * {@code Option} of none if this sequence does not have any elements
	 */
	public final Option<T> first() {
		Iterator<T> iterator = this.source.iterator();

		if (iterator.hasNext()) {
			return Option.some(iterator.next());
		}

		return Option.none();
	}

	/**
	 * Returns an {@link ninja.sequence.monad.Option} with the first element in
	 * this sequence that satisfies a condition or an {@code Option} of none if
	 * this sequence does not have any elements or none of the elements satsify
	 * the condition.
	 *
	 * @param predicate A function to test each element for a condition
	 * @throws java.lang.IllegalArgumentException if the specified predicate is null
	 * @return an {@code Option} with the first element in this sequence that
	 * satisfies a condition or an{@code Option} of none if this sequence does
	 * not have any elements or none of the elements  satsify the condition
	 */
	public final Option<T> first(Predicate<? super T> predicate) {
		return select(predicate).first();
	}

	/**
	 *
	 * @param keySelector
	 * @param <K>
	 * @return
	 */
	public final <K> Sequence<GroupedSequence<K, T>> groupBy(Func<? super T, ? extends K> keySelector) {
		return groupBy(keySelector, new DefaultEqualityComparator<K>());
	}

	/**
	 *
	 * @param keySelector
	 * @param comparator
	 * @param <K>
	 * @return
	 */
	public final <K> Sequence<GroupedSequence<K, T>> groupBy(Func<? super T, ? extends K> keySelector, EqualityComparator<? super K> comparator) {
		return groupBy(keySelector, Funcs.<T>forward(), comparator);
	}

	/**
	 *
	 * @param keySelector
	 * @param elementSelector
	 * @param <K>
	 * @param <R>
	 * @return
	 */
	public final <K, R> Sequence<GroupedSequence<K, R>> groupBy(Func<? super T, ? extends K> keySelector, Func<? super T, ? extends R> elementSelector) {
		return groupBy(keySelector, elementSelector, new DefaultEqualityComparator<K>());
	}

	/**
	 *
	 * @param keySelector
	 * @param elementSelector
	 * @param comparator
	 * @param <K>
	 * @param <R>
	 * @return
	 */
	public final <K, R> Sequence<GroupedSequence<K, R>> groupBy(Func<? super T, ? extends K> keySelector, Func<? super T, ? extends R> elementSelector, EqualityComparator<? super K> comparator) {
		Check.argumentNotNull(keySelector, "keySelector must not be null.");
		Check.argumentNotNull(elementSelector, "elementSelector must not be null.");
		Check.argumentNotNull(comparator, "comparator must not be null.");

		// HashMap allows to have null keys. Thats just fine for the grouping.
		// TODO: Test this behaviour!
		Map<Key<K>, List<R>> map = new HashMap<Key<K>, List<R>>();

		for (T source : this.source) {
			Key<K> key = new Key<K>(keySelector.invoke(source), comparator);

			List<R> values = map.get(key);

			if (values == null) {
				values = new ArrayList<R>();
				map.put(key, values);
			}

			values.add(elementSelector.invoke(source));
		}

		return new Sequence<Map.Entry<Key<K>, List<R>>>(map.entrySet()).map(
			new Func<Map.Entry<Key<K>, List<R>>, GroupedSequence<K, R>>() {
				@Override
				public GroupedSequence<K, R> invoke(Map.Entry<Key<K>, List<R>> entry) {
					return GroupedSequence.create(entry.getKey().getValue(), entry.getValue());
				}
			}
		);
	}

	/**
	 * Produces the set intersection of two sequences by using the default equality comparer to compare values.
	 * The intersection of two sets A and B is defined as the set that contains all the elements of A that also appear
	 * in B, but no other elements.
	 * When the object returned by this method is enumerated, Intersect enumerates first, collecting all distinct
	 * elements of that sequence. It then enumerates second, marking those elements that occur in both sequences.
	 * Finally, the marked elements are yielded in the order in which they were collected.

	 The default equality comparer, Default, is used to compare values of the types that implement the IEqualityComparer<T>
	 generic interface. To compare a custom data type, you need to implement this interface and provide your own GetHashCode
	 and Equals methods for the type.

	 * @param other
	 * @throws java.lang.IllegalArgumentException if other is null
	 * @return
	 */
	public final Sequence<T> intersect(T... other) {
		Check.argumentNotNull(other, "other must not be null.");

		return intersect(array(other));
	}

	/**
	 *
	 * @param other
	 * @return
	 */
	public final Sequence<T> intersect(Iterable<? extends T> other) {
		return intersect(other, new DefaultEqualityComparator<T>());
	}

	private <T> Sequence<T> create(Iterable<T> source) {
		return new Sequence<T>(source);
	}

	/**
	 *
	 * @param other
	 * @param comparator
	 * @throws java.lang.IllegalArgumentException if other or the comparator is null
	 * @return
	 */
	public final Sequence<T> intersect(Iterable<? extends T> other, final EqualityComparator<? super T> comparator) {
		final List<Key<T>> keys = new ArrayList<Key<T>>();
		final Map<Key<T>, Boolean> flags = new HashMap<Key<T>, Boolean>();

		// TODO: refactor

		for (Key<T> item : map(
			new Func<T, Key<T>>() {
				@Override
				public Key<T> invoke(T arg) {
					return new Key<T>(arg, comparator);
				}
			}
		).select(
			new Predicate<Key<T>>() {
				@Override
				public boolean invoke(Key<T> item) {
					return !flags.containsKey(item);
				}
			}
		)
			) {
			flags.put(item, false);
			keys.add(item);
		}

		Sequence<? extends T> ts = create(other);

		for (Key<T> item : create(other).map(
			new Func<T, Key<T>>() {
				@Override
				public Key<T> invoke(T item) {
					return new Key<T>(item, comparator);
				}
			}
		)) {
			if (flags.containsKey(item)) {
				flags.put(item, true);
			}
		}

		return create(keys).select(
			new Predicate<Key<T>>() {
				@Override
				public boolean invoke(Key<T> arg) {
					return flags.get(arg);
				}
			}).map(
			new Func<Key<T>, T>() {
				@Override
				public T invoke(Key<T> arg) {
					return arg.getValue();
				}
			}
		);
	}

	/**
	 *
	 * @param inner
	 * @param outerKeySelector
	 * @param innerKeySelector
	 * @param f
	 * @param <TInner>
	 * @param <K>
	 * @param <R>
	 * @return
	 */
	public final <TInner, K, R> Sequence<R> join(Iterable<TInner> inner, final Func<? super T, ? extends K> outerKeySelector, final Func<? super TInner, ? extends K> innerKeySelector, final Func2<? super T, ? super TInner, ? extends R> f) {
		final Map<K, GroupedSequence<K, TInner>> map = new Sequence<TInner>(inner).groupBy(innerKeySelector).asHashMap(
			new Func<GroupedSequence<K, TInner>, K>() {
				@Override
				public K invoke(GroupedSequence<K, TInner> arg) {
					return arg.getKey();
				}
			}
		);

		return new Sequence<R>(
			new Iterable<R>() {
				@Override
				public Iterator<R> iterator() {
					return new JoinIterator<T, TInner, K, R>(source.iterator(), map, outerKeySelector, f);
				}
			}
		);
	}

	/**
	 *
	 * @param inner
	 * @param outerKey
	 * @param innerKey
	 * @param f
	 * @param comparator
	 * @param <TInner>
	 * @param <K>
	 * @param <R>
	 * @return
	 */
	public final <TInner, K, R> Sequence<R> join(Iterable<TInner> inner, Func<? super T, ? extends K> outerKey,
											Func<? super TInner, ? extends K> innerKey, Func2<? super T, ? super TInner, ? extends R> f,
											EqualityComparator<? super K> comparator) {

		return null;
	}

	/**
	 * Returns an {@link ninja.sequence.monad.Option} with the last element of
	 * this sequence or an {@code Option} of none if this sequence does not have
	 * any elements.
	 *
	 * @return an {@code Option} with the last element of this sequence or an
	 * {@code Option} of none if this sequence does not have any elements
	 */
	public final Option<T> last() {
		if (this.source instanceof List) {
			List<T> list = (List<T>)this.source;

			return !list.isEmpty() ? Option.some(list.get(list.size() - 1)) : Option.<T>none();
		}

		Iterator<? extends T> iterator = this.source.iterator();

		if (!iterator.hasNext()) {
			return Option.none();
		}

		T last = iterator.next();
		while (iterator.hasNext()) {
			last = iterator.next();
		}

		return Option.some(last);
	}

	/**
	 * Returns an {@link ninja.sequence.monad.Option} with the last element in
	 * this sequence that satisfies a condition or an {@code Option} of none if
	 * this sequence does not have any elements or none of the elements satsify
	 * the condition.
	 *
	 * @param predicate A function to test each element for a condition
	 * @throws java.lang.IllegalArgumentException if the specified predicate is null
	 * @return an {@code Option} with the last element in this sequence that satisfies
	 * a condition or an{@code Option} of none if this sequence does not have any
	 * elements or none of the elements satsify the condition
	 */
	public final Option<T> last(Predicate<? super T> predicate) {
		return select(predicate).last();
	}

	/**
	 * Projects each element of a sequence into a new form.
	 * This projection method requires the transform function, selector, to produce one value for each value in the
	 * source sequence, source. If selector returns a value that is itself a collection, it is up to the consumer to
	 * traverse the subsequences manually. In such a situation, it might be better for your query to return a single
	 * coalesced sequence of values. To achieve this, use the SelectMany method instead of Select. Although SelectMany
	 * works similarly to Select, it differs in that the transform function returns a collection that is then expanded
	 * by SelectMany before it is returned.
	 *
	 * In query expression syntax, a select (Visual C#) or Select (Visual Basic) clause translates to an invocation of Select.
	 *
	 * @param resultSelector
	 * @param <R>
	 * @throws java.lang.IllegalArgumentException if the specified resultSelector is null
	 * @return
	 */
	public final <R> Sequence<R> map(final Func<? super T, ? extends R> resultSelector) {
		return new Sequence<R>(
			new Iterable<R>() {
				@Override
				public Iterator<R> iterator() {
					return new MapIterator<T, R>(source.iterator(), resultSelector);
				}
			}
		);
	}

	/**
	 * Prepends one ore more values to a sequence.
	 * @param other
	 * @throws java.lang.IllegalArgumentException if other is null
	 * @return Returns a sequence where one or more values is prepended to it.

	 */
	@SafeVarargs
	public final Sequence<T> prepend(T... other) {
		Check.argumentNotNull(other, "other must not be null.");

		return prepend(array(other));
	}

	/**
	 *
	 * @param other
	 * @return
	 */
	public final Sequence<T> prepend(final Iterable<? extends T> other) {
		Check.argumentNotNull(other, "other must not be null.");

		return new Sequence<T>(
			new Iterable<T>() {
				@Override
				public Iterator<T> iterator() {
					return new ConcatIterator<T>(other.iterator(), source.iterator());
				}
			}
		);
	}

	/**
	 * Inverts the order of the elements in a sequence. Unlike OrderBy, this sorting method does not consider the actual
	 * values themselves in determining the order. Rather, it just returns the elements in the reverse order of which they are produced by the underlying source.

	 * @throws java.lang.IllegalArgumentException if other is null
	 * @return A sequence whose elements correspond to those of the input sequence in reverse order.
	 */
	public final Sequence<T> reverse() {
		Iterable<T> iterable;

		if (source instanceof List) {
			iterable = new Iterable<T>() {
				@Override
				public Iterator<T> iterator() {
					List<T> list = ((List<T>)source);

					return new InvertedListIterator<T>(list.listIterator(list.size()));
				}
			};
		} else if (this.source instanceof NavigableSet) {
			iterable = new Iterable<T>() {
				@Override
				public Iterator<T> iterator() {
					return ((NavigableSet<T>)source).descendingIterator();
				}
			};
		} else {
			final ListIterator<T> listIterator = new ArrayList<T>().listIterator();

			for (T element : source) {
				listIterator.add(element);
			}

			iterable = new Iterable<T>() {
				@Override
				public Iterator<T> iterator() {
					return new InvertedListIterator<T>(listIterator);
				}
			};
		}

		return new Sequence<T>(iterable);
	}

	/**
	 * Filters a sequence of values based on a predicate.
	 * The default implementation of this method is using deferred execution.
	 *
	 * A.k.a filter, where
	 *
	 * @param predicate A function to test each element for a condition.
	 * @throws java.lang.IllegalArgumentException if the specified predicate is null
	 * @return An IEnumerable<T> that contains elements of the input sequence that satisfy the condition.
	 */
	public final Sequence<T> select(final Predicate<? super T> predicate) {
		return new Sequence<T>(
			new Iterable<T>() {
				@Override
				public Iterator<T> iterator() {
					return new SelectIterator<T>(source.iterator(), predicate);
				}
			}
		);
	}

	/**
	 *
	 * @param count The number of elements to skip in this sequence.
	 * @return
	 */
	public final Sequence<T> skip(final long count) {
		return new Sequence<T>(
			new Iterable<T>() {
				@Override
				public Iterator<T> iterator() {
					return new SkipIterator<T>(source.iterator(), count);
				}
			}
		);
	}

	/**
	 *
	 * @param predicate
	 * @throws java.lang.IllegalArgumentException if the specified predicate is null
	 * @return
	 */
	public final Sequence<T> skipWhile(final Predicate<? super T> predicate) {
		Check.argumentNotNull(predicate, "predicate must not be null.");

		return new Sequence<T>(
			new Iterable<T>() {
				@Override
				public Iterator<T> iterator() {
					return new SkipWhileIterator<T>(source.iterator(), predicate);
				}
			}
		);
	}

	/**
	 *
	 * @param count The number of elements to skip in this sequence.
	 * @return
	 */
	public final Sequence<T> take(final long count) {
		return new Sequence<T>(
			new Iterable<T>() {
				@Override
				public Iterator<T> iterator() {
					return new TakeIterator<T>(source.iterator(), count);
				}
			}
		);
	}

	/**
	 *
	 * @param predicate
	 * @throws java.lang.IllegalArgumentException if the specified predicate is null
	 * @return
	 */
	public final Sequence<T> takeWhile(final Predicate<? super T> predicate) {
		return new Sequence<T>(
			new Iterable<T>() {
				@Override
				public Iterator<T> iterator() {
					return new TakeWhileIterator<T>(source.iterator(), predicate);
				}
			}
		);
	}

	/**
	 *
	 * @param keySelector
	 * @param <K>
	 * @throws java.lang.IllegalArgumentException if the specified keySelector is null
	 * @return
	 */
	public final <K> SortedSequence<T, K> sortBy(Func<? super T, ? extends K> keySelector) {
		return sortBy(keySelector, new DefaultComparator<K>());
	}

	/**
	 *
	 * @param keySelector
	 * @param comparator
	 * @param <K>
	 * @return
	 */
	public final <K> SortedSequence<T, K> sortBy(Func<? super T, ? extends K> keySelector, Comparator<? super K> comparator) {
		return SortedSequence.create(this, keySelector, comparator, false);
	}

	/**
	 *
	 * @param keySelector
	 * @param <K>
	 * @return
	 */
	public final <K> SortedSequence<T, K> sortByDescending(Func<? super T, ? extends K> keySelector) {
		return sortByDescending(keySelector, null);
	}

	/**
	 *
	 * @param keySelector
	 * @param comparator
	 * @param <K>
	 * @return
	 */
	public final <K> SortedSequence<T, K> sortByDescending(Func<? super T, ? extends K> keySelector, Comparator<? super K> comparator) {
		return SortedSequence.create(this, keySelector, comparator, true);
	}

	/**
	 *
	 * @param other
	 * @return
	 */
	public final Sequence<T> union(T... other) {
		return union(array(other));
	}

	/**
	 *
	 * @param other
	 * @return
	 */
	public final Sequence<T> union(Iterable<? extends T> other) {
		return concat(other).distinct();
	}

	/**
	 *
	 * @param other
	 * @param comparator
	 * @return
	 */
	public final Sequence<T> union(Iterable<? extends T> other, EqualityComparator<? super T> comparator) {
		return concat(other).distinct(comparator);
	}

	/**
	 *
	 * @param other
	 * @param <TOther>
	 * @return
	 */
	public final <TOther> Sequence<Tuple<T, TOther>> zip(TOther... other) {
		return zip(array(other));
	}

	/**
	 *
	 * @param other
	 * @param <TOther>
	 * @return
	 */
	public final <TOther> Sequence<Tuple<T, TOther>> zip(final Iterable<? extends TOther> other) {
		return new Sequence<Tuple<T, TOther>>(
			new Iterable<Tuple<T, TOther>>() {
				@Override
				public Iterator<Tuple<T, TOther>> iterator() {
					return new ZipIterator<T, TOther>(source.iterator(), other.iterator());
				}
			}
		);
	}

	/**
	 *
	 * @param other
	 * @param f
	 * @param <TOther>
	 * @param <R>
	 * @return
	 */
	public final <TOther, R> Sequence<R> zip(Iterable<? extends TOther> other, Func2<? super T, ? super TOther, ? extends R> f) {
		return null;
	}

	/**
	 *
	 * @param other
	 * @param <TOther>
	 * @return
	 */
	public final <TOther> Sequence<Tuple<Option<T>, Option<TOther>>> zipAll(TOther... other) {
		return zipAll(array(other));
	}

	/**
	 *
	 * @param other
	 * @param <TOther>
	 * @return
	 */
	public final <TOther> Sequence<Tuple<Option<T>, Option<TOther>>> zipAll(final Iterable<? extends TOther> other) {
		return new Sequence<Tuple<Option<T>, Option<TOther>>>(
			new Iterable<Tuple<Option<T>, Option<TOther>>>() {
				@Override
				public Iterator<Tuple<Option<T>, Option<TOther>>> iterator() {
					return new ZipAllIterator<T, TOther>(source.iterator(), other.iterator());
				}
			}
		);
	}

	/**
	 *
	 * @param other
	 * @param f
	 * @param <TOther>
	 * @param <R>
	 * @return
	 */
	public final <TOther, R> Sequence<R> zipAll(Iterable<? extends TOther> other, Func2<Option<? super T>, Option<? super TOther>, ? extends R> f) {
		return null;
	}


	@Override
	public Iterator<T> iterator() {
		return this.source.iterator();
	}

	private <T> Iterable<T> array(final T... elements) {
		return new Iterable<T>() {
			@Override
			public Iterator<T> iterator() {
				return new ArrayIterator<T>(elements);
			}
		};
	}
}
