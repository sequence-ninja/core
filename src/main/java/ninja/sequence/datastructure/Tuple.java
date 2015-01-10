package ninja.sequence.datastructure;

public class Tuple<TFirst, TSecond> {
	private final TFirst first;
	private final TSecond second;

	public static <TFirst, TSecond> Tuple<TFirst, TSecond> create(TFirst first, TSecond second) {
		return new Tuple<TFirst, TSecond>(first, second);
	}

	public Tuple(TFirst first, TSecond second) {
		this.first = first;
		this.second = second;
	}

	public TFirst getFirst() {
		return this.first;
	}

	public TSecond getSecond() {
		return this.second;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}

		if (other == null || getClass() != other.getClass()) {
			return false;
		}

		Tuple tuple = (Tuple) other;

		return !(first != null ? !first.equals(tuple.first) : tuple.first != null)
			&& !(second != null ? !second.equals(tuple.second) : tuple.second != null);
	}

	@Override
	public int hashCode() {
		int result = first != null ? first.hashCode() : 0;
		result = 31 * result + (second != null ? second.hashCode() : 0);
		return result;
	}
}
