package ninja.sequence.internal;

import java.util.Iterator;

import ninja.sequence.contract.Check;
import ninja.sequence.delegate.Func;

public class BindIterator<TSource, TResult> extends AbstractIterator<TSource, TResult> {
	private final Func<? super TSource, ? extends Iterable<? extends TResult>> selector;

	private Iterator<? extends TResult> inner;

	public BindIterator(Iterator<? extends TSource> parent, Func<? super TSource, ? extends Iterable<? extends TResult>> selector) {
		super(parent);

		this.selector = Check.argumentNotNull(selector, "selector must not be null.");
	}

	@Override
	protected TResult computeNext() {
		if (this.inner != null && this.inner.hasNext()) {
			return this.inner.next();
		}

		while (this.parent.hasNext()) {
			this.inner = this.selector.invoke(this.parent.next()).iterator();

			// TODO: handle null inner?

			if (this.inner.hasNext()) {
				return this.inner.next();
			}
		}

		return computationEnd();
	}
}
