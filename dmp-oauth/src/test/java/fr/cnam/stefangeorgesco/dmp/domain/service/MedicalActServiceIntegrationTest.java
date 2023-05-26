package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dto.MedicalActDTO;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@SqlGroup({ @Sql(scripts = "/sql/create-medical-acts.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-medical-acts.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class MedicalActServiceIntegrationTest {

	@Autowired
	private MedicalActService medicalActService;

	@Test
	public void testFindMedicalActSuccess() {

		MedicalActDTO medicalActDTO = assertDoesNotThrow(() -> medicalActService.findMedicalAct("HCAE201"));

		assertEquals("HCAE201", medicalActDTO.getId());
		assertEquals("Dilatation de sténose du conduit d'une glande salivaire par endoscopie [sialendoscopie] ",
				medicalActDTO.getDescription());
	}

	@Test
	public void testFindFailureMedicalActDoesNotExist() {

		FinderException ex = assertThrows(FinderException.class, () -> medicalActService.findMedicalAct("H000000"));

		assertEquals("Acte médical non trouvé.", ex.getMessage());
	}

	@Test
	public void testFindMedicalActsByIdOrDescriptionFound9() {

		List<MedicalActDTO> medicalActsDTO = medicalActService.findMedicalActsByIdOrDescription("radio", 10);

		assertEquals(9, medicalActsDTO.size());
	}

	@Test
	public void testFindMedicalActsByIdOrDescriptionFound0() {

		List<MedicalActDTO> medicalActsDTO = medicalActService.findMedicalActsByIdOrDescription("rid", 10);

		assertEquals(0, medicalActsDTO.size());
	}

	@Test
	public void testFindMedicalActsByIdOrDescriptionSearchStringIsBlank() {

		List<MedicalActDTO> medicalActsDTO = medicalActService.findMedicalActsByIdOrDescription("", 10);

		assertEquals(0, medicalActsDTO.size());
	}
}
