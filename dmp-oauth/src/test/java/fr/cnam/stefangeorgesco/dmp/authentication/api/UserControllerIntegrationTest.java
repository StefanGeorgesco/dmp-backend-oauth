package fr.cnam.stefangeorgesco.dmp.authentication.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.service.KeycloakService;
import fr.cnam.stefangeorgesco.dmp.domain.dao.FileDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.SpecialtyDAO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerIntegrationTest {

	@MockBean
	private KeycloakService keycloakService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	UserDTO userDTO;

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
	}

	@AfterEach
	public void tearDown() {
		fileDAO.delete(patientFile);
		fileDAO.delete(doctor);
		specialtyDAO.delete(specialty);
	}

	@Test
	public void testCreateDoctorAccountSuccess() throws Exception {

		when(keycloakService.createUser(any(UserDTO.class))).thenReturn(HttpStatus.CREATED);

		mockMvc.perform(
				post("/user").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userDTO)))
				.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("Le compte utilisateur a été créé.")));
	}

	@Test
	public void testCreatePatientAccountSuccess() throws Exception {

		when(keycloakService.createUser(any(UserDTO.class))).thenReturn(HttpStatus.CREATED);

		userDTO.setId("patientFileId");
		userDTO.setSecurityCode("7890");

		mockMvc.perform(
				post("/user").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userDTO)))
				.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("Le compte utilisateur a été créé.")));

	}

	@Test
	public void testCreateDoctorAccountFailureUserDTONonValid() throws Exception {

		userDTO.setId(null);
		userDTO.setUsername("");
		userDTO.setPassword("123");

		mockMvc.perform(
				post("/user").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userDTO)))
				.andExpect(status().isNotAcceptable()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is("L'identifiant est obligatoire.")))
				.andExpect(jsonPath("$.username", is("Le non utilisateur est obligatoire.")))
				.andExpect(jsonPath("$.password", is("Le mot de passe doit contenir au moins 4 caractères.")));
	}

	@Test
	public void testCreateDoctorAccountFailureUserAccountAlreadyExistsById() throws Exception {

		when(keycloakService.userExistsById(userDTO.getId())).thenReturn(true);

		mockMvc.perform(
				post("/user").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userDTO)))
				.andExpect(status().isConflict()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("Le compte utilisateur existe déjà.")));

	}

	@Test
	public void testCreateDoctorAccountFailureUserAccountAlreadyExistsByUsername() throws Exception {

		when(keycloakService.userExistsByUsername(userDTO.getUsername())).thenReturn(true);
		
		mockMvc.perform(
				post("/user").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userDTO)))
				.andExpect(status().isConflict()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("Le nom d'utilisateur existe déjà.")));

	}

	@Test
	public void testCreatePatientAccountFailureFileDoesNotExist() throws Exception {

		fileDAO.delete(patientFile);

		userDTO.setId("patientFileId");
		userDTO.setSecurityCode("7890");

		mockMvc.perform(
				post("/user").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userDTO)))
				.andExpect(status().isNotFound()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("Le dossier n'existe pas.")));
	}

	@Test
	public void testCreateDoctorAccountFailureCheckUserDataError() throws Exception {

		userDTO.setSecurityCode("1111");

		mockMvc.perform(
				post("/user").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userDTO)))
				.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("Les données ne correspondent pas.")));
	}

}
