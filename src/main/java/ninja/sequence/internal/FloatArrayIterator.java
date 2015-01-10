package ninja.sequence.internal;

import ninja.sequence.contract.Check;

public class FloatArrayIterator extends AbstractArrayIterator<Float> {
	private final float[] array;

	public FloatArrayIterator(float[] array) {
		super(Check.argumentNotNull(array, "array must not be null.").length);

		this.array = array;
	}

	@Override
	protected Float next(int index) {
		return this.array[index];
	}
}
