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

public class MedicalActTest {

	private static ValidatorFactory validatorFactory;
	private static Validator validator;

	public MedicalAct medicalAct;

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
		medicalAct = new MedicalAct();
		medicalAct.setId("id");
		medicalAct.setDescription("A medical act");
	}

	@Test
	public void specialtyValidationSpecialtyValid() {

		Set<ConstraintViolation<MedicalAct>> violations = validator.validate(medicalAct);

		assertEquals(0, violations.size());
	}

	@Test
	public void specialtyValidationInvalidIdBlank() {

		medicalAct.setId("");

		Set<ConstraintViolation<MedicalAct>> violations = validator.validate(medicalAct);

		assertEquals(1, violations.size());
		assertEquals("L'identifiant est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void specialtyValidationInvalidDescriptionBlank() {

		medicalAct.setDescription("");

		Set<ConstraintViolation<MedicalAct>> violations = validator.validate(medicalAct);

		assertEquals(1, violations.size());
		assertEquals("La description est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void specialtyValidationInvalidIdNull() {

		medicalAct.setId(null);

		Set<ConstraintViolation<MedicalAct>> violations = validator.validate(medicalAct);

		assertEquals(1, violations.size());
		assertEquals("L'identifiant est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void specialtyValidationInvalidDescriptionNull() {

		medicalAct.setDescription(null);

		Set<ConstraintViolation<MedicalAct>> violations = validator.validate(medicalAct);

		assertEquals(1, violations.size());
		assertEquals("La description est obligatoire.", violations.iterator().next().getMessage());
	}

}
