package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dto.DiseaseDTO;
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
@SqlGroup({ @Sql(scripts = "/sql/create-diseases.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-diseases.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class DiseaseServiceIntegrationTest {

	@Autowired
	private DiseaseService diseaseService;

	@Test
	public void testFindDiseaseSuccess() {

		DiseaseDTO diseaseDTO = assertDoesNotThrow(() -> diseaseService.findDisease("J01"));

		assertEquals("J01", diseaseDTO.getId());
		assertEquals("Sinusite aiguë", diseaseDTO.getDescription());
	}

	@Test
	public void testFindDiseaseFailureDiseaseDoesNotExist() {

		FinderException ex = assertThrows(FinderException.class, () -> diseaseService.findDisease("J000"));

		assertEquals("Maladie non trouvée.", ex.getMessage());
	}

	@Test
	public void testFindDiseasesByIdOrDescriptionFound8() {

		List<DiseaseDTO> diseasesDTO = diseaseService.findDiseasesByIdOrDescription("sinusite", 10);

		assertEquals(8, diseasesDTO.size());
	}

	@Test
	public void testFindDiseasesByIdOrDescriptionFound0() {

		List<DiseaseDTO> diseasesDTO = diseaseService.findDiseasesByIdOrDescription("mas", 10);

		assertEquals(0, diseasesDTO.size());
	}

	@Test
	public void testFindDiseasesByIdOrDescriptionSearchStringIsBlank() {

		List<DiseaseDTO> diseasesDTO = diseaseService.findDiseasesByIdOrDescription("", 10);

		assertEquals(0, diseasesDTO.size());
	}
}
