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
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import fr.cnam.stefangeorgesco.dmp.domain.model.Act;
import fr.cnam.stefangeorgesco.dmp.domain.model.Diagnosis;
import fr.cnam.stefangeorgesco.dmp.domain.model.Disease;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.Mail;
import fr.cnam.stefangeorgesco.dmp.domain.model.MedicalAct;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFileItem;
import fr.cnam.stefangeorgesco.dmp.domain.model.Prescription;
import fr.cnam.stefangeorgesco.dmp.domain.model.Symptom;

@TestPropertySource("/application-test.properties")
@SpringBootTest
@SqlGroup({ @Sql(scripts = "/sql/create-specialties.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-files.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-diseases.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-medical-acts.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-patient-file-items.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-patient-file-items.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-diseases.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-medical-acts.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-files.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-specialties.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class PatientFileItemDAOTest {

	@Autowired
	private PatientFileItemDAO patientFileItemDAO;

	@Autowired
	private Doctor authoringDoctor;

	@Autowired
	private Doctor recipientDoctor;

	@Autowired
	private PatientFile patientFile;

	@Autowired
	private MedicalAct medicalAct;

	@Autowired
	private Act act;

	private PatientFileItem savedPatientFileItem;

	@Autowired
	private Disease disease;

	@Autowired
	private Diagnosis diagnosis;

	@Autowired
	private Mail mail;

	@Autowired
	private Prescription prescription;

	@Autowired
	private Symptom symptom;

	private LocalDate date;

	private long count;

	private String comment;

	private String text;

	private String description;

	private String id;

	private UUID uuid;

	@BeforeEach
	public void setUp() {
		authoringDoctor.setId("D001");
		recipientDoctor.setId("D002");
		patientFile.setId("P001");
		date = LocalDate.now();

		medicalAct.setId("HBSD001");
		act.setDate(date);
		act.setComments("a comment on this act");
		act.setAuthoringDoctor(authoringDoctor);
		act.setPatientFile(patientFile);
		act.setMedicalAct(medicalAct);

		disease.setId("J019");
		diagnosis.setDate(date);
		diagnosis.setComments("a comment on this diagnosis");
		diagnosis.setAuthoringDoctor(authoringDoctor);
		diagnosis.setPatientFile(patientFile);
		diagnosis.setDisease(disease);

		mail.setDate(date);
		mail.setComments("a comment on this mail");
		mail.setAuthoringDoctor(authoringDoctor);
		mail.setPatientFile(patientFile);
		mail.setText("This is a mail to doctor...");
		mail.setRecipientDoctor(recipientDoctor);

		prescription.setDate(date);
		prescription.setComments("a comment on this prescription");
		prescription.setAuthoringDoctor(authoringDoctor);
		prescription.setPatientFile(patientFile);
		prescription.setDescription("this prescription...");

		symptom.setDate(date);
		symptom.setComments("a comment on this prescription");
		symptom.setAuthoringDoctor(authoringDoctor);
		symptom.setPatientFile(patientFile);
		symptom.setDescription("this prescription...");

		comment = "un commentaire";
		text = "texte du message";
		description = "description de l'élément";
	}

	@Test
	public void testPatientFileItemDAOSaveCreateActSuccess() {

		count = patientFileItemDAO.count();

		assertDoesNotThrow(() -> patientFileItemDAO.save(act));

		assertEquals(count + 1, patientFileItemDAO.count());
	}

	@Test
	public void testPatientFileItemDAOSaveCreateDiagnosisSuccess() {

		count = patientFileItemDAO.count();

		assertDoesNotThrow(() -> patientFileItemDAO.save(diagnosis));

		assertEquals(count + 1, patientFileItemDAO.count());
	}

	@Test
	public void testPatientFileItemDAOSaveCreateMailSuccess() {

		count = patientFileItemDAO.count();

		assertDoesNotThrow(() -> patientFileItemDAO.save(mail));

		assertEquals(count + 1, patientFileItemDAO.count());
	}

	@Test
	public void testPatientFileItemDAOSaveCreatePrescriptionSuccess() {

		count = patientFileItemDAO.count();

		assertDoesNotThrow(() -> patientFileItemDAO.save(prescription));

		assertEquals(count + 1, patientFileItemDAO.count());
	}

	@Test
	public void testPatientFileItemDAOSaveCreateSymptomSuccess() {

		count = patientFileItemDAO.count();

		assertDoesNotThrow(() -> patientFileItemDAO.save(symptom));

		assertEquals(count + 1, patientFileItemDAO.count());
	}

	@Test
	public void testPatientFileItemDAOSaveCreateActFailureMedicalActDoesNotExist() {

		medicalAct.setId("ID");

		count = patientFileItemDAO.count();

		assertThrows(RuntimeException.class, () -> patientFileItemDAO.save(act));

		assertEquals(count, patientFileItemDAO.count());
	}

	@Test
	public void testPatientFileItemDAOSaveCreateMailFailureRecipientDoctorDoesNotExist() {

		recipientDoctor.setId("ID");

		count = patientFileItemDAO.count();

		assertThrows(RuntimeException.class, () -> patientFileItemDAO.save(mail));

		assertEquals(count, patientFileItemDAO.count());
	}

	@Test
	public void testPatientFileItemDAOSaveCreatePrescriptionFailureAuthoringDoctorDoesNotExist() {

		authoringDoctor.setId("ID");

		count = patientFileItemDAO.count();

		assertThrows(RuntimeException.class, () -> patientFileItemDAO.save(prescription));

		assertEquals(count, patientFileItemDAO.count());
	}

	@Test
	public void testPatientFileItemDAOSaveCreateSymptomFailurePatientFileDoesNotExist() {

		patientFile.setId("ID");

		count = patientFileItemDAO.count();

		assertThrows(RuntimeException.class, () -> patientFileItemDAO.save(symptom));

		assertEquals(count, patientFileItemDAO.count());
	}

	@Test
	public void testPatientFileItemDAOSaveUpdateActSuccess() {

		id = "HBQK389";

		act = (Act) patientFileItemDAO.findById(UUID.fromString("1b57e70f-8eb0-4a97-99c6-5d44f138c22c")).get();

		assertNotEquals(comment, act.getComments());
		assertNotEquals(id, act.getMedicalAct().getId());

		act.setComments(comment);
		act.getMedicalAct().setId(id);

		savedPatientFileItem = assertDoesNotThrow(() -> patientFileItemDAO.save(act));

		assertEquals(comment, savedPatientFileItem.getComments());
		assertEquals(id, ((Act) savedPatientFileItem).getMedicalAct().getId());
		assertEquals(
				"Radiographie intrabuccale rétroalvéolaire et/ou rétrocoronaire d'un secteur de 1 à 3 dents contigües",
				((Act) savedPatientFileItem).getMedicalAct().getDescription());
	}

	@Test
	public void testPatientFileItemDAOSaveUpdateDiagnosisSuccess() {

		id = "J011";

		diagnosis = (Diagnosis) patientFileItemDAO.findById(UUID.fromString("707b71f1-0bbd-46ec-b79c-c9717bd6b2cd"))
				.get();

		assertNotEquals(comment, diagnosis.getComments());
		assertNotEquals(id, diagnosis.getDisease().getId());

		diagnosis.setComments(comment);
		diagnosis.getDisease().setId(id);

		savedPatientFileItem = assertDoesNotThrow(() -> patientFileItemDAO.save(diagnosis));

		assertEquals(comment, savedPatientFileItem.getComments());
		assertEquals(id, ((Diagnosis) savedPatientFileItem).getDisease().getId());
		assertEquals("Sinusite frontale aiguë", ((Diagnosis) savedPatientFileItem).getDisease().getDescription());
	}

	@Test
	public void testPatientFileItemDAOSaveUpdateMailSuccess() {

		id = "D013";

		mail = (Mail) patientFileItemDAO.findById(UUID.fromString("3ab3d311-585c-498e-aaca-728c00beb86e")).get();

		assertNotEquals(comment, mail.getComments());
		assertNotEquals(text, mail.getText());
		assertNotEquals(id, mail.getRecipientDoctor().getId());

		mail.setComments(comment);
		mail.setText(text);
		mail.getRecipientDoctor().setId(id);

		savedPatientFileItem = assertDoesNotThrow(() -> patientFileItemDAO.save(mail));

		assertEquals(comment, savedPatientFileItem.getComments());
		assertEquals(text, ((Mail) savedPatientFileItem).getText());
		assertEquals(id, ((Mail) savedPatientFileItem).getRecipientDoctor().getId());
	}

	@Test
	public void testPatientFileItemDAOSaveUpdatePrescriptionSuccess() {

		prescription = (Prescription) patientFileItemDAO
				.findById(UUID.fromString("31571533-a9d4-4b10-ac46-8afe0247e6cd")).get();

		assertNotEquals(comment, prescription.getComments());
		assertNotEquals(description, prescription.getDescription());

		prescription.setComments(comment);
		prescription.setDescription(description);

		savedPatientFileItem = assertDoesNotThrow(() -> patientFileItemDAO.save(prescription));

		assertEquals(comment, savedPatientFileItem.getComments());
		assertEquals(description, ((Prescription) savedPatientFileItem).getDescription());
	}

	@Test
	public void testPatientFileItemDAOSaveUpdateSymptomSuccess() {

		symptom = (Symptom) patientFileItemDAO.findById(UUID.fromString("142763cf-6eeb-47a5-b8f8-8ec85f0025c4")).get();

		assertNotEquals(comment, symptom.getComments());
		assertNotEquals(description, symptom.getDescription());

		symptom.setComments(comment);
		symptom.setDescription(description);

		savedPatientFileItem = assertDoesNotThrow(() -> patientFileItemDAO.save(symptom));

		assertEquals(comment, savedPatientFileItem.getComments());
		assertEquals(description, ((Symptom) savedPatientFileItem).getDescription());
	}

	@Test
	public void testPatientFileItemDAODeleteByIdSuccess() {

		uuid = UUID.fromString("c793da7f-5ca8-41f5-a0f0-1cc77b34b6fe");

		count = patientFileItemDAO.count();

		assertTrue(patientFileItemDAO.existsById(uuid));

		patientFileItemDAO.deleteById(uuid);

		assertFalse(patientFileItemDAO.existsById(uuid));

		assertEquals(count - 1, patientFileItemDAO.count());
	}

	@Test
	public void testPatientFileItemDAOFindByPatientFileIdFound10() {

		List<PatientFileItem> patientFileItemsList = new ArrayList<>();

		Iterable<PatientFileItem> patientFileItems = patientFileItemDAO.findByPatientFileId("P005");

		patientFileItems.forEach(patientFileItemsList::add);

		assertEquals(10, patientFileItemsList.size());
		assertTrue(patientFileItemsList.get(0) instanceof Act);
		assertTrue(patientFileItemsList.get(9) instanceof Symptom);
	}

	@Test
	public void testPatientFileItemDAODeleteAllByPatientFileIdSuccessDeleted9() {

		List<PatientFileItem> patientFileItemsList = new ArrayList<>();

		Iterable<PatientFileItem> patientFileItems = patientFileItemDAO.findByPatientFileId("P001");

		patientFileItems.forEach(patientFileItemsList::add);

		assertEquals(9, patientFileItemsList.size());

		int number = patientFileItemDAO.deleteAllByPatientFileId("P001");

		assertEquals(9, number);

		patientFileItemsList = new ArrayList<>();

		patientFileItems = patientFileItemDAO.findByPatientFileId("P001");

		patientFileItems.forEach(patientFileItemsList::add);

		assertEquals(0, patientFileItemsList.size());
	}

	@Test
	public void testPatientFileItemDAODeleteAllByPatientFileIdSuccessDeleted0() {

		List<PatientFileItem> patientFileItemsList = new ArrayList<>();

		Iterable<PatientFileItem> patientFileItems = patientFileItemDAO.findByPatientFileId("P014");

		patientFileItems.forEach(patientFileItemsList::add);

		assertEquals(0, patientFileItemsList.size());

		int number = patientFileItemDAO.deleteAllByPatientFileId("P014");

		assertEquals(0, number);
	}
}
