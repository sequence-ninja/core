package ninja.sequence;

import java.util.Arrays;

import ninja.sequence.delegate.Action;
import ninja.sequence.delegate.EqualityComparator;
import ninja.sequence.monad.Option;

public class CodeSnippet {
	public void gna() {
		Sequence<Boolean> b1 = Sequence.of(false);
		Sequence<Boolean> b2 = Sequence.of(false, false);
		Sequence<Boolean> b3 = Sequence.of(new boolean[]{false, true}).prepend(false, true);

		Sequence<Long> l1 = Sequence.of(1L);
		Sequence<Long> l2 = Sequence.of(1L, 2L);
		Sequence<Long> l3 = Sequence.of(new long[]{1L, 2L});

		Sequence<Integer> i1 = Sequence.of(1);
		Sequence<Integer> i2 = Sequence.of(1, 2);
		Sequence<Integer> i3 = Sequence.of(new int[]{1, 2});

		Sequence<String> s1 = Sequence.of("a");
		Sequence<String> s2 = Sequence.of("a", "b");
		Sequence<String> s3 = Sequence.of(new String[]{"a", "b"});
		Sequence<String> s4 = Sequence.of((String)null);

		Sequence<String> s5 = Sequence.of(Arrays.asList("a"));


		Option<Integer> first = Sequence.range(1, 5).concat(6).prepend(0).skip(3).take(3).first();

		first.ifPresent(
			new Action<Integer>() {
				@Override
				public void invoke(Integer arg) {
					System.out.print(arg);
				}
			}
		);

//		for (Integer integer : Sequence.range(1, 5).concat(6).prepend(0).skip(3).take(3).asArrayList()) {
//			System.out.println(integer);
//		}



		Sequence.of('a').difference('b');
		Sequence.of('a').difference('b', 'c');
		Sequence.of('a').difference(Arrays.asList('b', 'c'));

		Sequence.of('a').difference('a', new EqualityComparator<Character>() {
			@Override
			public boolean equals(Character first, Character second) {
				return false;
			}
		});
		Sequence.of('a').difference('a', 'b', new EqualityComparator<Character>() {
			@Override
			public boolean equals(Character first, Character second) {
				return false;
			}
		});
		Sequence.of('a').difference(new Character[]{'a', 'b'}, new EqualityComparator<Character>() {
			@Override
			public boolean equals(Character first, Character second) {
				return false;
			}
		});
		Sequence.of('a').difference(Arrays.asList('a', 'b'), new EqualityComparator<Character>() {
			@Override
			public boolean equals(Character first, Character second) {
				return false;
			}
		});

	}

	public static void main(String[] args) {
		new CodeSnippet().gna();
	}
}
