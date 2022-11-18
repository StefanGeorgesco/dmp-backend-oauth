package fr.cnam.stefangeorgesco.dmp.authentication.domain.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import org.springframework.security.crypto.password.PasswordEncoder;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;
import fr.cnam.stefangeorgesco.dmp.domain.dao.FileDAO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class UserServiceTest {

	@MockBean
	private FileDAO fileDAO;

	@MockBean
	private Doctor doctor;

	@MockBean
	private PatientFile patientFile;
	
	@MockBean
	private KeycloakService keycloakService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserDTO userDTO;

	@Autowired
	private User user;
	
	private String username;

	private ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

	@BeforeEach
	public void setup() {
		userDTO.setId("1");
		userDTO.setUsername("username");
		userDTO.setPassword("password");
		userDTO.setSecurityCode("securityCode");
		
		username = "nom_utilisateur";
		user.setUsername(username);
	}

	@Test
	public void testCreateDoctorAccountSuccess() throws CheckException {
		doNothing().when(doctor).checkUserData(any(User.class), any(PasswordEncoder.class));
		when(keycloakService.userExistsById(userDTO.getId())).thenReturn(false);
		when(keycloakService.userExistsByUsername(userDTO.getUsername())).thenReturn(false);
		when(fileDAO.findById(userDTO.getId())).thenReturn(Optional.of(doctor));
		when(keycloakService.createUser(userDTO)).thenReturn(HttpStatus.CREATED);
		
		assertDoesNotThrow(() -> userService.createUser(userDTO));

		verify(keycloakService, times(1)).userExistsById(userDTO.getId());
		verify(keycloakService, times(1)).userExistsByUsername(userDTO.getUsername());
		verify(fileDAO, times(1)).findById(userDTO.getId());
		verify(doctor, times(1)).checkUserData(any(User.class), any(PasswordEncoder.class));
		verify(keycloakService, times(1)).createUser(any(UserDTO.class));
	}

	@Test
	public void testCreatePatientAccountSuccess() throws CheckException {
		doNothing().when(patientFile).checkUserData(any(User.class), any(PasswordEncoder.class));
		when(keycloakService.userExistsById(userDTO.getId())).thenReturn(false);
		when(keycloakService.userExistsByUsername(userDTO.getUsername())).thenReturn(false);
		when(fileDAO.findById(userDTO.getId())).thenReturn(Optional.of(patientFile));
		when(keycloakService.createUser(userDTO)).thenReturn(HttpStatus.CREATED);

		assertDoesNotThrow(() -> userService.createUser(userDTO));

		verify(keycloakService, times(1)).userExistsById(userDTO.getId());
		verify(keycloakService, times(1)).userExistsByUsername(userDTO.getUsername());
		verify(fileDAO, times(1)).findById(userDTO.getId());
		verify(patientFile, times(1)).checkUserData(any(User.class), any(PasswordEncoder.class));
		verify(keycloakService, times(1)).createUser(any(UserDTO.class));
	}

	@Test
	public void testCreateAccountFailureUserAccountAlreadyExistsById() {
		when(keycloakService.userExistsById(userDTO.getId())).thenReturn(true);

		DuplicateKeyException ex = assertThrows(DuplicateKeyException.class, () -> userService.createUser(userDTO));

		assertEquals("Le compte utilisateur existe déjà.", ex.getMessage());
		verify(keycloakService, times(0)).createUser(any(UserDTO.class));
	}

	@Test
	public void testCreateAccountFailureUserAccountAlreadyExistsByUsername() {
		when(keycloakService.userExistsByUsername(userDTO.getUsername())).thenReturn(true);

		DuplicateKeyException ex = assertThrows(DuplicateKeyException.class, () -> userService.createUser(userDTO));

		assertEquals("Le nom d'utilisateur existe déjà.", ex.getMessage());
		verify(keycloakService, times(0)).createUser(any(UserDTO.class));
	}

	@Test
	public void testCreateAccountFailureFileDoesNotExist() {
		when(keycloakService.userExistsById(userDTO.getId())).thenReturn(false);
		when(fileDAO.findById(userDTO.getId())).thenReturn(Optional.ofNullable(null));

		FinderException ex = assertThrows(FinderException.class, () -> userService.createUser(userDTO));

		assertEquals("Le dossier n'existe pas.", ex.getMessage());
		verify(keycloakService, times(0)).createUser(any(UserDTO.class));
	}

	@Test
	public void testCreateDoctorAccountFailureCheckUserDataError() throws CheckException {
		doThrow(new CheckException("Les données ne correspondent pas.")).when(doctor)
				.checkUserData(userCaptor.capture(), any(PasswordEncoder.class));
		when(keycloakService.userExistsById(userDTO.getId())).thenReturn(false);
		when(fileDAO.findById(userDTO.getId())).thenReturn(Optional.of(doctor));

		CheckException ex = assertThrows(CheckException.class, () -> userService.createUser(userDTO));

		assertEquals("Les données ne correspondent pas.", ex.getMessage());

		verify(keycloakService, times(1)).userExistsById(userDTO.getId());
		verify(fileDAO, times(1)).findById(userDTO.getId());
		verify(doctor, times(1)).checkUserData(any(User.class), any(PasswordEncoder.class));
		verify(keycloakService, times(0)).createUser(any(UserDTO.class));

		user = userCaptor.getValue();

		assertEquals(userDTO.getId(), user.getId());
		assertEquals(userDTO.getUsername(), user.getUsername());
		assertEquals(userDTO.getSecurityCode(), user.getSecurityCode());
	}

	@Test
	public void testCreatePatientAccountFailureCheckUserDataError() throws CheckException {
		doThrow(new CheckException("Les données ne correspondent pas.")).when(patientFile)
				.checkUserData(userCaptor.capture(), any(PasswordEncoder.class));
		when(keycloakService.userExistsById(userDTO.getId())).thenReturn(false);
		when(fileDAO.findById(userDTO.getId())).thenReturn(Optional.of(patientFile));

		CheckException ex = assertThrows(CheckException.class, () -> userService.createUser(userDTO));

		assertEquals("Les données ne correspondent pas.", ex.getMessage());

		verify(keycloakService, times(1)).userExistsById(userDTO.getId());
		verify(fileDAO, times(1)).findById(userDTO.getId());
		verify(patientFile, times(1)).checkUserData(any(User.class), any(PasswordEncoder.class));
		verify(keycloakService, times(0)).createUser(any(UserDTO.class));

		user = userCaptor.getValue();

		assertEquals(userDTO.getId(), user.getId());
		assertEquals(userDTO.getUsername(), user.getUsername());
		assertEquals(userDTO.getSecurityCode(), user.getSecurityCode());
	}

	@Test
	public void testDeleteUserSuccess() {
		when(keycloakService.userExistsById("P001")).thenReturn(true);
		when(keycloakService.deleteUser("P001")).thenReturn(HttpStatus.NO_CONTENT);

		assertDoesNotThrow(() -> userService.deleteUser("P001"));

		verify(keycloakService, times(1)).deleteUser("P001");
	}

}
