package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dao.SpecialtyDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SpecialtyServiceTest {

	@MockBean
	private SpecialtyDAO specialtyDAO;

	@Autowired
	private SpecialtyService specialtyService;

	private SpecialtyDTO specialtyDTO1;

	private Specialty specialty1;

	private Specialty specialty2;

	@BeforeEach
	public void setupBeforeEach() {
		specialtyDTO1 = new SpecialtyDTO();
		specialtyDTO1.setId("S001");

		specialty1 = new Specialty();
		specialty1.setId("S001");
		specialty1.setDescription("First specialty");
		specialty2 = new Specialty();
		specialty2.setId("S002");
		specialty2.setDescription("Second specialty");
	}

	@Test
	public void testFindSpecialtySuccess() {

		when(specialtyDAO.findById(specialty1.getId())).thenReturn(Optional.of(specialty1));

		specialtyDTO1 = assertDoesNotThrow(() -> specialtyService.findSpecialty(specialty1.getId()));

		verify(specialtyDAO, times(1)).findById(specialty1.getId());

		assertEquals(specialty1.getId(), specialtyDTO1.getId());
		assertEquals(specialty1.getDescription(), specialtyDTO1.getDescription());
	}

	@Test
	void testFindSpecialtyFailureSpecialtyDoesNotExist() {

		when(specialtyDAO.findById(specialty1.getId())).thenReturn(Optional.empty());

		FinderException ex = assertThrows(FinderException.class, () -> specialtyService.findSpecialty(specialty1.getId()));

		assertEquals("Spécialité non trouvée.", ex.getMessage());
	}

	@Test
	void testFindSpecialtiesByIdOrDescriptionFound2() {

		when(specialtyDAO.findByIdOrDescription("chirur")).thenReturn(List.of(specialty1, specialty2));

		List<SpecialtyDTO> specialtiesDTO = specialtyService.findSpecialtiesByIdOrDescription("chirur");

		verify(specialtyDAO, times(1)).findByIdOrDescription("chirur");

		assertEquals(2, specialtiesDTO.size());
		assertEquals("S001", specialtiesDTO.get(0).getId());
		assertEquals("First specialty", specialtiesDTO.get(0).getDescription());
	}

	@Test
	void testFindSpecialtiesFound2() {

		when(specialtyDAO.findAll()).thenReturn(List.of(specialty1, specialty2));

		List<SpecialtyDTO> specialtiesDTO = specialtyService.findAllSpecialties();

		verify(specialtyDAO, times(1)).findAll();

		assertEquals(2, specialtiesDTO.size());
		assertEquals("S001", specialtiesDTO.get(0).getId());
		assertEquals("First specialty", specialtiesDTO.get(0).getDescription());
	}

}
