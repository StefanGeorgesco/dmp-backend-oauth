package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.service.IAMService;
import fr.cnam.stefangeorgesco.dmp.domain.dao.PatientFileDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.AddressDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondenceDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileItemDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DeleteException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
@SqlGroup({ @Sql(scripts = "/sql/create-specialties.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-files.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-correspondences.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-diseases.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-medical-acts.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-patient-file-items.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-patient-file-items.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-diseases.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-medical-acts.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-correspondences.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-files.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-specialties.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class PatientFileServiceIntegrationTest {

	@MockBean
	private IAMService IAMService;
	
	@MockBean
	private RnippService rnippService;

	@Autowired
	private PatientFileDAO patientFileDAO;

	@Autowired
	private PatientFileService patientFileService;

	@Autowired
	private CorrespondenceService correspondenceService;

	@Autowired
	private PatientFileItemService patientFileItemService;

	private PatientFileDTO patientFileDTO;

	private PatientFileDTO patientFileDTOResponse;

	private PatientFile patientFile;

	private PatientFile savedPatientFile;

	private String id;

	@BeforeEach
	public void setup() {
		AddressDTO addressDTO = new AddressDTO();
		addressDTO.setStreet1("1 Rue Lecourbe");
		addressDTO.setZipcode("75015");
		addressDTO.setCity("Paris Cedex 15");
		addressDTO.setCountry("France-");

		patientFileDTO = new PatientFileDTO();
		patientFileDTO.setId("P002");
		patientFileDTO.setFirstname("Patrick");
		patientFileDTO.setLastname("Dubois");
		patientFileDTO.setDateOfBirth(LocalDate.of(2000, 2, 13));
		patientFileDTO.setPhone("9876543210");
		patientFileDTO.setEmail("patrick.dubois@mail.fr");
		patientFileDTO.setAddressDTO(addressDTO);
		patientFileDTO.setReferringDoctorId("D001");

		Address address = new Address();
		address.setStreet1("1 Rue Lecourbe");
		address.setZipcode("75015");
		address.setCity("Paris Cedex 15");
		address.setCountry("France-");

		Doctor doctor = new Doctor();
		doctor.setId("D001");

		patientFile = new PatientFile();
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

	@Test
	public void testCreatePatientFileSuccess() throws CheckException {
		doNothing().when(rnippService).checkPatientData(patientFileDTO);

		assertFalse(patientFileDAO.existsById("P002"));

		assertDoesNotThrow(() -> patientFileService.createPatientFile(patientFileDTO));

		assertTrue(patientFileDAO.existsById("P002"));
	}

	@Test
	public void testCreatePatientFileFailurePatientFileAlreadyExist() throws CheckException {
		patientFileDAO.save(patientFile);

		doNothing().when(rnippService).checkPatientData(patientFileDTO);

		DuplicateKeyException ex = assertThrows(DuplicateKeyException.class,
				() -> patientFileService.createPatientFile(patientFileDTO));

		assertEquals("Un dossier avec cet identifiant existe déjà.", ex.getMessage());
	}

	@Test
	public void testUpdatePatientFileSuccess() {
		patientFileDTO.setReferringDoctorId("D002"); // try to change doctor
		patientFileDTO.setId("P001"); // file exists
		when(IAMService.updateUser(any(UserDTO.class))).thenReturn(HttpStatus.ACCEPTED);
		assertTrue(patientFileDAO.existsById("P001"));

		patientFileDTOResponse = assertDoesNotThrow(() -> patientFileService.updatePatientFile(patientFileDTO));

		verify(IAMService, times(1)).updateUser(any(UserDTO.class));
		savedPatientFile = patientFileDAO.findById("P001").orElseThrow();

		// no change in saved object
		assertEquals("P001", savedPatientFile.getId());
		assertEquals("Eric", savedPatientFile.getFirstname());
		assertEquals("Martin", savedPatientFile.getLastname());
		assertEquals("0000", savedPatientFile.getSecurityCode());
		assertEquals("D001", savedPatientFile.getReferringDoctor().getId());

		assertEquals(patientFileDTO.getId(), savedPatientFile.getId());
		// changes in saved object
		assertEquals(patientFileDTO.getPhone(), savedPatientFile.getPhone());
		assertEquals(patientFileDTO.getEmail(), savedPatientFile.getEmail());
		assertEquals(patientFileDTO.getAddressDTO().getStreet1(), savedPatientFile.getAddress().getStreet1());
		assertEquals(patientFileDTO.getAddressDTO().getZipcode(), savedPatientFile.getAddress().getZipcode());
		assertEquals(patientFileDTO.getAddressDTO().getCity(), savedPatientFile.getAddress().getCity());
		assertEquals(patientFileDTO.getAddressDTO().getCountry(), savedPatientFile.getAddress().getCountry());

		// no change in returned object (except null securityCode)
		assertEquals("P001", patientFileDTOResponse.getId());
		assertEquals("Eric", patientFileDTOResponse.getFirstname());
		assertEquals("Martin", patientFileDTOResponse.getLastname());
		assertNull(patientFileDTOResponse.getSecurityCode());
		assertEquals("D001", patientFileDTOResponse.getReferringDoctorId());

		assertEquals(patientFileDTO.getId(), patientFileDTOResponse.getId());
		// changes in returned object
		assertEquals(patientFileDTO.getPhone(), patientFileDTOResponse.getPhone());
		assertEquals(patientFileDTO.getEmail(), patientFileDTOResponse.getEmail());
		assertEquals(patientFileDTO.getAddressDTO().getStreet1(), patientFileDTOResponse.getAddressDTO().getStreet1());
		assertEquals(patientFileDTO.getAddressDTO().getZipcode(), patientFileDTOResponse.getAddressDTO().getZipcode());
		assertEquals(patientFileDTO.getAddressDTO().getCity(), patientFileDTOResponse.getAddressDTO().getCity());
		assertEquals(patientFileDTO.getAddressDTO().getCountry(), patientFileDTOResponse.getAddressDTO().getCountry());
	}

	@Test
	public void testFindPatientFileSuccess() {
		patientFileDTOResponse = assertDoesNotThrow(() -> patientFileService.findPatientFile("P001"));

		assertEquals("P001", patientFileDTOResponse.getId());
		assertEquals("1 rue de la Paix", patientFileDTOResponse.getAddressDTO().getStreet1());
		assertEquals("D001", patientFileDTOResponse.getReferringDoctorId());
		assertNull(patientFileDTOResponse.getSecurityCode());
		assertEquals("1995-05-15", patientFileDTOResponse.getDateOfBirth().toString());
	}

	@Test
	public void testFindPatientFileFailurePatientFileDoesNotExist() {

		FinderException ex = assertThrows(FinderException.class, () -> patientFileService.findPatientFile("P002"));

		assertEquals("Dossier patient non trouvé.", ex.getMessage());
	}

	@Test
	public void testUpdateReferringDoctorSuccess() {
		patientFileDTO.setId("P001");

		PatientFile persistentPatientFile = patientFileDAO.findById("P001").orElseThrow();
		assertEquals("D001", persistentPatientFile.getReferringDoctor().getId());

		patientFileDTO.setReferringDoctorId("D002");

		patientFileDTOResponse = assertDoesNotThrow(() -> patientFileService.updateReferringDoctor(patientFileDTO));

		savedPatientFile = patientFileDAO.findById("P001").orElseThrow();

		assertEquals("D002", savedPatientFile.getReferringDoctor().getId());
		assertEquals("D002", patientFileDTOResponse.getReferringDoctorId());
	}

	@Test
	public void testUpdateReferringDoctorFailurePatientFileDoesNotExist() {
		patientFileDTO.setId("P002");

		FinderException ex = assertThrows(FinderException.class,
				() -> patientFileService.updateReferringDoctor(patientFileDTO));

		assertEquals("Dossier patient non trouvé.", ex.getMessage());
	}

	@Test
	public void testUpdateReferringDoctorFailureNewDoctorDoesNotExist() {
		patientFileDTO.setId("P001");

		assertTrue(patientFileDAO.existsById("P001"));

		patientFileDTO.setReferringDoctorId("D003");

		FinderException ex = assertThrows(FinderException.class,
				() -> patientFileService.updateReferringDoctor(patientFileDTO));

		assertEquals("Dossier de médecin non trouvé.", ex.getMessage());
	}

	@Test
	public void testFindPatientFilesByIdOrFirstnameOrLastnameFound4() {

		List<PatientFileDTO> patientFiles = patientFileService.findPatientFilesByIdOrFirstnameOrLastname("ma");

		assertEquals(5, patientFiles.size());
		assertEquals("P001", patientFiles.get(0).getId());
		assertEquals("P005", patientFiles.get(1).getId());
		assertEquals("P011", patientFiles.get(2).getId());
		assertEquals("P013", patientFiles.get(3).getId());
	}

	@Test
	public void testFindPatientFilesByIdOrFirstnameOrLastnameFound11() {

		List<PatientFileDTO> patientFiles = patientFileService.findPatientFilesByIdOrFirstnameOrLastname("P0");

		assertEquals(12, patientFiles.size());
	}

	@Test
	public void testFindPatientFilesByIdOrFirstnameOrLastnameFound0() {

		List<PatientFileDTO> patientFiles = patientFileService.findPatientFilesByIdOrFirstnameOrLastname("za");

		assertEquals(0, patientFiles.size());
	}

	@Test
	public void testFindPatientFilesByIdOrFirstnameOrLastnameFound0SearchStringIsBlank() {

		List<PatientFileDTO> patientFiles = patientFileService.findPatientFilesByIdOrFirstnameOrLastname("");

		assertEquals(0, patientFiles.size());
	}

	@Test
	public void testDeletePatientFileSuccessNoUser() {

		id = "P005";

		when(IAMService.userExistsById(id)).thenReturn(false);

		assertTrue(patientFileDAO.existsById(id));

		List<CorrespondenceDTO> correspondenceDTOs = correspondenceService.findCorrespondencesByPatientFileId(id);
		List<PatientFileItemDTO> patientFileItemDTOs = patientFileItemService.findPatientFileItemsByPatientFileId(id);

		assertEquals(1, correspondenceDTOs.size());
		assertEquals(10, patientFileItemDTOs.size());

		assertDoesNotThrow(() -> patientFileService.deletePatientFile(id));

		verify(IAMService, times(1)).userExistsById(id);
		assertFalse(patientFileDAO.existsById(id));

		correspondenceDTOs = correspondenceService.findCorrespondencesByPatientFileId(id);
		patientFileItemDTOs = patientFileItemService.findPatientFileItemsByPatientFileId(id);

		assertEquals(0, correspondenceDTOs.size());
		assertEquals(0, patientFileItemDTOs.size());
	}

	@Test
	public void testDeletePatientFileSuccessUserPresent() {

		id = "P005";

		when(IAMService.userExistsById(id)).thenReturn(true);
		when(IAMService.deleteUser(id)).thenReturn(HttpStatus.NO_CONTENT);


		assertTrue(patientFileDAO.existsById(id));

		List<CorrespondenceDTO> correspondenceDTOs = correspondenceService.findCorrespondencesByPatientFileId(id);
		List<PatientFileItemDTO> patientFileItemDTOs = patientFileItemService.findPatientFileItemsByPatientFileId(id);

		assertEquals(1, correspondenceDTOs.size());
		assertEquals(10, patientFileItemDTOs.size());

		assertDoesNotThrow(() -> patientFileService.deletePatientFile(id));

		verify(IAMService, times(1)).userExistsById(id);
		verify(IAMService, times(1)).deleteUser(id);
		assertFalse(patientFileDAO.existsById(id));

		correspondenceDTOs = correspondenceService.findCorrespondencesByPatientFileId(id);
		patientFileItemDTOs = patientFileItemService.findPatientFileItemsByPatientFileId(id);

		assertEquals(0, correspondenceDTOs.size());
		assertEquals(0, patientFileItemDTOs.size());
	}

	@Test
	public void testDeletePatientFileFailurePatientFileDoesNotExist() {

		id = "P002";

		DeleteException ex = assertThrows(DeleteException.class, () -> patientFileService.deletePatientFile(id));

		assertEquals("Le dossier patient n'a pas pu être supprimé.", ex.getMessage());
	}
}
