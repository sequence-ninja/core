package ninja.sequence.internal;

import java.util.Iterator;

public class FloatArrayIterable implements Iterable<Float> {
	private final float[] array;

	public FloatArrayIterable(float[] array) {
		if (array == null) {
			throw new IllegalArgumentException("array must not be null.");
		}

		this.array = array;
	}

	@Override
	public Iterator<Float> iterator() {
		return new FloatArrayIterator();
	}

	private class FloatArrayIterator extends AbstractArrayIterator<Float> {
		public FloatArrayIterator() {
			super(array.length);
		}

		@Override
		protected Float next(int index) {
			return array[index];
		}
	}
}
