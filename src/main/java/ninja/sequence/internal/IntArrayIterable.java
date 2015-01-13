package ninja.sequence.internal;

import java.util.Iterator;

public class IntArrayIterable implements Iterable<Integer> {
	private final int[] array;

	public IntArrayIterable(int[] array) {
		if (array == null) {
			throw new IllegalArgumentException("array must not be null.");
		}

		this.array = array;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new IntArrayIterator();
	}

	private class IntArrayIterator extends AbstractArrayIterator<Integer> {
		public IntArrayIterator() {
			super(array.length);
		}

		@Override
		protected Integer next(int index) {
			return array[index];
		}
	}
}
