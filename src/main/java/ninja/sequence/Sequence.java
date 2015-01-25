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
import ninja.sequence.internal.DifferenceIterable;
import ninja.sequence.internal.DistinctIterator;
import ninja.sequence.internal.DoubleArrayIterable;
import ninja.sequence.internal.FloatArrayIterable;
import ninja.sequence.internal.IntArrayIterable;
import ninja.sequence.internal.InvertedListIterator;
import ninja.sequence.internal.JoinIterator;
import ninja.sequence.internal.LongArrayIterable;
import ninja.sequence.internal.MapIterator;
import ninja.sequence.internal.RangeIterable;
import ninja.sequence.internal.RepeatIterable;
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
	 * @param <T> the type of the sequence elements
	 * @param iterable the iterable to create the new sequence from
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
	 * @param <K> the key type of the map
	 * @param <V> the element type of the map
	 * @param map the map to create the new sequence from
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
	 * @param <T> the type of the sequence elements
	 * @param values the elements of the new sequence
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
	public static Sequence<Boolean> of(boolean[] array) {
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
	public static Sequence<Byte> of(byte[] array) {
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
	public static Sequence<Character> of(char[] array) {
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
	public static Sequence<Double> of(double[] array) {
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
	public static Sequence<Float> of(float[] array) {
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
	public static Sequence<Integer> of(int[] array) {
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
	public static Sequence<Long> of(long[] array) {
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
	public static Sequence<Short> of(short[] array) {
		if (array == null) {
			throw new IllegalArgumentException("array must not be null.");
		}

		return new Sequence<Short>(new ShortArrayIterable(array));
	}

	/**
	 * Creates a new Sequence of numbers within a specified range.
	 *
	 * @param from the number to start the range from
	 * @param count the amount of sequential numbers to generate
	 * @return the new sequence containing the given range of numbers
	 * @throws IllegalArgumentException if {@code count} is less than 0 or the total numbers to generate
	 *                                  exceeds the maximum value of an integer
	 */
	public static Sequence<Integer> range(final int from, final int count) {
		if (count < 0) {
			throw new IllegalArgumentException("count must be greater or equal 0.");
		}

		if ((long)from + count - 1> Integer.MAX_VALUE) {
			throw new IllegalArgumentException("The total numbers to process exceeds the maximum possible value.");
		}

		return new Sequence<Integer>(new RangeIterable(count, from));
	}

	/**
	 * Creates a new Sequence containing the specified {@code value} repeatedly.
	 *
	 * @param <T> the type of the sequence elements
	 * @param value the value to be repeated
	 * @param count the number of times the value shoud be repeated
	 * @return the new sequence containing the given value repeated multiple times
	 * @throws IllegalArgumentException if {@code count} is less than 0
	 */
	public static <T> Sequence<T> repeat(final T value, final int count) {
		if (count < 0) {
			throw new IllegalArgumentException("count must be greater or equal 0.");
		}

		return new Sequence<T>(new RepeatIterable<T>(value, count));
	}

	/**
	 * Aggregates the elements of this sequence by calling the {@code accumulator} for each element.
	 *
	 * If there is more than 1 element in the sequence to aggregate, the first element is taken as the seed
	 * and is used together with the next element as the arguments to the {@code Accumulator}.
	 * For any further iterations, the result of the previous aggregation, together with the next element
	 * in the sequence is used as aguments to {@code Accumuator}.
	 *
	 * A.k.a. reduce left
	 *
	 * @param accumulator the accumulator to aggregate the elements
	 * @return the aggregated result or the first element if this sequence holds exactly 1
	 *         element or an option of none if this sequence is empty
	 * @throws IllegalArgumentException if the specified {@code accumulator} function is null
	 */
	public final Option<T> aggregate(Accumulator<T, ? super T> accumulator) {
		if (accumulator == null) {
			throw new IllegalArgumentException("accumulator must not be null.");
		}

		Iterator<? extends T> iterator = this.source.iterator();

		if (!iterator.hasNext()) {
			return Option.none();
		}

		return Option.some(aggregate(iterator, iterator.next(), accumulator, Funcs.<T>forward()));
	}

	/**
	 * Aggregates the elements of this sequence by calling the {@code accumulator} for each element.
	 * The specified {@code seed} is the value used as the initial value for the accumulator.
	 *
	 * A.k.a. fold left
	 *
	 * @param <S> the type of the seed
	 * @param seed the value used as the initial value
	 * @param accumulator the accumulator to aggregate the elements
	 * @return the aggregated result or the {@code seed} if this sequence is empty
	 * @throws IllegalArgumentException if the specified {@code accumulator} function is null
	 */
	public final <S> S aggregate(S seed, Accumulator<S, ? super T> accumulator) {
		return aggregate(this.source.iterator(), seed, accumulator, Funcs.<S>forward());
	}

	/**
	 * Aggregates the elements of this sequence by calling the {@code accumulator} for each element and
	 * maps the final result.
	 * The specified {@code seed} is the value used as the initial value for the accumulator.
	 *
	 * A.k.a. fold left
	 *
	 * @param <S> the type of the seed
	 * @param <R> the type of the result
	 * @param seed the value used as the initial value
	 * @param accumulator the accumulator to aggregate the elements
	 * @param map the mapping function used to map the final aggregated result
	 * @return the aggregated and mapped result or the mapped {@code seed} if this sequence is empty
	 * @throws IllegalArgumentException if the specified {@code accumulator} function is null
	 * @throws IllegalArgumentException if the specified {@code map} function is null
	 */
	public final <S, R> R aggregate(S seed, Accumulator<S, ? super T> accumulator, Func<S, ? extends R> map) {
		return aggregate(this.source.iterator(), seed, accumulator, map);
	}

	private <S, R> R aggregate(Iterator<? extends T> source, S seed, Accumulator<S, ? super T> accumulator, Func<S, ? extends R> map) {
		if (accumulator == null) {
			throw new IllegalArgumentException("accumulator must not be null.");
		}

		if (map == null) {
			throw new IllegalArgumentException("map must not be null.");
		}

		S result = seed;

		while (source.hasNext()) {
			result = accumulator.accumulate(result, source.next());
		}

		return map.invoke(result);
	}

	/**
	 * Determines whether all elements of this sequence satisfy a condition.
	 * The iteration is stopped as soon as the result can be determined, e.g. first
	 * element found that does not satsify the condition.
	 *
	 * A.k.a. every
	 *
	 * @param predicate a function to test each element for a condition.
	 * @return {@code true} if every element of this sequence passes the test
	 *         in the specified predicate, or if this sequence does not contain any
	 *         elements; otherwise, {@code false}
	 * @throws IllegalArgumentException if the specified predicate is null
	 */
	public final boolean all(Predicate<? super T> predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException("predicate must not be null.");
		}

		for (T element : this.source) {
			if (!predicate.invoke(element)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Determines whether this sequence contains any elements. The iteration
	 * is stopped as soon as the result can be determined.
	 *
	 * A.k.a. isEmpty
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
	 * A.k.a. contains
	 *
	 * @param predicate A function to test each element for a condition.
	 * @return true if this traversable contains any elements; otherwise, false.
	 * @throws IllegalArgumentException if the specified predicate is null
	 */
	public final boolean any(Predicate<? super T> predicate) {
		return select(predicate).any();
	}

	/**
	 * Returns an array containing all the elements of this sequence in proper
	 * order.
	 *
	 * The query is immediatly evaluated.
	 *
	 * @param allocator A function that provides the length of this sequence, to produce a new
	 *                  array of the given type.
	 * @return an array containing the elements of this traversable
	 * @throws IllegalArgumentException if the specified function is null
	 * @throws NullPointerException if the resulting array of the function is null
	 * @throws UnsupportedOperationException if the length of the resulting array of the function
	 *                                       is less then the amount elements of this sequence
	 */
	public final T[] asArray(Func<Integer, T[]> allocator) {
		if (allocator == null) {
			throw new IllegalArgumentException("allocator must not be null.");
		}

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
	 * Returns a new {@code ArrayList} containing all the elements of this sequence
	 * in proper order.
	 *
	 * The query is immediatly evaluated.
	 *
	 * @return an {@code ArrayList} containing the elements of this sequence
	 */
	public final ArrayList<T> asArrayList() {
		return fill(new ArrayList<T>());
	}

	/**
	 * Returns a new {@code HashMap} according to the specified key selector function.
	 *
	 * @param <K> The type of the key
	 * @param keySelector a function to extract the key of an element
	 * @return a {@code HashMap} containing ... key and its element....
	 * @throws IllegalArgumentException if the specified keySelector is null
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
	 * @return
	 * @throws IllegalArgumentException if the specified keySelector or elementSelector is null
	 */
	public final <K, V> HashMap<K, V> asHashMap(Func<? super T, ? extends K> keySelector, Func<? super T, ? extends V> elementSelector) {
		return fill(new HashMap<K, V>(), keySelector, elementSelector);
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
	 * @return
	 * @throws IllegalArgumentException if the specified keySelector is null
	 */
	public final <E> HashSet<E> asHashSet(Func<? super T, ? extends E> valueSelector) {
		if (valueSelector == null) {
			throw new IllegalArgumentException("valueSelector must not be null.");
		}

		return map(valueSelector).fill(new HashSet<E>());
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
	 * @return
	 * @throws IllegalArgumentException if the specified resultSelector is null
	 */

	// even though the function of bind should actually return a Sequence, maybe it makes sense to return an iterable instead...
	public final <R> Sequence<R> bind(final Func<? super T, ? extends Sequence<? extends R>> collectionSelector) {
		if (collectionSelector == null) {
			throw new IllegalArgumentException("collectionSelector must not be null.");
		}

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
	 * @return
	 */
	@SafeVarargs
	@SuppressWarnings("varargs")
	public final Sequence<T> concat(T... other) {
		return concat(array(other));
	}

	/**
	 *
	 * @param other
	 * @return
	 * @throws IllegalArgumentException if other is null
	 */
	public final Sequence<T> concat(final Iterable<? extends T> other) {
		if (other == null) {
			throw new IllegalArgumentException("other must not be null.");
		}

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
	 * @return the number of elements in this traversable that satisifes a condition
	 * @throws IllegalArgumentException if the specified predicate is null
	 */
	public final long count(final Predicate<? super T> predicate) {
		return select(predicate).count();
	}

	/**
	 * The set difference of two sets is defined as the members of the first set that do not appear in the second set.
	 * @param other An IEnumerable<T> whose elements that also occur in the first sequence will cause those elements to
	 *              be removed of the returned sequence.
	 * @return
	 */
	public final Sequence<T> difference(T[] other) {
		if (other == null) {
			throw new IllegalArgumentException("other must no be null.");
		}

		return difference(array(other));
	}

	/**
	 *
	 * @param other
	 * @throws IllegalArgumentException if other is null
	 * @return
	 */
	public final Sequence<T> difference(Iterable<? extends T> other) {
		return difference(other, new DefaultEqualityComparator<T>());
	}

	public final Sequence<T> difference(final T[] other, final EqualityComparator<? super T> comparator) {
		return null;
	}

	/**
	 *
	 * @param other
	 * @param comparator
	 * @throws IllegalArgumentException if other or the comparator is null
	 * @return
	 */
	public final Sequence<T> difference(final Iterable<? extends T> other, final EqualityComparator<? super T> comparator) {
		if (other == null) {
			throw new IllegalArgumentException("other must no be null.");
		}

		if (comparator == null) {
			throw new IllegalArgumentException("comparator must no be null.");
		}

		return new Sequence<T>(new DifferenceIterable<T>(this.source, other, comparator));
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
	 * @throws IllegalArgumentException if the comparator is null
	 * @return
	 */
	public final Sequence<T> distinct(final EqualityComparator<? super T> comparator) {
		if (comparator == null) {
			throw new IllegalArgumentException("comparator must not be null.");
		}

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
	 *
	 * @param collection
	 * @param <R>
	 * @return
	 * @throws IllegalArgumentException if the specified collection is null
	 */
	// TODO; rename to something like: fill
	public final <R extends Collection<T>> R fill(R collection) {
		if (collection == null) {
			throw new IllegalArgumentException("collection must not be null.");
		}

		for (T element : this.source) {
			collection.add(element);
		}

		return collection;
	}

	/**
	 *
	 * @param map
	 * @param keySelector
	 * @param <K>
	 * @param <R>
	 * @return
	 * @throws IllegalArgumentException if the specified map or keySelector is null
	 */
	// TODO: rename to something like: fill
	public final <K, R extends Map<K, T>> R fill(R map, Func<? super T, ? extends K> keySelector) {
		return fill(map, keySelector, Funcs.<T>forward());
	}

	/**
	 *
	 * @param map
	 * @param keySelector
	 * @param elementSelector
	 * @param <K>
	 * @param <V>
	 * @param <R>
	 * @return
	 * @throws IllegalArgumentException if the specified map, keySelector or elementSelector is null
	 */
	// TODO: rename to something like: fill
	public final <K, V, R extends Map<K, V>> R fill(R map, Func<? super T, ? extends K> keySelector, Func<? super T, ? extends V> elementSelector) {
		if (map == null) {
			throw new IllegalArgumentException("map must not be null.");
		}

		if (keySelector == null) {
			throw new IllegalArgumentException("keySelector must not be null.");
		}

		if (elementSelector == null) {
			throw new IllegalArgumentException("elementSelector must not be null.");
		}

		for (T element : this.source) {
			map.put(keySelector.invoke(element), elementSelector.invoke(element));
		}

		return map;
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
		if (keySelector == null) {
			throw new IllegalArgumentException("keySelector must not be null.");
		}

		if (elementSelector == null) {
			throw new IllegalArgumentException("elementSelector must not be null.");
		}

		if (comparator == null) {
			throw new IllegalArgumentException("comparator must not be null.");
		}

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
	 * @throws IllegalArgumentException if other is null
	 * @return
	 */
	public final Sequence<T> intersect(T[] other) {
		if (other == null) {
			throw new IllegalArgumentException("other must no be null.");
		}

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

	/**
	 *
	 * @param other
	 * @param comparator
	 * @return
	 */
	public final Sequence<T> intersect(T[] other, final EqualityComparator<? super T> comparator) {
		return intersect(array(other), comparator);
	}

	/**
	 *
	 * @param other
	 * @param comparator
	 * @throws IllegalArgumentException if other or the comparator is null
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

	private <T> Sequence<T> create(Iterable<T> source) {
		return new Sequence<T>(source);
	}

	/**
	 *
	 * @param <T2>
	 * @param <K>
	 * @param <R>
	 * @param inner
	 * @param outerKeySelector
	 * @param innerKeySelector
	 * @param f
	 * @return
	 */
	public final <T2, K, R> Sequence<R> join(T2[] inner, Func<? super T, ? extends K> outerKeySelector, Func<? super T2, ? extends K> innerKeySelector, Func2<? super T, ? super T2, ? extends R> f) {
		return join(array(inner), outerKeySelector, innerKeySelector, f);
	}

	/**
	 *
	 * @param <T2>
	 * @param <K>
	 * @param <R>
	 * @param inner
	 * @param outerKeySelector
	 * @param innerKeySelector
	 * @param f
	 * @return
	 */
	public final <T2, K, R> Sequence<R> join(Iterable<T2> inner, final Func<? super T, ? extends K> outerKeySelector, final Func<? super T2, ? extends K> innerKeySelector, final Func2<? super T, ? super T2, ? extends R> f) {
		final Map<K, GroupedSequence<K, T2>> map = new Sequence<T2>(inner).groupBy(innerKeySelector).asHashMap(
			new Func<GroupedSequence<K, T2>, K>() {
				@Override
				public K invoke(GroupedSequence<K, T2> arg) {
					return arg.getKey();
				}
			}
		);

		return new Sequence<R>(
			new Iterable<R>() {
				@Override
				public Iterator<R> iterator() {
					return new JoinIterator<T, T2, K, R>(source.iterator(), map, outerKeySelector, f);
				}
			}
		);
	}

	/**
	 *
	 * @param <T2>
	 * @param <K>
	 * @param <R>
	 * @param inner
	 * @param outerKey
	 * @param innerKey
	 * @param f
	 * @param comparator
	 * @return
	 */
	public final <T2, K, R> Sequence<R> join(T2[] inner, Func<? super T, ? extends K> outerKey,
											 Func<? super T2, ? extends K> innerKey, Func2<? super T, ? super T2, ? extends R> f,
											 EqualityComparator<? super K> comparator) {

		return join(array(inner), outerKey, innerKey, f, comparator);
	}

	/**
	 *
	 * @param <T2>
	 * @param <K>
	 * @param <R>
	 * @param inner
	 * @param outerKey
	 * @param innerKey
	 * @param f
	 * @param comparator
	 * @return
	 */
	public final <T2, K, R> Sequence<R> join(Iterable<T2> inner, Func<? super T, ? extends K> outerKey,
											Func<? super T2, ? extends K> innerKey, Func2<? super T, ? super T2, ? extends R> f,
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
	 * @param <R>
	 * @param resultSelector
	 * @throws IllegalArgumentException if the specified resultSelector is null
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
	 * @return Returns a sequence where one or more values is prepended to it.

	 */
	@SafeVarargs
	@SuppressWarnings("varargs")
	public final Sequence<T> prepend(T... other) {
		return prepend(array(other));
	}

	/**
	 *
	 * @param other
	 * @return
	 */
	public final Sequence<T> prepend(final Iterable<? extends T> other) {
		if (other == null) {
			throw new IllegalArgumentException("other must not be null.");
		}

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

	 * @throws IllegalArgumentException if other is null
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
	 * A.k.a. offset
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
	 * @throws IllegalArgumentException if the specified predicate is null
	 * @return
	 */
	public final Sequence<T> skipWhile(final Predicate<? super T> predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException("predicate must not be null.");
		}

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
	 * A.k.a head
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
	 * @throws IllegalArgumentException if the specified predicate is null
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
	 * A.k.a. orderBy
	 *
	 * @param <K>
	 * @param keySelector
	 * @throws IllegalArgumentException if the specified keySelector is null
	 * @return
	 */
	public final <K> SortedSequence<T, K> sortBy(Func<? super T, ? extends K> keySelector) {
		return sortBy(keySelector, new DefaultComparator<K>());
	}

	/**
	 *
	 * A.k.a. orderBy
	 *
	 * @param <K>
	 * @param keySelector
	 * @param comparator
	 * @return
	 */
	public final <K> SortedSequence<T, K> sortBy(Func<? super T, ? extends K> keySelector, Comparator<? super K> comparator) {
		return SortedSequence.create(this, keySelector, comparator, false);
	}

	/**
	 *
	 * A.k.a. orderByDescending
	 *
	 * @param <K>
	 * @param keySelector
	 * @return
	 */
	public final <K> SortedSequence<T, K> sortByDescending(Func<? super T, ? extends K> keySelector) {
		return sortByDescending(keySelector, null);
	}

	/**
	 *
	 * A.k.a. orderByDescending
	 *
	 * @param <K>
	 * @param keySelector
	 * @param comparator
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
	public final Sequence<T> union(T[] other) {
		if (other == null) {
			throw new IllegalArgumentException("other must no be null.");
		}

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
	public final Sequence<T> union(T[] other, EqualityComparator<? super T> comparator) {
		return concat(other).distinct(comparator);
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
	 * @param <T2>
	 * @param other
	 * @return
	 */
	public final <T2> Sequence<Tuple<T, T2>> zip(T2[] other) {
		if (other == null) {
			throw new IllegalArgumentException("other must no be null.");
		}

		return zip(array(other));
	}

	/**
	 *
	 * @param <T2>
	 * @param other
	 * @return
	 */
	public final <T2> Sequence<Tuple<T, T2>> zip(final Iterable<? extends T2> other) {
		return new Sequence<Tuple<T, T2>>(
			new Iterable<Tuple<T, T2>>() {
				@Override
				public Iterator<Tuple<T, T2>> iterator() {
					return new ZipIterator<T, T2>(source.iterator(), other.iterator());
				}
			}
		);
	}

	/**
	 *
	 * @param <T2>
	 * @param <R>
	 * @param other
	 * @param f
	 * @return
	 */
	public final <T2, R> Sequence<R> zip(T2[] other, Func2<? super T, ? super T2, ? extends R> f) {
		return zip(array(other), f);
	}

	/**
	 *
	 * @param <T2>
	 * @param <R>
	 * @param other
	 * @param f
	 * @return
	 */
	public final <T2, R> Sequence<R> zip(Iterable<? extends T2> other, Func2<? super T, ? super T2, ? extends R> f) {
		return null;
	}

	/**
	 *
	 * @param <T2>
	 * @param other
	 * @return
	 */
	public final <T2> Sequence<Tuple<Option<T>, Option<T2>>> zipAll(T2[] other) {
		if (other == null) {
			throw new IllegalArgumentException("other must no be null.");
		}

		return zipAll(array(other));
	}

	/**
	 *
	 * @param <T2>
	 * @param other
	 * @return
	 */
	public final <T2> Sequence<Tuple<Option<T>, Option<T2>>> zipAll(final Iterable<? extends T2> other) {
		return new Sequence<Tuple<Option<T>, Option<T2>>>(
			new Iterable<Tuple<Option<T>, Option<T2>>>() {
				@Override
				public Iterator<Tuple<Option<T>, Option<T2>>> iterator() {
					return new ZipAllIterator<T, T2>(source.iterator(), other.iterator());
				}
			}
		);
	}

	/**
	 *
	 * @param <T2>
	 * @param <R>
	 * @param other
	 * @param f
	 * @return
	 */
	public final <T2, R> Sequence<R> zipAll(T2[] other, Func2<Option<? super T>, Option<? super T2>, ? extends R> f) {
		return null;
	}

	/**
	 *
	 * @param <T2>
	 * @param <R>
	 * @param other
	 * @param f
	 * @return
	 */
	public final <T2, R> Sequence<R> zipAll(Iterable<? extends T2> other, Func2<Option<? super T>, Option<? super T2>, ? extends R> f) {
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
