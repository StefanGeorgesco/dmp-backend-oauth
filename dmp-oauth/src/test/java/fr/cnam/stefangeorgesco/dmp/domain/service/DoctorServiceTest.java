package fr.cnam.stefangeorgesco.dmp.domain.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.ArgumentCaptor;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.service.UserService;
import fr.cnam.stefangeorgesco.dmp.domain.dao.DoctorDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.FileDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.SpecialtyDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.AddressDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DeleteException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class DoctorServiceTest {

	@MockBean
	private FileDAO fileDAO;

	@MockBean
	private DoctorDAO doctorDAO;

	@MockBean
	private SpecialtyDAO specialtyDAO;

	@MockBean
	private UserService userService;

	@Autowired
	private DoctorService doctorService;

	@Autowired
	private SpecialtyDTO specialtyDTO1;

	@Autowired
	private SpecialtyDTO specialtyDTO2;

	@Autowired
	private AddressDTO addressDTO;

	@Autowired
	private DoctorDTO doctorDTO;

	@Autowired
	private DoctorDTO doctorDTO1;

	@Autowired
	private DoctorDTO doctorDTO2;

	@Autowired
	private DoctorDTO response;

	@Autowired
	private Specialty specialty1;

	@Autowired
	private Specialty specialty2;

	@Autowired
	private Doctor savedDoctor;

	@Autowired
	private Doctor persistentDoctor;

	@Autowired
	private Address address1;

	@Autowired
	private Address address2;

	@Autowired
	private Doctor foundDoctor1;

	@Autowired
	private Doctor foundDoctor2;

	private Set<SpecialtyDTO> specialtyDTOs;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private ArgumentCaptor<Doctor> doctorCaptor = ArgumentCaptor.forClass(Doctor.class);

	@BeforeEach
	public void setupBeforeEach() {
		specialtyDTO1.setId("S001");
		specialtyDTO2.setId("S002");
		specialtyDTOs = new HashSet<SpecialtyDTO>();
		specialtyDTOs.add(specialtyDTO1);
		specialtyDTOs.add(specialtyDTO2);
		addressDTO.setStreet1("1 Rue Lecourbe");
		addressDTO.setZipcode("75015");
		addressDTO.setCity("Paris");
		addressDTO.setCountry("France");
		doctorDTO.setId("D001");
		doctorDTO.setFirstname("Pierre");
		doctorDTO.setLastname("Martin");
		doctorDTO.setPhone("012345679");
		doctorDTO.setEmail("pierre.martin@docteurs.fr");
		doctorDTO.setSpecialtiesDTO(specialtyDTOs);
		doctorDTO.setAddressDTO(addressDTO);

		specialty1.setId("S001");
		specialty1.setDescription("First specialty");
		specialty2.setId("S002");
		specialty2.setDescription("Second specialty");

		persistentDoctor.setId(doctorDTO.getId());
		persistentDoctor.setFirstname("firstname");
		persistentDoctor.setLastname("lastname");
		persistentDoctor.setSecurityCode("securityCode");
		persistentDoctor.setSpecialties(List.of(specialty1, specialty2));

		address1.setStreet1("street1_1");
		address1.setZipcode("zipcode_1");
		address1.setCity("city_1");
		address1.setCountry("country_1");

		address2.setStreet1("street1_2");
		address2.setZipcode("zipcode_2");
		address2.setCity("city_2");
		address2.setCountry("country_2");

		foundDoctor1.setId("ID_1");
		foundDoctor1.setFirstname("firstname_1");
		foundDoctor1.setLastname("lastname_1");
		foundDoctor1.setSecurityCode("securityCode_1");
		foundDoctor1.setSpecialties(List.of(specialty1, specialty2));
		foundDoctor1.setAddress(address1);

		foundDoctor2.setId("ID_2");
		foundDoctor2.setFirstname("firstname_2");
		foundDoctor2.setLastname("lastname_2");
		foundDoctor2.setSecurityCode("securityCode_2");
		foundDoctor2.setSpecialties(List.of(specialty1, specialty2));
		foundDoctor2.setAddress(address2);
	}

	@Test
	public void testCreateDoctorSuccess() {
		when(fileDAO.existsById(doctorDTO.getId())).thenReturn(false);
		when(specialtyDAO.findById(specialtyDTO1.getId())).thenReturn(Optional.of(specialty1));
		when(specialtyDAO.findById(specialtyDTO2.getId())).thenReturn(Optional.of(specialty2));
		when(doctorDAO.save(doctorCaptor.capture())).thenAnswer(invocation -> invocation.getArguments()[0]);

		assertDoesNotThrow(() -> doctorService.createDoctor(doctorDTO));

		verify(fileDAO, times(1)).existsById(doctorDTO.getId());
		verify(specialtyDAO, times(2)).findById(any(String.class));
		verify(doctorDAO, times(1)).save(any(Doctor.class));

		savedDoctor = doctorCaptor.getValue();

		assertEquals(doctorDTO.getId(), savedDoctor.getId());
		assertEquals(doctorDTO.getFirstname(), savedDoctor.getFirstname());
		assertEquals(doctorDTO.getPhone(), savedDoctor.getPhone());
		assertEquals(doctorDTO.getEmail(), savedDoctor.getEmail());
		assertEquals(doctorDTO.getSpecialtiesDTO().size(), savedDoctor.getSpecialties().size());
		assertEquals(doctorDTO.getSpecialtiesDTO().iterator().next().getDescription(),
				savedDoctor.getSpecialties().iterator().next().getDescription());
		assertEquals(doctorDTO.getAddressDTO().getZipcode(), savedDoctor.getAddress().getZipcode());
		assertTrue(bCryptPasswordEncoder.matches(doctorDTO.getSecurityCode(), savedDoctor.getSecurityCode()));

		assertNotNull(doctorDTO.getSecurityCode());
		assertTrue(doctorDTO.getSecurityCode().length() >= 10);
		assertEquals(2, doctorDTO.getSpecialtiesDTO().size());
		Iterator<SpecialtyDTO> it = doctorDTO.getSpecialtiesDTO().iterator();
		SpecialtyDTO specialtyDTO = it.next();
		assertTrue("S001".equals(specialtyDTO.getId()) && "First specialty".equals(specialtyDTO.getDescription())
				|| "S002".equals(specialtyDTO.getId()) && "Second specialty".equals(specialtyDTO.getDescription()));
		specialtyDTO = it.next();
		assertTrue("S001".equals(specialtyDTO.getId()) && "First specialty".equals(specialtyDTO.getDescription())
				|| "S002".equals(specialtyDTO.getId()) && "Second specialty".equals(specialtyDTO.getDescription()));
	}

	@Test
	public void testCreateDoctorFailureDoctorAlreadyExists() {
		when(fileDAO.existsById(doctorDTO.getId())).thenReturn(true);

		DuplicateKeyException ex = assertThrows(DuplicateKeyException.class,
				() -> doctorService.createDoctor(doctorDTO));

		verify(fileDAO, times(1)).existsById(doctorDTO.getId());
		verify(doctorDAO, times(0)).save(any(Doctor.class));

		assertEquals("Un dossier avec cet identifiant existe déjà.", ex.getMessage());
	}

	@Test
	public void testUpdateDoctorSuccess() {
		when(doctorDAO.findById(doctorDTO.getId())).thenReturn(Optional.of(persistentDoctor));
		when(doctorDAO.save(doctorCaptor.capture())).thenAnswer(invocation -> invocation.getArguments()[0]);

		response = assertDoesNotThrow(() -> doctorService.updateDoctor(doctorDTO));

		verify(doctorDAO, times(1)).findById(doctorDTO.getId());
		verify(doctorDAO, times(1)).save(any(Doctor.class));

		savedDoctor = doctorCaptor.getValue();

		// unchanged - compared to captured saved object
		assertEquals(persistentDoctor.getId(), savedDoctor.getId());
		assertEquals(persistentDoctor.getFirstname(), savedDoctor.getFirstname());
		assertEquals(persistentDoctor.getLastname(), savedDoctor.getLastname());
		assertEquals(persistentDoctor.getSecurityCode(), savedDoctor.getSecurityCode());
		assertEquals(persistentDoctor.getSpecialties().size(), savedDoctor.getSpecialties().size());
		Iterator<Specialty> itPersistentSp = persistentDoctor.getSpecialties().iterator();
		Iterator<Specialty> itSavedSp = savedDoctor.getSpecialties().iterator();
		Specialty persistentSp = itPersistentSp.next();
		Specialty savedSp = itSavedSp.next();
		assertEquals(persistentSp.getId(), savedSp.getId());
		assertEquals(persistentSp.getDescription(), savedSp.getDescription());
		persistentSp = itPersistentSp.next();
		savedSp = itSavedSp.next();
		assertEquals(persistentSp.getId(), savedSp.getId());
		assertEquals(persistentSp.getDescription(), savedSp.getDescription());

		// updated - compared to captured saved object
		assertEquals(doctorDTO.getId(), savedDoctor.getId());
		assertEquals(doctorDTO.getPhone(), savedDoctor.getPhone());
		assertEquals(doctorDTO.getEmail(), savedDoctor.getEmail());
		assertEquals(doctorDTO.getAddressDTO().getStreet1(), savedDoctor.getAddress().getStreet1());
		assertEquals(doctorDTO.getAddressDTO().getZipcode(), savedDoctor.getAddress().getZipcode());
		assertEquals(doctorDTO.getAddressDTO().getCity(), savedDoctor.getAddress().getCity());
		assertEquals(doctorDTO.getAddressDTO().getCountry(), savedDoctor.getAddress().getCountry());

		// unchanged - compared to response DTO object (except null security code)
		assertEquals(persistentDoctor.getId(), response.getId());
		assertEquals(persistentDoctor.getFirstname(), response.getFirstname());
		assertEquals(persistentDoctor.getLastname(), response.getLastname());
		assertEquals(null, response.getSecurityCode());
		assertEquals(persistentDoctor.getSpecialties().size(), response.getSpecialtiesDTO().size());
		itPersistentSp = persistentDoctor.getSpecialties().iterator();
		Iterator<SpecialtyDTO> itSpDTO = response.getSpecialtiesDTO().iterator();
		persistentSp = itPersistentSp.next();
		SpecialtyDTO spDTO = itSpDTO.next();
		assertEquals(persistentSp.getId(), spDTO.getId());
		assertEquals(persistentSp.getDescription(), spDTO.getDescription());
		persistentSp = itPersistentSp.next();
		spDTO = itSpDTO.next();
		assertEquals(persistentSp.getId(), spDTO.getId());
		assertEquals(persistentSp.getDescription(), spDTO.getDescription());

		// updated - compared to response DTO object
		assertEquals(doctorDTO.getId(), response.getId());
		assertEquals(doctorDTO.getPhone(), response.getPhone());
		assertEquals(doctorDTO.getEmail(), response.getEmail());
		assertEquals(doctorDTO.getAddressDTO().getStreet1(), response.getAddressDTO().getStreet1());
		assertEquals(doctorDTO.getAddressDTO().getZipcode(), response.getAddressDTO().getZipcode());
		assertEquals(doctorDTO.getAddressDTO().getCity(), response.getAddressDTO().getCity());
		assertEquals(doctorDTO.getAddressDTO().getCountry(), response.getAddressDTO().getCountry());
	}

	@Test
	public void testFindDoctorSuccess() {
		when(doctorDAO.findById("D001")).thenReturn(Optional.of(persistentDoctor));

		response = assertDoesNotThrow(() -> doctorService.findDoctor("D001"));

		verify(doctorDAO, times(1)).findById("D001");

		assertEquals("D001", response.getId());
		assertEquals("firstname", response.getFirstname());
		assertEquals("lastname", response.getLastname());
		assertEquals(null, response.getSecurityCode());
		assertEquals(2, response.getSpecialtiesDTO().size());
		Iterator<SpecialtyDTO> itSpDTO = response.getSpecialtiesDTO().iterator();
		SpecialtyDTO spDTO = itSpDTO.next();
		assertEquals("S001", spDTO.getId());
		assertEquals("First specialty", spDTO.getDescription());
		spDTO = itSpDTO.next();
		assertEquals("S002", spDTO.getId());
		assertEquals("Second specialty", spDTO.getDescription());
	}

	@Test
	public void testFindDoctorFailureDoctorDoesNotExist() throws FinderException {
		when(doctorDAO.findById("D003")).thenReturn(Optional.ofNullable(null));

		FinderException ex = assertThrows(FinderException.class, () -> doctorService.findDoctor("D001"));

		verify(doctorDAO, times(1)).findById("D001");

		assertEquals("Le dossier de médecin n'a pas été trouvé.", ex.getMessage());
	}

	@Test
	public void testDeleteDoctorSuccessNoUser() throws DeleteException {
		doNothing().when(doctorDAO).deleteById("D002");
		doThrow(new DeleteException("")).when(userService).deleteUser("D002");

		assertDoesNotThrow(() -> doctorService.deleteDoctor("D002"));

		verify(doctorDAO, times(1)).deleteById("D002");
		verify(userService, times(1)).deleteUser("D002");
	}

	@Test
	public void testDeleteDoctorSuccessUserPresent() throws DeleteException {
		doNothing().when(doctorDAO).deleteById("D001");
		doNothing().when(userService).deleteUser("D001");

		assertDoesNotThrow(() -> doctorService.deleteDoctor("D001"));

		verify(doctorDAO, times(1)).deleteById("D001");
		verify(userService, times(1)).deleteUser("D001");
	}

	@Test
	public void testDeleteDoctorFailureDoctorDoesNotExist() throws DeleteException {
		doThrow(new RuntimeException("")).when(doctorDAO).deleteById("D003");
		doThrow(new DeleteException("")).when(userService).deleteUser("D003");

		DeleteException ex = assertThrows(DeleteException.class, () -> doctorService.deleteDoctor("D003"));

		verify(doctorDAO, times(1)).deleteById("D003");
		verify(userService, times(0)).deleteUser("D003");
		assertEquals("Le dossier de médecin n'a pas pu être supprimé.", ex.getMessage());
	}

	@Test
	public void testFindDoctorsByIdOrFirstnameOrLastnameFound2() {
		when(doctorDAO.findByIdOrFirstnameOrLastname("la")).thenReturn(List.of(foundDoctor1, foundDoctor2));

		List<DoctorDTO> doctorsDTO = doctorService.findDoctorsByIdOrFirstnameOrLastname("la");

		verify(doctorDAO, times(1)).findByIdOrFirstnameOrLastname("la");

		assertEquals(2, doctorsDTO.size());

		doctorDTO1 = doctorsDTO.get(0);
		doctorDTO2 = doctorsDTO.get(1);

		assertEquals("ID_1", doctorDTO1.getId());
		assertEquals("street1_1", doctorDTO1.getAddressDTO().getStreet1());
		assertEquals("S001", doctorDTO1.getSpecialtiesDTO().iterator().next().getId());
		assertEquals("ID_2", doctorDTO2.getId());
		assertEquals("street1_2", doctorDTO2.getAddressDTO().getStreet1());

		Iterator<SpecialtyDTO> itSpDTO = doctorDTO2.getSpecialtiesDTO().iterator();

		assertEquals("S001", itSpDTO.next().getId());
		assertEquals("S002", itSpDTO.next().getId());
	}

	@Test
	public void testFindDoctorsByIdOrFirstnameOrLastnameFound0() {
		when(doctorDAO.findByIdOrFirstnameOrLastname("la")).thenReturn(List.of());

		List<DoctorDTO> doctors = doctorService.findDoctorsByIdOrFirstnameOrLastname("la");

		verify(doctorDAO, times(1)).findByIdOrFirstnameOrLastname("la");

		assertEquals(0, doctors.size());
	}

	@Test
	public void testFindDoctorsByIdOrFirstnameOrLastnameFound0SearchStringIsBlank() {
		when(doctorDAO.findByIdOrFirstnameOrLastname("")).thenReturn(List.of(foundDoctor1, foundDoctor2));

		List<DoctorDTO> doctors = doctorService.findDoctorsByIdOrFirstnameOrLastname("");

		verify(doctorDAO, times(0)).findByIdOrFirstnameOrLastname("");

		assertEquals(0, doctors.size());
	}

	@Test
	public void testFindSpecialtySuccess() {

		when(specialtyDAO.findById(specialty1.getId())).thenReturn(Optional.of(specialty1));

		specialtyDTO1 = assertDoesNotThrow(() -> doctorService.findSpecialty(specialty1.getId()));

		verify(specialtyDAO, times(1)).findById(specialty1.getId());

		assertEquals(specialty1.getId(), specialtyDTO1.getId());
		assertEquals(specialty1.getDescription(), specialtyDTO1.getDescription());
	}

	@Test
	void testFindSpecialtyFailureSpecialtyDoesNotExist() {

		when(specialtyDAO.findById(specialty1.getId())).thenReturn(Optional.ofNullable(null));

		FinderException ex = assertThrows(FinderException.class, () -> doctorService.findSpecialty(specialty1.getId()));

		assertEquals("Spécialité non trouvée.", ex.getMessage());
	}

	@Test
	void testFindSpecialtiesByIdOrDescriptionFound2() {

		when(specialtyDAO.findByIdOrDescription("chirur")).thenReturn(List.of(specialty1, specialty2));

		List<SpecialtyDTO> specialtiesDTO = doctorService.findSpecialtiesByIdOrDescription("chirur");

		verify(specialtyDAO, times(1)).findByIdOrDescription("chirur");

		assertEquals(2, specialtiesDTO.size());
		assertEquals("S001", specialtiesDTO.get(0).getId());
		assertEquals("First specialty", specialtiesDTO.get(0).getDescription());
	}

	@Test
	void testFindSpecialtiesFound2() {

		when(specialtyDAO.findAll()).thenReturn(List.of(specialty1, specialty2));

		List<SpecialtyDTO> specialtiesDTO = doctorService.findAllSpecialties();

		verify(specialtyDAO, times(1)).findAll();

		assertEquals(2, specialtiesDTO.size());
		assertEquals("S001", specialtiesDTO.get(0).getId());
		assertEquals("First specialty", specialtiesDTO.get(0).getDescription());
	}

}
