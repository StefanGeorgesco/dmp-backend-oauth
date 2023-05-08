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

public class DiseaseTest {

	private static ValidatorFactory validatorFactory;
	private static Validator validator;

	private Disease disease;

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
		disease = new Disease();
		disease.setId("id");
		disease.setDescription("A disease");
	}

	@Test
	public void diseaseValidationDiseaseValid() {

		Set<ConstraintViolation<Disease>> violations = validator.validate(disease);

		assertEquals(0, violations.size());
	}

	@Test
	public void diseaseValidationInvalidIdBlank() {

		disease.setId("");

		Set<ConstraintViolation<Disease>> violations = validator.validate(disease);

		assertEquals(1, violations.size());
		assertEquals("L'identifiant est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void diseaseValidationInvalidDescriptionBlank() {

		disease.setDescription("");

		Set<ConstraintViolation<Disease>> violations = validator.validate(disease);

		assertEquals(1, violations.size());
		assertEquals("La description est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void diseaseValidationInvalidIdNull() {

		disease.setId(null);

		Set<ConstraintViolation<Disease>> violations = validator.validate(disease);

		assertEquals(1, violations.size());
		assertEquals("L'identifiant est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void diseaseValidationInvalidDescriptionNull() {

		disease.setDescription(null);

		Set<ConstraintViolation<Disease>> violations = validator.validate(disease);

		assertEquals(1, violations.size());
		assertEquals("La description est obligatoire.", violations.iterator().next().getMessage());
	}

}
