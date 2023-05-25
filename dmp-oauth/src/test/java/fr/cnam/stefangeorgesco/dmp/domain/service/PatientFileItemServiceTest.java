package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dao.PatientFileItemDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.*;
import fr.cnam.stefangeorgesco.dmp.domain.model.*;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PatientFileItemServiceTest {

	@MockBean
	private PatientFileItemDAO patientFileItemDAO;

	@Autowired
	private PatientFileItemService patientFileItemService;

	private final ArgumentCaptor<PatientFileItem> patientFileItemCaptor = ArgumentCaptor.forClass(PatientFileItem.class);

	private ActDTO actDTO;

	private DiagnosisDTO diagnosisDTO;

	private MailDTO mailDTO;

	private PrescriptionDTO prescriptionDTO;

	private SymptomDTO symptomDTO;

	private Disease disease1;

	private Disease disease2;

	private MedicalAct medicalAct2;

	private Doctor doctor1;

	private Doctor doctor2;

	private PatientFile patientFile1;

	private PatientFile patientFile2;

	private Act act;

	private Diagnosis diagnosis;

	private Mail mail;

	private Prescription prescription;

	private Symptom symptom;

	private UUID uuid;

	private LocalDate date;

	@BeforeEach
	public void setup() {
		Address address = new Address();
		address.setStreet1("street 1");
		address.setZipcode("zipcode");
		address.setCity("City");
		address.setCountry("Country");

		Specialty specialty1 = new Specialty();
		specialty1.setId("S001");
		specialty1.setDescription("Specialty 1");
		Specialty specialty2 = new Specialty();
		specialty2.setId("S002");
		specialty2.setDescription("Specialty 2");

		disease1 = new Disease();
		disease1.setId("DIS001");
		disease1.setDescription("Disease 1");
		disease2 = new Disease();
		disease2.setId("DIS002");
		disease2.setDescription("Disease 2");

		MedicalAct medicalAct1 = new MedicalAct();
		medicalAct1.setId("MA001");
		medicalAct1.setDescription("Medical act 1");
		medicalAct2 = new MedicalAct();
		medicalAct2.setId("MA002");
		medicalAct2.setDescription("Medical act 2");

		doctor1 = new Doctor();
		doctor1.setId("D001");
		doctor2 = new Doctor();
		doctor2.setId("D002");
		doctor2.setFirstname("firstname");
		doctor2.setLastname("lastname");
		doctor2.setSpecialties(List.of(specialty1, specialty2));

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

		date = LocalDate.now();

		MedicalActDTO medicalActDTO = new MedicalActDTO();
		medicalActDTO.setId("MA001");

		actDTO = new ActDTO();
		actDTO.setDate(date);
		actDTO.setComments("act comment");
		actDTO.setAuthoringDoctorId("D001");
		actDTO.setPatientFileId("P001");
		actDTO.setMedicalActDTO(medicalActDTO);

		DiseaseDTO diseaseDTO = new DiseaseDTO();
		diseaseDTO.setId("DIS001");

		diagnosisDTO = new DiagnosisDTO();
		diagnosisDTO.setDate(date);
		diagnosisDTO.setComments("diagnosis comment");
		diagnosisDTO.setAuthoringDoctorId("D001");
		diagnosisDTO.setPatientFileId("P001");
		diagnosisDTO.setDiseaseDTO(diseaseDTO);

		mailDTO = new MailDTO();
		mailDTO.setDate(date);
		mailDTO.setComments("mail comment");
		mailDTO.setAuthoringDoctorId("D001");
		mailDTO.setPatientFileId("P001");
		mailDTO.setText("texte du message");
		mailDTO.setRecipientDoctorId("D002");

		prescriptionDTO = new PrescriptionDTO();
		prescriptionDTO.setDate(date);
		prescriptionDTO.setComments("prescription comment");
		prescriptionDTO.setAuthoringDoctorId("D001");
		prescriptionDTO.setPatientFileId("P001");
		prescriptionDTO.setDescription("prescription description");

		symptomDTO = new SymptomDTO();
		symptomDTO.setDate(date);
		symptomDTO.setComments("symptom comment");
		symptomDTO.setAuthoringDoctorId("D001");
		symptomDTO.setPatientFileId("P001");
		symptomDTO.setDescription("symptom description");

		uuid = UUID.randomUUID();

		act = new Act();
		act.setId(uuid);
		act.setDate(actDTO.getDate());
		act.setComments(actDTO.getComments());
		act.setAuthoringDoctor(doctor1);
		act.setPatientFile(patientFile1);
		act.setMedicalAct(medicalAct1);

		diagnosis = new Diagnosis();
		diagnosis.setId(uuid);
		diagnosis.setDate(actDTO.getDate());
		diagnosis.setComments(actDTO.getComments());
		diagnosis.setAuthoringDoctor(doctor1);
		diagnosis.setPatientFile(patientFile1);
		diagnosis.setDisease(disease1);

		mail = new Mail();
		mail.setId(uuid);
		mail.setDate(actDTO.getDate());
		mail.setComments(actDTO.getComments());
		mail.setAuthoringDoctor(doctor1);
		mail.setPatientFile(patientFile1);
		mail.setText("A text");
		mail.setRecipientDoctor(doctor2);

		prescription = new Prescription();
		prescription.setId(uuid);
		prescription.setDate(actDTO.getDate());
		prescription.setComments(actDTO.getComments());
		prescription.setAuthoringDoctor(doctor1);
		prescription.setPatientFile(patientFile1);
		prescription.setDescription("A prescription description");

		symptom = new Symptom();
		symptom.setId(uuid);
		symptom.setDate(actDTO.getDate());
		symptom.setComments(actDTO.getComments());
		symptom.setAuthoringDoctor(doctor1);
		symptom.setPatientFile(patientFile1);
		symptom.setDescription("A symptom description");
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

		PatientFileItemDTO patientFileItemDTO = assertDoesNotThrow(() -> patientFileItemService.findPatientFileItem(uuid));

		verify(patientFileItemDAO, times(1)).findById(uuid);

		assertTrue(patientFileItemDTO instanceof DiagnosisDTO);

		DiagnosisDTO diagnosisDTOResponse = (DiagnosisDTO) patientFileItemDTO;

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

		when(patientFileItemDAO.findById(uuid)).thenReturn(Optional.empty());

		FinderException ex = assertThrows(FinderException.class, () -> patientFileItemService.findPatientFileItem(uuid));

		verify(patientFileItemDAO, times(1)).findById(uuid);

		assertEquals("Elément médical non trouvé.", ex.getMessage());
	}

	@Test
	public void testCreateActSuccess() {

		patientFile1.setId("P001");

		when(patientFileItemDAO.save(patientFileItemCaptor.capture())).thenReturn(act);
		when(patientFileItemDAO.findById(uuid)).thenReturn(Optional.of(act));

		PatientFileItemDTO patientFileItemDTOResponse = assertDoesNotThrow(
				() -> patientFileItemService.createPatientFileItem(actDTO));

		verify(patientFileItemDAO, times(1)).save(any(Act.class));
		verify(patientFileItemDAO, times(1)).findById(uuid);

		PatientFileItem savedPatientFileItem = patientFileItemCaptor.getValue();

		assertNull(savedPatientFileItem.getId());
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
				() -> patientFileItemService.updatePatientFileItem(actDTO));

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
				() -> patientFileItemService.updatePatientFileItem(diagnosisDTO));

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
				() -> patientFileItemService.updatePatientFileItem(mailDTO));

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
				() -> patientFileItemService.updatePatientFileItem(prescriptionDTO));

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
				() -> patientFileItemService.updatePatientFileItem(symptomDTO));

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

		patientFileItemService.deletePatientFileItem(uuid);

		verify(patientFileItemDAO, times(1)).deleteById(uuid);
	}

	@Test
	public void testFindPatientFileItemsByPatientFileIdFound5() {

		when(patientFileItemDAO.findByPatientFileId("P001"))
				.thenReturn(List.of(act, diagnosis, mail, prescription, symptom));

		List<PatientFileItemDTO> patientFileItemsDTO = patientFileItemService.findPatientFileItemsByPatientFileId("P001");

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

		List<PatientFileItemDTO> patientFileItemsDTO = patientFileItemService.findPatientFileItemsByPatientFileId("P027");

		verify(patientFileItemDAO, times(1)).findByPatientFileId("P027");

		assertEquals(0, patientFileItemsDTO.size());
	}
}
