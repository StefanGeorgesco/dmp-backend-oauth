package fr.cnam.stefangeorgesco.dmp.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnam.stefangeorgesco.dmp.domain.dao.CorrespondenceDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondenceDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@SqlGroup({ @Sql(scripts = "/sql/create-specialties.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-files.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-correspondences.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-correspondences.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-files.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-specialties.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class CorrespondenceControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private CorrespondenceDAO correspondenceDAO;

	private CorrespondenceDTO correspondenceDTO;

	private long count;

	private UUID uuid;

	@BeforeEach
	public void setup() {
		correspondenceDTO = new CorrespondenceDTO();
		correspondenceDTO.setDateUntil(LocalDate.now().plusDays(1));
		correspondenceDTO.setDoctorId("D002");
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testCreateCorrespondanceSuccess() throws Exception {

		count = correspondenceDAO.count();

		mockMvc.perform(post("/patient-file/P001/correspondence").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(correspondenceDTO))).andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.dateUntil", is(correspondenceDTO.getDateUntil().toString())))
				.andExpect(jsonPath("$.doctorId", is("D002"))).andExpect(jsonPath("$.doctorFirstname", is("Jean")))
				.andExpect(jsonPath("$.doctorLastname", is("Dupont")))
				.andExpect(jsonPath("$.doctorSpecialties", hasSize(2)))
				.andExpect(jsonPath("$.doctorSpecialties[0]", is("chirurgie vasculaire")))
				.andExpect(jsonPath("$.doctorSpecialties[1]", is("neurochirurgie")))
				.andExpect(jsonPath("$.patientFileId", is("P001"))).andExpect(jsonPath("$.id", hasLength(36)));

		assertEquals(count + 1, correspondenceDAO.count());
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testCreateCorrespondanceFailurePatientFileDoesNotExist() throws Exception {

		count = correspondenceDAO.count();

		mockMvc.perform(post("/patient-file/P002/correspondence").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(correspondenceDTO))).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("Dossier patient non trouvé.")));

		assertEquals(count, correspondenceDAO.count());
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testCreateCorrespondanceFailureDoctorIsNotReferringDoctor() throws Exception {

		count = correspondenceDAO.count();

		mockMvc.perform(post("/patient-file/P004/correspondence").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(correspondenceDTO))).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("L'utilisateur n'est pas le médecin référent.")));

		assertEquals(count, correspondenceDAO.count());
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testCreateCorrespondanceFailureCorrespondingDoctorIsReferringDoctor() throws Exception {

		correspondenceDTO.setDoctorId("D001");

		count = correspondenceDAO.count();

		mockMvc.perform(post("/patient-file/P001/correspondence").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(correspondenceDTO))).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(
						jsonPath("$.message", is("Impossible de créer une correspondance pour le médecin référent.")));

		assertEquals(count, correspondenceDAO.count());
	}

	@Test
	@WithMockUser(username="P001",roles={"PATIENT"})
	public void testCreateCorrespondanceFailureBadRolePatient() throws Exception {

		mockMvc.perform(post("/patient-file/P001/correspondence").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(correspondenceDTO))).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testCreateCorrespondanceFailureBadRoleAdmin() throws Exception {

		mockMvc.perform(post("/patient-file/P001/correspondence").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(correspondenceDTO))).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testCreateCorrespondanceFailureUnauthenticatedUser() throws Exception {

		mockMvc.perform(post("/patient-file/P001/correspondence").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(correspondenceDTO))).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testDeleteCorrespondanceSuccess() throws Exception {

		count = correspondenceDAO.count();

		uuid = UUID.fromString("e1eb3425-d257-4c5e-8600-b125731c458c"); // P001 (referring D001), corresponding D007

		assertTrue(correspondenceDAO.existsById(uuid));

		mockMvc.perform(delete("/patient-file/P001/correspondence/" + uuid.toString())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.status", is(200)))
				.andExpect(jsonPath("$.message", is("La correspondance a bien été supprimée.")));

		assertEquals(count - 1, correspondenceDAO.count());
		assertFalse(correspondenceDAO.existsById(uuid));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testDeleteCorrespondanceFailureDoctorIsNotReferringDoctor() throws Exception {

		count = correspondenceDAO.count();

		uuid = UUID.fromString("a376a45f-17d3-4b75-ad08-6b1da02616b6"); // P004 (referring D004), corresponding D002

		assertTrue(correspondenceDAO.existsById(uuid));

		mockMvc.perform(delete("/patient-file/P004/correspondence/" + uuid.toString())
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(correspondenceDTO)))
				.andExpect(status().isConflict()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("L'utilisateur n'est pas le médecin référent.")));

		assertEquals(count, correspondenceDAO.count());
		assertTrue(correspondenceDAO.existsById(uuid));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testDeleteCorrespondanceFailurePatientFileAndCorrespondanceDontMatch() throws Exception {

		count = correspondenceDAO.count();

		uuid = UUID.fromString("a376a45f-17d3-4b75-ad08-6b1da02616b6"); // P004 (referring D004), corresponding D002

		assertTrue(correspondenceDAO.existsById(uuid));

		mockMvc.perform(delete("/patient-file/P001/correspondence/" + uuid.toString())
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(correspondenceDTO)))
				.andExpect(status().isNotFound()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("Correspondance non trouvée pour le dossier patient 'P001'")));

		assertEquals(count, correspondenceDAO.count());
		assertTrue(correspondenceDAO.existsById(uuid));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testDeleteCorrespondanceFailureCorrespondanceDoesNotExist() throws Exception {

		count = correspondenceDAO.count();

		uuid = UUID.fromString("e1eb3425-d257-4c5e-8600-b125731c458d"); // does not exist

		assertFalse(correspondenceDAO.existsById(uuid));

		mockMvc.perform(delete("/patient-file/P001/correspondence/" + uuid.toString())
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(correspondenceDTO)))
				.andExpect(status().isNotFound()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("Correspondance non trouvée.")));

		assertEquals(count, correspondenceDAO.count());
	}

	@Test
	@WithMockUser(username="P001",roles={"PATIENT"})
	public void testDeleteCorrespondanceFailureBadRolePatient() throws Exception {

		uuid = UUID.fromString("e1eb3425-d257-4c5e-8600-b125731c458c"); // P001 (referring D001), corresponding D007

		assertTrue(correspondenceDAO.existsById(uuid));

		mockMvc.perform(delete("/patient-file/P001/correspondence/" + uuid.toString()))
				.andExpect(status().isForbidden());

		assertTrue(correspondenceDAO.existsById(uuid));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testDeleteCorrespondanceFailureBadRoleAdmin() throws Exception {

		uuid = UUID.fromString("e1eb3425-d257-4c5e-8600-b125731c458c"); // P001 (referring D001), corresponding D007

		assertTrue(correspondenceDAO.existsById(uuid));

		mockMvc.perform(delete("/patient-file/P001/correspondence/" + uuid.toString()))
				.andExpect(status().isForbidden());

		assertTrue(correspondenceDAO.existsById(uuid));
	}

	@Test
	@WithAnonymousUser
	public void testDeleteCorrespondanceFailureUnauthenticatedUser() throws Exception {

		uuid = UUID.fromString("e1eb3425-d257-4c5e-8600-b125731c458c"); // P001 (referring D001), corresponding D007

		assertTrue(correspondenceDAO.existsById(uuid));

		mockMvc.perform(delete("/patient-file/P001/correspondence/" + uuid.toString()))
				.andExpect(status().isUnauthorized());

		assertTrue(correspondenceDAO.existsById(uuid));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testFindCorrespondancesByPatientFileIdSuccessUserIsReferringDoctor() throws Exception {

		mockMvc.perform(get("/patient-file/P001/correspondence")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].id", is("5b17ffa7-81e2-43ac-9246-7cab5b2f0f6b")))
				.andExpect(jsonPath("$[0].doctorId", is("D002")))
				.andExpect(jsonPath("$[0].doctorFirstname", is("Jean")))
				.andExpect(jsonPath("$[0].doctorLastname", is("Dupont")))
				.andExpect(jsonPath("$[0].dateUntil", is("2023-05-02")));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testFindCorrespondancesByPatientFileIdSuccessUserIsActiveCorrespondingDoctor() throws Exception {

		mockMvc.perform(get("/patient-file/P006/correspondence")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[1].id", is("8ea37abc-052d-4bc3-9aa1-f9a47e366e11")))
				.andExpect(jsonPath("$[1].doctorId", is("D001")))
				.andExpect(jsonPath("$[1].doctorFirstname", is("John")))
				.andExpect(jsonPath("$[1].doctorLastname", is("Smith")))
				.andExpect(jsonPath("$[1].dateUntil", is("2027-05-07")));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testFindCorrespondancesByPatientFileIdFailureCorrespondenceExpired() throws Exception {

		mockMvc.perform(get("/patient-file/P013/correspondence")).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("L'utilisateur n'est pas le médecin référent ou correspondant.")));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testFindCorrespondancesByPatientFileIdReturns0UserIsNotReferringNorCorrespondingDoctor()
			throws Exception {

		mockMvc.perform(get("/patient-file/P004/correspondence")).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("L'utilisateur n'est pas le médecin référent ou correspondant.")));
	}

	@Test
	@WithMockUser(username="P001",roles={"PATIENT"})
	public void testFindCorrespondancesByPatientFileIdFailureBadRolePatient() throws Exception {

		mockMvc.perform(get("/patient-file/P001/correspondence")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testFindCorrespondancesByPatientFileIdFailureBadRoleAdmin() throws Exception {

		mockMvc.perform(get("/patient-file/P001/correspondence")).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testFindCorrespondancesByPatientFileIdFailureUnauthenticatedUser() throws Exception {

		mockMvc.perform(get("/patient-file/P001/correspondence")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(username="P001",roles={"PATIENT"}) // P001
	public void testFindPatientCorrespondancesSuccess() throws Exception {

		mockMvc.perform(get("/patient-file/details/correspondence")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].id", is("5b17ffa7-81e2-43ac-9246-7cab5b2f0f6b")))
				.andExpect(jsonPath("$[0].doctorId", is("D002")))
				.andExpect(jsonPath("$[0].doctorFirstname", is("Jean")))
				.andExpect(jsonPath("$[0].doctorLastname", is("Dupont")))
				.andExpect(jsonPath("$[0].dateUntil", is("2023-05-02")));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"}) // ROLE_DOCTOR
	public void testFindPatientCorrespondancesFailureBadRoleDoctor() throws Exception {

		mockMvc.perform(get("/patient-file/details/correspondence")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testFindPatientCorrespondancesFailureBadRoleAdmin() throws Exception {

		mockMvc.perform(get("/patient-file/details/correspondence")).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testFindPatientCorrespondancesFailureUnauthenticatedUser() throws Exception {

		mockMvc.perform(get("/patient-file/details/correspondence")).andExpect(status().isUnauthorized());
	}
}
