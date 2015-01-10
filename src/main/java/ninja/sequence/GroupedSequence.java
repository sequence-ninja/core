package ninja.sequence;

public class GroupedSequence<K, T> extends Sequence<T> {
	private final K key;

	public static <T, K> GroupedSequence<K, T> create(K key, Iterable<T> source) {
		return new GroupedSequence<K, T>(key, source);
	}

	protected GroupedSequence(K key, Iterable<T> source) {
		super(source);

		this.key = key;
	}

	public final K getKey() {
		return this.key;
	}
}
