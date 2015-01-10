package ninja.sequence.internal;

import ninja.sequence.contract.Check;

public class LongArrayIterator extends AbstractArrayIterator<Long> {
	private final long[] array;

	public LongArrayIterator(long[] array) {
		super(Check.argumentNotNull(array, "array must not be null.").length);

		this.array = array;
	}

	@Override
	protected Long next(int index) {
		return this.array[index];
	}
}
