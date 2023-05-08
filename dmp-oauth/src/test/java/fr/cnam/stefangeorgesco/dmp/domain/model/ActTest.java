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

public class ActTest {

	private static ValidatorFactory validatorFactory;
	private static Validator validator;
	private LocalDate futureDate;
	private LocalDate pastDate;

	private Act act;

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
		MedicalAct medicalAct = new MedicalAct();
		LocalDate now = LocalDate.now();
		act = new Act();
		pastDate = now.minusDays(1);
		futureDate = now.plusDays(1);
		act.setDate(now);
		act.setAuthoringDoctor(authoringDoctor);
		act.setPatientFile(patientFile);
		act.setMedicalAct(medicalAct);
	}

	@Test
	public void actValidationSymptomValidDateNow() {

		Set<ConstraintViolation<Act>> violations = validator.validate(act);

		assertEquals(0, violations.size());
	}

	@Test
	public void actValidationSymptomValidDatePast() {

		act.setDate(pastDate);

		Set<ConstraintViolation<Act>> violations = validator.validate(act);

		assertEquals(0, violations.size());
	}

	@Test
	public void actValidationInvalidDateFuture() {

		act.setDate(futureDate);

		Set<ConstraintViolation<Act>> violations = validator.validate(act);

		assertEquals(1, violations.size());
		assertEquals("La date de l'élément médical doit être dans le passé ou aujourd'hui.",
				violations.iterator().next().getMessage());
	}

	@Test
	public void actValidationInvalidDateNull() {

		act.setDate(null);

		Set<ConstraintViolation<Act>> violations = validator.validate(act);

		assertEquals(1, violations.size());
		assertEquals("La date de l'élément médical est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void actValidationInvalidAuthoringDoctorNull() {

		act.setAuthoringDoctor(null);

		Set<ConstraintViolation<Act>> violations = validator.validate(act);

		assertEquals(1, violations.size());
		assertEquals("Le médecin auteur est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void actValidationInvalidPatientFileNull() {

		act.setPatientFile(null);

		Set<ConstraintViolation<Act>> violations = validator.validate(act);

		assertEquals(1, violations.size());
		assertEquals("Le dossier patient est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void actValidationInvalidMedicalActNull() {

		act.setMedicalAct(null);

		Set<ConstraintViolation<Act>> violations = validator.validate(act);

		assertEquals(1, violations.size());
		assertEquals("L'acte médical est obligatoire.", violations.iterator().next().getMessage());
	}

}
