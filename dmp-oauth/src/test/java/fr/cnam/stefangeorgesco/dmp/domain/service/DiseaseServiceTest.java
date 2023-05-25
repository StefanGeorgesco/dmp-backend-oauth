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

	private DiseaseDTO diseaseDTO;

	private Disease disease1;

	private Disease disease2;

	@BeforeEach
	public void setup() {
		disease1 = new Disease();
		disease1.setId("DIS001");
		disease1.setDescription("Disease 1");
		disease2 = new Disease();
		disease2.setId("DIS002");
		disease2.setDescription("Disease 2");
		diseaseDTO = new DiseaseDTO();
		diseaseDTO.setId("DIS001");
	}

	@Test
	public void testFindDiseaseSuccess() {

		when(diseaseDAO.findById(disease1.getId())).thenReturn(Optional.of(disease1));

		diseaseDTO = assertDoesNotThrow(() -> diseaseService.findDisease(disease1.getId()));

		verify(diseaseDAO, times(1)).findById(disease1.getId());

		assertEquals(disease1.getId(), diseaseDTO.getId());
		assertEquals(disease1.getDescription(), diseaseDTO.getDescription());
	}

	@Test
	public void testFindDiseasesByIdOrDescriptionFound2() {

		when(diseaseDAO.findByIdOrDescription("sinusite", 10)).thenReturn(List.of(disease1, disease2));

		List<DiseaseDTO> diseasesDTO = diseaseService.findDiseasesByIdOrDescription("sinusite", 10);

		verify(diseaseDAO, times(1)).findByIdOrDescription("sinusite", 10);

		assertEquals(2, diseasesDTO.size());
		assertEquals("DIS001", diseasesDTO.get(0).getId());
		assertEquals("Disease 1", diseasesDTO.get(0).getDescription());
	}

}
