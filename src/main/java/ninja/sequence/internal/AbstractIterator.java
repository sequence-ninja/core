package ninja.sequence.internal;

import java.util.Iterator;
import java.util.NoSuchElementException;

import ninja.sequence.contract.Check;

public abstract class AbstractIterator<TSource, TResult> extends ImmutableIterator<TResult> implements Iterator<TResult> {
	private enum State {
		READY, NOT_READY, DONE, FAILED
	}

	protected final Iterator<? extends TSource> parent;

	private State state = State.NOT_READY;

	private TResult next;

	public AbstractIterator(Iterator<? extends TSource> parent) {
		this.parent = Check.argumentNotNull(parent, "parent must not be null.");
	}

	protected abstract TResult computeNext();

	protected final TResult computationEnd() {
		state = State.DONE;
		return null;
	}

	private boolean tryToComputeNext() {
		this.state = State.FAILED; // temporary pessimism
		this.next = computeNext();

		if (state != State.DONE) {
			state = State.READY;
			return true;
		}

		return false;
	}

	@Override
	public boolean hasNext() {
		if (this.state == State.FAILED) {
			throw new IllegalStateException();
		}

		switch (state) {
			case DONE:
				return false;
			case READY:
				return true;
			default:
		}

		return tryToComputeNext();
	}

	@Override
	public TResult next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}

		this.state = State.NOT_READY;
		return this.next;
	}
}
