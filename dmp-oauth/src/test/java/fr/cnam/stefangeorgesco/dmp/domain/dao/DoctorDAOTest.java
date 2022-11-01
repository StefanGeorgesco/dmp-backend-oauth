package fr.cnam.stefangeorgesco.dmp.domain.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;

@TestPropertySource("/application-test.properties")
@SpringBootTest
@SqlGroup({ @Sql(scripts = "/sql/create-specialties.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-files.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-files.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-specialties.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class DoctorDAOTest {

	@Autowired
	private JdbcTemplate jdbc;

	@Autowired
	SpecialtyDAO specialtyDAO;

	@Autowired
	DoctorDAO doctorDAO;

	@Autowired
	private Address doctorAddress;

	@Autowired
	private Specialty specialty1;

	@Autowired
	private Specialty specialty2;

	@Autowired
	private Doctor doctor;

	private List<Specialty> specialties;

	@BeforeEach
	public void setup() {
		specialty1.setId("S001");
		specialty2.setId("S002");
		specialties = new ArrayList<>();
		specialties.add(specialty1);
		specialties.add(specialty2);
		doctorAddress.setStreet1("street");
		doctorAddress.setCity("city");
		doctorAddress.setZipcode("zip");
		doctorAddress.setCountry("country");
		doctor.setId("D003");
		doctor.setFirstname("firstname");
		doctor.setLastname("lastname");
		doctor.setPhone("0123456789");
		doctor.setEmail("doctor@doctors.com");
		doctor.setAddress(doctorAddress);
		doctor.setSpecialties(specialties);
		doctor.setSecurityCode("12345678");
	}

	@Test
	public void testDoctorDAOExistsById() {
		assertTrue(doctorDAO.existsById("D001"));
		assertFalse(doctorDAO.existsById("D003"));
		assertFalse(doctorDAO.existsById("P001"));
	}

	@Test
	public void testDoctorDAOFindById() {
		Optional<Doctor> optionalDoctor = doctorDAO.findById("D001");

		assertTrue(optionalDoctor.isPresent());

		doctor = optionalDoctor.get();

		assertEquals(doctor.getFirstname(), "John");
		assertEquals(doctor.getLastname(), "Smith");
		assertNotNull(doctor.getSpecialties());
		assertEquals(2, doctor.getSpecialties().size());
		assertEquals("S001", ((List<Specialty>) doctor.getSpecialties()).get(0).getId());
		assertEquals("S024", ((List<Specialty>) doctor.getSpecialties()).get(1).getId());
	}

	@Test
	public void testDoctorDAOSaveCreateSuccess() {
		assertFalse(doctorDAO.existsById("D003"));

		doctorDAO.save(doctor);

		assertTrue(doctorDAO.existsById("D003"));
	}

	@Test
	public void testDoctorDAOSaveCreateFailureInvalidData() {
		doctor.getSpecialties().clear();

		assertThrows(RuntimeException.class, () -> doctorDAO.save(doctor));

		assertFalse(doctorDAO.existsById("D003"));
	}

	@Test
	public void testDoctorDAOSaveCreateFailureSpecialtyDoesNotExist() {
		((List<Specialty>) doctor.getSpecialties()).get(1).setId("S103");

		assertThrows(RuntimeException.class, () -> doctorDAO.save(doctor));

		assertFalse(doctorDAO.existsById("D003"));
	}

	@Test
	public void testDoctorDAOSaveUpdateSuccess() {

		doctor = doctorDAO.findById("D001").get();

		assertNotEquals("mail@mail.com", doctor.getEmail());

		doctor.setEmail("mail@mail.com");

		doctorDAO.save(doctor);

		doctor = doctorDAO.findById("D001").get();

		assertEquals("mail@mail.com", doctor.getEmail());
	}

	@Test
	void testDoctorDAODeleteSuccess() {
		assertTrue(doctorDAO.existsById("D002"));

		List<Map<String, Object>> results = jdbc
				.queryForList("SELECT * FROM t_doctor_specialty where t_doctor_specialty.doctor_id='D002';");

		assertEquals(2, results.size());

		doctor.setId("D002");

		assertDoesNotThrow(() -> doctorDAO.delete(doctor));

		assertFalse(doctorDAO.existsById("D002"));

		results = jdbc.queryForList("SELECT * FROM t_doctor_specialty where t_doctor_specialty.doctor_id='D002';");

		assertEquals(0, results.size());
	}

	@Test
	public void testDoctorDAODeleteFailureDoctorIsReferringDoctor() {
		assertTrue(doctorDAO.existsById("D001"));

		doctor.setId("D001");

		assertThrows(RuntimeException.class, () -> doctorDAO.delete(doctor));

		assertTrue(doctorDAO.existsById("D001"));
	}

	@Test
	void testDoctorDAODeleteByIdSuccess() {
		assertTrue(doctorDAO.existsById("D002"));

		List<Map<String, Object>> results = jdbc
				.queryForList("SELECT * FROM t_doctor_specialty where t_doctor_specialty.doctor_id='D002';");

		assertEquals(2, results.size());

		assertDoesNotThrow(() -> doctorDAO.deleteById("D002"));

		assertFalse(doctorDAO.existsById("D002"));

		results = jdbc.queryForList("SELECT * FROM t_doctor_specialty where t_doctor_specialty.doctor_id='D002';");

		assertEquals(0, results.size());
	}

	@Test
	public void testDoctorDAODeleteByIdFailureDoctorIsReferringDoctor() {
		assertTrue(doctorDAO.existsById("D001"));

		assertThrows(RuntimeException.class, () -> doctorDAO.deleteById("D001"));

		assertTrue(doctorDAO.existsById("D001"));
	}

	@Test
	public void testDoctorDAOfindByIdOrFirstnameOrLastnameFound2() {

		List<Doctor> doctorsList = new ArrayList<>();

		Iterable<Doctor> doctors = doctorDAO.findByIdOrFirstnameOrLastname("el");

		doctors.forEach(doctorsList::add);

		assertEquals(2, doctorsList.size());
		assertEquals("D010", doctorsList.get(0).getId());
		assertEquals("D012", doctorsList.get(1).getId());
	}

	@Test
	public void testDoctorDAOfindByIdOrFirstnameOrLastnameFound12() {

		List<Doctor> doctorsList = new ArrayList<>();

		Iterable<Doctor> doctors = doctorDAO.findByIdOrFirstnameOrLastname("D0");

		doctors.forEach(doctorsList::add);

		assertEquals(12, doctorsList.size());
	}

	@Test
	public void testDoctorDAOfindByIdOrFirstnameOrLastnameFound0() {

		List<Doctor> doctorsList = new ArrayList<>();

		Iterable<Doctor> doctors = doctorDAO.findByIdOrFirstnameOrLastname("za");

		doctors.forEach(doctorsList::add);

		assertEquals(0, doctorsList.size());
	}

}
