package ninja.sequence.delegate;

public interface Accumulator<TAccumulate, TSource> {
	TAccumulate accumulate(TAccumulate a, TSource b);
}
