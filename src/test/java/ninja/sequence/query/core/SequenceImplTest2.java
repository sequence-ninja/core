package ninja.sequence.query.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ninja.sequence.delegate.Accumulator;
import ninja.sequence.delegate.Func;
import ninja.sequence.delegate.Predicate;
import ninja.sequence.monad.Option;

@RunWith(MockitoJUnitRunner.class)
public class SequenceImplTest2 {
	private <T> SequenceImpl<T> sequence(Iterable<T> iterable) {
		return new SequenceImpl<T>(iterable);
	}

	@Test
	public void aggregate_accumulatorMustNotBeNull() {
		try {
			sequence(Lists.newArrayList("ninja")).aggregate(null);
			Assertions.failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(IllegalArgumentException.class);
			assertThat(e).hasMessage("accumulator must not be null.");
		}
	}

	@Test
	public void aggregate_mapMustNotBeNull() {
		try {
			sequence(Lists.newArrayList("ninja")).aggregate("hanzo", mockedAccumulator(), null);
			Assertions.failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(IllegalArgumentException.class);
			assertThat(e).hasMessage("map must not be null.");
		}
	}

	@Test
	public void aggregate_onEmptySequence_returnsAbsentOption() {
		// When
		Accumulator<Object, Object> accumulator = mockedAccumulator();
		Option<Object> actual = sequence(Lists.emptyList()).aggregate(accumulator);

		// Then
		assertThat(actual.isPresent()).isFalse();
		verifyNoMoreInteractions(accumulator);
	}

	@Test
	public void aggregate_onEmptySequence_returnsIdentity() {
		// When
		Accumulator<Integer, Integer> accumulator = mockedAccumulator();
		Integer actual = sequence(Lists.newArrayList(1, 2)).aggregate(5, accumulator);

		// Then
		assertThat(actual).isEqualTo(5);

		verifyZeroInteractions(accumulator);
	}

	@Test
	public void aggregate_accumulation() {
		// When
		Accumulator<Integer, Integer> accumulator = mockedAccumulator();
		Option<Integer> actual = sequence(Lists.newArrayList(1, 2)).aggregate(accumulator);

		// Then
		assertThat(actual.isPresent()).isTrue();

		verify(accumulator).accumulate(1, 2);
		verifyNoMoreInteractions(accumulator);
	}

	@Test
	public void all_predicateMustNoBeNull() {
		try {
			sequence(Lists.newArrayList(1, 2, 3)).all(null);
			Assertions.failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(IllegalArgumentException.class);
			assertThat(e).hasMessage("predicate must not be null.");
		}
	}

	@Test
	public void all_onEmptySequence_returnsTrue() {
		// When
		Predicate<Integer> predicate = mockedPredicate();
		boolean actual = sequence(Lists.<Integer>emptyList()).all(predicate);

		// Then
		assertThat(actual).isTrue();
		verifyZeroInteractions(predicate);
	}

	@Test
	public void all_withTruthfulPredicate_returnsTrue() {
		// When
		Predicate<Integer> predicate = mockedPredicate(true);
		boolean actual = sequence(Lists.newArrayList(1, 2, 3)).all(predicate);

		// Then
		assertThat(actual).isTrue();

		verify(predicate).invoke(1);
		verify(predicate).invoke(2);
		verify(predicate).invoke(3);

		verifyNoMoreInteractions(predicate);
	}

	@Test
	public void all_withFalsyPredicate_returnsFalse() {
		// When
		Predicate<Integer> predicate = mockedPredicate(false);
		boolean actual = sequence(Lists.newArrayList(1, 2, 3)).all(predicate);

		// Then
		assertThat(actual).isFalse();

		verify(predicate).invoke(1);

		verifyNoMoreInteractions(predicate);
	}

	@Test
	public void any_onEmptySequence_returnsFalse() {
		// When
		boolean actual = sequence(Lists.newArrayList()).any();

		// Then
		assertThat(actual).isFalse();
	}

