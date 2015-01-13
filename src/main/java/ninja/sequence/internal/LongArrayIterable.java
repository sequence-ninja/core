package ninja.sequence.internal;

import java.util.Iterator;

public class LongArrayIterable implements Iterable<Long> {
	private final long[] array;

	public LongArrayIterable(long[] array) {
		if (array == null) {
			throw new IllegalArgumentException("array must not be null.");
		}

		this.array = array;
	}

	@Override
	public Iterator<Long> iterator() {
		return new LongArrayIterator();
	}

	private class LongArrayIterator extends AbstractArrayIterator<Long> {
		public LongArrayIterator() {
			super(array.length);
		}

		@Override
		protected Long next(int index) {
			return array[index];
		}
	}
}

