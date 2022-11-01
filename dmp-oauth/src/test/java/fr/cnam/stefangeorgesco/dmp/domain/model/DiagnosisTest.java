package fr.cnam.stefangeorgesco.dmp.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
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
public class DiagnosisTest {

	private static Validator validator;
	private LocalDate now;
	private LocalDate futureDate;
	private LocalDate pastDate;

	@Autowired
	private Diagnosis diagnosis;

	@Autowired
	private Doctor authoringDoctor;

	@Autowired
	private PatientFile patientFile;

	@Autowired
	private Disease disease;

	@BeforeAll
	public static void setupAll() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@BeforeEach
	public void setupEach() {
		disease.setId("diseaseId");
		disease.setDescription("A disease");
		now = LocalDate.now();
		pastDate = now.minusDays(1);
		futureDate = now.plusDays(1);
		diagnosis.setDate(now);
		diagnosis.setAuthoringDoctor(authoringDoctor);
		diagnosis.setPatientFile(patientFile);
		diagnosis.setDisease(disease);
	}

	@Test
	public void diagnosisValidationValidDateNow() {

		Set<ConstraintViolation<Diagnosis>> violations = validator.validate(diagnosis);

		assertEquals(0, violations.size());
	}

	@Test
	public void diagnosisValidationValidDatePast() {

		diagnosis.setDate(pastDate);

		Set<ConstraintViolation<Diagnosis>> violations = validator.validate(diagnosis);

		assertEquals(0, violations.size());
	}

	@Test
	public void diagnosisValidationInvalidDateFuture() {

		diagnosis.setDate(futureDate);

		Set<ConstraintViolation<Diagnosis>> violations = validator.validate(diagnosis);

		assertEquals(1, violations.size());
		assertEquals("La date de l'élément médical doit être dans le passé ou aujourd'hui.",
				violations.iterator().next().getMessage());
	}

	@Test
	public void diagnosisValidationInvalidDateNull() {

		diagnosis.setDate(null);

		Set<ConstraintViolation<Diagnosis>> violations = validator.validate(diagnosis);

		assertEquals(1, violations.size());
		assertEquals("La date de l'élément médical est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void actValidationInvalidAuthoringDoctorNull() {

		diagnosis.setAuthoringDoctor(null);

		Set<ConstraintViolation<Diagnosis>> violations = validator.validate(diagnosis);

		assertEquals(1, violations.size());
		assertEquals("Le médecin auteur est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void actValidationInvalidPatientFileNull() {

		diagnosis.setPatientFile(null);

		Set<ConstraintViolation<Diagnosis>> violations = validator.validate(diagnosis);

		assertEquals(1, violations.size());
		assertEquals("Le dossier patient est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void diagnosisValidationInvalidDiseaseNull() {

		diagnosis.setDisease(null);

		Set<ConstraintViolation<Diagnosis>> violations = validator.validate(diagnosis);

		assertEquals(1, violations.size());
		assertEquals("La maladie est obligatoire.", violations.iterator().next().getMessage());
	}

}
