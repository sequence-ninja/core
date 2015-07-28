package ninja.sequence.monad;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import ninja.sequence.contract.Check;
import ninja.sequence.delegate.Action;
import ninja.sequence.delegate.Func;


// TODO: rename none -> empty

/**
 * A wrapper which may or may not contain a non-null value.
 *
 * If a value is set, {@code isPresent()} will return {@code true} and
 * {@code get()} will return the value.
 */
public abstract class Option<T> implements Iterable<T> {
	private static final Option<?> NONE = new None<Object>();

	/**
	 * Creates an instance of none. No value is present in this Option.
	 *
	 * @param <T> Type of the non-existent value
	 * @return a none {@code Option}
	 */
	public static <T> Option<T> none() {
		@SuppressWarnings("unchecked")
		Option<T> none = (None<T>)NONE;

		return none;
	}

	/**
	 * Creates an instance with the value present.
	 *
	 * @param value the value to be present, may be null.
	 */
	public static <T> Option<T> some(T value) {
		return new Some<T>(value);
	}

	/**
	 * Depending on the value, either an Option of some, if the value is not null,
	 * otherwise an Option of none is created.
	 *
	 * @param <T> Type of the value
	 * @param value the value, which may be null
	 * @return an {@code Option} of some, if the specified value is not null, none otherwise
	 */
	public static <T> Option<T> fromNullable(T value) {
		if (value == null) {
			return none();
		}

		return some(value);
	}

	private Option() {}

	/**
	 * If a value is present in this {@code Option}, the value is returned,
	 * otherwise a {@code NoSuchElementException} is being throwed.
	 *
	 * @return the value if present
	 * @throws java.util.NoSuchElementException if there is no value present
	 */
	public abstract T get();

	/**
	 * Returns {@code true} if  a value present, {@code false} otherwise.
	 *
	 * @return {@code true} if  a value present, {@code false} otherwise
	 */
	public abstract boolean isPresent();

	/**
	 * If a value is present, the specified callable is being invoked,
	 * otherwise nothing will be done.
	 *
	 * @param action that is to be invoked when a value is present
	 * @throws IllegalArgumentException if the value is present and {@code callable} is null
	 */
	public abstract void ifPresent(Action<? super T> action);

	/**
	 * A value access method to obtain the value contained in this {@code Option}
	 * or an alternative. If a value is present, it is returned. If no value is
	 * present, the supplied alternative value is returned. The supplied alternative may be null.
	 *
	 * @param value the alternative value that is to be returned if the value of this {@code Option} is
	 *              not present
	 * @return the value of this {@code Option} if it is present, the given alternative value otherwise
	 */
	public abstract T getOrElse(T value);

	/**
	 * Returns this {@code Option} if it is some, otherwise return the provided alternative.
	 *
	 * @param other the {@code Option} to be returned if this {@code Option} is none
	 * @return the value, if present, otherwise {@code other}
	 */
	public abstract Option<T> or(Option<? extends T> other);

	/**
	 * Return the value if present, otherwise return {@code other}.
	 *
	 * @param other the value to be returned if there is no value present, may
	 * be null
	 * @return the value, if present, otherwise {@code other}
	 */
	public abstract Option<T> or(T other);

	/**
	 * If a value is present, the provided mapping function is applied to it
	 * and an {@code Option} containing that result is returned. Otherwise an
	 * {@code Option} of none is returned. The return of the value of the
	 * mapping function may be null.
	 *
	 * @param <TResult> The type of the result of the mapping function
	 * @param func a mapping function to apply to the value, if present
	 * @return an {@code Option} containing the result of the applied mapping
	 * function to the value of this {@code Option}, if a value is present,
	 * otherwise an {@code Option} of none
	 * @throws java.lang.IllegalArgumentException if a value is presend and the
	 * mapping function is null
	 */
	public abstract <TResult> Option<TResult> map(Func<? super T, TResult> func);

