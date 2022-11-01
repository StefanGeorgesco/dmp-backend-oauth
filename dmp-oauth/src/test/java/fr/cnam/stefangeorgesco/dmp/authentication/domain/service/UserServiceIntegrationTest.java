package fr.cnam.stefangeorgesco.dmp.authentication.domain.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dao.UserDAO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;
import fr.cnam.stefangeorgesco.dmp.domain.dao.FileDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.SpecialtyDAO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DeleteException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;

@TestPropertySource("/application-test.properties")
@SpringBootTest
@SqlGroup({ @Sql(scripts = "/sql/create-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class UserServiceIntegrationTest {

	@MockBean
	private KeycloakService keycloakService;

	@Autowired
	private UserDTO userDTO;

	@Autowired
	private UserService userService;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private FileDAO fileDAO;

	@Autowired
	SpecialtyDAO specialtyDAO;

	@Autowired
	private Address doctorAddress;

	@Autowired
	private Address patientFileAddress;

	@Autowired
	private Specialty specialty;

	@Autowired
	private Doctor doctor;

	@Autowired
	private PatientFile patientFile;

	@Autowired
	private User user;
	
	private String username;

	private List<Specialty> specialties;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@BeforeEach
	public void setup() {
		specialty.setId("S001");
		specialty.setDescription("A specialty");

		specialtyDAO.save(specialty);

		specialties = new ArrayList<>();
		specialties.add(specialty);

		doctorAddress.setStreet1("street");
		doctorAddress.setCity("city");
		doctorAddress.setZipcode("zip");
		doctorAddress.setCountry("country");

		doctor.setId("doctorId");
		doctor.setFirstname("firstname");
		doctor.setLastname("lastname");
		doctor.setPhone("0123456789");
		doctor.setEmail("doctor@doctors.com");
		doctor.setAddress(doctorAddress);
		doctor.setSpecialties(specialties);
		doctor.setSecurityCode(bCryptPasswordEncoder.encode("12345678"));

		fileDAO.save(doctor);

		patientFileAddress.setStreet1("1 rue de la Paix");
		patientFileAddress.setCity("Paris");
		patientFileAddress.setZipcode("75001");
		patientFileAddress.setCountry("France");

		patientFile.setId("patientFileId");
		patientFile.setFirstname("Eric");
		patientFile.setLastname("Martin");
		patientFile.setDateOfBirth(LocalDate.of(2000, 2, 13));
		patientFile.setPhone("1111111111");
		patientFile.setEmail("eric.martin@free.fr");
		patientFile.setAddress(patientFileAddress);
		patientFile.setReferringDoctor(doctor);
		patientFile.setSecurityCode(bCryptPasswordEncoder.encode("7890"));

		fileDAO.save(patientFile);

		userDTO.setId("doctorId");
		userDTO.setUsername("username");
		userDTO.setPassword("password");
		userDTO.setSecurityCode("12345678");
		
		username = "nom_utilisateur";
		user.setUsername(username);
	}

	@AfterEach
	public void tearDown() {
		fileDAO.delete(patientFile);
		fileDAO.delete(doctor);
		specialtyDAO.delete(specialty);
		if (userDAO.existsById("doctorId")) {
			userDAO.deleteById("doctorId");
		}
		if (userDAO.existsById("patientFileId")) {
			userDAO.deleteById("patientFileId");
		}
	}

	@Test
	public void testCreateDoctorAccountSuccess() {
		
		when(keycloakService.createKeycloakUser(userDTO)).thenReturn(HttpStatus.CREATED);

		assertFalse(userDAO.existsById("doctorId"));

		assertDoesNotThrow(() -> userService.createUser(userDTO));

		assertTrue(userDAO.existsById("doctorId"));
	}

	@Test
	public void testCreatePatientAccountSuccess() {

		when(keycloakService.createKeycloakUser(userDTO)).thenReturn(HttpStatus.CREATED);

		assertFalse(userDAO.existsById("patientFileId"));

		userDTO.setId("patientFileId");
		userDTO.setSecurityCode("7890");

		assertDoesNotThrow(() -> userService.createUser(userDTO));

		assertTrue(userDAO.existsById("patientFileId"));
	}

	@Test
	public void testCreateDoctorAccountFailureUserAccountAlreadyExistsById() {

		user.setId("doctorId");
		user.setUsername("John");
		user.setPassword("0123");
		user.setSecurityCode("0000");
		userDAO.save(user);

		DuplicateKeyException ex = assertThrows(DuplicateKeyException.class, () -> userService.createUser(userDTO));
		assertEquals("Le compte utilisateur existe déjà.", ex.getMessage());
	}

	@Test
	public void testCreatePatientAccountFailureUserAccountAlreadyExistsById() {

		user.setId("patientFileId");
		user.setUsername("John");
		user.setPassword("0123");
		user.setSecurityCode("0000");
		userDAO.save(user);

		userDTO.setId("patientFileId");
		userDTO.setSecurityCode("7890");

		DuplicateKeyException ex = assertThrows(DuplicateKeyException.class, () -> userService.createUser(userDTO));
		assertEquals("Le compte utilisateur existe déjà.", ex.getMessage());
	}

	@Test
	public void testCreateDoctorAccountFailureUserAccountAlreadyExistsByUsername() {

		user.setId("id");
		user.setUsername("username");
		user.setPassword("0123");
		user.setSecurityCode("0000");
		userDAO.save(user);

		DuplicateKeyException ex = assertThrows(DuplicateKeyException.class, () -> userService.createUser(userDTO));
		assertEquals("Le nom d'utilisateur existe déjà.", ex.getMessage());
	}

	@Test
	public void testCreatePatientAccountFailureUserAccountAlreadyExistsByUsername() {

		user.setId("id");
		user.setUsername("username");
		user.setPassword("0123");
		user.setSecurityCode("0000");
		userDAO.save(user);

		userDTO.setId("patientFileId");
		userDTO.setSecurityCode("7890");

		DuplicateKeyException ex = assertThrows(DuplicateKeyException.class, () -> userService.createUser(userDTO));
		assertEquals("Le nom d'utilisateur existe déjà.", ex.getMessage());
	}

	@Test
	public void testCreateDoctorAccountFailureFileDoesNotExist() {

		fileDAO.delete(patientFile);
		fileDAO.delete(doctor);

		assertThrows(FinderException.class, () -> userService.createUser(userDTO));

		assertFalse(userDAO.existsById("doctorId"));
	}

	@Test
	public void testCreatePatientAccountFailureFileDoesNotExist() {

		fileDAO.delete(patientFile);

		userDTO.setId("patientFileId");
		userDTO.setSecurityCode("7890");

		assertThrows(FinderException.class, () -> userService.createUser(userDTO));

		assertFalse(userDAO.existsById("patientFileId"));
	}

	@Test
	public void testCreateDoctorAccountFailureCheckUserDataError() {

		userDTO.setSecurityCode("1111");

		assertThrows(CheckException.class, () -> userService.createUser(userDTO));

		assertFalse(userDAO.existsById("doctorId"));
	}

	@Test
	public void testCreatePatientAccountFailureCheckUserDataError() {

		userDTO.setId("patientFileId");
		userDTO.setSecurityCode("1111");

		assertThrows(CheckException.class, () -> userService.createUser(userDTO));

		assertFalse(userDAO.existsById("patientFileId"));
	}

	@Test
	public void testFindUserByUsernameSuccess() {
		UserDTO userDTO = assertDoesNotThrow(() -> userService.findUserByUsername("user"));

		assertEquals("D001", userDTO.getId());
		assertNull(userDTO.getPassword());
		assertNull(userDTO.getSecurityCode());
	}

	@Test
	public void testFindUserByUsernameFailureUserDoesNotExist() {
		FinderException ex = assertThrows(FinderException.class, () -> userService.findUserByUsername("user0"));

		assertEquals("Compte utilisateur non trouvé.", ex.getMessage());
	}

	@Test
	public void testDeleteUserSuccess() {
		when(keycloakService.deleteKeycloakUser("user")).thenReturn(HttpStatus.NO_CONTENT);
		
		assertTrue(userDAO.existsById("D001"));

		assertDoesNotThrow(() -> userService.deleteUser("D001"));

		assertFalse(userDAO.existsById("D001"));
	}

	@Test
	public void testDeleteUserFailureUserDoesNotExist() {
		assertFalse(userDAO.existsById("D002"));

		DeleteException ex = assertThrows(DeleteException.class, () -> userService.deleteUser("D002"));

		assertEquals("Compte utilisateur non trouvé.", ex.getMessage());
	}

}
