package ninja.sequence.internal;

import java.util.Iterator;

public class CharArrayIterable implements Iterable<Character> {
	private final char[] array;

	public CharArrayIterable(char[] array) {
		if (array == null) {
			throw new IllegalArgumentException("array must not be null.");
		}

		this.array = array;
	}

	@Override
	public Iterator<Character> iterator() {
		return new CharArrayIterator();
	}

	private class CharArrayIterator extends AbstractArrayIterator<Character> {
		public CharArrayIterator() {
			super(array.length);
		}

		@Override
		protected Character next(int index) {
			return array[index];
		}
	}
}
