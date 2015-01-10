package ninja.sequence.internal;

import java.util.Iterator;

import ninja.sequence.contract.Check;
import ninja.sequence.datastructure.Tuple;
import ninja.sequence.monad.Option;

public class ZipAllIterator<TSource, TOther> extends AbstractIterator<TSource, Tuple<Option<TSource>, Option<TOther>>> {
	private final Iterator<? extends TOther> other;

	public ZipAllIterator(Iterator<? extends TSource> parent, Iterator<? extends TOther> other) {
		super(parent);

		this.other = Check.argumentNotNull(other, "other must not be null.");
	}

	@Override
	protected Tuple<Option<TSource>, Option<TOther>> computeNext() {
		if (super.parent.hasNext() || this.other.hasNext()) {
			Option<TSource> source = Option.none();
			Option<TOther> sourceOther = Option.none();

			if (super.parent.hasNext()) {
				source = Option.some(super.parent.next());
			}

			if (this.other.hasNext()) {
				sourceOther = Option.some(this.other.next());
			}

			return new Tuple<Option<TSource>, Option<TOther>>(source, sourceOther);
		}

		return computationEnd();
	}
}
