package ninja.sequence.internal;

import java.util.NoSuchElementException;

public class RangeIterator extends ImmutableIterator<Integer> {
	private final int count;
	private final int from;
	int current;

	public RangeIterator(int count, int from) {
		this.count = count;
		this.from = from;
		current = 0;
	}

	@Override
	public boolean hasNext() {
		return this.current < count;
	}

	@Override
	public Integer next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}

		return from + this.current++;
	}
}
