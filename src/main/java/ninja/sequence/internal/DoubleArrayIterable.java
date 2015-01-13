package ninja.sequence.internal;

import java.util.Iterator;

public class DoubleArrayIterable implements Iterable<Double> {
	private final double[] array;

	public DoubleArrayIterable(double[] array) {
		if (array == null) {
			throw new IllegalArgumentException("array must not be null.");
		}

		this.array = array;
	}

	@Override
	public Iterator<Double> iterator() {
		return new DoubleArrayIterator();
	}

	private class DoubleArrayIterator extends AbstractArrayIterator<Double> {
		public DoubleArrayIterator() {
			super(array.length);
		}

		@Override
		protected Double next(int index) {
			return array[index];
		}
	}
}
