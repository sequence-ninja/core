package ninja.sequence.internal;

import java.util.ListIterator;
import java.util.NoSuchElementException;

import ninja.sequence.contract.Check;

public class InvertedListIterator<T> extends ImmutableIterator<T> {
	private final ListIterator<T> listIterator;

	public InvertedListIterator(ListIterator<T> listIterator) {
		this.listIterator = Check.argumentNotNull(listIterator, "listIterator must not be null.");
	}

	@Override
	public boolean hasNext() {
		return this.listIterator.hasPrevious();
	}

	@Override
	public T next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}

		return this.listIterator.previous();
	}
}
