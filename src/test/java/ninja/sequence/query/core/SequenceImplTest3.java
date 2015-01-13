//package ninja.sequence.query.core;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import java.util.LinkedHashSet;
//
//import org.assertj.core.util.Lists;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import ninja.sequence.delegate.Accumulator;
//import ninja.sequence.delegate.Predicate;
//
//@RunWith(MockitoJUnitRunner.class)
//public class SequenceImplTest3 {
//	private <T> SequenceImpl<T> sequence(Iterable<T> iterable) {
//		return new SequenceImpl<T>(iterable);
//	}
//
//	@Test
//	public void asCollection_returnsLinkedHashSet() {
//		// When
//		LinkedHashSet<String> actual = sequence(Lists.newArrayList("ninja", "hanzo")).asCollection(new LinkedHashSet<String>());
//
//		// Then
//		assertThat(actual).containsExactly("ninja", "hanzo");
//	}
//
//	public void bind() {
//
//	}
//
//	@SuppressWarnings("unchecked")
//	private <I, T> Accumulator<I, T> mockedAccumulator() {
//		return mock(Accumulator.class);
//	}
//
//	@SuppressWarnings("unchecked")
//	private <T> Predicate<T> mockedPredicate() {
//		return mock(Predicate.class);
//	}
//
//	private <T> Predicate<T> mockedPredicate(boolean result) {
//		Predicate<T> predicate = mockedPredicate();
//
//		when(predicate.invoke(Mockito.<T>any())).thenReturn(result);
//
//		return predicate;
//	}
//}
