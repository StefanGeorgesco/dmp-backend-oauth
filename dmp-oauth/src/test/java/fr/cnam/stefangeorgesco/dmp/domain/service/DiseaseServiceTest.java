package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dao.DiseaseDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DiseaseDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Disease;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DiseaseServiceTest {

	@MockBean
	private DiseaseDAO diseaseDAO;

	@Autowired
	private DiseaseService diseaseService;

	private Disease disease;

	@BeforeEach
	public void setup() {
		disease = new Disease();
		disease.setId("DIS001");
		disease.setDescription("Disease 1");
	}

	@Test
	public void testFindDiseaseSuccess() {

		DiseaseDTO diseaseDTO = new DiseaseDTO();
		diseaseDTO.setId("DIS001");

		when(diseaseDAO.findById(disease.getId())).thenReturn(Optional.of(disease));

		diseaseDTO = assertDoesNotThrow(() -> diseaseService.findDisease(disease.getId()));

		verify(diseaseDAO, times(1)).findById(disease.getId());

		assertEquals(disease.getId(), diseaseDTO.getId());
		assertEquals(disease.getDescription(), diseaseDTO.getDescription());
	}

	@Test
	public void testFindDiseasesByIdOrDescriptionFound2() {

		Disease disease2 = new Disease();
		disease2.setId("DIS002");
		disease2.setDescription("Disease 2");

		when(diseaseDAO.findByIdOrDescription("sinusite", 10)).thenReturn(List.of(disease, disease2));

		List<DiseaseDTO> diseasesDTO = diseaseService.findDiseasesByIdOrDescription("sinusite", 10);

		verify(diseaseDAO, times(1)).findByIdOrDescription("sinusite", 10);

		assertEquals(2, diseasesDTO.size());
		assertEquals("DIS001", diseasesDTO.get(0).getId());
		assertEquals("Disease 1", diseasesDTO.get(0).getDescription());
	}

}
