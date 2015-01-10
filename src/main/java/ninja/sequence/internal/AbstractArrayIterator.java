package ninja.sequence.internal;

import java.util.NoSuchElementException;

public abstract class AbstractArrayIterator<T> extends ImmutableIterator<T> {
	private int index = 0;
	private int length = 0;

	protected AbstractArrayIterator(int length) {
		this.length = length;
	}

	@Override
	public boolean hasNext() {
		return this.index < this.length;
	}

	@Override
	public T next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}

		return next(this.index++);
	}

	protected abstract T next(int index);
}
