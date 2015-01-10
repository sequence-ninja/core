package ninja.sequence.delegate;

public interface Func<T, TResult> {
	TResult invoke(T arg);
}
