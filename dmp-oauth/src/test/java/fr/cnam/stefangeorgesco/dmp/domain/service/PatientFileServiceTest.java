package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.service.UserService;
import fr.cnam.stefangeorgesco.dmp.domain.dao.*;
import fr.cnam.stefangeorgesco.dmp.domain.dto.AddressDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DeleteException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PatientFileServiceTest {

	@MockBean
	private RnippService rnippService;

	@MockBean
	private UserService userService;

	@MockBean
	private PatientFileDAO patientFileDAO;

	@MockBean
	private FileDAO fileDAO;

	@MockBean
	private DoctorDAO doctorDAO;

	@MockBean
	private CorrespondenceDAO correspondenceDAO;

	@MockBean
	private PatientFileItemDAO patientFileItemDAO;

	@Autowired
	private PatientFileService patientFileService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private final ArgumentCaptor<PatientFile> patientFileCaptor = ArgumentCaptor.forClass(PatientFile.class);

	private PatientFileDTO patientFileDTO;

	private PatientFileDTO patientFileDTOResponse;

	private Doctor newDoctor;

	private PatientFile persistentPatientFile;

	private PatientFile savedPatientFile;

	private PatientFile patientFile1;

	private PatientFile patientFile2;

	@BeforeEach
	public void setup() {
		AddressDTO addressDTO = new AddressDTO();
		addressDTO.setStreet1("1 Rue Lecourbe");
		addressDTO.setZipcode("75015");
		addressDTO.setCity("Paris");
		addressDTO.setCountry("France");

		patientFileDTO = new PatientFileDTO();
		patientFileDTO.setId("P001");
		patientFileDTO.setFirstname("Patrick");
		patientFileDTO.setLastname("Dubois");
		patientFileDTO.setDateOfBirth(LocalDate.of(2000, 2, 13));
		patientFileDTO.setPhone("9876543210");
		patientFileDTO.setEmail("patrick.dubois@mail.fr");
		patientFileDTO.setAddressDTO(addressDTO);
		patientFileDTO.setReferringDoctorId("D001");

		Address address = new Address();
		address.setStreet1("street 1");
		address.setZipcode("zipcode");
		address.setCity("City");
		address.setCountry("Country");

		Doctor doctor1 = new Doctor();
		doctor1.setId("D001");

		newDoctor = new Doctor();

		persistentPatientFile = new PatientFile();
		persistentPatientFile.setId(patientFileDTO.getId());
		persistentPatientFile.setFirstname("firstname");
		persistentPatientFile.setLastname("lastname");
		persistentPatientFile.setDateOfBirth(LocalDate.of(2000, 2, 13));
		persistentPatientFile.setAddress(address);
		persistentPatientFile.setSecurityCode("securityCode");
		persistentPatientFile.setReferringDoctor(doctor1);

		patientFile1 =  new PatientFile();
		patientFile1.setId("ID_1");
		patientFile1.setFirstname("firstname_1");
		patientFile1.setLastname("lastname_1");
		patientFile1.setDateOfBirth(LocalDate.of(2000, 2, 13));
		patientFile1.setAddress(address);
		patientFile1.setSecurityCode("securityCode_1");
		patientFile1.setReferringDoctor(doctor1);

		patientFile2 = new PatientFile();
		patientFile2.setId("ID_2");
		patientFile2.setFirstname("firstname_2");
		patientFile2.setLastname("lastname_2");
		patientFile2.setDateOfBirth(LocalDate.of(1995, 8, 21));
		patientFile2.setAddress(address);
		patientFile2.setSecurityCode("securityCode_2");
		patientFile2.setReferringDoctor(doctor1);
	}

	@Test
	public void testCreatePatientFileSuccess() throws CheckException {
		doNothing().when(rnippService).checkPatientData(patientFileDTO);
		when(fileDAO.existsById(patientFileDTO.getId())).thenReturn(false);
		when(patientFileDAO.save(patientFileCaptor.capture())).thenAnswer(invocation -> invocation.getArguments()[0]);

		patientFileDTOResponse = assertDoesNotThrow(() -> patientFileService.createPatientFile(patientFileDTO));

		verify(rnippService, times(1)).checkPatientData(patientFileDTO);
		verify(fileDAO, times(1)).existsById(patientFileDTO.getId());
		verify(patientFileDAO, times(1)).save(any(PatientFile.class));

		savedPatientFile = patientFileCaptor.getValue();

		assertEquals(patientFileDTO.getId(), savedPatientFile.getId());
		assertEquals(patientFileDTO.getFirstname(), savedPatientFile.getFirstname());
		assertEquals(patientFileDTO.getLastname(), savedPatientFile.getLastname());
		assertEquals(patientFileDTO.getPhone(), savedPatientFile.getPhone());
		assertEquals(patientFileDTO.getEmail(), savedPatientFile.getEmail());
		assertEquals(patientFileDTO.getAddressDTO().getStreet1(), savedPatientFile.getAddress().getStreet1());
		assertEquals(patientFileDTO.getAddressDTO().getCountry(), savedPatientFile.getAddress().getCountry());
		assertEquals(patientFileDTO.getReferringDoctorId(), savedPatientFile.getReferringDoctor().getId());

		assertNotNull(patientFileDTOResponse.getSecurityCode());
		assertTrue(patientFileDTOResponse.getSecurityCode().length() >= 10);
		assertTrue(bCryptPasswordEncoder.matches(patientFileDTOResponse.getSecurityCode(),
				savedPatientFile.getSecurityCode()));
	}

	@Test
	public void testCreatePatientFileFailureRnippServiceThrowsException() throws CheckException {
		doThrow(new CheckException("patient data did not match")).when(rnippService).checkPatientData(patientFileDTO);

		CheckException ex = assertThrows(CheckException.class,
				() -> patientFileService.createPatientFile(patientFileDTO));

		assertEquals("patient data did not match", ex.getMessage());
	}

	@Test
	public void testCreatePatientFileFailurePatientFileAlreadyExist() throws CheckException {
		doNothing().when(rnippService).checkPatientData(patientFileDTO);
		when(fileDAO.existsById(patientFileDTO.getId())).thenReturn(true);

		DuplicateKeyException ex = assertThrows(DuplicateKeyException.class,
				() -> patientFileService.createPatientFile(patientFileDTO));

		verify(rnippService, times(1)).checkPatientData(patientFileDTO);
		verify(fileDAO, times(1)).existsById(patientFileDTO.getId());
		verify(patientFileDAO, times(0)).save(any(PatientFile.class));

		assertEquals("Un dossier avec cet identifiant existe déjà.", ex.getMessage());
	}

	@Test
	public void testUpdatePatientFileSuccess() {
		when(patientFileDAO.findById(patientFileDTO.getId())).thenReturn(Optional.of(persistentPatientFile));
		when(patientFileDAO.save(patientFileCaptor.capture())).thenAnswer(invocation -> invocation.getArguments()[0]);

		patientFileDTOResponse = assertDoesNotThrow(() -> patientFileService.updatePatientFile(patientFileDTO));

		verify(patientFileDAO, times(1)).findById(patientFileDTO.getId());
		verify(patientFileDAO, times(1)).save(any(PatientFile.class));

		savedPatientFile = patientFileCaptor.getValue();

		// unchanged - compared to captured saved object
		assertEquals(persistentPatientFile.getId(), savedPatientFile.getId());
		assertEquals(persistentPatientFile.getFirstname(), savedPatientFile.getFirstname());
		assertEquals(persistentPatientFile.getLastname(), savedPatientFile.getLastname());
		assertEquals(persistentPatientFile.getSecurityCode(), savedPatientFile.getSecurityCode());
		assertEquals(persistentPatientFile.getReferringDoctor().getId(), savedPatientFile.getReferringDoctor().getId());

		// updated - compared to captured saved object
		assertEquals(patientFileDTO.getId(), savedPatientFile.getId());
		assertEquals(patientFileDTO.getPhone(), savedPatientFile.getPhone());
		assertEquals(patientFileDTO.getEmail(), savedPatientFile.getEmail());
		assertEquals(patientFileDTO.getAddressDTO().getStreet1(), savedPatientFile.getAddress().getStreet1());
		assertEquals(patientFileDTO.getAddressDTO().getZipcode(), savedPatientFile.getAddress().getZipcode());
		assertEquals(patientFileDTO.getAddressDTO().getCity(), savedPatientFile.getAddress().getCity());
		assertEquals(patientFileDTO.getAddressDTO().getCountry(), savedPatientFile.getAddress().getCountry());

		// unchanged - compared to response DTO object (except null security code)
		assertEquals(persistentPatientFile.getId(), patientFileDTOResponse.getId());
		assertEquals(persistentPatientFile.getFirstname(), patientFileDTOResponse.getFirstname());
		assertEquals(persistentPatientFile.getLastname(), patientFileDTOResponse.getLastname());
		assertNull(patientFileDTOResponse.getSecurityCode());
		assertEquals(persistentPatientFile.getReferringDoctor().getId(), patientFileDTOResponse.getReferringDoctorId());

		// updated - compared to response DTO object
		assertEquals(patientFileDTO.getId(), patientFileDTOResponse.getId());
		assertEquals(patientFileDTO.getPhone(), patientFileDTOResponse.getPhone());
		assertEquals(patientFileDTO.getEmail(), patientFileDTOResponse.getEmail());
		assertEquals(patientFileDTO.getAddressDTO().getStreet1(), patientFileDTOResponse.getAddressDTO().getStreet1());
		assertEquals(patientFileDTO.getAddressDTO().getZipcode(), patientFileDTOResponse.getAddressDTO().getZipcode());
		assertEquals(patientFileDTO.getAddressDTO().getCity(), patientFileDTOResponse.getAddressDTO().getCity());
		assertEquals(patientFileDTO.getAddressDTO().getCountry(), patientFileDTOResponse.getAddressDTO().getCountry());
	}

	@Test
	public void testFindPatientFileSuccess() {
		when(patientFileDAO.findById("P001")).thenReturn(Optional.of(persistentPatientFile));

		patientFileDTOResponse = assertDoesNotThrow(() -> patientFileService.findPatientFile("P001"));

		verify(patientFileDAO, times(1)).findById("P001");

		assertEquals("P001", patientFileDTOResponse.getId());
		assertEquals("firstname", patientFileDTOResponse.getFirstname());
		assertEquals("lastname", patientFileDTOResponse.getLastname());
		assertNull(patientFileDTOResponse.getSecurityCode());
		assertEquals("D001", patientFileDTOResponse.getReferringDoctorId());
		assertEquals("2000-02-13", patientFileDTOResponse.getDateOfBirth().toString());
	}

	@Test
	public void testFindPatientFileFailurePatientFileDoesNotExist() {
		when(patientFileDAO.findById("P003")).thenReturn(Optional.empty());

		FinderException ex = assertThrows(FinderException.class, () -> patientFileService.findPatientFile("P003"));

		verify(patientFileDAO, times(1)).findById("P003");

		assertEquals("Dossier patient non trouvé.", ex.getMessage());
	}

	@Test
	public void testUpdateReferringDoctorSuccess() {

		patientFileDTO.setReferringDoctorId("D002");
		newDoctor.setId("D002");
		assertEquals("D001", persistentPatientFile.getReferringDoctor().getId());

		when(patientFileDAO.findById(patientFileDTO.getId())).thenReturn(Optional.of(persistentPatientFile));
		when(doctorDAO.findById(patientFileDTO.getReferringDoctorId())).thenReturn(Optional.of(newDoctor));
		when(patientFileDAO.save(patientFileCaptor.capture())).thenAnswer(invocation -> invocation.getArguments()[0]);

		patientFileDTOResponse = assertDoesNotThrow(() -> patientFileService.updateReferringDoctor(patientFileDTO));

		verify(patientFileDAO, times(1)).findById(patientFileDTO.getId());
		verify(doctorDAO, times(1)).findById(patientFileDTO.getReferringDoctorId());
		verify(patientFileDAO, times(1)).save(any(PatientFile.class));

		savedPatientFile = patientFileCaptor.getValue();

		// unchanged - compared to captured saved object
		assertEquals(persistentPatientFile.getId(), savedPatientFile.getId());
		assertEquals(persistentPatientFile.getFirstname(), savedPatientFile.getFirstname());
		assertEquals(persistentPatientFile.getLastname(), savedPatientFile.getLastname());
		assertEquals(persistentPatientFile.getSecurityCode(), savedPatientFile.getSecurityCode());
		assertEquals(persistentPatientFile.getId(), savedPatientFile.getId());
		assertEquals(persistentPatientFile.getPhone(), savedPatientFile.getPhone());
		assertEquals(persistentPatientFile.getEmail(), savedPatientFile.getEmail());
		assertEquals(persistentPatientFile.getAddress().getStreet1(), savedPatientFile.getAddress().getStreet1());
		assertEquals(persistentPatientFile.getAddress().getZipcode(), savedPatientFile.getAddress().getZipcode());
		assertEquals(persistentPatientFile.getAddress().getCity(), savedPatientFile.getAddress().getCity());
		assertEquals(persistentPatientFile.getAddress().getCountry(), savedPatientFile.getAddress().getCountry());

		// unchanged - compared to response DTO object (except null security code)
		assertEquals(persistentPatientFile.getId(), patientFileDTOResponse.getId());
		assertEquals(persistentPatientFile.getFirstname(), patientFileDTOResponse.getFirstname());
		assertEquals(persistentPatientFile.getLastname(), patientFileDTOResponse.getLastname());
		assertNull(patientFileDTOResponse.getSecurityCode());
		assertEquals(persistentPatientFile.getId(), patientFileDTOResponse.getId());
		assertEquals(persistentPatientFile.getPhone(), patientFileDTOResponse.getPhone());
		assertEquals(persistentPatientFile.getEmail(), patientFileDTOResponse.getEmail());
		assertEquals(persistentPatientFile.getAddress().getStreet1(),
				patientFileDTOResponse.getAddressDTO().getStreet1());
		assertEquals(persistentPatientFile.getAddress().getZipcode(),
				patientFileDTOResponse.getAddressDTO().getZipcode());
		assertEquals(persistentPatientFile.getAddress().getCity(), patientFileDTOResponse.getAddressDTO().getCity());
		assertEquals(persistentPatientFile.getAddress().getCountry(),
				patientFileDTOResponse.getAddressDTO().getCountry());

		// updated - compared to captured saved object
		assertEquals(patientFileDTO.getReferringDoctorId(), savedPatientFile.getReferringDoctor().getId());
		assertNotEquals("D001", savedPatientFile.getReferringDoctor().getId());

		// updated - compared to response DTO object
		assertEquals(patientFileDTO.getReferringDoctorId(), patientFileDTOResponse.getReferringDoctorId());
		assertNotEquals("D001", patientFileDTOResponse.getReferringDoctorId());
	}

	@Test
	public void testUpdateReferringDoctorFailurePatientFileDoesNotExist() {
		patientFileDTO.setReferringDoctorId("D002");

		when(patientFileDAO.findById(patientFileDTO.getId())).thenReturn(Optional.empty());

		FinderException ex = assertThrows(FinderException.class,
				() -> patientFileService.updateReferringDoctor(patientFileDTO));

		verify(patientFileDAO, times(1)).findById(patientFileDTO.getId());
		verify(doctorDAO, times(0)).findById(patientFileDTO.getReferringDoctorId());
		verify(patientFileDAO, times(0)).save(any(PatientFile.class));

		assertEquals("Dossier patient non trouvé.", ex.getMessage());
	}

	@Test
	public void testUpdateReferringDoctorFailureNewDoctorDoesNotExist() {
		patientFileDTO.setReferringDoctorId("D002");

		when(patientFileDAO.findById(patientFileDTO.getId())).thenReturn(Optional.ofNullable(persistentPatientFile));
		when(doctorDAO.findById(patientFileDTO.getReferringDoctorId())).thenReturn(Optional.empty());

		FinderException ex = assertThrows(FinderException.class,
				() -> patientFileService.updateReferringDoctor(patientFileDTO));

		verify(patientFileDAO, times(1)).findById(patientFileDTO.getId());
		verify(doctorDAO, times(1)).findById(patientFileDTO.getReferringDoctorId());
		verify(patientFileDAO, times(0)).save(any(PatientFile.class));

		assertEquals("Dossier de médecin non trouvé.", ex.getMessage());
	}

	@Test
	public void testFindPatientFileByIdOrFirstnameOrLastnameFound2() {
		when(patientFileDAO.findByIdOrFirstnameOrLastname("la")).thenReturn(List.of(patientFile1, patientFile2));

		List<PatientFileDTO> patientFilesDTO = patientFileService.findPatientFilesByIdOrFirstnameOrLastname("la");

		verify(patientFileDAO, times(1)).findByIdOrFirstnameOrLastname("la");

		assertEquals(2, patientFilesDTO.size());

		PatientFileDTO patientFileDTO1 = patientFilesDTO.get(0);
		PatientFileDTO patientFileDTO2 = patientFilesDTO.get(1);

		assertEquals("ID_1", patientFileDTO1.getId());
		assertEquals("2000-02-13", patientFileDTO1.getDateOfBirth().toString());
		assertEquals("zipcode", patientFileDTO1.getAddressDTO().getZipcode());
		assertEquals("D001", patientFileDTO1.getReferringDoctorId());
		assertEquals("ID_2", patientFileDTO2.getId());
		assertEquals("1995-08-21", patientFileDTO2.getDateOfBirth().toString());
		assertEquals("zipcode", patientFileDTO2.getAddressDTO().getZipcode());
		assertEquals("D001", patientFileDTO2.getReferringDoctorId());
	}

	@Test
	public void testFindPatientFileByIdOrFirstnameOrLastnameFound0() {
		when(patientFileDAO.findByIdOrFirstnameOrLastname("za")).thenReturn(List.of());

		List<PatientFileDTO> patientFilesDTO = patientFileService.findPatientFilesByIdOrFirstnameOrLastname("za");

		verify(patientFileDAO, times(1)).findByIdOrFirstnameOrLastname("za");

		assertEquals(0, patientFilesDTO.size());
	}

	@Test
	public void testFindPatientFileByIdOrFirstnameOrLastnameFound0SearchStringIsBlank() {
		when(patientFileDAO.findByIdOrFirstnameOrLastname("")).thenReturn(List.of());

		List<PatientFileDTO> patientFilesDTO = patientFileService.findPatientFilesByIdOrFirstnameOrLastname("");

		verify(patientFileDAO, times(0)).findByIdOrFirstnameOrLastname("");

		assertEquals(0, patientFilesDTO.size());
	}

	@Test
	public void testDeletePatientFileSuccessNoUser() throws DeleteException {
		when(correspondenceDAO.deleteAllByPatientFileId("P001")).thenReturn(3);
		when(patientFileItemDAO.deleteAllByPatientFileId("P001")).thenReturn(9);
		doNothing().when(patientFileDAO).deleteById("P001");
		doThrow(new DeleteException("")).when(userService).deleteUser("P001");

		assertDoesNotThrow(() -> patientFileService.deletePatientFile("P001"));

		verify(correspondenceDAO, times(1)).deleteAllByPatientFileId("P001");
		verify(patientFileItemDAO, times(1)).deleteAllByPatientFileId("P001");
		verify(patientFileDAO, times(1)).deleteById("P001");
		verify(userService, times(1)).deleteUser("P001");
	}

	@Test
	public void testDeletePatientFileSuccessUserPresent() throws DeleteException {
		when(correspondenceDAO.deleteAllByPatientFileId("P001")).thenReturn(3);
		when(patientFileItemDAO.deleteAllByPatientFileId("P001")).thenReturn(9);
		doNothing().when(patientFileDAO).deleteById("P001");
		doNothing().when(userService).deleteUser("P001");

		assertDoesNotThrow(() -> patientFileService.deletePatientFile("P001"));

		verify(correspondenceDAO, times(1)).deleteAllByPatientFileId("P001");
		verify(patientFileItemDAO, times(1)).deleteAllByPatientFileId("P001");
		verify(patientFileDAO, times(1)).deleteById("P001");
		verify(userService, times(1)).deleteUser("P001");
	}

	@Test
	public void testDeletePatientFileFailurePatientFileDoesNotExist() throws DeleteException {
		when(correspondenceDAO.deleteAllByPatientFileId("P001")).thenReturn(0);
		when(patientFileItemDAO.deleteAllByPatientFileId("P001")).thenReturn(0);
		doThrow(new RuntimeException("")).when(patientFileDAO).deleteById("P001");
		doThrow(new DeleteException("")).when(userService).deleteUser("P001");

		DeleteException ex = assertThrows(DeleteException.class, () -> patientFileService.deletePatientFile("P001"));

		verify(correspondenceDAO, times(1)).deleteAllByPatientFileId("P001");
		verify(patientFileItemDAO, times(1)).deleteAllByPatientFileId("P001");
		verify(patientFileDAO, times(1)).deleteById("P001");
		verify(userService, times(0)).deleteUser("P001");
		assertEquals("Le dossier patient n'a pas pu être supprimé.", ex.getMessage());
	}
}
