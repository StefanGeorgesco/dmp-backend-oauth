package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dao.MedicalActDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.MedicalActDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.MedicalAct;
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
public class MedicalActServiceTest {

	@MockBean
	private MedicalActDAO medicalActDAO;

	@Autowired
	private MedicalActService medicalActService;

	private MedicalActDTO medicalActDTO;

	private MedicalAct medicalAct1;

	private MedicalAct medicalAct2;

	@BeforeEach
	public void setup() {
		medicalAct1 = new MedicalAct();
		medicalAct1.setId("MA001");
		medicalAct1.setDescription("Medical act 1");
		medicalAct2 = new MedicalAct();
		medicalAct2.setId("MA002");
		medicalAct2.setDescription("Medical act 2");

		medicalActDTO = new MedicalActDTO();
		medicalActDTO.setId("MA001");
	}

	@Test
	public void testFindMedicalActSuccess() {

		when(medicalActDAO.findById(medicalAct1.getId())).thenReturn(Optional.of(medicalAct1));

		medicalActDTO = assertDoesNotThrow(() -> medicalActService.findMedicalAct(medicalAct1.getId()));

		verify(medicalActDAO, times(1)).findById(medicalAct1.getId());

		assertEquals(medicalAct1.getId(), medicalActDTO.getId());
		assertEquals(medicalAct1.getDescription(), medicalActDTO.getDescription());
	}

	@Test
	public void testFindMedicalActsByIdOrDescriptionFound2() {

		when(medicalActDAO.findByIdOrDescription("radio", 10)).thenReturn(List.of(medicalAct1, medicalAct2));

		List<MedicalActDTO> medicalActsDTO = medicalActService.findMedicalActsByIdOrDescription("radio", 10);

		verify(medicalActDAO, times(1)).findByIdOrDescription("radio", 10);

		assertEquals(2, medicalActsDTO.size());
		assertEquals("MA001", medicalActsDTO.get(0).getId());
		assertEquals("Medical act 1", medicalActsDTO.get(0).getDescription());
	}
}