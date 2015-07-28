package ninja.sequence.monad;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.NoSuchElementException;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ninja.sequence.delegate.Action;

@RunWith(MockitoJUnitRunner.class)
public class OptionTest {
	@Mock
	private Action<String> actionOfString;

	@Test
	public void test_some() {
		assertThat(Option.some(5)).isInstanceOf(Option.Some.class);
	}

	@Test
	public void test_none() {
		assertThat(Option.none()).isInstanceOf(Option.None.class);
	}

	@Test
	public void test_fromNullable_nonNull() {
		assertThat(Option.fromNullable(12)).isInstanceOf(Option.Some.class);
	}

	@Test
	public void test_fromNullable_null() {
		assertThat(Option.fromNullable(null)).isInstanceOf(Option.None.class);
	}

	@Test
	public void test_isPresent_returnsTrue() {
		assertThat(Option.some("some").isPresent()).isTrue();
	}

	@Test
	public void test_isPresent_returnsFalse() {
		assertThat(Option.none().isPresent()).isFalse();
	}

	@Test
	public void test_ifPresent_verifyInvoked1Time() {
		Option.some("some").ifPresent(actionOfString);

		verify(this.actionOfString, times(1)).invoke("some");
	}

	@Test
	public void test_ifPresent_verifyNeverInvoked() {
		Option.<String>none().ifPresent(actionOfString);

		verify(this.actionOfString, never()).invoke(Matchers.<String>any());
	}

	@Test
	public void test_get_returnsValue() {
		assertThat(Option.some("some").get()).isEqualTo("some");
	}

	@Test
	public void test_get_throwsNoSuchElementException() {
		try {
			Option.none().get();

			Assertions.failBecauseExceptionWasNotThrown(NoSuchElementException.class);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(NoSuchElementException.class);
		}
	}

	@Test
	public void test_getOrElse_returnsSome() {
		assertThat(Option.some("some").getOrElse("other")).isEqualTo("some");
	}

	@Test
	public void test_getOrElse_returnsOther() {
		assertThat(Option.<String>none().getOrElse("other")).isEqualTo("other");
	}

	@Test
	public void test_or_option_returnsSelf() {
		Option<String> some = Option.some("some");

		assertThat(some.or(Option.some("some"))).isSameAs(some);
	}

	@Test
	public void test_or_option_otherIsNull_returnsSelf() {
		Option<String> some = Option.some("some");

		assertThat(some.or((Option<String>)null)).isSameAs(some);
	}

	@Test
	public void test_or_option_returnsOther() {
		Option<String> none = Option.none();
		Option<String> other = Option.some("some");

		assertThat(none.or(other)).isSameAs(other);
	}

	@Test
	public void test_or_option_otherIsNull_throwsIllegalArgumentException() {
		Option<String> none = Option.none();

		try {
			none.or((Option<String>)null);
			Assertions.failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(IllegalArgumentException.class).hasMessage("other must not be null.");
		}
	}

	@Test
	public void test_or_returnsSelf() {
		Option<String> some = Option.some("some");

		assertThat(some.or("some")).isSameAs(some);
	}

	@Test
	public void test_or_otherIsNull_returnsSelf() {
		Option<String> some = Option.some("some");

		assertThat(some.or((String)null)).isSameAs(some);
	}

	@Test
	public void test_or_returnsOther() {
		Option<String> none = Option.none();
		Option<String> actual = none.or("some");

		Assertions.assertThat(actual).isInstanceOf(Option.Some.class);
		assertThat(actual.get()).isEqualTo("some");
	}

	@Test
	public void test_or_otherIsNull_returnsSomeWithValueOfNull() {
		Option<String> none = Option.none();
		Option<String> actual = none.or((String) null);

		Assertions.assertThat(actual).isInstanceOf(Option.Some.class);
		assertThat(actual.get()).isNull();
	}

	@Test
	public void equals_none() {
		assertThat(Option.none().equals(Option.none())).isTrue();
	}

	@Test
	public void equals_some() {
		assertThat(Option.some("a").equals(Option.some("a"))).isTrue();
		assertThat(Option.some("a").equals(Option.some("b"))).isFalse();
	}


	// TODO: test iterator

	// TODO: test map
	// TODO: test bind
	// TODO: test toString

}