	/**
	 * If a value is present, the provided mapping function is applied to it
	 * and that result is returned. Otherwise an {@code Option} of none is
	 * returned. The return of the value of the mapping function must not be null.
	 *
	 * This method is similar to {@link #map(ninja.sequence.delegate.Func)} but
	 * differs in that the result of the provided mapping function retuns already an
	 * {@code Option}. The resulting {@code Option} is not wrapped with an additional
	 * {@code Option}.
	 *
	 * @param <TResult> The type parameter of the {@code Option} returned by the
	 *                  mapping function
	 * @param func a mapping function to apply to the value, if present
	 * @return the result of the applied mapping function to the value of this
	 * {@code Option}, if a value is present, otherwise an {@code Option} of none
	 * @throws java.lang.IllegalArgumentException if a value is presend and the
	 * mapping function is null
	 * @throws NullPointerException if the mapping function returns null
	 */
	public abstract <TResult> Option<TResult> bind(Func<? super T, Option<TResult>> func);

	public abstract List<T> asList();

	public abstract Set<T> asSet();

	static class None<T> extends Option<T> {
		@Override
		public boolean isPresent() {
			return false;
		}

		@Override
		public T get() {
			throw new NoSuchElementException();
		}

		@Override
		public void ifPresent(Action<? super T> action) {}

		@Override
		public T getOrElse(T value) {
			return value;
		}

		@Override
		@SuppressWarnings("unchecked")
		public Option<T> or(Option<? extends T> other) {
			Check.argumentNotNull(other, "other must not be null.");

			return (Option<T>)other;
		}

		@Override
		public Option<T> or(T other) {
			return some(other);
		}

		@Override
		public <TResult> Option<TResult> map(Func<? super T, TResult> func) {
			return none();
		}

		@Override
		public <TResult> Option<TResult> bind(Func<? super T, Option<TResult>> func) {
			return none();
		}

		@Override
		public List<T> asList() {
			return Collections.emptyList();
		}

		@Override
		public Set<T> asSet() {
			return Collections.emptySet();
		}

		@Override
		public Iterator<T> iterator() {
			return Collections.emptyIterator();
		}

		@Override
		public String toString() {
			return "None";
		}
	}

	static class Some<T> extends Option<T> {
		private final T value;

		public Some(T value) {
			this.value = value;
		}

		@Override
		public boolean isPresent() {
			return true;
		}

		@Override
		public T get() {
			return this.value;
		}

		@Override
		public void ifPresent(Action<? super T> action) {
			Check.argumentNotNull(action, "callable must not be null");

			action.invoke(this.value);
		}

		@Override
		public T getOrElse(T value) {
			return this.value;
		}

		@Override
		public Option<T> or(Option<? extends T> other) {
			return this;
		}

		@Override
		public Option<T> or(T other) {
			return this;
		}

		@Override
		public <TResult> Option<TResult> map(Func<? super T, TResult> func) {
			Check.argumentNotNull(func, "f must not be null");

			return some(func.invoke(this.value));
		}

		@Override
		public <TResult> Option<TResult> bind(Func<? super T, Option<TResult>> func) {
			Check.argumentNotNull(func, "func must not be null");

			Option<TResult> result = func.invoke(this.value);

			if (result == null) {
				throw new NullPointerException("The result of the provided function func must not be null.");
			}

			return result;
		}

		@Override
		public List<T> asList() {
			return Collections.singletonList(this.value);
		}

		@Override
		public Set<T> asSet() {
			return Collections.singleton(this.value);
		}

		@Override
		public Iterator<T> iterator() {
			return Collections.singletonList(this.value).iterator();
		}

		@Override
		public boolean equals(Object other) {
			if (this == other) {
				return true;
			}

			if (other == null || getClass() != other.getClass()) {
				return false;
			}

			Option.Some some = (Option.Some) other;

			return !(this.value != null ? !this.value.equals(some.value) : some.value != null);
		}

		@Override
		public int hashCode() {
			return this.value != null ? this.value.hashCode() : 0;
		}

		@Override
		public String toString() {
			return String.format("Some(%s)", this.value);
		}
	}
}
