package fr.cnam.stefangeorgesco.dmp.api;

import fr.cnam.stefangeorgesco.dmp.domain.dao.MedicalActDAO;
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
@SqlGroup({ @Sql(scripts = "/sql/create-medical-acts.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-medical-acts.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class MedicalActControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private MedicalActDAO medicalActDAO;

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"}) // ROLE_DOCTOR
	public void testGetMedicalActByIdSuccessUserIsDoctor() throws Exception {

		assertTrue(medicalActDAO.existsById("HCAE201"));

		mockMvc.perform(get("/medical-act/HCAE201")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id", is("HCAE201")))
				.andExpect(jsonPath("$.description", is(
						"Dilatation de sténose du conduit d'une glande salivaire par endoscopie [sialendoscopie] ")));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"}) // ROLE_DOCTOR
	public void testGetMedicalActByIdFailureUserIsDoctorMedicalActDoesNotExist() throws Exception {

		assertFalse(medicalActDAO.existsById("H000000"));

		mockMvc.perform(get("/medical-act/H000000")).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("Acte médical non trouvé.")));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testGetMedicalActByIdFailureUserIsAdmin() throws Exception {

		assertTrue(medicalActDAO.existsById("HCAE201"));

		mockMvc.perform(get("/medical-act/HCAE201")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username="P001",roles={"PATIENT"})
	public void testGetMedicalActByIdFailureUserIsPatient() throws Exception {

		assertTrue(medicalActDAO.existsById("HCAE201"));

		mockMvc.perform(get("/medical-act/HCAE201")).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testGetMedicalActByIdFailureUnauthenticatedUser() throws Exception {

		assertTrue(medicalActDAO.existsById("HCAE201"));

		mockMvc.perform(get("/medical-act/HCAE201")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"}) // ROLE_DOCTOR
	public void testGetMedicalActsByIdOrDescriptionFound9UserIsDoctor() throws Exception {

		mockMvc.perform(get("/medical-act?q=radio")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(9)))
				.andExpect(jsonPath("$[2].id", is("HBQK389"))).andExpect(jsonPath("$[2].description", is(
						"Radiographie intrabuccale rétroalvéolaire et/ou rétrocoronaire d'un secteur de 1 à 3 dents contigües")));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"}) // ROLE_DOCTOR
	public void testGetMedicalActsByIdOrDescriptionLimit5UserIsDoctor() throws Exception {

		mockMvc.perform(get("/medical-act?q=radio&limit=5")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(5)));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"}) // ROLE_DOCTOR
	public void testGetMedicalActsByIdOrDescriptionFound0UserIsDoctor() throws Exception {

		mockMvc.perform(get("/medical-act?q=rid")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"}) // ROLE_DOCTOR
	public void testGetMedicalActsByIdOrDescriptionFound0SearchStringIsBlankUserIsDoctor() throws Exception {

		mockMvc.perform(get("/medical-act?q=")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"}) // ROLE_DOCTOR
	public void testGetMedicalActsByIdOrDescriptionErrorQSearchStringIsAbsentUserIsDoctor() throws Exception {

		mockMvc.perform(get("/medical-act")).andExpect(status().isInternalServerError())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testGetMedicalActsByIdOrDescriptionFailureUserIsAdmin() throws Exception {

		mockMvc.perform(get("/medical-act?q=radio")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username="P001",roles={"PATIENT"})
	public void testGetMedicalActsByIdOrDescriptionFailureUserIsPatient() throws Exception {

		mockMvc.perform(get("/medical-act?q=radio")).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testGetMedicalActsByIdOrDescriptionFailureUnauthenticatedUser() throws Exception {

		mockMvc.perform(get("/medical-act?q=radio")).andExpect(status().isUnauthorized());
	}
}
