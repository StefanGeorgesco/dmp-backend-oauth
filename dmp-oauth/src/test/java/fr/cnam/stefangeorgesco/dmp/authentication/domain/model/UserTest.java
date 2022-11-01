package fr.cnam.stefangeorgesco.dmp.authentication.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class UserTest {

	private static Validator validator;

	@Autowired
	private User user;

	@BeforeAll
	public static void setupAll() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@BeforeEach
	public void setupEach() {
		user.setId("A");
		user.setUsername("a");
		user.setPassword("1234");
	}

	@Test
	public void userValidationUserValid() {

		Set<ConstraintViolation<User>> violations = validator.validate(user);

		assertEquals(0, violations.size());
	}

	@Test
	public void userValidationIdInvalidBlank() {

		user.setId("");

		Set<ConstraintViolation<User>> violations = validator.validate(user);

		assertEquals(1, violations.size());
		assertEquals("L'identifiant est obligatoire.", violations.iterator().next().getMessage());

	}

	@Test
	public void userValidationUsernameInvalidBlank() {

		user.setUsername("");

		Set<ConstraintViolation<User>> violations = validator.validate(user);

		assertEquals(1, violations.size());
		assertEquals("Le non utilisateur est obligatoire.", violations.iterator().next().getMessage());

	}

	@Test
	public void userValidationIdInvalidNull() {

		user.setId(null);

		Set<ConstraintViolation<User>> violations = validator.validate(user);

		assertEquals(1, violations.size());
		assertEquals("L'identifiant est obligatoire.", violations.iterator().next().getMessage());

	}

	@Test
	public void userValidationUsernameInvalidNull() {

		user.setUsername(null);

		Set<ConstraintViolation<User>> violations = validator.validate(user);

		assertEquals(1, violations.size());
		assertEquals("Le non utilisateur est obligatoire.", violations.iterator().next().getMessage());

	}

}
