package fr.cnam.stefangeorgesco.dmp.domain.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.service.KeycloakService;
import fr.cnam.stefangeorgesco.dmp.domain.dao.CorrespondenceDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.DoctorDAO;
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
import fr.cnam.stefangeorgesco.dmp.domain.model.Diagnosis;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.Mail;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import fr.cnam.stefangeorgesco.dmp.domain.model.Prescription;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;
import fr.cnam.stefangeorgesco.dmp.domain.model.Symptom;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CreateException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DeleteException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.UpdateException;

@TestPropertySource("/application-test.properties")
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
	private KeycloakService keycloakService;
	
	@MockBean
	private RnippService rnippService;

	@Autowired
	private DoctorDAO doctorDAO;

	@Autowired
	private PatientFileDAO patientFileDAO;

	@Autowired
	private CorrespondenceDAO correspondenceDAO;

	@Autowired
	private PatientFileItemDAO patientFileItemDAO;

	@Autowired
	private MedicalActDAO medicalActDAO;

	@Autowired
	private PatientFileService patientFileService;

	@Autowired
	private AddressDTO addressDTO;

	@Autowired
	private PatientFileDTO patientFileDTO;

	@Autowired
	private PatientFileDTO patientFileDTOResponse;

	@Autowired
	private CorrespondenceDTO correspondenceDTO;

	@Autowired
	private DiseaseDTO diseaseDTO;

	@Autowired
	private MedicalActDTO medicalActDTO;

	@Autowired
	private ActDTO actDTO;

	@Autowired
	private DiagnosisDTO diagnosisDTO;

	@Autowired
	private MailDTO mailDTO;

	@Autowired
	private PrescriptionDTO prescriptionDTO;

	@Autowired
	private SymptomDTO symptomDTO;

	private PatientFileItemDTO patientFileItemDTOResponse;

	@Autowired
	private CorrespondenceDTO correspondenceDTOResponse;

	@Autowired
	private User user;

	@Autowired
	private Address address;

	@Autowired
	private Doctor doctor;

	@Autowired
	private Doctor authoringDoctor;

	@Autowired
	private Doctor recipientDoctor;

	@Autowired
	private PatientFile patientFile;

	@Autowired
	private PatientFile savedPatientFile;

	@Autowired
	private PatientFile persistentPatientFile;

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

	private long count;

	private UUID uuid;

	private String id;

	private String comment;

	private String text;

	private String description;

	@BeforeEach
	public void setup() {
		addressDTO.setStreet1("1 Rue Lecourbe");
		addressDTO.setZipcode("75015");
		addressDTO.setCity("Paris Cedex 15");
		addressDTO.setCountry("France-");
		patientFileDTO.setId("P002");
		patientFileDTO.setFirstname("Patrick");
		patientFileDTO.setLastname("Dubois");
		patientFileDTO.setDateOfBirth(LocalDate.of(2000, 2, 13));
		patientFileDTO.setPhone("9876543210");
		patientFileDTO.setEmail("patrick.dubois@mail.fr");
		patientFileDTO.setAddressDTO(addressDTO);
		patientFileDTO.setReferringDoctorId("D001");

		address.setStreet1("1 Rue Lecourbe");
		address.setZipcode("75015");
		address.setCity("Paris Cedex 15");
		address.setCountry("France-");
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

		correspondenceDTO.setDateUntil(LocalDate.now().plusDays(1));
		correspondenceDTO.setDoctorId("D002");
		correspondenceDTO.setPatientFileId("P001");

		comment = "A comment";
		text = "A text";
		description = "A description";

		user.setId("P002");
		user.setUsername("username");
		user.setPassword("password");
		user.setSecurityCode("code");
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

		assertTrue(patientFileDAO.existsById("P001"));

		patientFileDTOResponse = assertDoesNotThrow(() -> patientFileService.updatePatientFile(patientFileDTO));

		savedPatientFile = patientFileDAO.findById("P001").get();

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
		assertEquals(null, patientFileDTOResponse.getSecurityCode());
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

		persistentPatientFile = patientFileDAO.findById("P001").get();
		assertEquals("D001", persistentPatientFile.getReferringDoctor().getId());

		patientFileDTO.setReferringDoctorId("D002");

		patientFileDTOResponse = assertDoesNotThrow(() -> patientFileService.updateReferringDoctor(patientFileDTO));

		savedPatientFile = patientFileDAO.findById("P001").get();

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
	public void testCreateCorrespondenceSuccess() {

		count = correspondenceDAO.count();

		assertNull(correspondenceDTO.getId());

		doctor = doctorDAO.findById(correspondenceDTO.getDoctorId()).get();

		correspondenceDTOResponse = assertDoesNotThrow(
				() -> patientFileService.createCorrespondence(correspondenceDTO));

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

		correspondenceDTO.setPatientFileId("P002");

		count = correspondenceDAO.count();

		CreateException ex = assertThrows(CreateException.class,
				() -> patientFileService.createCorrespondence(correspondenceDTO));

		assertEquals("La correspondance n'a pas pu être créé.", ex.getMessage());

		assertEquals(count, correspondenceDAO.count());
	}

	@Test
	public void testCreateCorrespondenceFailureDoctorDoesNotExist() {

		correspondenceDTO.setDoctorId("D003");

		count = correspondenceDAO.count();

		CreateException ex = assertThrows(CreateException.class,
				() -> patientFileService.createCorrespondence(correspondenceDTO));

		assertEquals("La correspondance n'a pas pu être créé.", ex.getMessage());

		assertEquals(count, correspondenceDAO.count());
	}

	@Test
	public void testDeleteCorrespondenceSuccess() {

		uuid = UUID.fromString("3d80bbeb-997e-4354-82d3-68cea80256d6");

		count = correspondenceDAO.count();

		assertTrue(correspondenceDAO.existsById(uuid));

		patientFileService.deleteCorrespondence(uuid);

		assertFalse(correspondenceDAO.existsById(uuid));

		assertEquals(count - 1, correspondenceDAO.count());
	}

	@Test
	public void testFindCorrespondenceSuccess() {

		uuid = UUID.fromString("3d80bbeb-997e-4354-82d3-68cea80256d6");

		assertTrue(correspondenceDAO.existsById(uuid));

		CorrespondenceDTO correspondenceDTO = assertDoesNotThrow(
				() -> patientFileService.findCorrespondence(uuid.toString()));

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
				() -> patientFileService.findCorrespondence(uuid.toString()));

		assertEquals("Correspondance non trouvée.", ex.getMessage());
	}

	@Test
	public void testFindCorrespondencesByPatientFileIdFound3() {

		List<CorrespondenceDTO> correspondencesDTO = patientFileService.findCorrespondencesByPatientFileId("P001");

		assertEquals(3, correspondencesDTO.size());
		assertEquals("2023-05-02", correspondencesDTO.get(0).getDateUntil().toString());
		assertEquals("e1eb3425-d257-4c5e-8600-b125731c458c", correspondencesDTO.get(1).getId().toString());
		assertEquals("D011", correspondencesDTO.get(2).getDoctorId());
	}

	@Test
	public void testFindCorrespondencesByPatientFileIdFound0() {

		List<CorrespondenceDTO> correspondencesDTO = patientFileService.findCorrespondencesByPatientFileId("P055");

		assertEquals(0, correspondencesDTO.size());
	}

	@Test
	public void testFindDiseaseSuccess() {

		diseaseDTO = assertDoesNotThrow(() -> patientFileService.findDisease("J01"));

		assertEquals("J01", diseaseDTO.getId());
		assertEquals("Sinusite aiguë", diseaseDTO.getDescription());
	}

	@Test
	public void testFindDiseaseFailureDiseaseDoesNotExist() {

		FinderException ex = assertThrows(FinderException.class, () -> patientFileService.findDisease("J000"));

		assertEquals("Maladie non trouvée.", ex.getMessage());
	}

	@Test
	public void testFindMedicalActSuccess() {

		medicalActDTO = assertDoesNotThrow(() -> patientFileService.findMedicalAct("HCAE201"));

		assertEquals("HCAE201", medicalActDTO.getId());
		assertEquals("Dilatation de sténose du conduit d'une glande salivaire par endoscopie [sialendoscopie] ",
				medicalActDTO.getDescription());
	}

	@Test
	public void testFindMedicalActFailureMedicalActDoesNotExist() {

		FinderException ex = assertThrows(FinderException.class, () -> patientFileService.findMedicalAct("H000000"));

		assertEquals("Acte médical non trouvé.", ex.getMessage());
	}

	@Test
	public void testFindDiseasesByIdOrDescriptionFound8() {

		List<DiseaseDTO> diseasesDTO = patientFileService.findDiseasesByIdOrDescription("sinusite", 10);

		assertEquals(8, diseasesDTO.size());
	}

	@Test
	public void testFindDiseasesByIdOrDescriptionFound0() {

		List<DiseaseDTO> diseasesDTO = patientFileService.findDiseasesByIdOrDescription("mas", 10);

		assertEquals(0, diseasesDTO.size());
	}

	@Test
	public void testFindDiseasesByIdOrDescriptionSearchStringIsBlank() {

		List<DiseaseDTO> diseasesDTO = patientFileService.findDiseasesByIdOrDescription("", 10);

		assertEquals(0, diseasesDTO.size());
	}

	@Test
	public void testFindMedicalActsByIdOrDescriptionFound9() {

		List<MedicalActDTO> medicalActsDTO = patientFileService.findMedicalActsByIdOrDescription("radio", 10);

		assertEquals(9, medicalActsDTO.size());
	}

	@Test
	public void testFindMedicalActsByIdOrDescriptionFound0() {

		List<MedicalActDTO> medicalActsDTO = patientFileService.findMedicalActsByIdOrDescription("rid", 10);

		assertEquals(0, medicalActsDTO.size());
	}

	@Test
	public void testFindMedicalActsByIdOrDescriptionSearchStringIsBlank() {

		List<MedicalActDTO> medicalActsDTO = patientFileService.findMedicalActsByIdOrDescription("", 10);

		assertEquals(0, medicalActsDTO.size());
	}

	@Test
	public void testCreateMailSuccess() {

		count = patientFileItemDAO.count();

		LocalDate now = LocalDate.now();

		assertNull(mailDTO.getId());

		mailDTO.setDate(now);
		mailDTO.setComments("comments on this mail");
		mailDTO.setAuthoringDoctorId("D001");
		mailDTO.setPatientFileId("P001");
		mailDTO.setText("text of this mail");
		mailDTO.setRecipientDoctorId("D002");

		authoringDoctor = doctorDAO.findById(mailDTO.getAuthoringDoctorId()).get();
		recipientDoctor = doctorDAO.findById(mailDTO.getRecipientDoctorId()).get();

		PatientFileItemDTO patientFileItemDTOResponse = assertDoesNotThrow(
				() -> patientFileService.createPatientFileItem(mailDTO));

		assertEquals(count + 1, patientFileItemDAO.count());

		assertNotNull(patientFileItemDTOResponse.getId());
		assertEquals(now, patientFileItemDTOResponse.getDate());
		assertEquals(mailDTO.getComments(), patientFileItemDTOResponse.getComments());
		assertEquals(authoringDoctor.getId(), patientFileItemDTOResponse.getAuthoringDoctorId());
		assertEquals(authoringDoctor.getFirstname(), patientFileItemDTOResponse.getAuthoringDoctorFirstname());
		assertEquals(authoringDoctor.getLastname(), patientFileItemDTOResponse.getAuthoringDoctorLastname());
		assertEquals(mailDTO.getPatientFileId(), patientFileItemDTOResponse.getPatientFileId());
		assertTrue(patientFileItemDTOResponse instanceof MailDTO);
		assertEquals(mailDTO.getText(), ((MailDTO) patientFileItemDTOResponse).getText());
		assertEquals(recipientDoctor.getId(), ((MailDTO) patientFileItemDTOResponse).getRecipientDoctorId());
		assertEquals(recipientDoctor.getFirstname(),
				((MailDTO) patientFileItemDTOResponse).getRecipientDoctorFirstname());
		assertEquals(recipientDoctor.getLastname(),
				((MailDTO) patientFileItemDTOResponse).getRecipientDoctorLastname());
	}

	@Test
	public void testCreatePrescriptionFailureDoctorDoesNotExist() {

		prescriptionDTO.setAuthoringDoctorId("D003");

		count = patientFileItemDAO.count();

		CreateException ex = assertThrows(CreateException.class,
				() -> patientFileService.createPatientFileItem(prescriptionDTO));

		assertEquals("L'élément médical n'a pas pu être créé.", ex.getMessage());

		assertEquals(count, patientFileItemDAO.count());
	}

	@Test
	public void testUpdateActSuccess() {

		id = "HBQK389";

		uuid = UUID.fromString("1b57e70f-8eb0-4a97-99c6-5d44f138c22c");

		medicalActDTO.setId(id);

		actDTO.setId(uuid);
		actDTO.setDate(LocalDate.now());
		actDTO.setComments("comments on this act");
		actDTO.setAuthoringDoctorId("D001");
		actDTO.setPatientFileId("P001");
		actDTO.setMedicalActDTO(medicalActDTO);

		act = (Act) patientFileItemDAO.findById(uuid).get();

		assertNotEquals(comment, act.getComments());
		assertNotEquals(id, act.getMedicalAct().getId());

		actDTO.setComments(comment);

		patientFileItemDTOResponse = assertDoesNotThrow(() -> patientFileService.updatePatientFileItem(actDTO));

		act = (Act) patientFileItemDAO.findById(uuid).get();

		assertEquals(comment, act.getComments());
		assertEquals(id, act.getMedicalAct().getId());
		assertEquals(
				"Radiographie intrabuccale rétroalvéolaire et/ou rétrocoronaire d'un secteur de 1 à 3 dents contigües",
				act.getMedicalAct().getDescription());
		assertEquals(comment, patientFileItemDTOResponse.getComments());
		assertEquals(id, ((ActDTO) patientFileItemDTOResponse).getMedicalActDTO().getId());
		assertEquals(
				"Radiographie intrabuccale rétroalvéolaire et/ou rétrocoronaire d'un secteur de 1 à 3 dents contigües",
				((ActDTO) patientFileItemDTOResponse).getMedicalActDTO().getDescription());
	}

	@Test
	public void testUpdateActFailureMedicalActDoesNotExist() {

		id = "HBQK389Z"; // does not exist

		uuid = UUID.fromString("1b57e70f-8eb0-4a97-99c6-5d44f138c22c");

		medicalActDTO.setId(id);

		actDTO.setId(uuid);
		actDTO.setDate(LocalDate.now());
		actDTO.setComments("comments on this act");
		actDTO.setAuthoringDoctorId("D001");
		actDTO.setPatientFileId("P001");
		actDTO.setMedicalActDTO(medicalActDTO);
		actDTO.setComments(comment);

		assertFalse(medicalActDAO.existsById(id));

		UpdateException ex = assertThrows(UpdateException.class,
				() -> patientFileService.updatePatientFileItem(actDTO));

		assertEquals("L'élément médical n'a pas pu être modifié.", ex.getMessage());
	}

	@Test
	public void testUpdateActFailureActDoesNotExist() {

		id = "HBQK389";

		uuid = UUID.fromString("1b57e70f-8eb0-4a97-99c6-5d44f138c22"); // does not exist

		medicalActDTO.setId(id);

		actDTO.setId(uuid);
		actDTO.setDate(LocalDate.now());
		actDTO.setComments("comments on this act");
		actDTO.setAuthoringDoctorId("D001");
		actDTO.setPatientFileId("P001");
		actDTO.setMedicalActDTO(medicalActDTO);
		actDTO.setComments(comment);

		assertFalse(patientFileItemDAO.existsById(uuid));

		FinderException ex = assertThrows(FinderException.class,
				() -> patientFileService.updatePatientFileItem(actDTO));

		assertTrue(ex.getMessage().contains("Elément médical non trouvé."));
	}

	@Test
	public void testUpdateActFailureWrongType() {

		id = "HBQK389";

		uuid = UUID.fromString("707b71f1-0bbd-46ec-b79c-c9717bd6b2cd"); // type Diagnosis

		medicalActDTO.setId(id);

		actDTO.setId(uuid);
		actDTO.setDate(LocalDate.now());
		actDTO.setComments("comments on this act");
		actDTO.setAuthoringDoctorId("D001");
		actDTO.setPatientFileId("P001");
		actDTO.setMedicalActDTO(medicalActDTO);
		actDTO.setComments(comment);

		UpdateException ex = assertThrows(UpdateException.class,
				() -> patientFileService.updatePatientFileItem(actDTO));

		assertEquals("Le type de l'élément médical est incorrect.", ex.getMessage());
	}

	@Test
	public void testUpdateDiagnosisSuccess() {

		id = "J011";

		uuid = UUID.fromString("707b71f1-0bbd-46ec-b79c-c9717bd6b2cd");

		diseaseDTO.setId(id);

		diagnosisDTO.setId(uuid);
		diagnosisDTO.setDate(LocalDate.now());
		diagnosisDTO.setAuthoringDoctorId("D001");
		diagnosisDTO.setPatientFileId("P001");
		diagnosisDTO.setDiseaseDTO(diseaseDTO);

		diagnosis = (Diagnosis) patientFileItemDAO.findById(uuid).get();

		assertNotEquals(comment, diagnosis.getComments());
		assertNotEquals(id, diagnosis.getDisease().getId());

		diagnosisDTO.setComments(comment);

		patientFileItemDTOResponse = assertDoesNotThrow(() -> patientFileService.updatePatientFileItem(diagnosisDTO));

		diagnosis = (Diagnosis) patientFileItemDAO.findById(uuid).get();

		assertEquals(comment, diagnosis.getComments());
		assertEquals(id, diagnosis.getDisease().getId());
		assertEquals("Sinusite frontale aiguë", diagnosis.getDisease().getDescription());
		assertEquals(comment, patientFileItemDTOResponse.getComments());
		assertEquals(id, ((DiagnosisDTO) patientFileItemDTOResponse).getDiseaseDTO().getId());
		assertEquals("Sinusite frontale aiguë",
				((DiagnosisDTO) patientFileItemDTOResponse).getDiseaseDTO().getDescription());
	}

	@Test
	public void testUpdateDiagnosisFailureWrongType() {

		id = "J011";

		uuid = UUID.fromString("3ab3d311-585c-498e-aaca-728c00beb86e"); // type Mail

		diseaseDTO.setId(id);

		diagnosisDTO.setId(uuid);
		diagnosisDTO.setDate(LocalDate.now());
		diagnosisDTO.setAuthoringDoctorId("D001");
		diagnosisDTO.setPatientFileId("P001");
		diagnosisDTO.setDiseaseDTO(diseaseDTO);
		diagnosisDTO.setComments(comment);

		UpdateException ex = assertThrows(UpdateException.class,
				() -> patientFileService.updatePatientFileItem(diagnosisDTO));

		assertEquals("Le type de l'élément médical est incorrect.", ex.getMessage());
	}

	@Test
	public void testUpdateMailSuccess() {

		id = "D013";

		uuid = UUID.fromString("3ab3d311-585c-498e-aaca-728c00beb86e");

		mailDTO.setId(uuid);
		mailDTO.setDate(LocalDate.now());
		mailDTO.setPatientFileId("P001");

		mail = (Mail) patientFileItemDAO.findById(uuid).get();

		assertNotEquals(comment, mail.getComments());
		assertNotEquals(text, mail.getText());
		assertNotEquals(id, mail.getRecipientDoctor().getId());

		mailDTO.setComments(comment);
		mailDTO.setText(text);
		mailDTO.setRecipientDoctorId(id);

		patientFileItemDTOResponse = assertDoesNotThrow(() -> patientFileService.updatePatientFileItem(mailDTO));

		mail = (Mail) patientFileItemDAO.findById(uuid).get();

		assertEquals(comment, mail.getComments());
		assertEquals(id, mail.getRecipientDoctor().getId());
		assertEquals("Hansen", mail.getRecipientDoctor().getLastname());
		assertEquals(comment, patientFileItemDTOResponse.getComments());
		assertEquals(id, ((MailDTO) patientFileItemDTOResponse).getRecipientDoctorId());
		assertEquals("Hansen", ((MailDTO) patientFileItemDTOResponse).getRecipientDoctorLastname());
	}

	@Test
	public void testUpdateMailFailureWrongType() {

		id = "D013";

		uuid = UUID.fromString("31571533-a9d4-4b10-ac46-8afe0247e6cd"); // type Prescription

		mailDTO.setId(uuid);
		mailDTO.setDate(LocalDate.now());
		mailDTO.setPatientFileId("P001");
		mailDTO.setComments(comment);
		mailDTO.setText(text);
		mailDTO.setRecipientDoctorId(id);

		UpdateException ex = assertThrows(UpdateException.class,
				() -> patientFileService.updatePatientFileItem(mailDTO));

		assertEquals("Le type de l'élément médical est incorrect.", ex.getMessage());
	}

	@Test
	public void testUpdatePrescriptionSuccess() {

		uuid = UUID.fromString("31571533-a9d4-4b10-ac46-8afe0247e6cd");

		prescriptionDTO.setId(uuid);
		prescriptionDTO.setDate(LocalDate.now());
		prescriptionDTO.setPatientFileId("P001");

		prescription = (Prescription) patientFileItemDAO.findById(uuid).get();

		assertNotEquals(comment, prescription.getComments());
		assertNotEquals(description, prescription.getDescription());

		prescriptionDTO.setComments(comment);
		prescriptionDTO.setDescription(description);

		patientFileItemDTOResponse = assertDoesNotThrow(
				() -> patientFileService.updatePatientFileItem(prescriptionDTO));

		prescription = (Prescription) patientFileItemDAO.findById(uuid).get();

		assertEquals(comment, prescription.getComments());
		assertEquals(description, prescription.getDescription());
		assertEquals(comment, patientFileItemDTOResponse.getComments());
		assertEquals(description, ((PrescriptionDTO) patientFileItemDTOResponse).getDescription());
	}

	@Test
	public void testUpdatePrescriptionFailureWrongType() {

		uuid = UUID.fromString("142763cf-6eeb-47a5-b8f8-8ec85f0025c4"); // type Symptom

		prescriptionDTO.setId(uuid);
		prescriptionDTO.setDate(LocalDate.now());
		prescriptionDTO.setPatientFileId("P001");
		prescriptionDTO.setComments(comment);
		prescriptionDTO.setDescription(description);

		UpdateException ex = assertThrows(UpdateException.class,
				() -> patientFileService.updatePatientFileItem(prescriptionDTO));

		assertEquals("Le type de l'élément médical est incorrect.", ex.getMessage());
	}

	@Test
	public void testUpdateSymptomSuccess() {

		uuid = UUID.fromString("142763cf-6eeb-47a5-b8f8-8ec85f0025c4");

		symptomDTO.setId(uuid);
		symptomDTO.setDate(LocalDate.now());
		symptomDTO.setPatientFileId("P001");

		symptom = (Symptom) patientFileItemDAO.findById(uuid).get();

		assertNotEquals(comment, symptom.getComments());
		assertNotEquals(description, symptom.getDescription());

		symptomDTO.setComments(comment);
		symptomDTO.setDescription(description);

		patientFileItemDTOResponse = assertDoesNotThrow(() -> patientFileService.updatePatientFileItem(symptomDTO));

		symptom = (Symptom) patientFileItemDAO.findById(uuid).get();

		assertEquals(comment, symptom.getComments());
		assertEquals(description, symptom.getDescription());
		assertEquals(comment, patientFileItemDTOResponse.getComments());
		assertEquals(description, ((SymptomDTO) patientFileItemDTOResponse).getDescription());
	}

	@Test
	public void testUpdateSymptomFailureWrongType() {

		uuid = UUID.fromString("1b57e70f-8eb0-4a97-99c6-5d44f138c22c"); // type Act

		symptomDTO.setId(uuid);
		symptomDTO.setDate(LocalDate.now());
		symptomDTO.setPatientFileId("P001");
		symptomDTO.setComments(comment);
		symptomDTO.setDescription(description);

		UpdateException ex = assertThrows(UpdateException.class,
				() -> patientFileService.updatePatientFileItem(symptomDTO));

		assertEquals("Le type de l'élément médical est incorrect.", ex.getMessage());
	}

	@Test
	public void testDeletePatientFileItemSuccess() {

		uuid = UUID.fromString("83b2f235-f183-4144-8eb8-2e4cac07d434");

		count = patientFileItemDAO.count();

		assertTrue(patientFileItemDAO.existsById(uuid));

		patientFileService.deletePatientFileItem(uuid);

		assertFalse(patientFileItemDAO.existsById(uuid));

		assertEquals(count - 1, patientFileItemDAO.count());
	}

	@Test
	public void testFindPatientFileItemSuccess() {

		uuid = UUID.fromString("142763cf-6eeb-47a5-b8f8-8ec85f0025c4");

		patientFileItemDTOResponse = assertDoesNotThrow(() -> patientFileService.findPatientFileItem(uuid));

		assertTrue(patientFileItemDTOResponse instanceof SymptomDTO);

		symptomDTO = (SymptomDTO) patientFileItemDTOResponse;

		assertEquals(uuid, symptomDTO.getId());
		assertEquals("2022-05-07", symptomDTO.getDate().toString());
		assertEquals("Ut posuere quam in placerat gravida.", symptomDTO.getComments());
		assertEquals("D001", symptomDTO.getAuthoringDoctorId());
		assertEquals("John", symptomDTO.getAuthoringDoctorFirstname());
		assertEquals("Smith", symptomDTO.getAuthoringDoctorLastname());
		assertEquals("P013", symptomDTO.getPatientFileId());
		assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", symptomDTO.getDescription());
	}

	@Test
	public void testFindPatientFileItemFailurePatientFileItemDoesNotExist() {

		uuid = UUID.fromString("142763cf-6eeb-47a5-b8f8-8ec85f0025c");

		assertFalse(patientFileItemDAO.existsById(uuid));

		FinderException ex = assertThrows(FinderException.class, () -> patientFileService.findPatientFileItem(uuid));

		assertEquals("Elément médical non trouvé.", ex.getMessage());
	}

	@Test
	public void testFindPatientFileItemsByPatientFileIdFound10() {

		List<PatientFileItemDTO> patientFileItemsDTO = patientFileService.findPatientFileItemsByPatientFileId("P005");

		assertEquals(10, patientFileItemsDTO.size());
		assertTrue(patientFileItemsDTO.get(1) instanceof DiagnosisDTO);
	}

	@Test
	public void testFindPatientFileItemsByPatientFileIdFound0PatientFileHasNoItems() {

		List<PatientFileItemDTO> patientFileItemsDTO = patientFileService.findPatientFileItemsByPatientFileId("P014");

		assertEquals(0, patientFileItemsDTO.size());
	}

	@Test
	public void testFindPatientFileItemsByPatientFileIdFound0PatientFileDoesNotExist() {

		List<PatientFileItemDTO> patientFileItemsDTO = patientFileService.findPatientFileItemsByPatientFileId("P002");

		assertEquals(0, patientFileItemsDTO.size());
	}

	@Test
	public void testDeletePatientFileSuccessNoUser() {

		id = "P005";

		when(keycloakService.userExistsById(id)).thenReturn(false);

		assertTrue(patientFileDAO.existsById(id));

		List<CorrespondenceDTO> correspondenceDTOs = patientFileService.findCorrespondencesByPatientFileId(id);
		List<PatientFileItemDTO> patientFileItemDTOs = patientFileService.findPatientFileItemsByPatientFileId(id);

		assertEquals(1, correspondenceDTOs.size());
		assertEquals(10, patientFileItemDTOs.size());

		assertDoesNotThrow(() -> patientFileService.deletePatientFile(id));

		verify(keycloakService, times(1)).userExistsById(id);
		assertFalse(patientFileDAO.existsById(id));

		correspondenceDTOs = patientFileService.findCorrespondencesByPatientFileId(id);
		patientFileItemDTOs = patientFileService.findPatientFileItemsByPatientFileId(id);

		assertEquals(0, correspondenceDTOs.size());
		assertEquals(0, patientFileItemDTOs.size());
	}

	@Test
	public void testDeletePatientFileSuccessUserPresent() {

		id = "P005";

		when(keycloakService.userExistsById(id)).thenReturn(true);
		when(keycloakService.deleteUser(id)).thenReturn(HttpStatus.NO_CONTENT);

		user.setId(id);
		assertTrue(patientFileDAO.existsById(id));

		List<CorrespondenceDTO> correspondenceDTOs = patientFileService.findCorrespondencesByPatientFileId(id);
		List<PatientFileItemDTO> patientFileItemDTOs = patientFileService.findPatientFileItemsByPatientFileId(id);

		assertEquals(1, correspondenceDTOs.size());
		assertEquals(10, patientFileItemDTOs.size());

		assertDoesNotThrow(() -> patientFileService.deletePatientFile(id));

		verify(keycloakService, times(1)).userExistsById(id);
		verify(keycloakService, times(1)).deleteUser(id);
		assertFalse(patientFileDAO.existsById(id));

		correspondenceDTOs = patientFileService.findCorrespondencesByPatientFileId(id);
		patientFileItemDTOs = patientFileService.findPatientFileItemsByPatientFileId(id);

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
