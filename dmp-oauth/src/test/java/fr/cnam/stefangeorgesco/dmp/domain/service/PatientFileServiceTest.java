package fr.cnam.stefangeorgesco.dmp.domain.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.service.UserService;
import fr.cnam.stefangeorgesco.dmp.domain.dao.CorrespondenceDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.DiseaseDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.DoctorDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.FileDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.MedicalActDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.PatientFileDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.PatientFileItemDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.ActDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.AddressDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondenceDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DiagnosisDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DiseaseDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.MailDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.MedicalActDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileItemDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PrescriptionDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.SymptomDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Act;
import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Correspondence;
import fr.cnam.stefangeorgesco.dmp.domain.model.Diagnosis;
import fr.cnam.stefangeorgesco.dmp.domain.model.Disease;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.Mail;
import fr.cnam.stefangeorgesco.dmp.domain.model.MedicalAct;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFileItem;
import fr.cnam.stefangeorgesco.dmp.domain.model.Prescription;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;
import fr.cnam.stefangeorgesco.dmp.domain.model.Symptom;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DeleteException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;

@TestPropertySource("/application-test.properties")
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
	private DiseaseDAO diseaseDAO;

	@MockBean
	private MedicalActDAO medicalActDAO;

	@MockBean
	private PatientFileItemDAO patientFileItemDAO;

	@Autowired
	private PatientFileService patientFileService;

	@Autowired
	private AddressDTO addressDTO;

	@Autowired
	private PatientFileDTO patientFileDTO;

	@Autowired
	private PatientFileDTO patientFileDTO1;

	@Autowired
	private PatientFileDTO patientFileDTO2;

	@Autowired
	private PatientFileDTO patientFileDTOResponse;

	@Autowired
	private CorrespondenceDTO correspondenceDTO;

	@Autowired
	private CorrespondenceDTO correspondenceDTOResponse;

	@Autowired
	private MedicalActDTO medicalActDTO;

	private PatientFileItemDTO patientFileItemDTO;

	@Autowired
	private ActDTO actDTO;

	@Autowired
	private DiseaseDTO diseaseDTO;

	@Autowired
	private DiagnosisDTO diagnosisDTO;

	@Autowired
	private DiagnosisDTO diagnosisDTOResponse;

	@Autowired
	private MailDTO mailDTO;

	@Autowired
	private PrescriptionDTO prescriptionDTO;

	@Autowired
	private SymptomDTO symptomDTO;

	@Autowired
	private Specialty specialty1;

	@Autowired
	private Specialty specialty2;

	@Autowired
	private Disease disease1;

	@Autowired
	private Disease disease2;

	@Autowired
	private MedicalAct medicalAct1;

	@Autowired
	private MedicalAct medicalAct2;

	@Autowired
	private Doctor doctor1;

	@Autowired
	private Doctor doctor2;

	@Autowired
	private Doctor newDoctor;

	@Autowired
	private Address address;

	@Autowired
	private PatientFile persistentPatientFile;

	@Autowired
	private PatientFile savedPatientFile;

	@Autowired
	private PatientFile patientFile1;

	@Autowired
	private PatientFile patientFile2;

	@Autowired
	private Correspondence persistentCorrespondence;

	@Autowired
	private Correspondence savedCorrespondence;

	@Autowired
	private Correspondence foundCorrespondence1;

	@Autowired
	private Correspondence foundCorrespondence2;

	@Autowired
	private Act act;

	@Autowired
	private Diagnosis diagnosis;

	@Autowired
	private Mail mail;

	@Autowired
	private Prescription prescription;

	@Autowired
	private Symptom symptom;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private ArgumentCaptor<PatientFile> patientFileCaptor = ArgumentCaptor.forClass(PatientFile.class);

	private ArgumentCaptor<Correspondence> correspondenceCaptor = ArgumentCaptor.forClass(Correspondence.class);

	private ArgumentCaptor<PatientFileItem> patientFileItemCaptor = ArgumentCaptor.forClass(PatientFileItem.class);

	private UUID uuid;

	private LocalDate date;

	@BeforeEach
	public void setup() {
		addressDTO.setStreet1("1 Rue Lecourbe");
		addressDTO.setZipcode("75015");
		addressDTO.setCity("Paris");
		addressDTO.setCountry("France");
		patientFileDTO.setId("P001");
		patientFileDTO.setFirstname("Patrick");
		patientFileDTO.setLastname("Dubois");
		patientFileDTO.setDateOfBirth(LocalDate.of(2000, 2, 13));
		patientFileDTO.setPhone("9876543210");
		patientFileDTO.setEmail("patrick.dubois@mail.fr");
		patientFileDTO.setAddressDTO(addressDTO);
		patientFileDTO.setReferringDoctorId("D001");

		address.setStreet1("street 1");
		address.setZipcode("zipcode");
		address.setCity("City");
		address.setCountry("Country");
		specialty1.setId("S001");
		specialty1.setDescription("Specialty 1");
		specialty2.setId("S002");
		specialty2.setDescription("Specialty 2");
		disease1.setId("DIS001");
		disease1.setDescription("Disease 1");
		disease2.setId("DIS002");
		disease2.setDescription("Disease 2");
		medicalAct1.setId("MA001");
		medicalAct1.setDescription("Medical act 1");
		medicalAct2.setId("MA002");
		medicalAct2.setDescription("Medical act 2");
		doctor1.setId("D001");
		doctor2.setId("D002");
		doctor2.setFirstname("firstname");
		doctor2.setLastname("lastname");
		doctor2.setSpecialties(List.of(specialty1, specialty2));
		persistentPatientFile.setId(patientFileDTO.getId());
		persistentPatientFile.setFirstname("firstname");
		persistentPatientFile.setLastname("lastname");
		persistentPatientFile.setDateOfBirth(LocalDate.of(2000, 2, 13));
		persistentPatientFile.setAddress(address);
		persistentPatientFile.setSecurityCode("securityCode");
		persistentPatientFile.setReferringDoctor(doctor1);

		patientFile1.setId("ID_1");
		patientFile1.setFirstname("firstname_1");
		patientFile1.setLastname("lastname_1");
		patientFile1.setDateOfBirth(LocalDate.of(2000, 2, 13));
		patientFile1.setAddress(address);
		patientFile1.setSecurityCode("securityCode_1");
		patientFile1.setReferringDoctor(doctor1);

		patientFile2.setId("ID_2");
		patientFile2.setFirstname("firstname_2");
		patientFile2.setLastname("lastname_2");
		patientFile2.setDateOfBirth(LocalDate.of(1995, 8, 21));
		patientFile2.setAddress(address);
		patientFile2.setSecurityCode("securityCode_2");
		patientFile2.setReferringDoctor(doctor1);

		correspondenceDTO.setDateUntil(LocalDate.now().plusDays(1));
		correspondenceDTO.setDoctorId("D002");
		correspondenceDTO.setPatientFileId("ID_1");

		date = LocalDate.now();

		medicalActDTO.setId("MA001");
		actDTO.setDate(date);
		actDTO.setComments("act comment");
		actDTO.setAuthoringDoctorId("D001");
		actDTO.setPatientFileId("P001");
		actDTO.setMedicalActDTO(medicalActDTO);

		diseaseDTO.setId("DIS001");
		diagnosisDTO.setDate(date);
		diagnosisDTO.setComments("diagnosis comment");
		diagnosisDTO.setAuthoringDoctorId("D001");
		diagnosisDTO.setPatientFileId("P001");
		diagnosisDTO.setDiseaseDTO(diseaseDTO);

		mailDTO.setDate(date);
		mailDTO.setComments("mail comment");
		mailDTO.setAuthoringDoctorId("D001");
		mailDTO.setPatientFileId("P001");
		mailDTO.setText("texte du message");
		mailDTO.setRecipientDoctorId("D002");

		prescriptionDTO.setDate(date);
		prescriptionDTO.setComments("prescription comment");
		prescriptionDTO.setAuthoringDoctorId("D001");
		prescriptionDTO.setPatientFileId("P001");
		prescriptionDTO.setDescription("prescription description");

		symptomDTO.setDate(date);
		symptomDTO.setComments("symptom comment");
		symptomDTO.setAuthoringDoctorId("D001");
		symptomDTO.setPatientFileId("P001");
		symptomDTO.setDescription("symptom description");

		persistentCorrespondence.setDateUntil(LocalDate.of(2022, 07, 29));
		persistentCorrespondence.setDoctor(doctor2);
		persistentCorrespondence.setPatientFile(patientFile1);

		foundCorrespondence1.setId(UUID.randomUUID());
		foundCorrespondence1.setDateUntil(LocalDate.of(2022, 8, 01));
		foundCorrespondence1.setDoctor(doctor1);
		foundCorrespondence1.setPatientFile(patientFile1);
		foundCorrespondence2.setId(UUID.randomUUID());
		foundCorrespondence2.setDateUntil(LocalDate.of(2022, 9, 05));
		foundCorrespondence2.setDoctor(doctor2);
		foundCorrespondence2.setPatientFile(patientFile2);

		uuid = UUID.randomUUID();

		act.setId(uuid);
		act.setDate(actDTO.getDate());
		act.setComments(actDTO.getComments());
		act.setAuthoringDoctor(doctor1);
		act.setPatientFile(patientFile1);
		act.setMedicalAct(medicalAct1);

		diagnosis.setId(uuid);
		diagnosis.setDate(actDTO.getDate());
		diagnosis.setComments(actDTO.getComments());
		diagnosis.setAuthoringDoctor(doctor1);
		diagnosis.setPatientFile(patientFile1);
		diagnosis.setDisease(disease1);

		mail.setId(uuid);
		mail.setDate(actDTO.getDate());
		mail.setComments(actDTO.getComments());
		mail.setAuthoringDoctor(doctor1);
		mail.setPatientFile(patientFile1);
		mail.setText("A text");
		mail.setRecipientDoctor(doctor2);

		prescription.setId(uuid);
		prescription.setDate(actDTO.getDate());
		prescription.setComments(actDTO.getComments());
		prescription.setAuthoringDoctor(doctor1);
		prescription.setPatientFile(patientFile1);
		prescription.setDescription("A prescription description");

		symptom.setId(uuid);
		symptom.setDate(actDTO.getDate());
		symptom.setComments(actDTO.getComments());
		symptom.setAuthoringDoctor(doctor1);
		symptom.setPatientFile(patientFile1);
		symptom.setDescription("A symptom description");
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
		assertEquals(null, patientFileDTOResponse.getSecurityCode());
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
		assertEquals(null, patientFileDTOResponse.getSecurityCode());
		assertEquals("D001", patientFileDTOResponse.getReferringDoctorId());
		assertEquals("2000-02-13", patientFileDTOResponse.getDateOfBirth().toString());
	}

	@Test
	public void testFindPatientFileFailurePatientFileDoesNotExist() throws FinderException {
		when(patientFileDAO.findById("P003")).thenReturn(Optional.ofNullable(null));

		FinderException ex = assertThrows(FinderException.class, () -> patientFileService.findPatientFile("P003"));

		verify(patientFileDAO, times(1)).findById("P003");

		assertEquals("Dossier patient non trouvé.", ex.getMessage());
	}

	@Test
	public void testFindPatientFileItemSuccess() {

		uuid = UUID.randomUUID();

		diagnosis.setId(uuid);
		diagnosis.setDate(date);
		diagnosis.setComments("comments");
		diagnosis.setAuthoringDoctor(doctor1);
		diagnosis.setPatientFile(patientFile1);
		diagnosis.setDisease(disease1);

		when(patientFileItemDAO.findById(uuid)).thenReturn(Optional.of(diagnosis));

		patientFileItemDTO = assertDoesNotThrow(() -> patientFileService.findPatientFileItem(uuid));

		verify(patientFileItemDAO, times(1)).findById(uuid);

		assertTrue(patientFileItemDTO instanceof DiagnosisDTO);

		diagnosisDTOResponse = (DiagnosisDTO) patientFileItemDTO;

		assertEquals(uuid, diagnosisDTOResponse.getId());
		assertEquals(date, diagnosisDTOResponse.getDate());
		assertEquals("comments", diagnosisDTOResponse.getComments());
		assertEquals(doctor1.getId(), diagnosisDTOResponse.getAuthoringDoctorId());
		assertEquals(doctor1.getFirstname(), diagnosisDTOResponse.getAuthoringDoctorFirstname());
		assertEquals(doctor1.getLastname(), diagnosisDTOResponse.getAuthoringDoctorLastname());
		assertEquals(patientFile1.getId(), diagnosisDTOResponse.getPatientFileId());
		assertEquals(disease1.getId(), diagnosisDTOResponse.getDiseaseDTO().getId());
		assertEquals(disease1.getDescription(), diagnosisDTOResponse.getDiseaseDTO().getDescription());
	}

	@Test
	public void testFindPatientFileItemFailurePatientFileItemDoesNotExist() {

		when(patientFileItemDAO.findById(uuid)).thenReturn(Optional.ofNullable(null));

		FinderException ex = assertThrows(FinderException.class, () -> patientFileService.findPatientFileItem(uuid));

		verify(patientFileItemDAO, times(1)).findById(uuid);

		assertEquals("Elément médical non trouvé.", ex.getMessage());
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
		assertEquals(null, patientFileDTOResponse.getSecurityCode());
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

		when(patientFileDAO.findById(patientFileDTO.getId())).thenReturn(Optional.ofNullable(null));

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
		when(doctorDAO.findById(patientFileDTO.getReferringDoctorId())).thenReturn(Optional.ofNullable(null));

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

		patientFileDTO1 = patientFilesDTO.get(0);
		patientFileDTO2 = patientFilesDTO.get(1);

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
	public void testCreateCorrespondenceSuccess() {

		uuid = UUID.randomUUID();
		persistentCorrespondence.setId(uuid);
		persistentCorrespondence.setDateUntil(correspondenceDTO.getDateUntil());

		when(correspondenceDAO.save(correspondenceCaptor.capture())).thenReturn(persistentCorrespondence);
		when(correspondenceDAO.findById(any(UUID.class))).thenReturn(Optional.of(persistentCorrespondence));

		correspondenceDTOResponse = assertDoesNotThrow(
				() -> patientFileService.createCorrespondence(correspondenceDTO));

		verify(correspondenceDAO, times(1)).save(any(Correspondence.class));

		savedCorrespondence = correspondenceCaptor.getValue();

		assertEquals(correspondenceDTO.getDateUntil(), savedCorrespondence.getDateUntil());
		assertEquals(correspondenceDTO.getDoctorId(), savedCorrespondence.getDoctor().getId());
		assertEquals(correspondenceDTO.getPatientFileId(), savedCorrespondence.getPatientFile().getId());

		assertEquals(correspondenceDTO.getDateUntil(), correspondenceDTOResponse.getDateUntil());
		assertEquals(correspondenceDTO.getDoctorId(), correspondenceDTOResponse.getDoctorId());
		assertEquals(correspondenceDTO.getPatientFileId(), correspondenceDTOResponse.getPatientFileId());
		assertEquals(persistentCorrespondence.getDoctor().getId(), correspondenceDTOResponse.getDoctorId());
		assertEquals(persistentCorrespondence.getDoctor().getFirstname(),
				correspondenceDTOResponse.getDoctorFirstname());
		assertEquals(persistentCorrespondence.getDoctor().getLastname(), correspondenceDTOResponse.getDoctorLastname());
		assertEquals(persistentCorrespondence.getDoctor().getSpecialties().stream().map(Specialty::getDescription)
				.collect(Collectors.toList()), correspondenceDTOResponse.getDoctorSpecialties());
	}

	@Test
	public void testDeleteCorrespondenceSuccess() {

		uuid = UUID.randomUUID();

		doNothing().when(correspondenceDAO).deleteById(uuid);

		patientFileService.deleteCorrespondence(uuid);

		verify(correspondenceDAO, times(1)).deleteById(uuid);
	}

	@Test
	public void testFindCorrespondenceSuccess() {
		uuid = UUID.randomUUID();

		persistentCorrespondence.setId(uuid);

		when(correspondenceDAO.findById(uuid)).thenReturn(Optional.of(persistentCorrespondence));

		correspondenceDTOResponse = assertDoesNotThrow(() -> patientFileService.findCorrespondence(uuid.toString()));

		verify(correspondenceDAO, times(1)).findById(uuid);

		assertEquals(persistentCorrespondence.getId(), correspondenceDTOResponse.getId());
		assertEquals(persistentCorrespondence.getDateUntil(), correspondenceDTOResponse.getDateUntil());
		assertEquals(persistentCorrespondence.getDoctor().getId(), correspondenceDTOResponse.getDoctorId());
		assertEquals(persistentCorrespondence.getPatientFile().getId(), correspondenceDTOResponse.getPatientFileId());
		assertEquals(persistentCorrespondence.getDoctor().getId(), correspondenceDTOResponse.getDoctorId());
		assertEquals(persistentCorrespondence.getDoctor().getFirstname(),
				correspondenceDTOResponse.getDoctorFirstname());
		assertEquals(persistentCorrespondence.getDoctor().getLastname(), correspondenceDTOResponse.getDoctorLastname());
		assertEquals(persistentCorrespondence.getDoctor().getSpecialties().stream().map(Specialty::getDescription)
				.collect(Collectors.toList()), correspondenceDTOResponse.getDoctorSpecialties());
	}

	@Test
	public void testFindCorrespondenceFailureCorrespondenceDoesNotExist() throws FinderException {
		uuid = UUID.randomUUID();

		when(correspondenceDAO.findById(uuid)).thenReturn(Optional.ofNullable(null));

		FinderException ex = assertThrows(FinderException.class,
				() -> patientFileService.findCorrespondence(uuid.toString()));

		verify(correspondenceDAO, times(1)).findById(uuid);

		assertEquals("Correspondance non trouvée.", ex.getMessage());
	}

	@Test
	public void testFindCorrespondencesByPatientFileIdFound3() {

		when(correspondenceDAO.findByPatientFileId("P001"))
				.thenReturn(List.of(foundCorrespondence1, foundCorrespondence2));

		List<CorrespondenceDTO> correspondencesDTO = patientFileService.findCorrespondencesByPatientFileId("P001");

		verify(correspondenceDAO, times(1)).findByPatientFileId("P001");

		assertEquals(2, correspondencesDTO.size());
		assertEquals(foundCorrespondence1.getId(), correspondencesDTO.get(0).getId());
		assertEquals("2022-08-01", correspondencesDTO.get(0).getDateUntil().toString());
		assertEquals("D001", correspondencesDTO.get(0).getDoctorId());
		assertEquals("ID_1", correspondencesDTO.get(0).getPatientFileId());
	}

	@Test
	public void testFindCorrespondencesByPatientFileIdFound0() {

		when(correspondenceDAO.findByPatientFileId("P055")).thenReturn(List.of());

		List<CorrespondenceDTO> correspondencesDTO = patientFileService.findCorrespondencesByPatientFileId("P055");

		verify(correspondenceDAO, times(1)).findByPatientFileId("P055");

		assertEquals(0, correspondencesDTO.size());
	}

	@Test
	public void testFindDiseaseSuccess() {

		when(diseaseDAO.findById(disease1.getId())).thenReturn(Optional.of(disease1));

		diseaseDTO = assertDoesNotThrow(() -> patientFileService.findDisease(disease1.getId()));

		verify(diseaseDAO, times(1)).findById(disease1.getId());

		assertEquals(disease1.getId(), diseaseDTO.getId());
		assertEquals(disease1.getDescription(), diseaseDTO.getDescription());
	}

	@Test
	public void testFindMedicalActSuccess() {

		when(medicalActDAO.findById(medicalAct1.getId())).thenReturn(Optional.of(medicalAct1));

		medicalActDTO = assertDoesNotThrow(() -> patientFileService.findMedicalAct(medicalAct1.getId()));

		verify(medicalActDAO, times(1)).findById(medicalAct1.getId());

		assertEquals(medicalAct1.getId(), medicalActDTO.getId());
		assertEquals(medicalAct1.getDescription(), medicalActDTO.getDescription());
	}

	@Test
	public void testFindDiseasesByIdOrDescriptionFound2() {

		when(diseaseDAO.findByIdOrDescription("sinusite", 10)).thenReturn(List.of(disease1, disease2));

		List<DiseaseDTO> diseasesDTO = patientFileService.findDiseasesByIdOrDescription("sinusite", 10);

		verify(diseaseDAO, times(1)).findByIdOrDescription("sinusite", 10);

		assertEquals(2, diseasesDTO.size());
		assertEquals("DIS001", diseasesDTO.get(0).getId());
		assertEquals("Disease 1", diseasesDTO.get(0).getDescription());
	}

	@Test
	public void testFindMedicalActsByIdOrDescriptionFound2() {

		when(medicalActDAO.findByIdOrDescription("radio", 10)).thenReturn(List.of(medicalAct1, medicalAct2));

		List<MedicalActDTO> medicalActsDTO = patientFileService.findMedicalActsByIdOrDescription("radio", 10);

		verify(medicalActDAO, times(1)).findByIdOrDescription("radio", 10);

		assertEquals(2, medicalActsDTO.size());
		assertEquals("MA001", medicalActsDTO.get(0).getId());
		assertEquals("Medical act 1", medicalActsDTO.get(0).getDescription());
	}

	@Test
	public void testCreateActSuccess() {

		patientFile1.setId("P001");

		when(patientFileItemDAO.save(patientFileItemCaptor.capture())).thenReturn(act);
		when(patientFileItemDAO.findById(uuid)).thenReturn(Optional.of(act));

		PatientFileItemDTO patientFileItemDTOResponse = assertDoesNotThrow(
				() -> patientFileService.createPatientFileItem(actDTO));

		verify(patientFileItemDAO, times(1)).save(any(Act.class));
		verify(patientFileItemDAO, times(1)).findById(uuid);

		PatientFileItem savedPatientFileItem = patientFileItemCaptor.getValue();

		assertEquals(null, savedPatientFileItem.getId());
		assertEquals(actDTO.getDate(), savedPatientFileItem.getDate());
		assertEquals(actDTO.getComments(), savedPatientFileItem.getComments());
		assertEquals(actDTO.getAuthoringDoctorId(), savedPatientFileItem.getAuthoringDoctor().getId());
		assertEquals(actDTO.getPatientFileId(), savedPatientFileItem.getPatientFile().getId());
		assertTrue(savedPatientFileItem instanceof Act);
		assertEquals(actDTO.getMedicalActDTO().getId(), ((Act) savedPatientFileItem).getMedicalAct().getId());

		assertEquals(uuid, patientFileItemDTOResponse.getId());
		assertEquals(act.getDate(), patientFileItemDTOResponse.getDate());
		assertEquals(act.getComments(), patientFileItemDTOResponse.getComments());
		assertEquals(act.getAuthoringDoctor().getId(), patientFileItemDTOResponse.getAuthoringDoctorId());
		assertEquals(act.getAuthoringDoctor().getFirstname(), patientFileItemDTOResponse.getAuthoringDoctorFirstname());
		assertEquals(act.getAuthoringDoctor().getLastname(), patientFileItemDTOResponse.getAuthoringDoctorLastname());
		assertEquals(act.getPatientFile().getId(), patientFileItemDTOResponse.getPatientFileId());
		assertTrue(patientFileItemDTOResponse instanceof ActDTO);
		assertEquals(act.getMedicalAct().getId(), ((ActDTO) patientFileItemDTOResponse).getMedicalActDTO().getId());
		assertEquals(act.getMedicalAct().getDescription(),
				((ActDTO) patientFileItemDTOResponse).getMedicalActDTO().getDescription());
	}

	@Test
	public void testUpdateActSuccess() {

		actDTO.setId(uuid);

		act.setId(uuid);
		act.setDate(LocalDate.of(2022, 7, 14));
		act.setComments("another comment");
		act.setAuthoringDoctor(doctor2);
		patientFile2.setId("P002");
		act.setPatientFile(patientFile2);
		act.setMedicalAct(medicalAct2);

		when(patientFileItemDAO.findById(uuid)).thenReturn(Optional.of(act));
		when(patientFileItemDAO.save(patientFileItemCaptor.capture())).thenReturn(act);

		PatientFileItemDTO patientFileItemDTOResponse = assertDoesNotThrow(
				() -> patientFileService.updatePatientFileItem(actDTO));

		verify(patientFileItemDAO, times(1)).findById(uuid);
		verify(patientFileItemDAO, times(1)).save(any(Act.class));

		PatientFileItem savedPatientFileItem = patientFileItemCaptor.getValue();

		assertEquals(uuid, savedPatientFileItem.getId());
		assertEquals(act.getDate(), savedPatientFileItem.getDate());
		assertEquals(actDTO.getComments(), savedPatientFileItem.getComments());
		assertEquals(act.getAuthoringDoctor().getId(), savedPatientFileItem.getAuthoringDoctor().getId());
		assertEquals(act.getPatientFile().getId(), savedPatientFileItem.getPatientFile().getId());
		assertTrue(savedPatientFileItem instanceof Act);
		assertEquals(actDTO.getMedicalActDTO().getId(), ((Act) savedPatientFileItem).getMedicalAct().getId());

		assertEquals(uuid, patientFileItemDTOResponse.getId());
		assertEquals(act.getDate(), patientFileItemDTOResponse.getDate());
		assertEquals(actDTO.getComments(), patientFileItemDTOResponse.getComments());
		assertEquals(act.getAuthoringDoctor().getId(), patientFileItemDTOResponse.getAuthoringDoctorId());
		assertEquals(act.getAuthoringDoctor().getFirstname(), patientFileItemDTOResponse.getAuthoringDoctorFirstname());
		assertEquals(act.getAuthoringDoctor().getLastname(), patientFileItemDTOResponse.getAuthoringDoctorLastname());
		assertEquals(act.getPatientFile().getId(), patientFileItemDTOResponse.getPatientFileId());
		assertTrue(patientFileItemDTOResponse instanceof ActDTO);
		assertEquals(actDTO.getMedicalActDTO().getId(),
				((ActDTO) patientFileItemDTOResponse).getMedicalActDTO().getId());
		assertEquals(act.getMedicalAct().getDescription(),
				((ActDTO) patientFileItemDTOResponse).getMedicalActDTO().getDescription());
	}

	@Test
	public void testUpdateDiagnosisSuccess() {

		diagnosisDTO.setId(uuid);

		diagnosis.setId(uuid);
		diagnosis.setDate(LocalDate.of(2022, 7, 14));
		diagnosis.setComments("another comment");
		diagnosis.setAuthoringDoctor(doctor2);
		patientFile2.setId("P002");
		diagnosis.setPatientFile(patientFile2);
		diagnosis.setDisease(disease2);

		when(patientFileItemDAO.findById(uuid)).thenReturn(Optional.of(diagnosis));
		when(patientFileItemDAO.save(patientFileItemCaptor.capture())).thenReturn(diagnosis);

		PatientFileItemDTO patientFileItemDTOResponse = assertDoesNotThrow(
				() -> patientFileService.updatePatientFileItem(diagnosisDTO));

		verify(patientFileItemDAO, times(1)).findById(uuid);
		verify(patientFileItemDAO, times(1)).save(any(Diagnosis.class));

		PatientFileItem savedPatientFileItem = patientFileItemCaptor.getValue();

		assertEquals(uuid, savedPatientFileItem.getId());
		assertEquals(diagnosis.getDate(), savedPatientFileItem.getDate());
		assertEquals(diagnosisDTO.getComments(), savedPatientFileItem.getComments());
		assertEquals(diagnosis.getAuthoringDoctor().getId(), savedPatientFileItem.getAuthoringDoctor().getId());
		assertEquals(diagnosis.getPatientFile().getId(), savedPatientFileItem.getPatientFile().getId());
		assertTrue(savedPatientFileItem instanceof Diagnosis);
		assertEquals(diagnosisDTO.getDiseaseDTO().getId(), ((Diagnosis) savedPatientFileItem).getDisease().getId());

		assertEquals(uuid, patientFileItemDTOResponse.getId());
		assertEquals(diagnosis.getDate(), patientFileItemDTOResponse.getDate());
		assertEquals(diagnosisDTO.getComments(), patientFileItemDTOResponse.getComments());
		assertEquals(diagnosis.getAuthoringDoctor().getId(), patientFileItemDTOResponse.getAuthoringDoctorId());
		assertEquals(diagnosis.getAuthoringDoctor().getFirstname(),
				patientFileItemDTOResponse.getAuthoringDoctorFirstname());
		assertEquals(diagnosis.getAuthoringDoctor().getLastname(),
				patientFileItemDTOResponse.getAuthoringDoctorLastname());
		assertEquals(diagnosis.getPatientFile().getId(), patientFileItemDTOResponse.getPatientFileId());
		assertTrue(patientFileItemDTOResponse instanceof DiagnosisDTO);
		assertEquals(diagnosisDTO.getDiseaseDTO().getId(),
				((DiagnosisDTO) patientFileItemDTOResponse).getDiseaseDTO().getId());
		assertEquals(diagnosis.getDisease().getDescription(),
				((DiagnosisDTO) patientFileItemDTOResponse).getDiseaseDTO().getDescription());
	}

	@Test
	public void testUpdateMailSuccess() {

		mailDTO.setId(uuid);

		mail.setId(uuid);
		mail.setDate(LocalDate.of(2022, 7, 14));
		mail.setComments("another comment");
		mail.setAuthoringDoctor(doctor2);
		patientFile2.setId("P002");
		mail.setPatientFile(patientFile2);
		mail.setRecipientDoctor(doctor2);

		when(patientFileItemDAO.findById(uuid)).thenReturn(Optional.of(mail));
		when(patientFileItemDAO.save(patientFileItemCaptor.capture())).thenReturn(mail);

		PatientFileItemDTO patientFileItemDTOResponse = assertDoesNotThrow(
				() -> patientFileService.updatePatientFileItem(mailDTO));

		verify(patientFileItemDAO, times(1)).findById(uuid);
		verify(patientFileItemDAO, times(1)).save(any(Mail.class));

		PatientFileItem savedPatientFileItem = patientFileItemCaptor.getValue();

		assertEquals(uuid, savedPatientFileItem.getId());
		assertEquals(mail.getDate(), savedPatientFileItem.getDate());
		assertEquals(mailDTO.getComments(), savedPatientFileItem.getComments());
		assertEquals(mail.getAuthoringDoctor().getId(), savedPatientFileItem.getAuthoringDoctor().getId());
		assertEquals(mail.getPatientFile().getId(), savedPatientFileItem.getPatientFile().getId());
		assertTrue(savedPatientFileItem instanceof Mail);
		assertEquals(mailDTO.getText(), ((Mail) savedPatientFileItem).getText());
		assertEquals(mailDTO.getRecipientDoctorId(), ((Mail) savedPatientFileItem).getRecipientDoctor().getId());

		assertEquals(uuid, patientFileItemDTOResponse.getId());
		assertEquals(mail.getDate(), patientFileItemDTOResponse.getDate());
		assertEquals(mailDTO.getComments(), patientFileItemDTOResponse.getComments());
		assertEquals(mail.getAuthoringDoctor().getId(), patientFileItemDTOResponse.getAuthoringDoctorId());
		assertEquals(mail.getAuthoringDoctor().getFirstname(),
				patientFileItemDTOResponse.getAuthoringDoctorFirstname());
		assertEquals(mail.getAuthoringDoctor().getLastname(), patientFileItemDTOResponse.getAuthoringDoctorLastname());
		assertEquals(mail.getPatientFile().getId(), patientFileItemDTOResponse.getPatientFileId());
		assertTrue(patientFileItemDTOResponse instanceof MailDTO);
		assertEquals(mailDTO.getText(), ((MailDTO) patientFileItemDTOResponse).getText());
		assertEquals(mailDTO.getRecipientDoctorId(), ((MailDTO) patientFileItemDTOResponse).getRecipientDoctorId());
	}

	@Test
	public void testUpdatePrescriptionSuccess() {

		prescriptionDTO.setId(uuid);

		prescription.setId(uuid);
		prescription.setDate(LocalDate.of(2022, 7, 14));
		prescription.setComments("another comment");
		prescription.setAuthoringDoctor(doctor2);
		patientFile2.setId("P002");
		prescription.setPatientFile(patientFile2);
		prescription.setDescription("another description");

		when(patientFileItemDAO.findById(uuid)).thenReturn(Optional.of(prescription));
		when(patientFileItemDAO.save(patientFileItemCaptor.capture())).thenReturn(prescription);

		PatientFileItemDTO patientFileItemDTOResponse = assertDoesNotThrow(
				() -> patientFileService.updatePatientFileItem(prescriptionDTO));

		verify(patientFileItemDAO, times(1)).findById(uuid);
		verify(patientFileItemDAO, times(1)).save(any(Prescription.class));

		PatientFileItem savedPatientFileItem = patientFileItemCaptor.getValue();

		assertEquals(uuid, savedPatientFileItem.getId());
		assertEquals(prescription.getDate(), savedPatientFileItem.getDate());
		assertEquals(prescriptionDTO.getComments(), savedPatientFileItem.getComments());
		assertEquals(prescription.getAuthoringDoctor().getId(), savedPatientFileItem.getAuthoringDoctor().getId());
		assertEquals(prescription.getPatientFile().getId(), savedPatientFileItem.getPatientFile().getId());
		assertTrue(savedPatientFileItem instanceof Prescription);
		assertEquals(prescriptionDTO.getDescription(), ((Prescription) savedPatientFileItem).getDescription());

		assertEquals(uuid, patientFileItemDTOResponse.getId());
		assertEquals(prescription.getDate(), patientFileItemDTOResponse.getDate());
		assertEquals(prescriptionDTO.getComments(), patientFileItemDTOResponse.getComments());
		assertEquals(prescription.getAuthoringDoctor().getId(), patientFileItemDTOResponse.getAuthoringDoctorId());
		assertEquals(prescription.getAuthoringDoctor().getFirstname(),
				patientFileItemDTOResponse.getAuthoringDoctorFirstname());
		assertEquals(prescription.getAuthoringDoctor().getLastname(),
				patientFileItemDTOResponse.getAuthoringDoctorLastname());
		assertEquals(prescription.getPatientFile().getId(), patientFileItemDTOResponse.getPatientFileId());
		assertTrue(patientFileItemDTOResponse instanceof PrescriptionDTO);
		assertEquals(prescriptionDTO.getDescription(), ((PrescriptionDTO) patientFileItemDTOResponse).getDescription());
	}

	@Test
	public void testUpdateSymptomSuccess() {

		symptomDTO.setId(uuid);

		symptom.setId(uuid);
		symptom.setDate(LocalDate.of(2022, 7, 14));
		symptom.setComments("another comment");
		symptom.setAuthoringDoctor(doctor2);
		patientFile2.setId("P002");
		symptom.setPatientFile(patientFile2);
		symptom.setDescription("another description");

		when(patientFileItemDAO.findById(uuid)).thenReturn(Optional.of(symptom));
		when(patientFileItemDAO.save(patientFileItemCaptor.capture())).thenReturn(symptom);

		PatientFileItemDTO patientFileItemDTOResponse = assertDoesNotThrow(
				() -> patientFileService.updatePatientFileItem(symptomDTO));

		verify(patientFileItemDAO, times(1)).findById(uuid);
		verify(patientFileItemDAO, times(1)).save(any(Symptom.class));

		PatientFileItem savedPatientFileItem = patientFileItemCaptor.getValue();

		assertEquals(uuid, savedPatientFileItem.getId());
		assertEquals(symptom.getDate(), savedPatientFileItem.getDate());
		assertEquals(symptomDTO.getComments(), savedPatientFileItem.getComments());
		assertEquals(symptom.getAuthoringDoctor().getId(), savedPatientFileItem.getAuthoringDoctor().getId());
		assertEquals(symptom.getPatientFile().getId(), savedPatientFileItem.getPatientFile().getId());
		assertTrue(savedPatientFileItem instanceof Symptom);
		assertEquals(symptomDTO.getDescription(), ((Symptom) savedPatientFileItem).getDescription());

		assertEquals(uuid, patientFileItemDTOResponse.getId());
		assertEquals(symptom.getDate(), patientFileItemDTOResponse.getDate());
		assertEquals(symptomDTO.getComments(), patientFileItemDTOResponse.getComments());
		assertEquals(symptom.getAuthoringDoctor().getId(), patientFileItemDTOResponse.getAuthoringDoctorId());
		assertEquals(symptom.getAuthoringDoctor().getFirstname(),
				patientFileItemDTOResponse.getAuthoringDoctorFirstname());
		assertEquals(symptom.getAuthoringDoctor().getLastname(),
				patientFileItemDTOResponse.getAuthoringDoctorLastname());
		assertEquals(symptom.getPatientFile().getId(), patientFileItemDTOResponse.getPatientFileId());
		assertTrue(patientFileItemDTOResponse instanceof SymptomDTO);
		assertEquals(symptomDTO.getDescription(), ((SymptomDTO) patientFileItemDTOResponse).getDescription());
	}

	@Test
	public void testDeletePatientFileItemSuccess() {

		uuid = UUID.randomUUID();

		doNothing().when(patientFileItemDAO).deleteById(uuid);

		patientFileService.deletePatientFileItem(uuid);

		verify(patientFileItemDAO, times(1)).deleteById(uuid);
	}

	@Test
	public void testFindPatientFileItemsByPatientFileIdFound5() {

		when(patientFileItemDAO.findByPatientFileId("P001"))
				.thenReturn(List.of(act, diagnosis, mail, prescription, symptom));

		List<PatientFileItemDTO> patientFileItemsDTO = patientFileService.findPatientFileItemsByPatientFileId("P001");

		verify(patientFileItemDAO, times(1)).findByPatientFileId("P001");

		assertEquals(5, patientFileItemsDTO.size());
		assertTrue(patientFileItemsDTO.get(0) instanceof ActDTO);
		assertTrue(patientFileItemsDTO.get(1) instanceof DiagnosisDTO);
		assertTrue(patientFileItemsDTO.get(2) instanceof MailDTO);
		assertTrue(patientFileItemsDTO.get(3) instanceof PrescriptionDTO);
		assertTrue(patientFileItemsDTO.get(4) instanceof SymptomDTO);
	}

	@Test
	public void testFindPatientFileItemsByPatientFileIdFound0() {

		when(patientFileItemDAO.findByPatientFileId("P027")).thenReturn(List.of());

		List<PatientFileItemDTO> patientFileItemsDTO = patientFileService.findPatientFileItemsByPatientFileId("P027");

		verify(patientFileItemDAO, times(1)).findByPatientFileId("P027");

		assertEquals(0, patientFileItemsDTO.size());
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
