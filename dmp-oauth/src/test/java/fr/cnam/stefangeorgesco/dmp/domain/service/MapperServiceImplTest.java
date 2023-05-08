package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dto.*;
import fr.cnam.stefangeorgesco.dmp.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class MapperServiceImplTest {

	@Autowired
	private MapperServiceImpl mapperService;

	private Specialty specialty1;

	private Specialty specialty2;

	private Doctor doctor1;

	private Doctor doctor2;

	private PatientFile patientFile;

	private Disease disease;

	private MedicalAct medicalAct;

	private Act act;

	private Diagnosis diagnosis;

	private Mail mail;

	private Symptom symptom;

	private DiseaseDTO diseaseDTO;

	private MedicalActDTO medicalActDTO;

	private ActDTO actDTO;

	private DiagnosisDTO diagnosisDTO;

	private MailDTO mailDTO;

	private SymptomDTO symptomDTO;

	@BeforeEach
	public void setup() {
		Address address = new Address();
		address.setStreet1("1 Rue Lecourbe");
		address.setZipcode("75015");
		address.setCity("Paris");
		address.setCountry("France");

		specialty1 = new Specialty();
		specialty1.setId("S001");
		specialty1.setDescription("First specialty");
		specialty2 = new Specialty();
		specialty2.setId("S002");
		specialty2.setDescription("Second specialty");

		doctor1 = new Doctor();
		doctor1.setId("D001");
		doctor1.setFirstname("Patrick");
		doctor1.setLastname("Dubois");
		doctor1.setPhone("9876543210");
		doctor1.setEmail("patrick.dubois@mail.fr");
		doctor1.setAddress(address);
		doctor1.setSpecialties(List.of(specialty1, specialty2));

		doctor2 = new Doctor();

		patientFile = new PatientFile();
		patientFile.setId("P001");

		disease = new Disease();
		medicalAct = new MedicalAct();

		act = new Act();
		diagnosis = new Diagnosis();
		mail = new Mail();
		symptom = new Symptom();

		diseaseDTO = new DiseaseDTO();
		medicalActDTO = new MedicalActDTO();

		actDTO = new ActDTO();
		diagnosisDTO = new DiagnosisDTO();
		mailDTO = new MailDTO();
		symptomDTO = new SymptomDTO();
	}

	@Test
	public void testModelMapperMailDTO2Mail() {
		mailDTO.setId(UUID.randomUUID());
		mailDTO.setDate(LocalDate.of(2022, 7, 22));
		mailDTO.setComments("A comment");
		mailDTO.setAuthoringDoctorId("DOO1");
		mailDTO.setPatientFileId("P001");
		mailDTO.setText("mail text");
		mailDTO.setRecipientDoctorId("D002");

		mail = (Mail) mapperService.mapToEntity(mailDTO);

		assertEquals(mailDTO.getId(), mail.getId());
		assertEquals(mailDTO.getDate(), mail.getDate());
		assertEquals(mailDTO.getComments(), mail.getComments());
		assertEquals(mailDTO.getAuthoringDoctorId(), mail.getAuthoringDoctor().getId());
		assertEquals(mailDTO.getPatientFileId(), mail.getPatientFile().getId());
		assertEquals(mailDTO.getText(), mail.getText());
		assertEquals(mailDTO.getRecipientDoctorId(), mail.getRecipientDoctor().getId());
	}

	@Test
	public void testModelMapperMail2MailDTO() {
		doctor2.setId("D002");
		doctor2.setFirstname("firstname_2");
		doctor2.setLastname("lastname_2");
		doctor2.setSpecialties(List.of(specialty2, specialty1));
		mail.setId(UUID.randomUUID());
		mail.setDate(LocalDate.of(2022, 7, 22));
		mail.setComments("A comment");
		mail.setAuthoringDoctor(doctor1);
		mail.setPatientFile(patientFile);
		mail.setText("mail text");
		mail.setRecipientDoctor(doctor2);

		mailDTO = (MailDTO) mapperService.mapToDTO(mail);

		assertEquals(mail.getId(), mailDTO.getId());
		assertEquals(mail.getDate(), mailDTO.getDate());
		assertEquals(mail.getComments(), mailDTO.getComments());
		assertEquals(mail.getAuthoringDoctor().getId(), mailDTO.getAuthoringDoctorId());
		assertEquals(mail.getAuthoringDoctor().getFirstname(), mailDTO.getAuthoringDoctorFirstname());
		assertEquals(mail.getAuthoringDoctor().getLastname(), mailDTO.getAuthoringDoctorLastname());
		assertEquals(mail.getAuthoringDoctor().getSpecialties().stream().map(Specialty::getDescription)
				.collect(Collectors.toList()), mailDTO.getAuthoringDoctorSpecialties());
		assertEquals(mail.getPatientFile().getId(), mailDTO.getPatientFileId());
		assertEquals(mail.getText(), mailDTO.getText());
		assertEquals(mail.getRecipientDoctor().getId(), mailDTO.getRecipientDoctorId());
		assertEquals(mail.getRecipientDoctor().getFirstname(), mailDTO.getRecipientDoctorFirstname());
		assertEquals(mail.getRecipientDoctor().getLastname(), mailDTO.getRecipientDoctorLastname());
		assertEquals(mail.getRecipientDoctor().getSpecialties().stream().map(Specialty::getDescription)
				.collect(Collectors.toList()), mailDTO.getRecipientDoctorSpecialties());
	}

	@Test
	public void testModelMapperDiagnosisDTO2Diagnosis() {
		diseaseDTO.setId("DIS001");
		diseaseDTO.setDescription("A disease");
		diagnosisDTO.setId(UUID.randomUUID());
		diagnosisDTO.setDate(LocalDate.of(2022, 7, 22));
		diagnosisDTO.setComments("A comment");
		diagnosisDTO.setAuthoringDoctorId("DOO1");
		diagnosisDTO.setPatientFileId("P001");
		diagnosisDTO.setDiseaseDTO(diseaseDTO);

		diagnosis = (Diagnosis) mapperService.mapToEntity(diagnosisDTO);

		assertEquals(diagnosisDTO.getId(), diagnosis.getId());
		assertEquals(diagnosisDTO.getDate(), diagnosis.getDate());
		assertEquals(diagnosisDTO.getComments(), diagnosis.getComments());
		assertEquals(diagnosisDTO.getAuthoringDoctorId(), diagnosis.getAuthoringDoctor().getId());
		assertEquals(diagnosisDTO.getPatientFileId(), diagnosis.getPatientFile().getId());
		assertEquals(diagnosisDTO.getDiseaseDTO().getId(), diagnosis.getDisease().getId());
		assertEquals(diagnosisDTO.getDiseaseDTO().getDescription(), diagnosis.getDisease().getDescription());
	}

	@Test
	public void testModelMapperDiagnosis2DiagnosisDTO() {
		disease.setId("DIS001");
		disease.setDescription("A disease");
		diagnosis.setId(UUID.randomUUID());
		diagnosis.setDate(LocalDate.of(2022, 7, 22));
		diagnosis.setComments("A comment");
		diagnosis.setAuthoringDoctor(doctor1);
		diagnosis.setPatientFile(patientFile);
		diagnosis.setDisease(disease);

		diagnosisDTO = (DiagnosisDTO) mapperService.mapToDTO(diagnosis);

		assertEquals(diagnosis.getId(), diagnosisDTO.getId());
		assertEquals(diagnosis.getDate(), diagnosisDTO.getDate());
		assertEquals(diagnosis.getComments(), diagnosisDTO.getComments());
		assertEquals(diagnosis.getAuthoringDoctor().getId(), diagnosisDTO.getAuthoringDoctorId());
		assertEquals(diagnosis.getAuthoringDoctor().getFirstname(), diagnosisDTO.getAuthoringDoctorFirstname());
		assertEquals(diagnosis.getAuthoringDoctor().getLastname(), diagnosisDTO.getAuthoringDoctorLastname());
		assertEquals(diagnosis.getAuthoringDoctor().getSpecialties().stream().map(Specialty::getDescription)
				.collect(Collectors.toList()), diagnosisDTO.getAuthoringDoctorSpecialties());
		assertEquals(diagnosis.getPatientFile().getId(), diagnosisDTO.getPatientFileId());
		assertEquals(diagnosis.getDisease().getId(), diagnosisDTO.getDiseaseDTO().getId());
		assertEquals(diagnosis.getDisease().getDescription(), diagnosisDTO.getDiseaseDTO().getDescription());
	}

	@Test
	public void testModelMapperActDTO2Act() {
		medicalActDTO.setId("MA001");
		medicalActDTO.setDescription("A medical act");
		actDTO.setId(UUID.randomUUID());
		actDTO.setDate(LocalDate.of(2022, 7, 22));
		actDTO.setComments("A comment");
		actDTO.setAuthoringDoctorId("DOO1");
		actDTO.setPatientFileId("P001");
		actDTO.setMedicalActDTO(medicalActDTO);

		act = (Act) mapperService.mapToEntity(actDTO);

		assertEquals(actDTO.getId(), act.getId());
		assertEquals(actDTO.getDate(), act.getDate());
		assertEquals(actDTO.getComments(), act.getComments());
		assertEquals(actDTO.getAuthoringDoctorId(), act.getAuthoringDoctor().getId());
		assertEquals(actDTO.getPatientFileId(), act.getPatientFile().getId());
		assertEquals(actDTO.getMedicalActDTO().getId(), act.getMedicalAct().getId());
		assertEquals(actDTO.getMedicalActDTO().getDescription(), act.getMedicalAct().getDescription());
	}

	@Test
	public void testModelMapperAct2ActDTO() {
		medicalAct.setId("MA001");
		medicalAct.setDescription("A disease");
		act.setId(UUID.randomUUID());
		act.setDate(LocalDate.of(2022, 7, 22));
		act.setComments("A comment");
		act.setAuthoringDoctor(doctor1);
		act.setPatientFile(patientFile);
		act.setMedicalAct(medicalAct);

		actDTO = (ActDTO) mapperService.mapToDTO(act);

		assertEquals(act.getId(), actDTO.getId());
		assertEquals(act.getDate(), actDTO.getDate());
		assertEquals(act.getComments(), actDTO.getComments());
		assertEquals(act.getAuthoringDoctor().getId(), actDTO.getAuthoringDoctorId());
		assertEquals(act.getAuthoringDoctor().getFirstname(), actDTO.getAuthoringDoctorFirstname());
		assertEquals(act.getAuthoringDoctor().getLastname(), actDTO.getAuthoringDoctorLastname());
		assertEquals(act.getAuthoringDoctor().getSpecialties().stream().map(Specialty::getDescription)
				.collect(Collectors.toList()), actDTO.getAuthoringDoctorSpecialties());
		assertEquals(act.getPatientFile().getId(), actDTO.getPatientFileId());
		assertEquals(act.getMedicalAct().getId(), actDTO.getMedicalActDTO().getId());
		assertEquals(act.getMedicalAct().getDescription(), actDTO.getMedicalActDTO().getDescription());
	}

	@Test
	public void testModelMapperSymptomDTO2Symptom() {
		symptomDTO.setId(UUID.randomUUID());
		symptomDTO.setDate(LocalDate.of(2022, 7, 22));
		symptomDTO.setComments("A comment");
		symptomDTO.setAuthoringDoctorId("DOO1");
		symptomDTO.setPatientFileId("P001");
		symptomDTO.setDescription("Symptom description");

		symptom = (Symptom) mapperService.mapToEntity(symptomDTO);

		assertEquals(symptomDTO.getId(), symptom.getId());
		assertEquals(symptomDTO.getDate(), symptom.getDate());
		assertEquals(symptomDTO.getComments(), symptom.getComments());
		assertEquals(symptomDTO.getAuthoringDoctorId(), symptom.getAuthoringDoctor().getId());
		assertEquals(symptomDTO.getPatientFileId(), symptom.getPatientFile().getId());
		assertEquals(symptomDTO.getDescription(), symptom.getDescription());
	}

	@Test
	public void testModelMapperSymptom2SymptomDTO() {
		symptom.setId(UUID.randomUUID());
		symptom.setDate(LocalDate.of(2022, 7, 22));
		symptom.setComments("A comment");
		symptom.setAuthoringDoctor(doctor1);
		symptom.setPatientFile(patientFile);
		symptom.setDescription("Symptom description");

		symptomDTO = (SymptomDTO) mapperService.mapToDTO(symptom);

		assertEquals(symptom.getId(), symptomDTO.getId());
		assertEquals(symptom.getDate(), symptomDTO.getDate());
		assertEquals(symptom.getComments(), symptomDTO.getComments());
		assertEquals(symptom.getAuthoringDoctor().getId(), symptomDTO.getAuthoringDoctorId());
		assertEquals(symptom.getAuthoringDoctor().getFirstname(), symptomDTO.getAuthoringDoctorFirstname());
		assertEquals(symptom.getAuthoringDoctor().getLastname(), symptomDTO.getAuthoringDoctorLastname());
		assertEquals(symptom.getAuthoringDoctor().getSpecialties().stream().map(Specialty::getDescription)
				.collect(Collectors.toList()), symptomDTO.getAuthoringDoctorSpecialties());
		assertEquals(symptom.getPatientFile().getId(), symptomDTO.getPatientFileId());
		assertEquals(symptom.getDescription(), symptomDTO.getDescription());
	}

}
