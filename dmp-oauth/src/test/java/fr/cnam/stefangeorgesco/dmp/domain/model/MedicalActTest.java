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
public class MedicalActTest {

	private static Validator validator;

	@Autowired
	public MedicalAct medicalAct;

	@BeforeAll
	public static void setupAll() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@BeforeEach
	public void setupEach() {
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
