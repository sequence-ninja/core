package ninja.sequence.util;

import ninja.sequence.delegate.Func;

public abstract class Funcs {
	private Funcs() {}

	public static <T> Func<T, T> forward() {
		return new Func<T, T>() {
			@Override
			public T invoke(T object) {
				return object;
			}
		};
	}
}
