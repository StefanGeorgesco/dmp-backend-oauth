package fr.cnam.stefangeorgesco.dmp.authentication.domain.service;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class UserServiceImplIntegrationTest {

	@MockBean
	private KeycloakService keycloakService;

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private FileDAO fileDAO;

	@Autowired
	private SpecialtyDAO specialtyDAO;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private UserDTO userDTO;

	private Specialty specialty;

	private Doctor doctor;

	private PatientFile patientFile;

	@BeforeEach
	public void setup() {
		specialty =  new Specialty();
		specialty.setId("S001");
		specialty.setDescription("A specialty");

		specialtyDAO.save(specialty);

		List<Specialty> specialties = new ArrayList<>();
		specialties.add(specialty);

		Address doctorAddress = new Address();
		doctorAddress.setStreet1("street");
		doctorAddress.setCity("city");
		doctorAddress.setZipcode("zip");
		doctorAddress.setCountry("country");

		doctor = new Doctor();
		doctor.setId("doctorId");
		doctor.setFirstname("firstname");
		doctor.setLastname("lastname");
		doctor.setPhone("0123456789");
		doctor.setEmail("doctor@doctors.com");
		doctor.setAddress(doctorAddress);
		doctor.setSpecialties(specialties);
		doctor.setSecurityCode(bCryptPasswordEncoder.encode("12345678"));

		fileDAO.save(doctor);

		Address patientFileAddress = new Address();
		patientFileAddress.setStreet1("1 rue de la Paix");
		patientFileAddress.setCity("Paris");
		patientFileAddress.setZipcode("75001");
		patientFileAddress.setCountry("France");

		patientFile = new PatientFile();
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

		userDTO = new UserDTO();
		userDTO.setId("doctorId");
		userDTO.setUsername("username");
		userDTO.setPassword("password");
		userDTO.setSecurityCode("12345678");
	}

	@AfterEach
	public void tearDown() {
		fileDAO.delete(patientFile);
		fileDAO.delete(doctor);
		specialtyDAO.delete(specialty);
	}

	@Test
	public void testCreateDoctorAccountSuccess() {
		
		when(keycloakService.createUser(userDTO)).thenReturn(HttpStatus.CREATED);

		assertDoesNotThrow(() -> userService.createUser(userDTO));

		verify(keycloakService, times(1)).createUser(userDTO);
	}

	@Test
	public void testCreatePatientAccountSuccess() {

		when(keycloakService.createUser(userDTO)).thenReturn(HttpStatus.CREATED);

		userDTO.setId("patientFileId");
		userDTO.setSecurityCode("7890");

		assertDoesNotThrow(() -> userService.createUser(userDTO));

		verify(keycloakService, times(1)).createUser(userDTO);
	}

	@Test
	public void testCreateDoctorAccountFailureUserAccountAlreadyExistsById() {

		when(keycloakService.userExistsById(userDTO.getId())).thenReturn(true);
		
		DuplicateKeyException ex = assertThrows(DuplicateKeyException.class, () -> userService.createUser(userDTO));

		verify(keycloakService, times(1)).userExistsById(userDTO.getId());
		assertEquals("Le compte utilisateur existe déjà.", ex.getMessage());
	}

	@Test
	public void testCreatePatientAccountFailureUserAccountAlreadyExistsById() {

		when(keycloakService.userExistsById(userDTO.getId())).thenReturn(true);
		
		DuplicateKeyException ex = assertThrows(DuplicateKeyException.class, () -> userService.createUser(userDTO));

		verify(keycloakService, times(1)).userExistsById(userDTO.getId());
		assertEquals("Le compte utilisateur existe déjà.", ex.getMessage());
	}

	@Test
	public void testCreateDoctorAccountFailureUserAccountAlreadyExistsByUsername() {

		when(keycloakService.userExistsByUsername(userDTO.getUsername())).thenReturn(true);

		DuplicateKeyException ex = assertThrows(DuplicateKeyException.class, () -> userService.createUser(userDTO));

		verify(keycloakService, times(1)).userExistsByUsername(userDTO.getUsername());
		assertEquals("Le nom d'utilisateur existe déjà.", ex.getMessage());
	}

	@Test
	public void testCreatePatientAccountFailureUserAccountAlreadyExistsByUsername() {

		when(keycloakService.userExistsByUsername(userDTO.getUsername())).thenReturn(true);

		DuplicateKeyException ex = assertThrows(DuplicateKeyException.class, () -> userService.createUser(userDTO));

		verify(keycloakService, times(1)).userExistsByUsername(userDTO.getUsername());
		assertEquals("Le nom d'utilisateur existe déjà.", ex.getMessage());
	}

	@Test
	public void testCreateDoctorAccountFailureFileDoesNotExist() {

		fileDAO.delete(patientFile);
		fileDAO.delete(doctor);

		assertThrows(FinderException.class, () -> userService.createUser(userDTO));
	}

	@Test
	public void testCreatePatientAccountFailureFileDoesNotExist() {

		fileDAO.delete(patientFile);

		userDTO.setId("patientFileId");
		userDTO.setSecurityCode("7890");

		assertThrows(FinderException.class, () -> userService.createUser(userDTO));
	}

	@Test
	public void testCreateDoctorAccountFailureCheckUserDataError() {

		userDTO.setSecurityCode("1111");

		assertThrows(CheckException.class, () -> userService.createUser(userDTO));
	}

	@Test
	public void testCreatePatientAccountFailureCheckUserDataError() {

		userDTO.setId("patientFileId");
		userDTO.setSecurityCode("1111");

		assertThrows(CheckException.class, () -> userService.createUser(userDTO));
	}

	@Test
	public void testDeleteUserSuccess() {
		when(keycloakService.userExistsById("D001")).thenReturn(true);
		when(keycloakService.deleteUser("D001")).thenReturn(HttpStatus.NO_CONTENT);
		
		assertDoesNotThrow(() -> userService.deleteUser("D001"));

		verify(keycloakService, times(1)).userExistsById("D001");
		verify(keycloakService, times(1)).deleteUser("D001");
	}

	@Test
	public void testDeleteUserFailureUserDoesNotExist() {
		when(keycloakService.userExistsById("D002")).thenReturn(false);

		DeleteException ex = assertThrows(DeleteException.class, () -> userService.deleteUser("D002"));

		verify(keycloakService, times(1)).userExistsById("D002");
		assertEquals("Compte utilisateur non trouvé.", ex.getMessage());
	}

}
