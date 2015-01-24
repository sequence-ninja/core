package ninja.sequence.internal;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RepeatIterable<T> implements Iterable<T> {
	private final T value;
	private final int count;

	public RepeatIterable(T value, int count) {
		if (count < 0) {
			throw new IllegalArgumentException("count must be greater or equal 0.");
		}

		this.value = value;
		this.count = count;
	}

	@Override
	public Iterator<T> iterator() {
		return new RepeatIterator();
	}

	private class RepeatIterator extends ImmutableIterator<T> {
		private int current = 0;

		@Override
		public boolean hasNext() {
			return this.current < count;
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			this.current++;

			return value;
		}
	}
}
