package ninja.sequence.internal;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RangeIterable implements Iterable<Integer> {
	private final int count;
	private final int from;

	public RangeIterable(int count, int from) {
		this.count = count;
		this.from = from;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new RangeIterator();
	}

	private class RangeIterator extends ImmutableIterator<Integer> {
		int current = 0;

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
}
