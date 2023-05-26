package fr.cnam.stefangeorgesco.dmp.authentication.domain.service;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;
import fr.cnam.stefangeorgesco.dmp.domain.dao.FileDAO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DeleteException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

	@MockBean
	private FileDAO fileDAO;

	@MockBean
	private Doctor doctor;

	@MockBean
	private PatientFile patientFile;

	@MockBean
	private IAMService IAMService;

	@Autowired
	private UserService userService;

	private UserDTO userDTO;

	private User user;

	private final ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

	@BeforeEach
	public void setup() {
		userDTO = new UserDTO();
		userDTO.setId("1");
		userDTO.setUsername("username");
		userDTO.setPassword("password");
		userDTO.setSecurityCode("securityCode");

		user = new User();
		user.setUsername("nom_utilisateur");
	}

	@Test
	public void testCreateDoctorAccountSuccess() throws CheckException {
		doNothing().when(doctor).checkUserData(any(User.class), any(PasswordEncoder.class));
		when(IAMService.userExistsById(userDTO.getId())).thenReturn(false);
		when(IAMService.userExistsByUsername(userDTO.getUsername())).thenReturn(false);
		when(fileDAO.findById(userDTO.getId())).thenReturn(Optional.of(doctor));
		when(IAMService.createUser(userDTO)).thenReturn(HttpStatus.CREATED);

		assertDoesNotThrow(() -> userService.createUser(userDTO));

		verify(doctor, times(1)).checkUserData(any(User.class), any(PasswordEncoder.class));
		verify(IAMService, times(1)).userExistsById(userDTO.getId());
		verify(IAMService, times(1)).userExistsByUsername(userDTO.getUsername());
		verify(fileDAO, times(1)).findById(userDTO.getId());
		verify(IAMService, times(1)).createUser(userDTO);
	}

	@Test
	public void testCreatePatientAccountSuccess() throws CheckException {
		doNothing().when(patientFile).checkUserData(any(User.class), any(PasswordEncoder.class));
		when(IAMService.userExistsById(userDTO.getId())).thenReturn(false);
		when(IAMService.userExistsByUsername(userDTO.getUsername())).thenReturn(false);
		when(fileDAO.findById(userDTO.getId())).thenReturn(Optional.of(patientFile));
		when(IAMService.createUser(userDTO)).thenReturn(HttpStatus.CREATED);

		assertDoesNotThrow(() -> userService.createUser(userDTO));

		verify(patientFile, times(1)).checkUserData(any(User.class), any(PasswordEncoder.class));
		verify(IAMService, times(1)).userExistsById(userDTO.getId());
		verify(IAMService, times(1)).userExistsByUsername(userDTO.getUsername());
		verify(fileDAO, times(1)).findById(userDTO.getId());
		verify(IAMService, times(1)).createUser(userDTO);
	}

	@Test
	public void testCreateAccountFailureUserAccountAlreadyExistsById() {
		when(IAMService.userExistsById(userDTO.getId())).thenReturn(true);

		DuplicateKeyException ex = assertThrows(DuplicateKeyException.class, () -> userService.createUser(userDTO));

		verify(IAMService, times(1)).userExistsById(userDTO.getId());
		verify(IAMService, times(0)).createUser(any(UserDTO.class));
		assertEquals("Le compte utilisateur existe déjà.", ex.getMessage());
	}

	@Test
	public void testCreateAccountFailureUserAccountAlreadyExistsByUsername() {
		when(IAMService.userExistsById(userDTO.getId())).thenReturn(false);
		when(IAMService.userExistsByUsername(userDTO.getUsername())).thenReturn(true);

		DuplicateKeyException ex = assertThrows(DuplicateKeyException.class, () -> userService.createUser(userDTO));

		verify(IAMService, times(1)).userExistsById(userDTO.getId());
		verify(IAMService, times(1)).userExistsByUsername(userDTO.getUsername());
		verify(IAMService, times(0)).createUser(any(UserDTO.class));
		assertEquals("Le nom d'utilisateur existe déjà.", ex.getMessage());
	}

	@Test
	public void testCreateAccountFailureFileDoesNotExist() {
		when(IAMService.userExistsById(userDTO.getId())).thenReturn(false);
		when(IAMService.userExistsByUsername(userDTO.getUsername())).thenReturn(false);
		when(fileDAO.findById(userDTO.getId())).thenReturn(Optional.empty());

		FinderException ex = assertThrows(FinderException.class, () -> userService.createUser(userDTO));

		verify(IAMService, times(1)).userExistsById(userDTO.getId());
		verify(IAMService, times(1)).userExistsByUsername(userDTO.getUsername());
		verify(IAMService, times(0)).createUser(any(UserDTO.class));
		assertEquals("Le dossier n'existe pas.", ex.getMessage());
	}

	@Test
	public void testCreateDoctorAccountFailureCheckUserDataError() throws CheckException {
		when(IAMService.userExistsById(userDTO.getId())).thenReturn(false);
		when(IAMService.userExistsByUsername(userDTO.getUsername())).thenReturn(false);
		when(fileDAO.findById(userDTO.getId())).thenReturn(Optional.of(doctor));
		doThrow(new CheckException("Les données ne correspondent pas.")).when(doctor)
				.checkUserData(userCaptor.capture(), any(PasswordEncoder.class));

		CheckException ex = assertThrows(CheckException.class, () -> userService.createUser(userDTO));

		assertEquals("Les données ne correspondent pas.", ex.getMessage());

		verify(IAMService, times(1)).userExistsById(userDTO.getId());
		verify(IAMService, times(1)).userExistsByUsername(userDTO.getUsername());
		verify(fileDAO, times(1)).findById(userDTO.getId());
		verify(doctor, times(1)).checkUserData(any(User.class), any(PasswordEncoder.class));
		verify(IAMService, times(0)).createUser(any(UserDTO.class));

		user = userCaptor.getValue();

		assertEquals(userDTO.getId(), user.getId());
		assertEquals(userDTO.getUsername(), user.getUsername());
		assertEquals(userDTO.getSecurityCode(), user.getSecurityCode());
	}

	@Test
	public void testCreatePatientAccountFailureCheckUserDataError() throws CheckException {
		when(IAMService.userExistsById(userDTO.getId())).thenReturn(false);
		when(IAMService.userExistsByUsername(userDTO.getUsername())).thenReturn(false);
		when(fileDAO.findById(userDTO.getId())).thenReturn(Optional.of(patientFile));
		doThrow(new CheckException("Les données ne correspondent pas.")).when(patientFile)
				.checkUserData(userCaptor.capture(), any(PasswordEncoder.class));

		CheckException ex = assertThrows(CheckException.class, () -> userService.createUser(userDTO));

		assertEquals("Les données ne correspondent pas.", ex.getMessage());

		verify(IAMService, times(1)).userExistsById(userDTO.getId());
		verify(IAMService, times(1)).userExistsByUsername(userDTO.getUsername());
		verify(fileDAO, times(1)).findById(userDTO.getId());
		verify(patientFile, times(1)).checkUserData(any(User.class), any(PasswordEncoder.class));
		verify(IAMService, times(0)).createUser(any(UserDTO.class));

		user = userCaptor.getValue();

		assertEquals(userDTO.getId(), user.getId());
		assertEquals(userDTO.getUsername(), user.getUsername());
		assertEquals(userDTO.getSecurityCode(), user.getSecurityCode());
	}

	@Test
	public void testDeleteUserSuccess() {
		when(IAMService.userExistsById("P001")).thenReturn(true);
		when(IAMService.deleteUser("P001")).thenReturn(HttpStatus.NO_CONTENT);

		assertDoesNotThrow(() -> userService.deleteUser("P001"));

		verify(IAMService, times(1)).userExistsById("P001");
		verify(IAMService, times(1)).deleteUser("P001");
	}

	@Test
	public void testDeleteUserFailureUserDoesNotExist() {
		when(IAMService.userExistsById("P001")).thenReturn(false);

		DeleteException ex = assertThrows(DeleteException.class, () -> userService.deleteUser("P001"));

		verify(IAMService, times(1)).userExistsById("P001");
		assertEquals("Compte utilisateur non trouvé.", ex.getMessage());
	}

	@Test
	public void testDeleteUserFailureKeycloakServiceException() {
		when(IAMService.userExistsById("P001")).thenReturn(true);
		doThrow(new WebClientResponseException(0, "", null, null, null)).when(IAMService).deleteUser("P001");

		DeleteException ex = assertThrows(DeleteException.class, () -> userService.deleteUser("P001"));

		verify(IAMService, times(1)).userExistsById("P001");
		verify(IAMService, times(1)).deleteUser("P001");
		assertEquals("Le compte utilisateur n'a pas pu être supprimé (erreur webclient Keycloak).", ex.getMessage());
	}

	@Test
	public void testDeleteUserFailureKeycloakServiceReturnsWrongHttpStatus() {
		when(IAMService.userExistsById("P001")).thenReturn(true);
		when(IAMService.deleteUser("P001")).thenReturn(HttpStatus.BAD_REQUEST);

		DeleteException ex = assertThrows(DeleteException.class, () -> userService.deleteUser("P001"));

		verify(IAMService, times(1)).userExistsById("P001");
		verify(IAMService, times(1)).deleteUser("P001");
		assertTrue(ex.getMessage()
				.startsWith("Le compte utilisateur n'a pas pu être supprimé (erreur Keycloak, HTTPStatus : "));
	}
}
