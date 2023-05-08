package fr.cnam.stefangeorgesco.dmp.domain.model;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SymptomTest {

	private static ValidatorFactory validatorFactory;
	private static Validator validator;
	private LocalDate futureDate;
	private LocalDate pastDate;

	private Symptom symptom;

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
		Doctor authoringDoctor = new Doctor();
		PatientFile patientFile = new PatientFile();
		symptom = new Symptom();
		LocalDate now = LocalDate.now();
		pastDate = now.minusDays(1);
		futureDate = now.plusDays(1);
		symptom.setDate(now);
		symptom.setDescription("Symptom description");
		symptom.setAuthoringDoctor(authoringDoctor);
		symptom.setPatientFile(patientFile);
	}

	@Test
	public void symptomValidationSymptomValidDateNow() {

		Set<ConstraintViolation<Symptom>> violations = validator.validate(symptom);

		assertEquals(0, violations.size());
	}

	@Test
	public void symptomValidationSymptomValidDatePast() {

		symptom.setDate(pastDate);

		Set<ConstraintViolation<Symptom>> violations = validator.validate(symptom);

		assertEquals(0, violations.size());
	}

	@Test
	public void symptomValidationInvalidDateFuture() {

		symptom.setDate(futureDate);

		Set<ConstraintViolation<Symptom>> violations = validator.validate(symptom);

		assertEquals(1, violations.size());
		assertEquals("La date de l'élément médical doit être dans le passé ou aujourd'hui.",
				violations.iterator().next().getMessage());
	}

	@Test
	public void symptomValidationInvalidDescriptionBlank() {

		symptom.setDescription("");

		Set<ConstraintViolation<Symptom>> violations = validator.validate(symptom);

		assertEquals(1, violations.size());
		assertEquals("La description est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void symptomValidationInvalidDateNull() {

		symptom.setDate(null);

		Set<ConstraintViolation<Symptom>> violations = validator.validate(symptom);

		assertEquals(1, violations.size());
		assertEquals("La date de l'élément médical est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void actValidationInvalidAuthoringDoctorNull() {

		symptom.setAuthoringDoctor(null);

		Set<ConstraintViolation<Symptom>> violations = validator.validate(symptom);

		assertEquals(1, violations.size());
		assertEquals("Le médecin auteur est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void actValidationInvalidPatientFileNull() {

		symptom.setPatientFile(null);

		Set<ConstraintViolation<Symptom>> violations = validator.validate(symptom);

		assertEquals(1, violations.size());
		assertEquals("Le dossier patient est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void symptomValidationInvalidDescriptionNull() {

		symptom.setDescription(null);

		Set<ConstraintViolation<Symptom>> violations = validator.validate(symptom);

		assertEquals(1, violations.size());
		assertEquals("La description est obligatoire.", violations.iterator().next().getMessage());
	}

}
