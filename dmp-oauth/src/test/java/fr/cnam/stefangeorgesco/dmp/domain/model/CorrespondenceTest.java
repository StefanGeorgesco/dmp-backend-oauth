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

public class CorrespondenceTest {

	private static ValidatorFactory validatorFactory;
	private static Validator validator;
	private LocalDate now;
	private LocalDate pastDate;

	private Correspondence correspondence;

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
		Doctor doctor = new Doctor();
		PatientFile patientFile = new PatientFile();
		correspondence = new Correspondence();
		now = LocalDate.now();
		pastDate = now.minusDays(1);
		LocalDate futureDate = now.plusDays(1);
		correspondence.setDateUntil(futureDate);
		correspondence.setDoctor(doctor);
		correspondence.setPatientFile(patientFile);
	}

	@Test
	public void correspondenceValidationCorrespondanceValid() {

		Set<ConstraintViolation<Correspondence>> violations = validator.validate(correspondence);

		assertEquals(0, violations.size());
	}

	@Test
	public void correspondenceValidationInvalidDateNow() {

		correspondence.setDateUntil(now);

		Set<ConstraintViolation<Correspondence>> violations = validator.validate(correspondence);

		assertEquals(1, violations.size());
		assertEquals("La date de la correspondance doit être dans le futur.",
				violations.iterator().next().getMessage());
	}

	@Test
	public void correspondenceValidationInvalidDatePast() {

		correspondence.setDateUntil(pastDate);

		Set<ConstraintViolation<Correspondence>> violations = validator.validate(correspondence);

		assertEquals(1, violations.size());
		assertEquals("La date de la correspondance doit être dans le futur.",
				violations.iterator().next().getMessage());
	}

	@Test
	public void correspondenceValidationInvalidDateNull() {

		correspondence.setDateUntil(null);

		Set<ConstraintViolation<Correspondence>> violations = validator.validate(correspondence);

		assertEquals(1, violations.size());
		assertEquals("La date de la correspondance est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void correspondenceValidationInvalidDoctorNull() {

		correspondence.setDoctor(null);

		Set<ConstraintViolation<Correspondence>> violations = validator.validate(correspondence);

		assertEquals(1, violations.size());
		assertEquals("L'identifiant du médecin est obligatoire.", violations.iterator().next().getMessage());

	}

	@Test
	public void correspondenceValidationInvalidPatientFileNull() {

		correspondence.setPatientFile(null);

		Set<ConstraintViolation<Correspondence>> violations = validator.validate(correspondence);

		assertEquals(1, violations.size());
		assertEquals("Le dossier patient est obligatoire.", violations.iterator().next().getMessage());

	}

}
