package fr.cnam.stefangeorgesco.dmp.domain.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import fr.cnam.stefangeorgesco.dmp.domain.model.Correspondence;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;

@TestPropertySource("/application-test.properties")
@SpringBootTest
@SqlGroup({ @Sql(scripts = "/sql/create-specialties.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-files.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-correspondences.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-correspondences.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-files.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-specialties.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class CorrespondenceDAOTest {

	@Autowired
	private CorrespondenceDAO correspondenceDAO;

	@Autowired
	private Doctor referringDoctor;

	@Autowired
	private Doctor correspondingDoctor;

	@Autowired
	private PatientFile patientFile;

	@Autowired
	private Correspondence correspondence;

	private LocalDate date;

	private long count;

	private UUID uuid;

	@BeforeEach
	public void setup() {
		referringDoctor.setId("D001");
		correspondingDoctor.setId("D002");
		patientFile.setId("P001");
		date = LocalDate.now().plusDays(1);

		correspondence.setDateUntil(date);
		correspondence.setDoctor(correspondingDoctor);
		correspondence.setPatientFile(patientFile);
	}

	@Test
	public void testCorrespondenceDAOSaveCreateSuccess() {

		count = correspondenceDAO.count();

		assertDoesNotThrow(() -> correspondenceDAO.save(correspondence));

		assertEquals(count + 1, correspondenceDAO.count());
	}

	@Test
	public void testCorrespondenceDAOSaveCreateFailurePatientFileDoesNotExist() {

		patientFile.setId("P002");

		count = correspondenceDAO.count();

		assertThrows(RuntimeException.class, () -> correspondenceDAO.save(correspondence));

		assertEquals(count, correspondenceDAO.count());
	}

	@Test
	public void testCorrespondenceDAOSaveCreateFailureDoctorDoesNotExist() {

		correspondingDoctor.setId("D003");

		count = correspondenceDAO.count();

		assertThrows(RuntimeException.class, () -> correspondenceDAO.save(correspondence));

		assertEquals(count, correspondenceDAO.count());
	}

	@Test
	public void testCorrespondenceDAODeleteByIdSuccess() {

		uuid = UUID.fromString("3d80bbeb-997e-4354-82d3-68cea80256d6");

		count = correspondenceDAO.count();

		assertTrue(correspondenceDAO.existsById(uuid));

		correspondenceDAO.deleteById(uuid);

		assertFalse(correspondenceDAO.existsById(uuid));

		assertEquals(count - 1, correspondenceDAO.count());
	}

	@Test
	public void testCorrespondenceDAOFindByPatientFileIdFound3() {

		List<Correspondence> correspondenceList = new ArrayList<>();

		Iterable<Correspondence> correspondences = correspondenceDAO.findByPatientFileId("P001");

		correspondences.forEach(correspondenceList::add);

		assertEquals(3, correspondenceList.size());
		assertEquals("2023-05-02", correspondenceList.get(0).getDateUntil().toString());
		assertEquals("e1eb3425-d257-4c5e-8600-b125731c458c", correspondenceList.get(1).getId().toString());
		assertEquals("D011", correspondenceList.get(2).getDoctor().getId());
	}

	@Test
	public void testCorrespondenceDAOFindByPatientFileIdFound0() {

		List<Correspondence> correspondenceList = new ArrayList<>();

		Iterable<Correspondence> correspondences = correspondenceDAO.findByPatientFileId("P055");

		correspondences.forEach(correspondenceList::add);

		assertEquals(0, correspondenceList.size());
	}

	@Test
	public void testCorrespondenceDAODeleteAllByPatientFileIdSuccessDeleted3() {

		List<Correspondence> correspondenceList = new ArrayList<>();

		Iterable<Correspondence> correspondences = correspondenceDAO.findByPatientFileId("P001");

		correspondences.forEach(correspondenceList::add);

		assertEquals(3, correspondenceList.size());

		int number = correspondenceDAO.deleteAllByPatientFileId("P001");

		assertEquals(3, number);

		correspondenceList = new ArrayList<>();

		correspondences = correspondenceDAO.findByPatientFileId("P001");

		correspondences.forEach(correspondenceList::add);

		assertEquals(0, correspondenceList.size());
	}

	@Test
	public void testCorrespondenceDAODeleteAllByPatientFileIdSuccessDeleted0() {

		List<Correspondence> correspondenceList = new ArrayList<>();

		Iterable<Correspondence> correspondences = correspondenceDAO.findByPatientFileId("P014");

		correspondences.forEach(correspondenceList::add);

		assertEquals(0, correspondenceList.size());

		int number = correspondenceDAO.deleteAllByPatientFileId("P014");

		assertEquals(0, number);
	}
}
