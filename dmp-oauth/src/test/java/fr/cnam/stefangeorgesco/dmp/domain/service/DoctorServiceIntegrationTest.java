package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.service.IAMService;
import fr.cnam.stefangeorgesco.dmp.domain.dao.DoctorDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.AddressDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DeleteException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
@SqlGroup({ @Sql(scripts = "/sql/create-specialties.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-files.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-files.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-specialties.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class DoctorServiceIntegrationTest {

	@MockBean
	private IAMService IAMService;
	
	@Autowired
	private DoctorDAO doctorDAO;

	@Autowired
	private DoctorService doctorService;

	private DoctorDTO doctorDTO;

	@BeforeEach
	public void setup() {
		SpecialtyDTO specialtyDTO = new SpecialtyDTO();
		specialtyDTO.setId("S001");
		specialtyDTO.setDescription("A specialty");

		List<SpecialtyDTO> specialtyDTOs = new ArrayList<>();
		specialtyDTOs.add(specialtyDTO);

		AddressDTO addressDTO = new AddressDTO();
		addressDTO.setStreet1("1 Rue Lecourbe");
		addressDTO.setZipcode("75015");
		addressDTO.setCity("Paris");
		addressDTO.setCountry("France");

		doctorDTO = new DoctorDTO();
		doctorDTO.setId("D003");
		doctorDTO.setFirstname("Pierre");
		doctorDTO.setLastname("Martin");
		doctorDTO.setPhone("012345679");
		doctorDTO.setEmail("pierre.martin@docteurs.fr");
		doctorDTO.setSpecialtiesDTO(specialtyDTOs);
		doctorDTO.setAddressDTO(addressDTO);
	}

	@AfterEach
	public void tearDown() {
		if (doctorDAO.existsById("D003")) {
			doctorDAO.deleteById("D003");
		}
	}

	@Test
	public void testCreateDoctorSuccess() {

		assertFalse(doctorDAO.existsById("D003"));

		assertDoesNotThrow(() -> doctorService.createDoctor(doctorDTO));

		assertTrue(doctorDAO.existsById("D003"));
	}

	@Test
	public void testCreateDoctorFailureDoctorAlreadyExists() {
		Specialty specialty = new Specialty();
		specialty.setId("S001");
		specialty.setDescription("A specialty");

		List<Specialty> specialties = new ArrayList<>();
		specialties.add(specialty);

		Address address = new Address();
		address.setStreet1("1 Rue Lecourbe");
		address.setZipcode("75015");
		address.setCity("Paris");
		address.setCountry("France");

		Doctor doctor = new Doctor();
		doctor.setId("D003");
		doctor.setFirstname("Pierre");
		doctor.setLastname("Martin");
		doctor.setPhone("012345679");
		doctor.setEmail("pierre.martin@docteurs.fr");
		doctor.setSpecialties(specialties);
		doctor.setAddress(address);
		doctor.setSecurityCode("code");

		doctorDAO.save(doctor);

		DuplicateKeyException ex = assertThrows(DuplicateKeyException.class,
				() -> doctorService.createDoctor(doctorDTO));

		assertEquals("Un dossier avec cet identifiant existe déjà.", ex.getMessage());
	}

	@Test
	public void testUpdateDoctorSuccess() {
		doctorDTO.setId("D001"); // file exists
		when(IAMService.updateUser(any(UserDTO.class))).thenReturn(HttpStatus.ACCEPTED);
		assertTrue(doctorDAO.existsById("D001"));

		DoctorDTO response = assertDoesNotThrow(() -> doctorService.updateDoctor(doctorDTO));

		verify(IAMService, times(1)).updateUser(any(UserDTO.class));
		Doctor savedDoctor = doctorDAO.findById("D001").orElseThrow();

		// no change in saved object
		assertEquals("D001", savedDoctor.getId());
		assertEquals("John", savedDoctor.getFirstname());
		assertEquals("Smith", savedDoctor.getLastname());
		assertEquals("45", savedDoctor.getSecurityCode());
		assertEquals(2, savedDoctor.getSpecialties().size());
		Iterator<Specialty> itSavedSp = savedDoctor.getSpecialties().iterator();
		Specialty savedSp = itSavedSp.next();
		assertEquals("S001", savedSp.getId());
		assertEquals("allergologie", savedSp.getDescription());
		savedSp = itSavedSp.next();
		assertEquals("S024", savedSp.getId());
		assertEquals("médecine générale", savedSp.getDescription());

		assertEquals(doctorDTO.getId(), savedDoctor.getId());
		// changes in saved object
		assertEquals(doctorDTO.getPhone(), savedDoctor.getPhone());
		assertEquals(doctorDTO.getEmail(), savedDoctor.getEmail());
		assertEquals(doctorDTO.getAddressDTO().getStreet1(), savedDoctor.getAddress().getStreet1());
		assertEquals(doctorDTO.getAddressDTO().getZipcode(), savedDoctor.getAddress().getZipcode());
		assertEquals(doctorDTO.getAddressDTO().getCity(), savedDoctor.getAddress().getCity());
		assertEquals(doctorDTO.getAddressDTO().getCountry(), savedDoctor.getAddress().getCountry());

		// no change in returned object (except null securityCode)
		assertEquals("D001", response.getId());
		assertEquals("John", response.getFirstname());
		assertEquals("Smith", response.getLastname());
		assertNull(response.getSecurityCode());
		assertEquals(2, response.getSpecialtiesDTO().size());
		Iterator<SpecialtyDTO> itsDTO = response.getSpecialtiesDTO().iterator();
		SpecialtyDTO spDTO = itsDTO.next();
		assertEquals("S001", spDTO.getId());
		assertEquals("allergologie", spDTO.getDescription());
		spDTO = itsDTO.next();
		assertEquals("S024", spDTO.getId());
		assertEquals("médecine générale", spDTO.getDescription());

		assertEquals(doctorDTO.getId(), response.getId());
		// changes in returned object
		assertEquals(doctorDTO.getPhone(), response.getPhone());
		assertEquals(doctorDTO.getEmail(), response.getEmail());
		assertEquals(doctorDTO.getAddressDTO().getStreet1(), response.getAddressDTO().getStreet1());
		assertEquals(doctorDTO.getAddressDTO().getZipcode(), response.getAddressDTO().getZipcode());
		assertEquals(doctorDTO.getAddressDTO().getCity(), response.getAddressDTO().getCity());
		assertEquals(doctorDTO.getAddressDTO().getCountry(), response.getAddressDTO().getCountry());
	}

	@Test
	public void testFindDoctorSuccess() {
		DoctorDTO doctorDTO = assertDoesNotThrow(() -> doctorService.findDoctor("D002"));

		assertEquals("D002", doctorDTO.getId());
		assertEquals("15 rue de Vaugirard", doctorDTO.getAddressDTO().getStreet1());
		assertEquals(2, doctorDTO.getSpecialtiesDTO().size());
		assertEquals("S012", ((List<SpecialtyDTO>) doctorDTO.getSpecialtiesDTO()).get(0).getId());
		assertEquals("chirurgie vasculaire",
				((List<SpecialtyDTO>) doctorDTO.getSpecialtiesDTO()).get(0).getDescription());
		assertEquals("S013", ((List<SpecialtyDTO>) doctorDTO.getSpecialtiesDTO()).get(1).getId());
		assertEquals("neurochirurgie", ((List<SpecialtyDTO>) doctorDTO.getSpecialtiesDTO()).get(1).getDescription());
		assertNull(doctorDTO.getSecurityCode());
	}

	@Test
	public void testFindDoctorFailureDoctorDoesNotExist() {

		FinderException ex = assertThrows(FinderException.class, () -> doctorService.findDoctor("D003"));

		assertEquals("Le dossier de médecin n'a pas été trouvé.", ex.getMessage());
	}

	@Test
	public void testDeleteDoctorSuccessNoUser() {

		when(IAMService.userExistsById("D002")).thenReturn(false);
		assertTrue(doctorDAO.existsById("D002"));

		assertDoesNotThrow(() -> doctorService.deleteDoctor("D002"));

		verify(IAMService, times(1)).userExistsById("D002");
		assertFalse(doctorDAO.existsById("D002"));
	}

	@Test
	public void testDeleteDoctorSuccessUserPresent() {
		
		when(IAMService.userExistsById("D002")).thenReturn(true);
		when(IAMService.deleteUser("D002")).thenReturn(HttpStatus.NO_CONTENT);
		assertTrue(doctorDAO.existsById("D002"));

		assertDoesNotThrow(() -> doctorService.deleteDoctor("D002"));

		verify(IAMService, times(1)).userExistsById("D002");
		verify(IAMService, times(1)).deleteUser("D002");
		assertFalse(doctorDAO.existsById("D002"));
	}

	@Test
	public void testDeleteDoctorFailureDoctorDoesNotExist() {

		assertFalse(doctorDAO.existsById("D003"));

		DeleteException ex = assertThrows(DeleteException.class, () -> doctorService.deleteDoctor("D003"));

		assertEquals("Le dossier de médecin n'a pas pu être supprimé.", ex.getMessage());
	}

	@Test
	public void testDeleteDoctorFailureDoctorIsReferringDoctor() {

		assertTrue(doctorDAO.existsById("D001"));

		DeleteException ex = assertThrows(DeleteException.class, () -> doctorService.deleteDoctor("D001"));

		assertEquals("Le dossier de médecin n'a pas pu être supprimé.", ex.getMessage());

		assertTrue(doctorDAO.existsById("D001"));
	}

	@Test
	public void testFindDoctorsByIdOrFirstnameOrLastnameFound2() {

		List<DoctorDTO> doctors = doctorService.findDoctorsByIdOrFirstnameOrLastname("el");

		assertEquals(2, doctors.size());
		assertEquals("D010", doctors.get(0).getId());
		assertEquals("D012", doctors.get(1).getId());
	}

	@Test
	public void testFindDoctorsByIdOrFirstnameOrLastnameFound12() {

		List<DoctorDTO> doctors = doctorService.findDoctorsByIdOrFirstnameOrLastname("D0");

		assertEquals(12, doctors.size());
	}

	@Test
	public void testFindDoctorsByIdOrFirstnameOrLastnameFound0() {

		List<DoctorDTO> doctors = doctorService.findDoctorsByIdOrFirstnameOrLastname("za");

		assertEquals(0, doctors.size());
	}

	@Test
	public void testFindDoctorsByIdOrFirstnameOrLastnameFound0SearchStringIsBlank() {

		List<DoctorDTO> doctors = doctorService.findDoctorsByIdOrFirstnameOrLastname("");

		assertEquals(0, doctors.size());
	}
}
