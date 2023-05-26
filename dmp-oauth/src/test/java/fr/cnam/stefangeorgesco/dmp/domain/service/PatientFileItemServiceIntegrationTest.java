package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dao.DoctorDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.MedicalActDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.PatientFileItemDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.*;
import fr.cnam.stefangeorgesco.dmp.domain.model.*;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CreateException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.UpdateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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
public class PatientFileItemServiceIntegrationTest {

	@Autowired
	private DoctorDAO doctorDAO;

	@Autowired
	private PatientFileItemDAO patientFileItemDAO;

	@Autowired
	private MedicalActDAO medicalActDAO;

	@Autowired
	private PatientFileItemService patientFileItemService;

	private DiseaseDTO diseaseDTO;

	private MedicalActDTO medicalActDTO;

	private ActDTO actDTO;

	private DiagnosisDTO diagnosisDTO;

	private MailDTO mailDTO;

	private PrescriptionDTO prescriptionDTO;

	private SymptomDTO symptomDTO;

	private PatientFileItemDTO patientFileItemDTOResponse;

	private long count;

	private UUID uuid;

	private String id;

	private String comment;

	private String description;

	@BeforeEach
	public void setup() {
		comment = "A comment";
		description = "A description";

		medicalActDTO = new MedicalActDTO();

		actDTO = new ActDTO();
		diagnosisDTO = new DiagnosisDTO();
		mailDTO = new MailDTO();
		prescriptionDTO = new PrescriptionDTO();
		symptomDTO = new SymptomDTO();
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

		Doctor authoringDoctor = doctorDAO.findById(mailDTO.getAuthoringDoctorId()).orElseThrow();
		Doctor recipientDoctor = doctorDAO.findById(mailDTO.getRecipientDoctorId()).orElseThrow();

		PatientFileItemDTO patientFileItemDTOResponse = assertDoesNotThrow(
				() -> patientFileItemService.createPatientFileItem(mailDTO));

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
				() -> patientFileItemService.createPatientFileItem(prescriptionDTO));

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

		Act act = (Act) patientFileItemDAO.findById(uuid).orElseThrow();

		assertNotEquals(comment, act.getComments());
		assertNotEquals(id, act.getMedicalAct().getId());

		actDTO.setComments(comment);

		patientFileItemDTOResponse = assertDoesNotThrow(() -> patientFileItemService.updatePatientFileItem(actDTO));

