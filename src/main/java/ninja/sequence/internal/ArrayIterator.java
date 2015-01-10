package ninja.sequence.internal;

import ninja.sequence.contract.Check;

public class ArrayIterator<TSource> extends AbstractArrayIterator<TSource> {
	private final TSource[] array;

	public ArrayIterator(TSource[] array) {
		super(Check.argumentNotNull(array, "array must not be null.").length);

		this.array = array;
	}

	@Override
	protected TSource next(int index) {
		return this.array[index];
	}
}
