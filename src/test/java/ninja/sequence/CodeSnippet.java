package ninja.sequence;

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
	}

	public static void main(String[] args) {
		new CodeSnippet().gna();
	}
}
