package ninja.sequence.internal;

import ninja.sequence.contract.Check;

public class DoubleArrayIterator extends AbstractArrayIterator<Double> {
	private final double[] array;

	public DoubleArrayIterator(double[] array) {
		super(Check.argumentNotNull(array, "array must not be null.").length);

		this.array = array;
	}

	@Override
	protected Double next(int index) {
		return this.array[index];
	}
}
