package ninja.sequence.delegate;

public interface Predicate<T> {
	boolean invoke(T arg);
}
