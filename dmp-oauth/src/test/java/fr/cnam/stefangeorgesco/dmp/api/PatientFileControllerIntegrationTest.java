package fr.cnam.stefangeorgesco.dmp.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.service.IAMService;
import fr.cnam.stefangeorgesco.dmp.domain.dao.PatientFileDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.AddressDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import fr.cnam.stefangeorgesco.dmp.domain.service.RnippService;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@SqlGroup({ @Sql(scripts = "/sql/create-specialties.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-files.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-files.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-specialties.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class PatientFileControllerIntegrationTest {

	@MockBean
	private IAMService IAMService;

	@MockBean
	private RnippService rnippService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private PatientFileDAO patientFileDAO;

	private PatientFileDTO patientFileDTO;

	private DoctorDTO doctorDTO;

	private String id;

	@BeforeEach
	public void setup() {
		AddressDTO patientAddressDTO = new AddressDTO();
		patientAddressDTO.setStreet1("1 Rue Lecourbe");
		patientAddressDTO.setZipcode("75015");
		patientAddressDTO.setCity("Paris Cedex 15");
		patientAddressDTO.setCountry("France-");

		patientFileDTO = new PatientFileDTO();
		patientFileDTO.setId("P002");
		patientFileDTO.setFirstname("Patrick");
		patientFileDTO.setLastname("Dubois");
		patientFileDTO.setDateOfBirth(LocalDate.of(2000, 2, 13));
		patientFileDTO.setPhone("9876543210");
		patientFileDTO.setEmail("patrick.dubois@mail.fr");
		patientFileDTO.setAddressDTO(patientAddressDTO);

		AddressDTO doctorAddressDTO = new AddressDTO();
		doctorAddressDTO.setStreet1("street 1");
		doctorAddressDTO.setZipcode("zipcode");
		doctorAddressDTO.setCity("city");
		doctorAddressDTO.setCountry("country");

		SpecialtyDTO specialtyDTO = new SpecialtyDTO();
		specialtyDTO.setId("id");
		specialtyDTO.setDescription("description");

		doctorDTO = new DoctorDTO();
		doctorDTO.setAddressDTO(doctorAddressDTO);
		doctorDTO.setSpecialtiesDTO(List.of(specialtyDTO));
		doctorDTO.setId("D002");
		doctorDTO.setFirstname("firstname");
		doctorDTO.setLastname("lastname");
		doctorDTO.setEmail("email@email.com");
		doctorDTO.setPhone("0000000000");
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testCreatePatientFileSuccess() throws Exception {
		doNothing().when(rnippService).checkPatientData(any(PatientFileDTO.class));

		assertFalse(patientFileDAO.existsById("P002"));

		mockMvc.perform(post("/patient-file").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(patientFileDTO))).andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstname", is("Patrick")))
				.andExpect(jsonPath("$.address.street1", is("1 Rue Lecourbe")))
				.andExpect(jsonPath("$.securityCode", notNullValue()))
				.andExpect(jsonPath("$.referringDoctorId", is("D001")))
				.andExpect(jsonPath("$.dateOfBirth", is("2000-02-13")));

		verify(rnippService, times(1)).checkPatientData(any(PatientFileDTO.class));
		assertTrue(patientFileDAO.existsById("P002"));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testCreatePatientFileFailurePatientFileAlreadyExist() throws Exception {
		Address address = new Address();
		address.setStreet1("street 1");
		address.setZipcode("zipcode");
		address.setCity("City");
		address.setCountry("Country");

		Doctor doctor = new Doctor();
		doctor.setId("D001");

		PatientFile patientFile = new PatientFile();
		patientFile.setId("P002");
		patientFile.setFirstname("Firstname");
		patientFile.setLastname("Lastname");
		patientFile.setDateOfBirth(LocalDate.of(2000, 2, 13));
		patientFile.setPhone("phone");
		patientFile.setEmail("email@email.com");
		patientFile.setAddress(address);
		patientFile.setSecurityCode("securityCode");
		patientFile.setReferringDoctor(doctor);

		patientFileDAO.save(patientFile);

		doNothing().when(rnippService).checkPatientData(any(PatientFileDTO.class));

		mockMvc.perform(post("/patient-file").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(patientFileDTO))).andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("Un dossier avec cet identifiant existe déjà.")));

		verify(rnippService, times(1)).checkPatientData(any(PatientFileDTO.class));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testCreatePatientFileFailurePatientFileDTONonValidLastname() throws Exception {
		patientFileDTO.setLastname("");

		mockMvc.perform(post("/patient-file").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(patientFileDTO))).andExpect(status().isNotAcceptable())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.lastname", is("Le nom est obligatoire.")));

		assertFalse(patientFileDAO.existsById("P002"));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testCreatePatientFileFailurePatientFileDTONonValidAddressCountry() throws Exception {
		patientFileDTO.getAddressDTO().setCountry(null);

		mockMvc.perform(post("/patient-file").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(patientFileDTO))).andExpect(status().isNotAcceptable())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.address_country", is("Champ 'country' invalide.")));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testCreatePatientFileFailureBadRole() throws Exception {
		mockMvc.perform(post("/patient-file").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(patientFileDTO))).andExpect(status().isForbidden());

		assertFalse(patientFileDAO.existsById("P002"));
	}

	@Test
	@WithAnonymousUser
	public void testCreatePatientFileFailureUnauthenticatedUser() throws Exception {
		mockMvc.perform(post("/patient-file").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(patientFileDTO))).andExpect(status().isUnauthorized());

	}

	@Test
	@WithMockUser(username="P001",roles={"PATIENT"}) // P001
	public void testUpdatePatientFileSuccess() throws Exception {
		patientFileDTO.setReferringDoctorId("D002"); // try to change doctor

		assertTrue(patientFileDAO.existsById("P001"));

		mockMvc.perform(put("/patient-file/details").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(patientFileDTO))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// no change (except null securityCode)
				.andExpect(jsonPath("$.id", is("P001"))).andExpect(jsonPath("$.firstname", is("Eric")))
				.andExpect(jsonPath("$.lastname", is("Martin"))).andExpect(jsonPath("$.referringDoctorId", is("D001")))
				.andExpect(jsonPath("$.dateOfBirth", is("1995-05-15")))
				// changes
				.andExpect(jsonPath("$.phone", is(patientFileDTO.getPhone())))
				.andExpect(jsonPath("$.email", is(patientFileDTO.getEmail())))
				.andExpect(jsonPath("$.address.street1", is(patientFileDTO.getAddressDTO().getStreet1())))
				.andExpect(jsonPath("$.address.zipcode", is(patientFileDTO.getAddressDTO().getZipcode())))
				.andExpect(jsonPath("$.address.city", is(patientFileDTO.getAddressDTO().getCity())))
				.andExpect(jsonPath("$.address.country", is(patientFileDTO.getAddressDTO().getCountry())))
				// absent
				.andExpect(jsonPath("$.securityCode").doesNotExist());
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testUpdatePatientFileFailureBadRole() throws Exception {
		mockMvc.perform(put("/patient-file/details").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(patientFileDTO))).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testUpdatePatientFileFailureUnauthenticatedUser() throws Exception {
		mockMvc.perform(put("/patient-file/details").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(patientFileDTO))).andExpect(status().isUnauthorized());

	}

	@Test
	@WithMockUser(username="P001",roles={"PATIENT"}) // P001
	public void testGetPatientFileDetailsSuccess() throws Exception {

		patientFileDTO.setId("P001");

		assertTrue(patientFileDAO.existsById("P001"));

		mockMvc.perform(get("/patient-file/details")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstname", is("Eric")))
				.andExpect(jsonPath("$.address.street1", is("1 rue de la Paix")))
				.andExpect(jsonPath("$.referringDoctorId", is("D001")))
				.andExpect(jsonPath("$.referringDoctorFirstname", is("John")))
				.andExpect(jsonPath("$.referringDoctorLastname", is("Smith")))
				.andExpect(jsonPath("$.dateOfBirth", is("1995-05-15")))
				.andExpect(jsonPath("$.securityCode").doesNotExist());
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testGetPatientFileDetailsFailureBadRole() throws Exception {

		mockMvc.perform(get("/patient-file/details")).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testGetPatientFileDetailsFailureUnauthenticatedUser() throws Exception {

		mockMvc.perform(get("/patient-file/details")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testGetPatientFileByIdUserIsDoctorSuccess() throws Exception {

		patientFileDTO.setId("P001");

		assertTrue(patientFileDAO.existsById("P001"));

		mockMvc.perform(get("/patient-file/P001")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstname", is("Eric")))
				.andExpect(jsonPath("$.address.street1", is("1 rue de la Paix")))
				.andExpect(jsonPath("$.referringDoctorId", is("D001")))
				.andExpect(jsonPath("$.referringDoctorFirstname", is("John")))
				.andExpect(jsonPath("$.referringDoctorLastname", is("Smith")))
				.andExpect(jsonPath("$.referringDoctorSpecialties", hasSize(2)))
				.andExpect(jsonPath("$.referringDoctorSpecialties[1]", is("médecine générale")))
				.andExpect(jsonPath("$.dateOfBirth", is("1995-05-15")))
				.andExpect(jsonPath("$.securityCode").doesNotExist());
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testGetPatientFileByIdUserIsAdminSuccess() throws Exception {

		patientFileDTO.setId("P001");

		assertTrue(patientFileDAO.existsById("P001"));

		mockMvc.perform(get("/patient-file/P001")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstname", is("Eric")))
				.andExpect(jsonPath("$.address.street1", is("1 rue de la Paix")))
				.andExpect(jsonPath("$.referringDoctorId", is("D001")))
				.andExpect(jsonPath("$.referringDoctorFirstname", is("John")))
				.andExpect(jsonPath("$.referringDoctorLastname", is("Smith")))
				.andExpect(jsonPath("$.dateOfBirth", is("1995-05-15")))
				.andExpect(jsonPath("$.securityCode").doesNotExist());
	}

	@Test
	@WithMockUser(username="P001",roles={"PATIENT"})
	public void testGetPatientFileByIdFailureUserIsPatient() throws Exception {

		mockMvc.perform(get("/patient-file/P001")).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testGetPatientFileByIdFailureUnauthenticatedUser() throws Exception {

		mockMvc.perform(get("/patient-file/P001")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testUpdateReferringDoctorSuccess() throws Exception {

		mockMvc.perform(put("/patient-file/P001/referring-doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstname", is("Eric")))
				.andExpect(jsonPath("$.address.street1", is("1 rue de la Paix")))
				.andExpect(jsonPath("$.referringDoctorId", is("D002"))) // changed
				.andExpect(jsonPath("$.dateOfBirth", is("1995-05-15")))
				.andExpect(jsonPath("$.securityCode").doesNotExist());
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testUpdateReferringDoctorFailureUserIsDoctor() throws Exception {

		mockMvc.perform(put("/patient-file/P001/referring-doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username="P001",roles={"PATIENT"})
	public void testUpdateReferringDoctorFailureUserIsPatient() throws Exception {

		mockMvc.perform(put("/patient-file/P001/referring-doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testUpdateReferringDoctorFailureUnauthenticatedUser() throws Exception {

		mockMvc.perform(put("/patient-file/P001/referring-doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testFindPatientFilesByIdOrFirstnameOrLastnameSuccessFound4UserIsAdmin() throws Exception {
		mockMvc.perform(get("/patient-file?q=ma")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(5)))
				.andExpect(jsonPath("$[0].id", is("P001"))).andExpect(jsonPath("$[1].id", is("P005")))
				.andExpect(jsonPath("$[2].id", is("P011"))).andExpect(jsonPath("$[3].id", is("P013")));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testFindPatientFilesByIdOrFirstnameOrLastnameSuccessFound0() throws Exception {
		mockMvc.perform(get("/patient-file?q=za")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testFindPatientFilesByIdOrFirstnameOrLastnameSuccessFound0SearchStringIsBlank() throws Exception {
		mockMvc.perform(get("/patient-file?q=")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testFindPatientFilesByIdOrFirstnameOrLastnameSuccessFound4UserIsPatientFile() throws Exception {
		mockMvc.perform(get("/patient-file?q=ma")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(5)))
				.andExpect(jsonPath("$[0].id", is("P001"))).andExpect(jsonPath("$[1].id", is("P005")))
				.andExpect(jsonPath("$[2].id", is("P011"))).andExpect(jsonPath("$[3].id", is("P013")));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testFindPatientFilesByIdOrFirstnameOrLastnameFailureMissingQParam() throws Exception {
		mockMvc.perform(get("/patient-file?question=ma")).andExpect(status().isInternalServerError())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	@WithMockUser(username="P001",roles={"PATIENT"})
	public void testFindPatientFilesByIdOrFirstnameOrLastnameFailureBadRolePatient() throws Exception {

		mockMvc.perform(get("/patient-file?q=ma")).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testFindPatientFilesByIdOrFirstnameOrLastnameFailureUnauthenticatedUser() throws Exception {

		mockMvc.perform(get("/patient-file?q=ma")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testDeletePatientFileSuccessNoUser() throws Exception {
		id = "P005";
		when(IAMService.userExistsById(id)).thenReturn(false);

		assertTrue(patientFileDAO.existsById(id));

		mockMvc.perform(delete("/patient-file/" + id)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.status", is(200)))
				.andExpect(jsonPath("$.message", is("Le dossier patient a bien été supprimé.")));

		assertFalse(patientFileDAO.existsById(id));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testDeletePatientFileSuccessUserPresent() throws Exception {
		id = "P005";
		when(IAMService.userExistsById(id)).thenReturn(true);
		when(IAMService.deleteUser(id)).thenReturn(HttpStatus.NO_CONTENT);

		assertTrue(patientFileDAO.existsById(id));

		mockMvc.perform(delete("/patient-file/" + id)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.status", is(200)))
				.andExpect(jsonPath("$.message", is("Le dossier patient a bien été supprimé.")));

		verify(IAMService, times(1)).userExistsById(id);
		verify(IAMService, times(1)).deleteUser(id);
		assertFalse(patientFileDAO.existsById(id));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testDeletePatientFileFailurePatientFileDoesNotExist() throws Exception {

		id = "P002";

		assertFalse(patientFileDAO.existsById(id));

		mockMvc.perform(delete("/patient-file/" + id)).andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.status", is(409)))
				.andExpect(jsonPath("$.message", is("Le dossier patient n'a pas pu être supprimé.")));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"}) // ROLE_DOCTOR
	public void testDeletePatientFileFailureBadRolePatientFile() throws Exception {

		id = "P005";

		mockMvc.perform(delete("/patient-file/" + id)).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username="P001",roles={"PATIENT"})
	public void testDeletePatientFileFailureBadRolePatient() throws Exception {

		id = "P005";

		mockMvc.perform(delete("/patient-file/" + id)).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testDeletePatientFileFailureUnauthenticatedUser() throws Exception {

		id = "P005";

		mockMvc.perform(delete("/patient-file/" + id)).andExpect(status().isUnauthorized());
	}

}
