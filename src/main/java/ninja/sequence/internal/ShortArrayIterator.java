package ninja.sequence.internal;

import ninja.sequence.contract.Check;

public class ShortArrayIterator extends AbstractArrayIterator<Short> {
	private final short[] array;

	public ShortArrayIterator(short[] array) {
		super(Check.argumentNotNull(array, "array must not be null.").length);

		this.array = array;
	}

	@Override
	protected Short next(int index) {
		return this.array[index];
	}
}
