package ninja.sequence.monad;

public class Indexed<T> {
	private final long index;
	private final T value;

	public Indexed(long index, T value) {
		this.index = index;
		this.value = value;
	}

	public long getIndex() {
		return this.index;
	}

	public T getValue() {
		return this.value;
	}
}
