package sk.uniba.grman19;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import sk.uniba.grman19.validation.EmailValidator;

public class MailValidatorTest {
	EmailValidator ev = new EmailValidator();

	@Test
	public void testNull() {
		assertThat(ev.isValid(null, null)).isTrue();
	}

	@Test
	public void testRootDomain() {
		assertThat(ev.isValid("root@localhost", null)).isTrue();
	}

	@Test
	public void testDomain() {
		assertThat(ev.isValid("root@google.com", null)).isTrue();
	}

	@Test
	public void testFullMail() {
		assertThat(ev.isValid("some.name123@google.com", null)).isTrue();
	}

	@Test
	public void testNoDomain() {
		assertThat(ev.isValid("some@", null)).isFalse();
	}

	@Test
	public void testNoName() {
		assertThat(ev.isValid("@localhost", null)).isFalse();
	}

	@Test
	public void testAt() {
		assertThat(ev.isValid("@", null)).isFalse();
	}
}
