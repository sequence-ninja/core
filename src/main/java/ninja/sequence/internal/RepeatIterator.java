package ninja.sequence.internal;

import java.util.NoSuchElementException;

public class RepeatIterator<T> extends ImmutableIterator<T> {
	private final T value;
	private final int count;

	private int current = 0;

	public RepeatIterator(T value, int count) {
		if (count < 0) {
			throw new IllegalArgumentException("count must be greater or equal 0.");
		}

		this.value = value;
		this.count = count;
	}

	@Override
	public boolean hasNext() {
		return this.current < this.count;
	}

	@Override
	public T next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}

		this.current++;

		return this.value;
	}
}
