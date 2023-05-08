package fr.cnam.stefangeorgesco.dmp.domain.dao;

import fr.cnam.stefangeorgesco.dmp.domain.model.MedicalAct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application-test.properties")
@DataJpaTest
@SqlGroup({ @Sql(scripts = "/sql/create-medical-acts.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-medical-acts.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class MedicalActDAOTest {

	@Autowired
	private MedicalActDAO medicalActDAO;

	@Test
	public void testMedicalActDAOFindByIdSuccess() {

		Optional<MedicalAct> optionalMedicalAct = medicalActDAO.findById("HCAE201");

		assertTrue(optionalMedicalAct.isPresent());

		MedicalAct medicalAct = optionalMedicalAct.get();

		assertEquals("HCAE201", medicalAct.getId());
		assertEquals("Dilatation de sténose du conduit d'une glande salivaire par endoscopie [sialendoscopie] ",
				medicalAct.getDescription());
	}

	@Test
	public void testMedicalActDAOFindByIdNotFound() {

		Optional<MedicalAct> optionalMedicalAct = medicalActDAO.findById("H000000");

		assertFalse(optionalMedicalAct.isPresent());
	}

	@Test
	public void testMedicalActDAOFindByIdOrDescriptionFound9() {

		List<MedicalAct> medicalActsList = new ArrayList<>();

		Iterable<MedicalAct> medicalActs = medicalActDAO.findByIdOrDescription("radio", 10);

		medicalActs.forEach(medicalActsList::add);

		assertEquals(9, medicalActsList.size());
	}

	@Test
	public void testMedicalActDAOFindByIdOrDescriptionFound0() {

		List<MedicalAct> medicalActsList = new ArrayList<>();

		Iterable<MedicalAct> medicalActs = medicalActDAO.findByIdOrDescription("rid", 10);

		medicalActs.forEach(medicalActsList::add);

		assertEquals(0, medicalActsList.size());
	}

}
