package ninja.sequence.internal;

import java.util.Iterator;

public class BooleanArrayIterable implements Iterable<Boolean> {
	private final boolean[] array;

	public BooleanArrayIterable(boolean[] array) {
		if (array == null) {
			throw new IllegalArgumentException("array must not be null.");
		}

		this.array = array;
	}

	@Override
	public Iterator<Boolean> iterator() {
		return new BooleanArrayIterator();
	}

	private class BooleanArrayIterator extends AbstractArrayIterator<Boolean> {
		public BooleanArrayIterator() {
			super(array.length);
		}

		@Override
		protected Boolean next(int index) {
			return array[index];
		}
	}
}
