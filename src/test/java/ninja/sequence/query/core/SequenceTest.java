package ninja.sequence.query.core;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import ninja.sequence.Sequence;

public class SequenceTest {
	@Test
	public void testFromIterable() {
		Sequence<Integer> actual = Sequence.of(asList(1, 2));

		assertThat(actual).containsExactly(1, 2);
	}

	@Test
	public void testFromMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, "leeloo");
		map.put(2, "multipass");

		Sequence<Map.Entry<Integer, String>> actual = Sequence.of(map);

		assertThat(actual).containsExactly(
			entry(1, "leeloo"), entry(2, "multipass")
		);
	}

	@Test
	public void testFromArray() {
		Integer[] integers = new Integer[] {1, 2};

		Sequence<Integer> actual = Sequence.of(integers);

		assertThat(actual).containsExactly(1, 2);
	}

	@Test
	public void testFromPrimitiveBooleanArray() {
		boolean[] booleans = new boolean[] {true, false};

		Sequence<Boolean> actual = Sequence.of(booleans);

		assertThat(actual).containsExactly(true, false);
	}

	@Test
	public void testFromPrimitiveByteArray() {
		byte[] bytes = new byte[] {'A', 'B'};

		Sequence<Byte> actual = Sequence.of(bytes);

		assertThat(actual).containsExactly(new Byte("65"), new Byte("66"));
	}

	@Test
	public void testFromPrimitiveCharArray() {
		char[] chars = new char[] {'a', 'b'};

		Sequence<Character> actual = Sequence.of(chars);

		assertThat(actual).containsExactly('a', 'b');
	}

	@Test
	public void testFromPrimitiveDoubleArray() {
		double[] doubles = new double[] {1.0, 2.0};

		Sequence<Double> actual = Sequence.of(doubles);

		assertThat(actual).containsExactly(1.0, 2.0);
	}

	@Test
	public void testFromPrimitiveFloatArray() {
		float[] floats = new float[] {1.0f, 2.0f};

		Sequence<Float> actual = Sequence.of(floats);

		assertThat(actual).containsExactly(1.0f, 2.0f);
	}

	@Test
	public void testFromPrimitiveIntegerArray() {
		int[] ints = new int[] {1, 2};

		Sequence<Integer> actual = Sequence.of(ints);

		assertThat(actual).containsExactly(1, 2);
	}

	@Test
	public void testFromPrimitiveLongArray() {
		long[] longs = new long[] {1L, 2L};

		Sequence<Long> actual = Sequence.of(longs);

		assertThat(actual).containsExactly(1L, 2L);
	}

	@Test
	public void testFromPrimitiveShortArray() {
		short[] longs = new short[] {1, 2};

		Sequence<Short> actual = Sequence.of(longs);

		assertThat(actual).containsExactly((short)1, (short)2);
	}

	@Test
	public void test_range() {
		// When
		Sequence<Integer> actual = Sequence.range(Integer.MAX_VALUE, 1);

		// Then
		assertThat(actual).containsExactly(Integer.MAX_VALUE);
	}

	@Test
	public void test_repeat() {
		// When
		Sequence<String> actual = Sequence.repeat("a", 5);

		// Then
		assertThat(actual).containsOnly("a", "a", "a", "a", "a");
	}

	// elegant way to suppress generic array creation warning
	private <TA, TB> Map.Entry entry(TA a, TB b) {
		return new HashMap.SimpleEntry<TA, TB>(a, b);
	}
}
