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

public class MailTest {

	private static ValidatorFactory validatorFactory;
	private static Validator validator;
	private LocalDate futureDate;
	private LocalDate pastDate;

	private Mail mail;

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
		Doctor recipientDoctor = new Doctor();
		mail = new Mail();
		LocalDate now = LocalDate.now();
		pastDate = now.minusDays(1);
		futureDate = now.plusDays(1);
		mail.setDate(now);
		mail.setAuthoringDoctor(authoringDoctor);
		mail.setPatientFile(patientFile);
		mail.setText("mail text");
		mail.setRecipientDoctor(recipientDoctor);
	}

	@Test
	public void mailValidationMailValidDateNow() {

		Set<ConstraintViolation<Mail>> violations = validator.validate(mail);

		assertEquals(0, violations.size());
	}

	@Test
	public void mailValidationMailValidDatePast() {

		mail.setDate(pastDate);

		Set<ConstraintViolation<Mail>> violations = validator.validate(mail);

		assertEquals(0, violations.size());
	}

	@Test
	public void mailValidationInvalidDateFuture() {

		mail.setDate(futureDate);

		Set<ConstraintViolation<Mail>> violations = validator.validate(mail);

		assertEquals(1, violations.size());
		assertEquals("La date de l'élément médical doit être dans le passé ou aujourd'hui.",
				violations.iterator().next().getMessage());
	}

	@Test
	public void mailValidationInvalidTextBlank() {

		mail.setText("");

		Set<ConstraintViolation<Mail>> violations = validator.validate(mail);

		assertEquals(1, violations.size());
		assertEquals("Le texte du courrier est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void mailValidationInvalidDateNull() {

		mail.setDate(null);

		Set<ConstraintViolation<Mail>> violations = validator.validate(mail);

		assertEquals(1, violations.size());
		assertEquals("La date de l'élément médical est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void actValidationInvalidAuthoringDoctorNull() {

		mail.setAuthoringDoctor(null);

		Set<ConstraintViolation<Mail>> violations = validator.validate(mail);

		assertEquals(1, violations.size());
		assertEquals("Le médecin auteur est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void actValidationInvalidPatientFileNull() {

		mail.setPatientFile(null);

		Set<ConstraintViolation<Mail>> violations = validator.validate(mail);

		assertEquals(1, violations.size());
		assertEquals("Le dossier patient est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void mailValidationInvalidTextNull() {

		mail.setText(null);

		Set<ConstraintViolation<Mail>> violations = validator.validate(mail);

		assertEquals(1, violations.size());
		assertEquals("Le texte du courrier est obligatoire.", violations.iterator().next().getMessage());
	}

	@Test
	public void mailValidationInvalidRecipientDoctorNull() {

		mail.setRecipientDoctor(null);

		Set<ConstraintViolation<Mail>> violations = validator.validate(mail);

		assertEquals(1, violations.size());
		assertEquals("Le médecin destinataire est obligatoire.", violations.iterator().next().getMessage());
	}

}
