package fr.cnam.stefangeorgesco.dmp.domain.dao;

import fr.cnam.stefangeorgesco.dmp.domain.model.Disease;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@SqlGroup({ @Sql(scripts = "/sql/create-diseases.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-diseases.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class DiseaseDAOTest {

	@Autowired
	private DiseaseDAO diseaseDAO;

	@Test
	public void testDiseaseDAOFindByIdSuccess() {

		Optional<Disease> optionalDisease = diseaseDAO.findById("J038");

		assertTrue(optionalDisease.isPresent());

		Disease disease = optionalDisease.get();

		assertEquals("J038", disease.getId());
		assertEquals("Amygdalite aiguë due à d'autres micro-organismes précisés", disease.getDescription());
	}

	@Test
	public void testDiseaseDAOFindByIdNotFound() {

		Optional<Disease> optionalDisease = diseaseDAO.findById("J000");

		assertFalse(optionalDisease.isPresent());
	}

	@Test
	public void testDiseaseDAOFindByIdOrDescriptionFound8() {

		List<Disease> diseasesList = new ArrayList<>();

		Iterable<Disease> diseases = diseaseDAO.findByIdOrDescription("sinusite", 10);

		diseases.forEach(diseasesList::add);

		assertEquals(8, diseasesList.size());
	}

	@Test
	public void testDiseaseDAOFindByIdOrDescriptionFound0() {

		List<Disease> diseasesList = new ArrayList<>();

		Iterable<Disease> diseases = diseaseDAO.findByIdOrDescription("mas", 10);

		diseases.forEach(diseasesList::add);

		assertEquals(0, diseasesList.size());
	}

}
