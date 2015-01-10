package ninja.sequence.internal;

import ninja.sequence.contract.Check;

public class CharArrayIterator extends AbstractArrayIterator<Character> {
	private final char[] array;

	public CharArrayIterator(char[] array) {
		super(Check.argumentNotNull(array, "array must not be null.").length);

		this.array = array;
	}

	@Override
	protected Character next(int index) {
		return this.array[index];
	}
}
