package fr.cnam.stefangeorgesco.dmp.domain.model;

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

public class SpecialtyTest {

	private static ValidatorFactory validatorFactory;
	private static Validator validator;

	public Specialty specialty;

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
		specialty = new Specialty();
		specialty.setId("id");
		specialty.setDescription("A specialty");
	}

	@Test
	public void specialtyValidationSpecialtyValid() {

		Set<ConstraintViolation<Specialty>> violations = validator.validate(specialty);

		assertEquals(0, violations.size());
	}

	@Test
	public void specialtyValidationInvalidIdBlank() {

		specialty.setId("");

		Set<ConstraintViolation<Specialty>> violations = validator.validate(specialty);

		assertEquals(1, violations.size());
		assertEquals("L'identifiant est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void specialtyValidationInvalidDescriptionBlank() {

		specialty.setDescription("");

		Set<ConstraintViolation<Specialty>> violations = validator.validate(specialty);

		assertEquals(1, violations.size());
		assertEquals("La description est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void specialtyValidationInvalidIdNull() {

		specialty.setId(null);

		Set<ConstraintViolation<Specialty>> violations = validator.validate(specialty);

		assertEquals(1, violations.size());
		assertEquals("L'identifiant est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void specialtyValidationInvalidDescriptionNull() {

		specialty.setDescription(null);

		Set<ConstraintViolation<Specialty>> violations = validator.validate(specialty);

		assertEquals(1, violations.size());
		assertEquals("La description est obligatoire.", violations.iterator().next().getMessage());
	}

}
