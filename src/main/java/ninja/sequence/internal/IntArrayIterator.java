package ninja.sequence.internal;

import ninja.sequence.contract.Check;

public class IntArrayIterator extends AbstractArrayIterator<Integer> {
	private final int[] array;

	public IntArrayIterator(int[] array) {
		super(Check.argumentNotNull(array, "array must not be null.").length);

		this.array = array;
	}

	@Override
	protected Integer next(int index) {
		return this.array[index];
	}
}
