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
public class MailTest {

	private static Validator validator;
	private LocalDate now;
	private LocalDate futureDate;
	private LocalDate pastDate;

	@Autowired
	private Mail mail;

	@Autowired
	private Doctor authoringDoctor;

	@Autowired
	private PatientFile patientFile;

	@Autowired
	private Doctor recipientDoctor;

	@BeforeAll
	public static void setupAll() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@BeforeEach
	public void setupEach() {
		now = LocalDate.now();
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
