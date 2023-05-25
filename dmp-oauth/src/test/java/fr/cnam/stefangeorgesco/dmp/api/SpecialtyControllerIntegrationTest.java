package fr.cnam.stefangeorgesco.dmp.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnam.stefangeorgesco.dmp.domain.dao.SpecialtyDAO;
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
@SqlGroup({ @Sql(scripts = "/sql/create-specialties.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-specialties.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class SpecialtyControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private SpecialtyDAO specialtyDAO;

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
	public void testGetSpecialtyByIdFailureUserIsAdminSpecialtyDoesNotExist() throws Exception {

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
