package ninja.sequence.contract;

public class Check {
	public static <T> T argumentNotNull(T object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}

		return object;
	}
}
