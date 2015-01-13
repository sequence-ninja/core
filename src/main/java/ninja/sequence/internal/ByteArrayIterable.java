package ninja.sequence.internal;

import java.util.Iterator;

public class ByteArrayIterable implements Iterable<Byte> {
	private final byte[] array;

	public ByteArrayIterable(byte[] array) {
		if (array == null) {
			throw new IllegalArgumentException("array must not be null.");
		}

		this.array = array;
	}

	@Override
	public Iterator<Byte> iterator() {
		return new ByteArrayIterator();
	}

	private class ByteArrayIterator extends AbstractArrayIterator<Byte> {
		public ByteArrayIterator() {
			super(array.length);
		}

		@Override
		protected Byte next(int index) {
			return array[index];
		}
	}
}
