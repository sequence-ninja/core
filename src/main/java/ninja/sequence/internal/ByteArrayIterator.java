package ninja.sequence.internal;

import ninja.sequence.contract.Check;

public class ByteArrayIterator extends AbstractArrayIterator<Byte> {
	private final byte[] array;

	public ByteArrayIterator(byte[] array) {
		super(Check.argumentNotNull(array, "array must not be null.").length);

		this.array = array;
	}

	@Override
	protected Byte next(int index) {
		return this.array[index];
	}
}
