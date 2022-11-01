package fr.cnam.stefangeorgesco.dmp.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
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
public class DoctorTest {

	private static Validator validator;

	@Autowired
	private Doctor doctor;

	@Autowired
	private Address address;

	private List<Specialty> specialties;

	@Autowired
	private Specialty specialty;

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
		specialty.setId("specialtyId");
		specialty.setDescription("A specialty");

		specialties = new ArrayList<>();
		specialties.add(specialty);

		address.setStreet1("street");
		address.setCity("city");
		address.setZipcode("zip");
		address.setCountry("country");

		doctor.setId("id");
		doctor.setFirstname("firstname");
		doctor.setLastname("lastname");
		doctor.setPhone("0123456789");
		doctor.setEmail("doctor@doctors.com");
		doctor.setAddress(address);
		doctor.setSpecialties(specialties);
		doctor.setSecurityCode(bCryptPasswordEncoder.encode("12345678"));

		user.setId("id");
		user.setSecurityCode("12345678");

	}

	@Test
	public void doctorValidationDoctorVAlid() {

		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);

		assertEquals(0, violations.size());
	}

	@Test
	public void doctorValidationIdInvalidBlank() {

		doctor.setId("");

		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);

		assertEquals(1, violations.size());
		assertEquals("L'identifiant est obligatoire.", violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationFirstnameInvalidBlank() {

		doctor.setFirstname("");

		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);

		assertEquals(1, violations.size());
		assertEquals("Le prénom est obligatoire.", violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationLastnameInvalidBlank() {

		doctor.setLastname("");

		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);

		assertEquals(1, violations.size());
		assertEquals("Le nom est obligatoire.", violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationPhoneInvalidBlank() {

		doctor.setPhone("");

		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);

		assertEquals(1, violations.size());
		assertEquals("Le numéro de téléphone est obligatoire.", violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationEmailInvalidFormat() {

		doctor.setEmail("email");

		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);

		assertEquals(1, violations.size());
		assertEquals("L'adresse email doit être fournie et respecter le format.",
				violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationIdInvalidNull() {

		doctor.setId(null);

		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);

		assertEquals(1, violations.size());
		assertEquals("L'identifiant est obligatoire.", violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationFirstnameInvalidNull() {

		doctor.setFirstname(null);

		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);

		assertEquals(1, violations.size());
		assertEquals("Le prénom est obligatoire.", violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationLastnameInvalidNull() {

		doctor.setLastname(null);

		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);

		assertEquals(1, violations.size());
		assertEquals("Le nom est obligatoire.", violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationPhoneInvalidNull() {

		doctor.setPhone(null);

		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);

		assertEquals(1, violations.size());
		assertEquals("Le numéro de téléphone est obligatoire.", violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationEmailInvalidNull() {

		doctor.setEmail(null);

		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);

		assertEquals(1, violations.size());
		assertEquals("L'adresse email est obligatoire.", violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationAddressInvalidNull() {

		doctor.setAddress(null);

		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);

		assertEquals(1, violations.size());
		assertEquals("L'adresse est obligatoire.", violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationSpecialtiesInvalidNull() {

		doctor.setSpecialties(null);

		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);

		assertEquals(1, violations.size());
		assertEquals("Les spécialités sont obligatoires.", violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationSpecialtiesInvalidEmpty() {

		doctor.getSpecialties().clear();

		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);

		assertEquals(1, violations.size());
		assertEquals("Le médecin doit avoir au moins une spécialité.", violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationAddressInvalidStreet1Null() {

		doctor.getAddress().setStreet1(null);

		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);

		assertEquals(1, violations.size());
		assertEquals("Champ 'street1' invalide.", violations.iterator().next().getMessage());

	}

	@Test
	public void checkUserDataMatchDoesNotThrowException() {

		assertDoesNotThrow(() -> doctor.checkUserData(user, bCryptPasswordEncoder));

	}

	@Test
	public void checkUserNullUserThrowsCheckException() {

		user = null;

		CheckException exception = assertThrows(CheckException.class,
				() -> doctor.checkUserData(user, bCryptPasswordEncoder));
		assertEquals("Impossible de vérifier un utilisateur 'null'.", exception.getMessage());

	}

	@Test
	public void checkUserNullUserIdThrowsCheckException() {

		user.setId(null);

		CheckException exception = assertThrows(CheckException.class,
				() -> doctor.checkUserData(user, bCryptPasswordEncoder));
		assertEquals("Impossible de vérifier un utilisateur avec un identifiant 'null'.", exception.getMessage());

	}

	@Test
	public void checkUserNullSecurityCodeThrowsCheckException() {

		user.setSecurityCode(null);

		CheckException exception = assertThrows(CheckException.class,
				() -> doctor.checkUserData(user, bCryptPasswordEncoder));
		assertEquals("Impossible de vérifier un utilisateur avec un code de sécurité 'null'.", exception.getMessage());

	}

	@Test
	public void checkUserDifferentUserIdThrowsCheckException() {

		user.setId("userId");

		CheckException exception = assertThrows(CheckException.class,
				() -> doctor.checkUserData(user, bCryptPasswordEncoder));
		assertEquals("Les données ne correspondent pas.", exception.getMessage());

	}

	@Test
	public void checkUserDifferentSecurityCodeThrowsCheckException() {

		user.setSecurityCode("01234567");

		CheckException exception = assertThrows(CheckException.class,
				() -> doctor.checkUserData(user, bCryptPasswordEncoder));
		assertEquals("Les données ne correspondent pas.", exception.getMessage());

	}

}
