package fr.cnam.stefangeorgesco.dmp.domain.dao;

import fr.cnam.stefangeorgesco.dmp.domain.model.Correspondence;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
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
	private PatientFileDAO patientFileDAO;

	@Autowired
	private DoctorDAO doctorDAO;

	private Correspondence correspondence;

	private long count;

	@BeforeEach
	public void setup() {
		Doctor correspondingDoctor = doctorDAO.findById("D002").orElse(new Doctor());
		PatientFile patientFile = patientFileDAO.findById("P001").orElse(new PatientFile());
		LocalDate date = LocalDate.now().plusDays(1);
		correspondence = new Correspondence();
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
	public void testCorrespondenceDAODeleteByIdSuccess() {

		UUID uuid = UUID.fromString("3d80bbeb-997e-4354-82d3-68cea80256d6");

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
