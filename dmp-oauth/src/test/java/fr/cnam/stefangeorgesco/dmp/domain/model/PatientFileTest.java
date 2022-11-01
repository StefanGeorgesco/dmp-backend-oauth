package fr.cnam.stefangeorgesco.dmp.domain.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class PatientFileTest {

	private static Validator validator;
	private LocalDate now;
	private LocalDate futureDate;
	private LocalDate pastDate;

	@Autowired
	private PatientFile patientFile;

	@Autowired
	private Address address;

	@Autowired
	private Doctor doctor;

	@Autowired
	private User user;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@BeforeAll
	public static void setupAll() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@BeforeEach
	public void setupEach() {
		now = LocalDate.now();
		pastDate = now.minusDays(1);
		futureDate = now.plusDays(1);

		address.setStreet1("street_patient");
		address.setCity("city_patient");
		address.setZipcode("zip_patient");
		address.setCountry("country_patient");

		patientFile.setId("id");
		patientFile.setFirstname("firstname");
		patientFile.setLastname("lastname");
		patientFile.setDateOfBirth(now);
		patientFile.setPhone("0123456789");
		patientFile.setEmail("patient@mail.com");
		patientFile.setAddress(address);
		patientFile.setSecurityCode(bCryptPasswordEncoder.encode("12345678"));
		patientFile.setReferringDoctor(doctor);

		user.setId("id");
		user.setSecurityCode("12345678");

	}

	@Test
	public void PatientFileValidationPatientFileVAlid() {

		Set<ConstraintViolation<PatientFile>> violations = validator.validate(patientFile);

		assertEquals(0, violations.size());
	}

	@Test
	public void patientFileValidationIdInvalidBlank() {

		patientFile.setId("");

		Set<ConstraintViolation<PatientFile>> violations = validator.validate(patientFile);

		assertEquals(1, violations.size());
		assertEquals("L'identifiant est obligatoire.", violations.iterator().next().getMessage());

	}

	@Test
	public void patientFileValidationFirstnameInvalidBlank() {

		patientFile.setFirstname("");

		Set<ConstraintViolation<PatientFile>> violations = validator.validate(patientFile);

		assertEquals(1, violations.size());
		assertEquals("Le prénom est obligatoire.", violations.iterator().next().getMessage());

	}

	@Test
	public void patientFileValidationLastnameInvalidBlank() {

		patientFile.setLastname("");

		Set<ConstraintViolation<PatientFile>> violations = validator.validate(patientFile);

		assertEquals(1, violations.size());
		assertEquals("Le nom est obligatoire.", violations.iterator().next().getMessage());

	}

	@Test
	public void patientFileValidationPhoneInvalidBlank() {

		patientFile.setPhone("");

		Set<ConstraintViolation<PatientFile>> violations = validator.validate(patientFile);

		assertEquals(1, violations.size());
		assertEquals("Le numéro de téléphone est obligatoire.", violations.iterator().next().getMessage());

	}

	@Test
	public void patientFileValidationEmailInvalidFormat() {

		patientFile.setEmail("email");

		Set<ConstraintViolation<PatientFile>> violations = validator.validate(patientFile);

		assertEquals(1, violations.size());
		assertEquals("L'adresse email doit être fournie et respecter le format.",
				violations.iterator().next().getMessage());

	}

	@Test
	public void patientFileValidationIdInvalidNull() {

		patientFile.setId(null);

		Set<ConstraintViolation<PatientFile>> violations = validator.validate(patientFile);

		assertEquals(1, violations.size());
		assertEquals("L'identifiant est obligatoire.", violations.iterator().next().getMessage());

	}

	@Test
	public void patientFileValidationFirstnameInvalidNull() {

		patientFile.setFirstname(null);

		Set<ConstraintViolation<PatientFile>> violations = validator.validate(patientFile);

		assertEquals(1, violations.size());
		assertEquals("Le prénom est obligatoire.", violations.iterator().next().getMessage());

	}

	@Test
	public void patientFileValidationLastnameInvalidNull() {

		patientFile.setLastname(null);

		Set<ConstraintViolation<PatientFile>> violations = validator.validate(patientFile);

		assertEquals(1, violations.size());
		assertEquals("Le nom est obligatoire.", violations.iterator().next().getMessage());

	}

	@Test
	public void patientFileValidationPhoneInvalidNull() {

		patientFile.setPhone(null);

		Set<ConstraintViolation<PatientFile>> violations = validator.validate(patientFile);

		assertEquals(1, violations.size());
		assertEquals("Le numéro de téléphone est obligatoire.", violations.iterator().next().getMessage());

	}

	@Test
	public void patientFileValidationEmailInvalidNull() {

		patientFile.setEmail(null);

		Set<ConstraintViolation<PatientFile>> violations = validator.validate(patientFile);

		assertEquals(1, violations.size());
		assertEquals("L'adresse email est obligatoire.", violations.iterator().next().getMessage());

	}

	@Test
	public void patientFileValidationAddressInvalidNull() {

		patientFile.setAddress(null);

		Set<ConstraintViolation<PatientFile>> violations = validator.validate(patientFile);

		assertEquals(1, violations.size());
		assertEquals("L'adresse est obligatoire.", violations.iterator().next().getMessage());

	}

	@Test
	public void patientFileValidationReferringDoctorInvalidNull() {

		patientFile.setReferringDoctor(null);

		Set<ConstraintViolation<PatientFile>> violations = validator.validate(patientFile);

		assertEquals(1, violations.size());
		assertEquals("Le médecin référent est obligatoire.", violations.iterator().next().getMessage());

	}

	@Test
	public void patientFileValidationAddressInvalidStreet1Null() {

		patientFile.getAddress().setStreet1(null);

		Set<ConstraintViolation<PatientFile>> violations = validator.validate(patientFile);

		assertEquals(1, violations.size());
		assertEquals("Champ 'street1' invalide.", violations.iterator().next().getMessage());

	}

	@Test
	public void patientFileValidationDateOfBirthInvalidNull() {

		patientFile.setDateOfBirth(null);

		Set<ConstraintViolation<PatientFile>> violations = validator.validate(patientFile);

		assertEquals(1, violations.size());
		assertEquals("La date de naissance est obligatoire.", violations.iterator().next().getMessage());

	}

	@Test
	public void patientFileValidationValidDateNow() {

		Set<ConstraintViolation<PatientFile>> violations = validator.validate(patientFile);

		assertEquals(0, violations.size());
	}

	@Test
	public void patientFileValidationValidDatePast() {

		patientFile.setDateOfBirth(pastDate);

		Set<ConstraintViolation<PatientFile>> violations = validator.validate(patientFile);

		assertEquals(0, violations.size());
	}

	@Test
	public void patientFileValidationInvalidDateFuture() {

		patientFile.setDateOfBirth(futureDate);

		Set<ConstraintViolation<PatientFile>> violations = validator.validate(patientFile);

		assertEquals(1, violations.size());
		assertEquals("La date de naissance doit être dans le passé ou aujourd'hui.",
				violations.iterator().next().getMessage());
	}

	@Test
	public void checkUserDataMatchDoesNotThrowException() {

		assertDoesNotThrow(() -> patientFile.checkUserData(user, bCryptPasswordEncoder));

	}

	@Test
	public void checkUserNullUserThrowsCheckException() {

		user = null;

		CheckException exception = assertThrows(CheckException.class,
				() -> patientFile.checkUserData(user, bCryptPasswordEncoder));
		assertEquals("Impossible de vérifier un utilisateur 'null'.", exception.getMessage());

	}

	@Test
	public void checkUserNullUserIdThrowsCheckException() {

		user.setId(null);

		CheckException exception = assertThrows(CheckException.class,
				() -> patientFile.checkUserData(user, bCryptPasswordEncoder));
		assertEquals("Impossible de vérifier un utilisateur avec un identifiant 'null'.", exception.getMessage());

	}

	@Test
	public void checkUserNullSecurityCodeThrowsCheckException() {

		user.setSecurityCode(null);

		CheckException exception = assertThrows(CheckException.class,
				() -> patientFile.checkUserData(user, bCryptPasswordEncoder));
		assertEquals("Impossible de vérifier un utilisateur avec un code de sécurité 'null'.", exception.getMessage());

	}

	@Test
	public void checkUserDifferentUserIdThrowsCheckException() {

		user.setId("userId");

		CheckException exception = assertThrows(CheckException.class,
				() -> patientFile.checkUserData(user, bCryptPasswordEncoder));
		assertEquals("Les données ne correspondent pas.", exception.getMessage());

	}

	@Test
	public void checkUserDifferentSecurityCodeThrowsCheckException() {

		user.setSecurityCode("01234567");

		CheckException exception = assertThrows(CheckException.class,
				() -> patientFile.checkUserData(user, bCryptPasswordEncoder));
		assertEquals("Les données ne correspondent pas.", exception.getMessage());

	}

}
