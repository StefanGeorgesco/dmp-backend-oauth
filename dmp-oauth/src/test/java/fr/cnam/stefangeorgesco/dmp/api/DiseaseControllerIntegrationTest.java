package fr.cnam.stefangeorgesco.dmp.api;

import fr.cnam.stefangeorgesco.dmp.domain.dao.DiseaseDAO;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@SqlGroup({ @Sql(scripts = "/sql/create-diseases.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-diseases.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class DiseaseControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private DiseaseDAO diseaseDAO;

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"}) // ROLE_DOCTOR
	public void testGetDiseaseByIdSuccessUserIsDoctor() throws Exception {

		assertTrue(diseaseDAO.existsById("J01"));

		mockMvc.perform(get("/disease/J01")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id", is("J01")))
				.andExpect(jsonPath("$.description", is("Sinusite aiguë")));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"}) // ROLE_DOCTOR
	public void testGetDiseaseByIdFailureUserIsDoctorDiseaseDoesNotExist() throws Exception {

		assertFalse(diseaseDAO.existsById("J000"));

		mockMvc.perform(get("/disease/J000")).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("Maladie non trouvée.")));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testGetDiseaseByIdFailureUserIsAdmin() throws Exception {

		assertTrue(diseaseDAO.existsById("J01"));

		mockMvc.perform(get("/disease/J01")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username="P001",roles={"PATIENT"})
	public void testGetDiseaseByIdFailureUserIsPatient() throws Exception {

		assertTrue(diseaseDAO.existsById("J01"));

		mockMvc.perform(get("/disease/J01")).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testGetDiseaseByIdFailureUnauthenticatedUser() throws Exception {

		assertTrue(diseaseDAO.existsById("J01"));

		mockMvc.perform(get("/disease/J01")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"}) // ROLE_DOCTOR
	public void testGetDiseasesByIdOrDescriptionFound8UserIsDoctor() throws Exception {

		mockMvc.perform(get("/disease?q=sinusite")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(8)))
				.andExpect(jsonPath("$[2].id", is("J011")))
				.andExpect(jsonPath("$[2].description", is("Sinusite frontale aiguë")));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"}) // ROLE_DOCTOR
	public void testGetDiseasesByIdOrDescriptionLimit5UserIsDoctor() throws Exception {

		mockMvc.perform(get("/disease?q=sinusite&limit=5")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(5)));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"}) // ROLE_DOCTOR
	public void testGetDiseasesByIdOrDescriptionFound0UserIsDoctor() throws Exception {

		mockMvc.perform(get("/disease?q=mas")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"}) // ROLE_DOCTOR
	public void testGetDiseasesByIdOrDescriptionFound0SearchStringIsBlankUserIsDoctor() throws Exception {

		mockMvc.perform(get("/disease?q=")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"}) // ROLE_DOCTOR
	public void testGetDiseasesByIdOrDescriptionErrorQSearchStringIsAbsentUserIsDoctor() throws Exception {

		mockMvc.perform(get("/disease")).andExpect(status().isInternalServerError())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testGetDiseasesByIdOrDescriptionFailureUserIsAdmin() throws Exception {

		mockMvc.perform(get("/disease?q=sinusite")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username="P001",roles={"PATIENT"})
	public void testGetDiseasesByIdOrDescriptionFailureUserIsPatient() throws Exception {

		mockMvc.perform(get("/disease?q=sinusite")).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testGetDiseasesByIdOrDescriptionFailureUnauthenticatedUser() throws Exception {

		mockMvc.perform(get("/disease?q=sinusite")).andExpect(status().isUnauthorized());
	}
}
