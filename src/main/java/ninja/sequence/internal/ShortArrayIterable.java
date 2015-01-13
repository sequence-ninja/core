package ninja.sequence.internal;

import java.util.Iterator;

public class ShortArrayIterable implements Iterable<Short> {
	private final short[] array;

	public ShortArrayIterable(short[] array) {
		if (array == null) {
			throw new IllegalArgumentException("array must not be null.");
		}

		this.array = array;
	}

	@Override
	public Iterator<Short> iterator() {
		return new ShortArrayIterator();
	}

	private class ShortArrayIterator extends AbstractArrayIterator<Short> {
		public ShortArrayIterator() {
			super(array.length);
		}

		@Override
		protected Short next(int index) {
			return array[index];
		}
	}
}
