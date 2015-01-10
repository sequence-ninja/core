package ninja.sequence.internal;

import java.util.Iterator;

import ninja.sequence.contract.Check;
import ninja.sequence.datastructure.Tuple;

public class ZipIterator<TSource, TOther> extends AbstractIterator<TSource, Tuple<TSource, TOther>> {
	private final Iterator<? extends TOther> otherIterator;

	public ZipIterator(Iterator<? extends TSource> parent, Iterator<? extends TOther> other) {
		super(parent);

		this.otherIterator = Check.argumentNotNull(other, "other must not be null.");
	}

	@Override
	protected Tuple<TSource, TOther> computeNext() {
		if (super.parent.hasNext() && this.otherIterator.hasNext()) {
			return new Tuple<TSource, TOther>(super.parent.next(), this.otherIterator.next());
		}

		return computationEnd();
	}
}
