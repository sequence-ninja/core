package ninja.sequence.util;

import ninja.sequence.delegate.Func;

public abstract class Funcs {
	private static final Func<Object, Object> SELF = new Func<Object, Object>() {
		@Override
		public Object invoke(Object object) {
			return object;
		}
	};

	private Funcs() {}

	/**
	 * Returns a {@code Func} that returns the value that is used to call the function.
	 *
	 * @param <T> the type of the value
	 * @return the func
	 */
	@SuppressWarnings("unchecked")
	public static <T> Func<T, T> self() {
		return (Func<T, T>) SELF;
	}
}
