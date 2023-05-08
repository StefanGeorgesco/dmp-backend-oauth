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

public class PrescriptionTest {

	private static ValidatorFactory validatorFactory;
	private static Validator validator;
	private LocalDate futureDate;
	private LocalDate pastDate;

	private Prescription prescription;

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
		prescription = new Prescription();
		LocalDate now = LocalDate.now();
		pastDate = now.minusDays(1);
		futureDate = now.plusDays(1);
		prescription.setDate(now);
		prescription.setDescription("Prescription description");
		prescription.setAuthoringDoctor(authoringDoctor);
		prescription.setPatientFile(patientFile);
	}

	@Test
	public void prescriptionValidationSymptomValidDateNow() {

		Set<ConstraintViolation<Prescription>> violations = validator.validate(prescription);

		assertEquals(0, violations.size());
	}

	@Test
	public void prescriptionValidationSymptomValidDatePast() {

		prescription.setDate(pastDate);

		Set<ConstraintViolation<Prescription>> violations = validator.validate(prescription);

		assertEquals(0, violations.size());
	}

	@Test
	public void prescriptionValidationInvalidDateFuture() {

		prescription.setDate(futureDate);

		Set<ConstraintViolation<Prescription>> violations = validator.validate(prescription);

		assertEquals(1, violations.size());
		assertEquals("La date de l'élément médical doit être dans le passé ou aujourd'hui.",
				violations.iterator().next().getMessage());
	}

	@Test
	public void prescriptionValidationInvalidDescriptionBlank() {

		prescription.setDescription("");

		Set<ConstraintViolation<Prescription>> violations = validator.validate(prescription);

		assertEquals(1, violations.size());
		assertEquals("La description est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void prescriptionValidationInvalidDateNull() {

		prescription.setDate(null);

		Set<ConstraintViolation<Prescription>> violations = validator.validate(prescription);

		assertEquals(1, violations.size());
		assertEquals("La date de l'élément médical est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void actValidationInvalidAuthoringDoctorNull() {

		prescription.setAuthoringDoctor(null);

		Set<ConstraintViolation<Prescription>> violations = validator.validate(prescription);

		assertEquals(1, violations.size());
		assertEquals("Le médecin auteur est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void actValidationInvalidPatientFileNull() {

		prescription.setPatientFile(null);

		Set<ConstraintViolation<Prescription>> violations = validator.validate(prescription);

		assertEquals(1, violations.size());
		assertEquals("Le dossier patient est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void prescriptionValidationInvalidDescriptionNull() {

		prescription.setDescription(null);

		Set<ConstraintViolation<Prescription>> violations = validator.validate(prescription);

		assertEquals(1, violations.size());
		assertEquals("La description est obligatoire.", violations.iterator().next().getMessage());
	}

}
