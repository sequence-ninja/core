package ninja.sequence.delegate;

public interface Action<T> {
	void invoke(T arg);
}
