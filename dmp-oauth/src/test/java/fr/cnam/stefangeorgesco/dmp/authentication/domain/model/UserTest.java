package fr.cnam.stefangeorgesco.dmp.authentication.domain.model;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {

	private static ValidatorFactory validatorFactory;
	private static Validator validator;

	private User user;

	@BeforeAll
	public static void setupAll() {
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@AfterAll
	static void afterAll() {
		validatorFactory.close();
	}

	@BeforeEach
	public void setupEach() {
		user = new User();
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
