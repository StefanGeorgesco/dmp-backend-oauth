package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO;
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
@SqlGroup({ @Sql(scripts = "/sql/create-specialties.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-specialties.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class SpecialtyServiceIntegrationTest {

	@Autowired
	private SpecialtyService specialtyService;

	@Test
	public void testFindSpecialtySuccess() {
		SpecialtyDTO specialtyDTO = assertDoesNotThrow(() -> specialtyService.findSpecialty("S045"));

		assertEquals("S045", specialtyDTO.getId());
		assertEquals("urologie", specialtyDTO.getDescription());
	}

	@Test
	public void testFindSpecialtyFailureSpecialtyDoesNotExist() {

		FinderException ex = assertThrows(FinderException.class, () -> specialtyService.findSpecialty("S145"));

		assertEquals("Spécialité non trouvée.", ex.getMessage());
	}

	@Test
	public void testFindSpecialtiesByIdOrDescriptionFound8() {

		List<SpecialtyDTO> specialtiesDTO = specialtyService.findSpecialtiesByIdOrDescription("chirur");

		assertEquals(8, specialtiesDTO.size());
	}

	@Test
	public void testFindSpecialtiesByIdOrDescriptionFound0() {

		List<SpecialtyDTO> specialtiesDTO = specialtyService.findSpecialtiesByIdOrDescription("tu");

		assertEquals(0, specialtiesDTO.size());
	}

	@Test
	public void testFindSpecialtiesByIdOrDescriptionSearchStringIsBlank() {

		List<SpecialtyDTO> specialtiesDTO = specialtyService.findSpecialtiesByIdOrDescription("");

		assertEquals(0, specialtiesDTO.size());
	}

	@Test
	public void testFindAllSpecialtiesFound45() {

		List<SpecialtyDTO> specialtiesDTO = specialtyService.findAllSpecialties();

		assertEquals(45, specialtiesDTO.size());
	}
}
