package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dao.CorrespondenceDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.DoctorDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondenceDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CreateException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@SqlGroup({ @Sql(scripts = "/sql/create-specialties.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-files.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-correspondences.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-correspondences.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-files.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-specialties.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class CorrespondenceServiceIntegrationTest {

	@Autowired
	private DoctorDAO doctorDAO;

	@Autowired
	private CorrespondenceDAO correspondenceDAO;

	@Autowired
	private CorrespondenceService correspondenceService;

	private CorrespondenceDTO correspondenceDTO;

	private long count;

	private UUID uuid;

	@Test
	public void testCreateCorrespondenceSuccess() {

		count = correspondenceDAO.count();

		correspondenceDTO = new CorrespondenceDTO();
		correspondenceDTO.setDateUntil(LocalDate.now().plusDays(1));
		correspondenceDTO.setDoctorId("D002");
		correspondenceDTO.setPatientFileId("P001");

		assertNull(correspondenceDTO.getId());

		Doctor doctor = doctorDAO.findById(correspondenceDTO.getDoctorId()).orElseThrow();

		CorrespondenceDTO correspondenceDTOResponse = assertDoesNotThrow(
				() -> correspondenceService.createCorrespondence(correspondenceDTO));

		assertEquals(count + 1, correspondenceDAO.count());

		assertEquals(correspondenceDTO.getDateUntil(), correspondenceDTOResponse.getDateUntil());
		assertEquals(correspondenceDTO.getDoctorId(), correspondenceDTOResponse.getDoctorId());
		assertEquals(correspondenceDTO.getPatientFileId(), correspondenceDTOResponse.getPatientFileId());
		assertEquals(doctor.getFirstname(), correspondenceDTOResponse.getDoctorFirstname());
		assertEquals(doctor.getLastname(), correspondenceDTOResponse.getDoctorLastname());
		assertEquals(doctor.getSpecialties().stream().map(Specialty::getDescription).collect(Collectors.toList()),
				correspondenceDTOResponse.getDoctorSpecialties());

		assertNotNull(correspondenceDTOResponse.getId());
	}

	@Test
	public void testCreateCorrespondenceFailurePatientFileDoesNotExist() {

		correspondenceDTO = new CorrespondenceDTO();
		correspondenceDTO.setDateUntil(LocalDate.now().plusDays(1));
		correspondenceDTO.setDoctorId("D002");
		correspondenceDTO.setPatientFileId("P002");

		count = correspondenceDAO.count();

		CreateException ex = assertThrows(CreateException.class,
				() -> correspondenceService.createCorrespondence(correspondenceDTO));

		assertEquals("La correspondance n'a pas pu être créé.", ex.getMessage());

		assertEquals(count, correspondenceDAO.count());
	}

	@Test
	public void testCreateCorrespondenceFailureDoctorDoesNotExist() {

		correspondenceDTO = new CorrespondenceDTO();
		correspondenceDTO.setDateUntil(LocalDate.now().plusDays(1));
		correspondenceDTO.setDoctorId("D003");
		correspondenceDTO.setPatientFileId("P001");

		count = correspondenceDAO.count();

		CreateException ex = assertThrows(CreateException.class,
				() -> correspondenceService.createCorrespondence(correspondenceDTO));

		assertEquals("La correspondance n'a pas pu être créé.", ex.getMessage());

		assertEquals(count, correspondenceDAO.count());
	}

	@Test
	public void testDeleteCorrespondenceSuccess() {

		uuid = UUID.fromString("3d80bbeb-997e-4354-82d3-68cea80256d6");

		count = correspondenceDAO.count();

		assertTrue(correspondenceDAO.existsById(uuid));

		correspondenceService.deleteCorrespondence(uuid);

		assertFalse(correspondenceDAO.existsById(uuid));

		assertEquals(count - 1, correspondenceDAO.count());
	}

	@Test
	public void testFindCorrespondenceSuccess() {

		uuid = UUID.fromString("3d80bbeb-997e-4354-82d3-68cea80256d6");

		assertTrue(correspondenceDAO.existsById(uuid));

		correspondenceDTO = assertDoesNotThrow(
				() -> correspondenceService.findCorrespondence(uuid.toString()));

		assertEquals("2023-08-14", correspondenceDTO.getDateUntil().toString());
		assertEquals("P004", correspondenceDTO.getPatientFileId());
		assertEquals("Melquisedeque", correspondenceDTO.getDoctorFirstname());
		assertEquals("Nascimento", correspondenceDTO.getDoctorLastname());
		assertEquals("[chirurgie thoracique, chirurgie vasculaire]",
				correspondenceDTO.getDoctorSpecialties().toString());
	}

	@Test
	public void testFindCorrespondenceFailureCorrespondenceDoesNotExist() {

		uuid = UUID.randomUUID();

		assertFalse(correspondenceDAO.existsById(uuid));

		FinderException ex = assertThrows(FinderException.class,
				() -> correspondenceService.findCorrespondence(uuid.toString()));

		assertEquals("Correspondance non trouvée.", ex.getMessage());
	}

	@Test
	public void testFindCorrespondencesByPatientFileIdFound3() {

		List<CorrespondenceDTO> correspondencesDTO = correspondenceService.findCorrespondencesByPatientFileId("P001");

		assertEquals(3, correspondencesDTO.size());
		assertEquals("2023-05-02", correspondencesDTO.get(0).getDateUntil().toString());
		assertEquals("e1eb3425-d257-4c5e-8600-b125731c458c", correspondencesDTO.get(1).getId().toString());
		assertEquals("D011", correspondencesDTO.get(2).getDoctorId());
	}

	@Test
	public void testFindCorrespondencesByPatientFileIdFound0() {

		List<CorrespondenceDTO> correspondencesDTO = correspondenceService.findCorrespondencesByPatientFileId("P055");

		assertEquals(0, correspondencesDTO.size());
	}
}
