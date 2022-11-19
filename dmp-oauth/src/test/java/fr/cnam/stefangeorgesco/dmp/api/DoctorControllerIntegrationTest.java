package fr.cnam.stefangeorgesco.dmp.api;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.service.KeycloakService;
import fr.cnam.stefangeorgesco.dmp.domain.dao.DoctorDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.SpecialtyDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.AddressDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@SqlGroup({ @Sql(scripts = "/sql/create-specialties.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-files.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-files.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-specialties.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class DoctorControllerIntegrationTest {

	@MockBean
	private KeycloakService keycloakService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private DoctorDAO doctorDAO;

	@Autowired
	private SpecialtyDAO specialtyDAO;

	@Autowired
	private SpecialtyDTO specialtyDTO1;

	@Autowired
	private SpecialtyDTO specialtyDTO2;

	@Autowired
	private AddressDTO addressDTO;

	@Autowired
	private DoctorDTO doctorDTO;

	@Autowired
	private Specialty specialty1;

	@Autowired
	private Specialty specialty2;

	@Autowired
	private Address address;

	@Autowired
	private Doctor doctor;

	@Autowired
	private User user;

	private List<SpecialtyDTO> specialtyDTOs;

	private List<Specialty> specialties;

	@BeforeEach
	public void setupBeforeEach() {
		specialtyDTO1.setId("S001");
		specialtyDTO1.setDescription("any");
		specialtyDTO2.setId("S002");
		specialtyDTO2.setDescription("any");

		specialtyDTOs = new ArrayList<>();
		specialtyDTOs.add(specialtyDTO1);
		specialtyDTOs.add(specialtyDTO2);

		addressDTO.setStreet1("1 Rue Lecourbe");
		addressDTO.setZipcode("75015");
		addressDTO.setCity("Paris");
		addressDTO.setCountry("France");

		doctorDTO.setId("D003");
		doctorDTO.setFirstname("Pierre");
		doctorDTO.setLastname("Martin");
		doctorDTO.setPhone("012345679");
		doctorDTO.setEmail("pierre.martin@docteurs.fr");
		doctorDTO.setSpecialtiesDTO(specialtyDTOs);
		doctorDTO.setAddressDTO(addressDTO);

		specialty1.setId("S001");
		specialty2.setId("S002");

		specialties = new ArrayList<>();
		specialties.add(specialty1);
		specialties.add(specialty2);

		address.setStreet1("1 Rue Lecourbe");
		address.setZipcode("75015");
		address.setCity("Paris");
		address.setCountry("France");

		doctor.setId("D003");
		doctor.setFirstname("Pierre");
		doctor.setLastname("Martin");
		doctor.setPhone("012345679");
		doctor.setEmail("pierre.martin@docteurs.fr");
		doctor.setSpecialties(specialties);
		doctor.setAddress(address);
		doctor.setSecurityCode("code");

		user.setId("D002");
		user.setUsername("username");
		user.setPassword("password");
		user.setSecurityCode("code");
	}

	@AfterEach
	public void tearDown() {
		if (doctorDAO.existsById("D003")) {
			doctorDAO.deleteById("D003");
		}
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testCreateDoctorSuccess() throws Exception {

		assertFalse(doctorDAO.existsById("D003"));

		mockMvc.perform(post("/doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstname", is("Pierre")))
				.andExpect(jsonPath("$.address.street1", is("1 Rue Lecourbe")))
				.andExpect(jsonPath("$.specialties", hasSize(2)))
				.andExpect(jsonPath("$.specialties[0].description", is("allergologie")))
				.andExpect(jsonPath("$.specialties[1].description", is("immunologie")))
				.andExpect(jsonPath("$.securityCode", notNullValue()));

		assertTrue(doctorDAO.existsById("D003"));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testCreateDoctorFailureDoctorAlreadyExists() throws Exception {
		doctorDAO.save(doctor);

		mockMvc.perform(post("/doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("Un dossier avec cet identifiant existe déjà.")));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testCreateDoctorFailureDoctorDTONonValidFirstname() throws Exception {
		doctorDTO.setFirstname(null);

		mockMvc.perform(post("/doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isNotAcceptable())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstname", is("Le prénom est obligatoire.")));

		assertFalse(doctorDAO.existsById("D003"));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testCreateDoctorFailureDoctorDTONonValidAddressStreet1() throws Exception {
		doctorDTO.getAddressDTO().setStreet1("");

		mockMvc.perform(post("/doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isNotAcceptable())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.address_street1", is("Champ 'street1' invalide.")));

		assertFalse(doctorDAO.existsById("D003"));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testCreateDoctorFailureDoctorDTOSpecialtiesNull() throws Exception {
		doctorDTO.setSpecialtiesDTO(null);

		mockMvc.perform(post("/doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isNotAcceptable())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.specialties", is("Les spécialités sont obligatoires.")));

		assertFalse(doctorDAO.existsById("D003"));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testCreateDoctorFailureDoctorDTONoSpecialty() throws Exception {
		doctorDTO.getSpecialtiesDTO().clear();

		mockMvc.perform(post("/doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isNotAcceptable())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.specialties", is("Le médecin doit avoir au moins une spécialité.")));

		assertFalse(doctorDAO.existsById("D003"));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testCreateDoctorFailureDoctorDTONonValidSpecialtyId() throws Exception {
		doctorDTO.getSpecialtiesDTO().iterator().next().setId("");

		mockMvc.perform(post("/doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isNotAcceptable())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.specialties0_id", is("L'identifiant est obligatoire.")));

		assertFalse(doctorDAO.existsById("D003"));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testCreateDoctorFailureSpecialtyDoesNotExist() throws Exception {

		((List<SpecialtyDTO>) doctorDTO.getSpecialtiesDTO()).get(1).setId("S100");

		mockMvc.perform(post("/doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("La spécialité n'existe pas.")));

		assertFalse(doctorDAO.existsById("D003"));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testCreateDoctorFailureBadRole() throws Exception {
		mockMvc.perform(post("/doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isForbidden());

		assertFalse(doctorDAO.existsById("D003"));
	}

	@Test
	@WithAnonymousUser
	public void testCreateDoctorFailureUnauthenticatedUser() throws Exception {
		mockMvc.perform(post("/doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isUnauthorized());

		assertFalse(doctorDAO.existsById("D003"));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testUpdateDoctorSuccess() throws Exception {
		assertTrue(doctorDAO.existsById("D001"));

		mockMvc.perform(put("/doctor/details").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// no change (except null securityCode)
				.andExpect(jsonPath("$.id", is("D001"))).andExpect(jsonPath("$.firstname", is("John")))
				.andExpect(jsonPath("$.lastname", is("Smith"))).andExpect(jsonPath("$.specialties", hasSize(2)))
				.andExpect(jsonPath("$.specialties[0].id", is("S001")))
				.andExpect(jsonPath("$.specialties[0].description", is("allergologie")))
				.andExpect(jsonPath("$.specialties[1].id", is("S024")))
				.andExpect(jsonPath("$.specialties[1].description", is("médecine générale")))
				// changes
				.andExpect(jsonPath("$.phone", is(doctorDTO.getPhone())))
				.andExpect(jsonPath("$.email", is(doctorDTO.getEmail())))
				.andExpect(jsonPath("$.address.street1", is(doctorDTO.getAddressDTO().getStreet1())))
				.andExpect(jsonPath("$.address.zipcode", is(doctorDTO.getAddressDTO().getZipcode())))
				.andExpect(jsonPath("$.address.city", is(doctorDTO.getAddressDTO().getCity())))
				.andExpect(jsonPath("$.address.country", is(doctorDTO.getAddressDTO().getCountry())))
				// absent
				.andExpect(jsonPath("$.securityCode").doesNotExist());
	}

	@Test
	@WithMockUser(username="P001",roles={"PATIENT"})
	public void testUpdateDoctorFailureBadRole() throws Exception {
		mockMvc.perform(put("/doctor/details").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testUpdateDoctorFailureUnauthenticatedUser() throws Exception {
		mockMvc.perform(put("/doctor/details").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isUnauthorized());

	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testGetDoctorDetailsSuccess() throws Exception {

		doctorDTO.setId("D001");

		assertTrue(doctorDAO.existsById("D001"));

		mockMvc.perform(get("/doctor/details")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstname", is("John")))
				.andExpect(jsonPath("$.address.street1", is("1 baker street")))
				.andExpect(jsonPath("$.specialties", hasSize(2)))
				.andExpect(jsonPath("$.specialties[0].description", is("allergologie")))
				.andExpect(jsonPath("$.specialties[1].description", is("médecine générale")))
				.andExpect(jsonPath("$.securityCode").doesNotExist());
	}

	@Test
	@WithMockUser(username="P001",roles={"PATIENT"})
	public void testGetDoctorDetailsFailureBadRole() throws Exception {

		mockMvc.perform(get("/doctor/details")).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testGetDoctorDetailsFailureUnauthenticatedUser() throws Exception {

		mockMvc.perform(get("/doctor/details")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testGetDoctorByIdUserIsDoctorSuccess() throws Exception {

		assertTrue(doctorDAO.existsById("D002"));

		mockMvc.perform(get("/doctor/D002")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstname", is("Jean")))
				.andExpect(jsonPath("$.address.street1", is("15 rue de Vaugirard")))
				.andExpect(jsonPath("$.specialties", hasSize(2)))
				.andExpect(jsonPath("$.specialties[0].description", is("chirurgie vasculaire")))
				.andExpect(jsonPath("$.specialties[1].description", is("neurochirurgie")))
				.andExpect(jsonPath("$.securityCode").doesNotExist());
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testGetDoctorByIdUserIsAdminSuccess() throws Exception {

		assertTrue(doctorDAO.existsById("D002"));

		mockMvc.perform(get("/doctor/D002")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstname", is("Jean")))
				.andExpect(jsonPath("$.address.street1", is("15 rue de Vaugirard")))
				.andExpect(jsonPath("$.specialties", hasSize(2)))
				.andExpect(jsonPath("$.specialties[0].description", is("chirurgie vasculaire")))
				.andExpect(jsonPath("$.specialties[1].description", is("neurochirurgie")))
				.andExpect(jsonPath("$.securityCode").doesNotExist());
	}

	@Test
	@WithMockUser(username="P001",roles={"PATIENT"})
	public void testGetDoctorByIdUserIsPatientSuccess() throws Exception {

		assertTrue(doctorDAO.existsById("D002"));

		mockMvc.perform(get("/doctor/D002")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstname", is("Jean")))
				.andExpect(jsonPath("$.address.street1", is("15 rue de Vaugirard")))
				.andExpect(jsonPath("$.specialties", hasSize(2)))
				.andExpect(jsonPath("$.specialties[0].description", is("chirurgie vasculaire")))
				.andExpect(jsonPath("$.specialties[1].description", is("neurochirurgie")))
				.andExpect(jsonPath("$.securityCode").doesNotExist());
	}

	@Test
	@WithAnonymousUser
	public void testGetDoctorByIdFailureUnauthenticatedUser() throws Exception {

		mockMvc.perform(get("/doctor/D002")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testDeleteDoctorSuccessNoUser() throws Exception {
		when(keycloakService.userExistsById("D002")).thenReturn(false);

		assertTrue(doctorDAO.existsById("D002"));

		mockMvc.perform(delete("/doctor/D002")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.status", is(200)))
				.andExpect(jsonPath("$.message", is("Le dossier de médecin a bien été supprimé.")));

		verify(keycloakService, times(1)).userExistsById("D002");
		assertFalse(doctorDAO.existsById("D002"));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testDeleteDoctorSuccessUserPresent() throws Exception {
		when(keycloakService.userExistsById("D002")).thenReturn(true);
		when(keycloakService.deleteUser(user.getId())).thenReturn(HttpStatus.NO_CONTENT);

		assertTrue(doctorDAO.existsById("D002"));

		mockMvc.perform(delete("/doctor/D002")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.status", is(200)))
				.andExpect(jsonPath("$.message", is("Le dossier de médecin a bien été supprimé.")));

		verify(keycloakService, times(1)).userExistsById("D002");
		verify(keycloakService, times(1)).deleteUser(user.getId());
		assertFalse(doctorDAO.existsById("D002"));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testDeleteDoctorFailureDoctorDoesNotExist() throws Exception {

		assertFalse(doctorDAO.existsById("D003"));

		mockMvc.perform(delete("/doctor/D003")).andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.status", is(409)))
				.andExpect(jsonPath("$.message", is("Le dossier de médecin n'a pas pu être supprimé.")));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testDeleteDoctorFailureBadRoleDoctor() throws Exception {

		mockMvc.perform(delete("/doctor/D002")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username="P001",roles={"PATIENT"})
	public void testDeleteDoctorFailureBadRolePatient() throws Exception {

		mockMvc.perform(delete("/doctor/D002")).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testDeleteDoctorFailureUnauthenticatedUser() throws Exception {

		mockMvc.perform(delete("/doctor/D002")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testFindDoctorsByIdOrFirstnameOrLastnameSuccessFound2UserIsAdmin() throws Exception {
		mockMvc.perform(get("/doctor?q=el")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id", is("D010"))).andExpect(jsonPath("$[1].id", is("D012")));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testFindDoctorsByIdOrFirstnameOrLastnameSuccessFound0() throws Exception {
		mockMvc.perform(get("/doctor?q=za")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testFindDoctorsByIdOrFirstnameOrLastnameSuccessFound0SearchStringIsBlank() throws Exception {
		mockMvc.perform(get("/doctor?q=")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testFindDoctorsByIdOrFirstnameOrLastnameSuccessFound2UserIsDoctor() throws Exception {
		mockMvc.perform(get("/doctor?q=el")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id", is("D010"))).andExpect(jsonPath("$[1].id", is("D012")));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testFindDoctorsByIdOrFirstnameOrLastnameFailureMissingQParam() throws Exception {
		mockMvc.perform(get("/doctor?question=el")).andExpect(status().isInternalServerError())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	@WithMockUser(username="P001",roles={"PATIENT"})
	public void testFindDoctorsByIdOrFirstnameOrLastnameFailureBadRolePatient() throws Exception {

		mockMvc.perform(get("/doctor?q=el")).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testFindDoctorsByIdOrFirstnameOrLastnameFailureUnauthenticatedUser() throws Exception {

		mockMvc.perform(get("/doctor?q=el")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testGetSpecialtyByIdSuccessUserIsAdmin() throws Exception {

		assertTrue(specialtyDAO.existsById("S003"));

		mockMvc.perform(get("/specialty/S003")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id", is("S003")))
				.andExpect(jsonPath("$.description", is("anesthésiologie")));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testGetSpecialtyByIdFailureUserIsAdminSPecialtyDoesNotExist() throws Exception {

		assertFalse(specialtyDAO.existsById("S103"));

		mockMvc.perform(get("/specialty/S103")).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("Spécialité non trouvée.")));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testGetSpecialtyByIdFailureUserIsDoctor() throws Exception {

		assertTrue(specialtyDAO.existsById("S003"));

		mockMvc.perform(get("/specialty/S003")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username="P001",roles={"PATIENT"})
	public void testGetSpecialtyByIdFailureUserIsPatient() throws Exception {

		assertTrue(specialtyDAO.existsById("S003"));

		mockMvc.perform(get("/specialty/S003")).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testGetSpecialtyByIdFailureUnauthenticatedUser() throws Exception {

		assertTrue(specialtyDAO.existsById("S003"));

		mockMvc.perform(get("/specialty/S003")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testGetSpecialtiesByIdOrDescriptionFound8UserIsAdmin() throws Exception {

		mockMvc.perform(get("/specialty?q=chirur")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(8)))
				.andExpect(jsonPath("$[2].id", is("S008")))
				.andExpect(jsonPath("$[2].description", is("chirurgie générale")));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testGetSpecialtiesByIdOrDescriptionFound0UserIsAdmin() throws Exception {

		mockMvc.perform(get("/specialty?q=tu")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testGetSpecialtiesByIdOrDescriptionFound0SearchStringIsBlankUserIsAdmin() throws Exception {

		mockMvc.perform(get("/specialty?q=")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testGetSpecialtiesSuccessUserIsAdmin() throws Exception {

		mockMvc.perform(get("/specialty")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(45)))
				.andExpect(jsonPath("$[2].id", is("S003")))
				.andExpect(jsonPath("$[2].description", is("anesthésiologie")));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testGetSpecialtiesByIdOrDescriptionFailureUserIsDoctor() throws Exception {

		mockMvc.perform(get("/specialty?q=chirur")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username="P001",roles={"PATIENT"})
	public void testGetSpecialtiesByIdOrDescriptionFailureUserIsPatient() throws Exception {

		mockMvc.perform(get("/specialty?q=chirur")).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testGetSpecialtiesByIdOrDescriptionFailureUnauthenticatedUser() throws Exception {

		mockMvc.perform(get("/specialty?q=chirur")).andExpect(status().isUnauthorized());
	}

}
