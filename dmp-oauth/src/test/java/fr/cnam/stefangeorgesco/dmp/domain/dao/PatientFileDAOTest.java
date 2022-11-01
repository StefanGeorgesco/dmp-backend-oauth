package fr.cnam.stefangeorgesco.dmp.domain.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
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
public class PatientFileDAOTest {

	@Autowired
	private PatientFileDAO patientFileDAO;

	@Autowired
	private Address address;

	@Autowired
	private Doctor doctor;

	@Autowired
	private PatientFile patientFile;

	@BeforeEach
	public void setup() {
		address.setStreet1("1 Rue Lecourbe");
		address.setZipcode("75015");
		address.setCity("Paris");
		address.setCountry("France");
		doctor.setId("D001");
		patientFile.setId("P002");
		patientFile.setFirstname("Patrick");
		patientFile.setLastname("Dubois");
		patientFile.setDateOfBirth(LocalDate.of(2000, 2, 13));
		patientFile.setPhone("9876543210");
		patientFile.setEmail("patrick.dubois@mail.fr");
		patientFile.setAddress(address);
		patientFile.setSecurityCode("code");
		patientFile.setReferringDoctor(doctor);
	}

	@AfterEach
	public void teardown() {
		if (patientFileDAO.existsById("P002")) {
			patientFileDAO.deleteById("P002");
		}
	}

	@Test
	public void testPatientFileDAOExistsById() {
		assertFalse(patientFileDAO.existsById("P002"));
		assertFalse(patientFileDAO.existsById("P003"));
		assertTrue(patientFileDAO.existsById("P001"));
	}

	@Test
	public void testPatientFileDAOFindById() {

		Optional<PatientFile> optionalPatientFile = patientFileDAO.findById("P001");

		assertTrue(optionalPatientFile.isPresent());

		PatientFile patientFile = optionalPatientFile.get();

		assertEquals(patientFile.getFirstname(), "Eric");
		assertEquals(patientFile.getLastname(), "Martin");
		assertEquals("D001", patientFile.getReferringDoctor().getId());
	}

	@Test
	public void testPatientFileDAOSaveCreateSuccess() {
		assertFalse(patientFileDAO.existsById("P002"));

		patientFileDAO.save(patientFile);

		assertTrue(patientFileDAO.existsById("P002"));
	}

	@Test
	public void testPatientFileDAOSaveCreateFailureInvalidData() {
		patientFile.getAddress().setCity(null);

		assertThrows(RuntimeException.class, () -> patientFileDAO.save(patientFile));

		assertFalse(patientFileDAO.existsById("P002"));
	}

	@Test
	public void testPatientFileDAOSaveCreateFailureDoctorDoesNotExist() {
		patientFile.getReferringDoctor().setId("D003");

		assertThrows(RuntimeException.class, () -> patientFileDAO.save(patientFile));

		assertFalse(patientFileDAO.existsById("P002"));
	}

	@Test
	public void testPatientFileDAOSaveUpdateEmailSuccess() {

		patientFile = patientFileDAO.findById("P001").get();

		assertNotEquals("mail@mail.com", patientFile.getEmail());

		patientFile.setEmail("mail@mail.com");

		patientFileDAO.save(patientFile);

		patientFile = patientFileDAO.findById("P001").get();

		assertEquals("mail@mail.com", patientFile.getEmail());
	}

	@Test
	public void testPatientFileDAOSaveUpdateReferringDoctorSuccess() {

		patientFile = patientFileDAO.findById("P001").get();

		assertEquals("D001", patientFile.getReferringDoctor().getId());
		assertEquals("Smith", patientFile.getReferringDoctor().getLastname());

		doctor.setId("D002");
		patientFile.setReferringDoctor(doctor);

		patientFileDAO.save(patientFile);

		patientFile = patientFileDAO.findById("P001").get();

		assertEquals("D002", patientFile.getReferringDoctor().getId());
		assertEquals("Dupont", patientFile.getReferringDoctor().getLastname());
	}

	@Test
	public void testPatientFileDAOfindByIdOrFirstnameOrLastnameFound4() {

		List<PatientFile> patientFilesList = new ArrayList<>();

		Iterable<PatientFile> patientFiles = patientFileDAO.findByIdOrFirstnameOrLastname("ma");

		patientFiles.forEach(patientFilesList::add);

		assertEquals(5, patientFilesList.size());
		assertEquals("P001", patientFilesList.get(0).getId());
		assertEquals("P005", patientFilesList.get(1).getId());
		assertEquals("P011", patientFilesList.get(2).getId());
		assertEquals("P013", patientFilesList.get(3).getId());
	}

	@Test
	public void testPatientFileDAOfindByIdOrFirstnameOrLastnameFound11() {

		List<PatientFile> patientFilesList = new ArrayList<>();

		Iterable<PatientFile> patientFiles = patientFileDAO.findByIdOrFirstnameOrLastname("P0");

		patientFiles.forEach(patientFilesList::add);

		assertEquals(12, patientFilesList.size());
	}

	@Test
	public void testPatientFileDAOfindByIdOrFirstnameOrLastnameFound0() {

		List<PatientFile> patientFilesList = new ArrayList<>();

		Iterable<PatientFile> patientFiles = patientFileDAO.findByIdOrFirstnameOrLastname("za");

		patientFiles.forEach(patientFilesList::add);

		assertEquals(0, patientFilesList.size());
	}

	@Test
	public void testPatientFileDAODeleteSuccess() {
		assertTrue(patientFileDAO.existsById("P014"));

		assertDoesNotThrow(() -> patientFileDAO.deleteById("P014"));

		assertFalse(patientFileDAO.existsById("P014"));
	}

	@Test
	public void testPatientFileDAODeleteFailurePatientFileIsReferredTo() {
		assertTrue(patientFileDAO.existsById("P001"));

		assertThrows(RuntimeException.class, () -> patientFileDAO.deleteById("P001"));

		assertTrue(patientFileDAO.existsById("P001"));
	}

	@Test
	public void testPatientFileDAODeleteFailurePatientFileDoesNotExist() {
		assertFalse(patientFileDAO.existsById("P002"));

		assertThrows(RuntimeException.class, () -> patientFileDAO.deleteById("P002"));
	}

}
