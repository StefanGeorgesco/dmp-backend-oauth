package fr.cnam.stefangeorgesco.dmp.domain.model;

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
public class SpecialtyTest {

	private static Validator validator;

	@Autowired
	public Specialty specialty;

	@BeforeAll
	public static void setupAll() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@BeforeEach
	public void setupEach() {
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