	@Test
	public void any_returnsTrue() {
		// When
		boolean actual = sequence(Lists.newArrayList(1, 2, 3)).any();

		// Then
		assertThat(actual).isTrue();
	}

	@Test
	public void any_withTruthfulPredicate_returnsTrue() {
		// When
		Predicate<Integer> predicate = mockedPredicate(true);
		boolean actual = sequence(Lists.newArrayList(1, 2, 3)).any(predicate);

		// Then
		assertThat(actual).isTrue();

		verify(predicate).invoke(1);

		verifyNoMoreInteractions(predicate);
	}

	@Test
	public void any_withFalsyPredicate_returnsFalse() {
		// When
		Predicate<Integer> predicate = mockedPredicate(false);
		boolean actual = sequence(Lists.newArrayList(1, 2, 3)).any(predicate);

		// Then
		assertThat(actual).isTrue();

		verify(predicate).invoke(1);
		verify(predicate).invoke(2);
		verify(predicate).invoke(3);

		verifyNoMoreInteractions(predicate);
	}

	@Test
	public void asArray_allocatorMustNotBeNull() {
		try {
			sequence(Lists.newArrayList("ninja")).asArray(null);
			Assertions.failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(IllegalArgumentException.class);
			assertThat(e).hasMessage("allocator must not be null.");
		}
	}

	@Test
	public void asArray_resultingArrayIsNull() {
		Func<Integer, String[]> allocator = new Func<Integer, String[]>() {
			@Override
			public String[] invoke(Integer arg) {
				return null;
			}
		};

		try {
			sequence(Lists.newArrayList("ninja")).asArray(allocator);
			Assertions.failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(NullPointerException.class);
			assertThat(e).hasMessage("The result of the allocator must not be null.");
		}
	}

	@Test
	public void asArray_allocatedArraySizeIsLessTheAmountOfElements() {
		Func<Integer, String[]> allocator = new Func<Integer, String[]>() {
			@Override
			public String[] invoke(Integer arg) {
				return new String[0];
			}
		};

		try {
			sequence(Lists.newArrayList("ninja")).asArray(allocator);
			Assertions.failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(UnsupportedOperationException.class);
			assertThat(e).hasMessage("Length of the returned array must be equal or greater than: 1");
		}
	}

	@Test
	public void asArray_returnsArray() {
		// When
		Func<Integer, String[]> allocator = new Func<Integer, String[]>() {
			@Override
			public String[] invoke(Integer arg) {
				return new String[arg];
			}
		};

		String[] actual = sequence(Lists.newArrayList("ninja", "hanzo")).asArray(allocator);

		// Then
		assertThat(actual).containsExactly("ninja", "hanzo");
	}

	@Test
	public void asArraList_returnsArrayList() {
		// When
		ArrayList<String> actual = sequence(Lists.newArrayList("ninja", "hanzo")).asArrayList();

		// Then
		assertThat(actual).containsExactly("ninja", "hanzo");
	}

	@Test
	public void asCollection_collectionMustNotBeNull() {
		try {
			sequence(Lists.newArrayList("ninja")).asCollection(null);
			Assertions.failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(IllegalArgumentException.class);
			assertThat(e).hasMessage("collection must not be null.");
		}
	}

	@Test
	public void asCollection_returnsLinkedHashSet() {
		// When
		LinkedHashSet<String> actual = sequence(Lists.newArrayList("ninja", "hanzo")).asCollection(new LinkedHashSet<String>());

		// Then
		assertThat(actual).containsExactly("ninja", "hanzo");
	}

	@SuppressWarnings("unchecked")
	private <I, T> Accumulator<I, T> mockedAccumulator() {
		return mock(Accumulator.class);
	}

	@SuppressWarnings("unchecked")
	private <T> Predicate<T> mockedPredicate() {
		return mock(Predicate.class);
	}

	private <T> Predicate<T> mockedPredicate(boolean result) {
		Predicate<T> predicate = mockedPredicate();

		when(predicate.invoke(Mockito.<T>any())).thenReturn(result);

		return predicate;
	}
}