		act = (Act) patientFileItemDAO.findById(uuid).orElseThrow();

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
				() -> patientFileItemService.updatePatientFileItem(actDTO));

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
				() -> patientFileItemService.updatePatientFileItem(actDTO));

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
				() -> patientFileItemService.updatePatientFileItem(actDTO));

		assertEquals("Le type de l'élément médical est incorrect.", ex.getMessage());
	}

	@Test
	public void testUpdateDiagnosisSuccess() {

		diseaseDTO = new DiseaseDTO();
		id = "J011";

		uuid = UUID.fromString("707b71f1-0bbd-46ec-b79c-c9717bd6b2cd");

		diseaseDTO.setId(id);

		diagnosisDTO.setId(uuid);
		diagnosisDTO.setDate(LocalDate.now());
		diagnosisDTO.setAuthoringDoctorId("D001");
		diagnosisDTO.setPatientFileId("P001");
		diagnosisDTO.setDiseaseDTO(diseaseDTO);

		Diagnosis diagnosis = (Diagnosis) patientFileItemDAO.findById(uuid).orElseThrow();

		assertNotEquals(comment, diagnosis.getComments());
		assertNotEquals(id, diagnosis.getDisease().getId());

		diagnosisDTO.setComments(comment);

		patientFileItemDTOResponse = assertDoesNotThrow(() -> patientFileItemService.updatePatientFileItem(diagnosisDTO));

		diagnosis = (Diagnosis) patientFileItemDAO.findById(uuid).orElseThrow();

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

		diseaseDTO = new DiseaseDTO();
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
				() -> patientFileItemService.updatePatientFileItem(diagnosisDTO));

		assertEquals("Le type de l'élément médical est incorrect.", ex.getMessage());
	}

	@Test
	public void testUpdateMailSuccess() {

		String text = "A text";
		id = "D013";

		uuid = UUID.fromString("3ab3d311-585c-498e-aaca-728c00beb86e");

		mailDTO.setId(uuid);
		mailDTO.setDate(LocalDate.now());
		mailDTO.setPatientFileId("P001");

		Mail mail = (Mail) patientFileItemDAO.findById(uuid).orElseThrow();

		assertNotEquals(comment, mail.getComments());
		assertNotEquals(text, mail.getText());
		assertNotEquals(id, mail.getRecipientDoctor().getId());

		mailDTO.setComments(comment);
		mailDTO.setText(text);
		mailDTO.setRecipientDoctorId(id);

		patientFileItemDTOResponse = assertDoesNotThrow(() -> patientFileItemService.updatePatientFileItem(mailDTO));

		mail = (Mail) patientFileItemDAO.findById(uuid).orElseThrow();

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
		mailDTO.setText("A text");
		mailDTO.setRecipientDoctorId(id);

		UpdateException ex = assertThrows(UpdateException.class,
				() -> patientFileItemService.updatePatientFileItem(mailDTO));

		assertEquals("Le type de l'élément médical est incorrect.", ex.getMessage());
	}

	@Test
	public void testUpdatePrescriptionSuccess() {

		uuid = UUID.fromString("31571533-a9d4-4b10-ac46-8afe0247e6cd");

		prescriptionDTO.setId(uuid);
		prescriptionDTO.setDate(LocalDate.now());
		prescriptionDTO.setPatientFileId("P001");

		Prescription prescription = (Prescription) patientFileItemDAO.findById(uuid).orElseThrow();

		assertNotEquals(comment, prescription.getComments());
		assertNotEquals(description, prescription.getDescription());

		prescriptionDTO.setComments(comment);
		prescriptionDTO.setDescription(description);

		patientFileItemDTOResponse = assertDoesNotThrow(
				() -> patientFileItemService.updatePatientFileItem(prescriptionDTO));

		prescription = (Prescription) patientFileItemDAO.findById(uuid).orElseThrow();

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
				() -> patientFileItemService.updatePatientFileItem(prescriptionDTO));

		assertEquals("Le type de l'élément médical est incorrect.", ex.getMessage());
	}

	@Test
	public void testUpdateSymptomSuccess() {

		uuid = UUID.fromString("142763cf-6eeb-47a5-b8f8-8ec85f0025c4");

		symptomDTO.setId(uuid);
		symptomDTO.setDate(LocalDate.now());
		symptomDTO.setPatientFileId("P001");

		Symptom symptom = (Symptom) patientFileItemDAO.findById(uuid).orElseThrow();

		assertNotEquals(comment, symptom.getComments());
		assertNotEquals(description, symptom.getDescription());

		symptomDTO.setComments(comment);
		symptomDTO.setDescription(description);

		patientFileItemDTOResponse = assertDoesNotThrow(() -> patientFileItemService.updatePatientFileItem(symptomDTO));

		symptom = (Symptom) patientFileItemDAO.findById(uuid).orElseThrow();

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
				() -> patientFileItemService.updatePatientFileItem(symptomDTO));

		assertEquals("Le type de l'élément médical est incorrect.", ex.getMessage());
	}

	@Test
	public void testDeletePatientFileItemSuccess() {

		uuid = UUID.fromString("83b2f235-f183-4144-8eb8-2e4cac07d434");

		count = patientFileItemDAO.count();

		assertTrue(patientFileItemDAO.existsById(uuid));

		patientFileItemService.deletePatientFileItem(uuid);

		assertFalse(patientFileItemDAO.existsById(uuid));

		assertEquals(count - 1, patientFileItemDAO.count());
	}

	@Test
	public void testFindPatientFileItemSuccess() {

		uuid = UUID.fromString("142763cf-6eeb-47a5-b8f8-8ec85f0025c4");

		patientFileItemDTOResponse = assertDoesNotThrow(() -> patientFileItemService.findPatientFileItem(uuid));

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

		FinderException ex = assertThrows(FinderException.class, () -> patientFileItemService.findPatientFileItem(uuid));

		assertEquals("Elément médical non trouvé.", ex.getMessage());
	}

	@Test
	public void testFindPatientFileItemsByPatientFileIdFound10() {

		List<PatientFileItemDTO> patientFileItemsDTO = patientFileItemService.findPatientFileItemsByPatientFileId("P005");

		assertEquals(10, patientFileItemsDTO.size());
		assertTrue(patientFileItemsDTO.get(1) instanceof DiagnosisDTO);
	}

	@Test
	public void testFindPatientFileItemsByPatientFileIdFound0PatientFileHasNoItems() {

		List<PatientFileItemDTO> patientFileItemsDTO = patientFileItemService.findPatientFileItemsByPatientFileId("P014");

		assertEquals(0, patientFileItemsDTO.size());
	}

	@Test
	public void testFindPatientFileItemsByPatientFileIdFound0PatientFileDoesNotExist() {

		List<PatientFileItemDTO> patientFileItemsDTO = patientFileItemService.findPatientFileItemsByPatientFileId("P002");

		assertEquals(0, patientFileItemsDTO.size());
	}
}
