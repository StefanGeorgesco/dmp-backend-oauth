package fr.cnam.stefangeorgesco.dmp.configuration;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import fr.cnam.stefangeorgesco.dmp.domain.dto.SymptomDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;
import fr.cnam.stefangeorgesco.dmp.domain.dto.ActDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.AddressDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondenceDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DiagnosisDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DiseaseDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.MailDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.MedicalActDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Symptom;
import fr.cnam.stefangeorgesco.dmp.domain.model.Act;
import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Correspondence;
import fr.cnam.stefangeorgesco.dmp.domain.model.Diagnosis;
import fr.cnam.stefangeorgesco.dmp.domain.model.Disease;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.Mail;
import fr.cnam.stefangeorgesco.dmp.domain.model.MedicalAct;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class ModelMapperTest {

	@Autowired
	private User user;

	@Autowired
	private Address address;

	@Autowired
	private Specialty specialty1;

	@Autowired
	private Specialty specialty2;

	@Autowired
	private Doctor doctor1;

	@Autowired
	private Doctor doctor2;

	@Autowired
	private PatientFile patientFile;

	@Autowired
	private Correspondence correspondence;

	@Autowired
	private Mail mail;

	@Autowired
	private Disease disease;

	@Autowired
	private Diagnosis diagnosis;

	@Autowired
	private MedicalAct medicalAct;

	@Autowired
	private Act act;

	@Autowired
	private Symptom symptom;

	@Autowired
	private UserDTO userDTO;

	@Autowired
	private AddressDTO addressDTO;

	@Autowired
	private SpecialtyDTO specialtyDTO1;

	@Autowired
	private SpecialtyDTO specialtyDTO2;

	@Autowired
	private DoctorDTO doctorDTO;

	@Autowired
	private PatientFileDTO patientFileDTO;

	@Autowired
	private CorrespondenceDTO correspondenceDTO;

	@Autowired
	private MailDTO mailDTO;

	@Autowired
	private DiseaseDTO diseaseDTO;

	@Autowired
	private DiagnosisDTO diagnosisDTO;

	@Autowired
	private MedicalActDTO medicalActDTO;

	@Autowired
	private ActDTO actDTO;

	@Autowired
	private SymptomDTO symptomDTO;

	@Autowired
	private ModelMapper commonModelMapper;

	@Autowired
	private ModelMapper userModelMapper;

	@Autowired
	private ModelMapper doctorModelMapper;

	@Autowired
	private ModelMapper patientFileModelMapper;

	@Autowired
	private ModelMapper diagnosisModelMapper;

	@Autowired
	private ModelMapper actModelMapper;

	@BeforeEach
	public void setup() {
		addressDTO.setStreet1("1 Rue Lecourbe");
		addressDTO.setZipcode("75015");
		addressDTO.setCity("Paris");
		addressDTO.setCountry("France");
		specialtyDTO1.setId("S001");
		specialtyDTO1.setDescription("First specialty");
		specialtyDTO2.setId("S002");
		specialtyDTO2.setDescription("Second specialty");
		doctorDTO.setId("P001");
		doctorDTO.setFirstname("Patrick");
		doctorDTO.setLastname("Dubois");
		doctorDTO.setPhone("9876543210");
		doctorDTO.setEmail("patrick.dubois@mail.fr");
		doctorDTO.setAddressDTO(addressDTO);
		doctorDTO.setSpecialtiesDTO(List.of(specialtyDTO1, specialtyDTO2));
		doctorDTO.setSecurityCode("code");

		address.setStreet1("1 Rue Lecourbe");
		address.setZipcode("75015");
		address.setCity("Paris");
		address.setCountry("France");
		specialty1.setId("S001");
		specialty1.setDescription("First specialty");
		specialty2.setId("S002");
		specialty2.setDescription("Second specialty");
		doctor1.setId("D001");
		doctor1.setFirstname("Patrick");
		doctor1.setLastname("Dubois");
		doctor1.setPhone("9876543210");
		doctor1.setEmail("patrick.dubois@mail.fr");
		doctor1.setAddress(address);
		doctor1.setSpecialties(List.of(specialty1, specialty2));
		patientFile.setId("P001");
	}

	@Test
	public void testModelMapperUserDTO2User() {
		userDTO.setId("userId");
		userDTO.setUsername("username");
		userDTO.setPassword("password");
		userDTO.setSecurityCode("code");

		user = commonModelMapper.map(userDTO, User.class);

		assertEquals(userDTO.getId(), user.getId());
		assertEquals(userDTO.getUsername(), user.getUsername());
		assertEquals(userDTO.getPassword(), user.getPassword());
		assertEquals(userDTO.getSecurityCode(), user.getSecurityCode());
	}

	@Test
	public void testModelMapperUser2UserDTO() {
		user.setId("userId");
		user.setUsername("username");
		user.setPassword("password");
		user.setSecurityCode("code");

		userDTO = userModelMapper.map(user, UserDTO.class);

		assertEquals(user.getId(), userDTO.getId());
		assertEquals(user.getUsername(), userDTO.getUsername());
		assertNull(userDTO.getPassword());
		assertNull(userDTO.getSecurityCode());
	}

	@Test
	public void testModelMapperDoctorDTO2Doctor() {

		doctor1 = commonModelMapper.map(doctorDTO, Doctor.class);

		assertEquals(doctorDTO.getAddressDTO().getStreet1(), doctor1.getAddress().getStreet1());
		assertEquals(doctorDTO.getAddressDTO().getZipcode(), doctor1.getAddress().getZipcode());
		assertEquals(doctorDTO.getAddressDTO().getCity(), doctor1.getAddress().getCity());
		assertEquals(doctorDTO.getAddressDTO().getCountry(), doctor1.getAddress().getCountry());
		assertEquals(doctorDTO.getId(), doctor1.getId());
		assertEquals(doctorDTO.getFirstname(), doctor1.getFirstname());
		assertEquals(doctorDTO.getLastname(), doctor1.getLastname());
		assertEquals(doctorDTO.getEmail(), doctor1.getEmail());
		assertEquals(doctorDTO.getSpecialtiesDTO().size(), doctor1.getSpecialties().size());
		Iterator<SpecialtyDTO> itDTO = doctorDTO.getSpecialtiesDTO().iterator();
		Iterator<Specialty> it = doctor1.getSpecialties().iterator();
		SpecialtyDTO spDTO = itDTO.next();
		Specialty sp = it.next();
		assertEquals(spDTO.getId(), sp.getId());
		assertEquals(spDTO.getDescription(), sp.getDescription());
		spDTO = itDTO.next();
		sp = it.next();
		assertEquals(spDTO.getId(), sp.getId());
		assertEquals(spDTO.getDescription(), sp.getDescription());
		assertEquals(doctorDTO.getSecurityCode(), doctor1.getSecurityCode());
	}

	@Test
	public void testModelMapperDoctor2DoctorDTO() {

		doctorDTO = doctorModelMapper.map(doctor1, DoctorDTO.class);

		assertEquals(doctor1.getAddress().getStreet1(), doctorDTO.getAddressDTO().getStreet1());
		assertEquals(doctor1.getAddress().getZipcode(), doctorDTO.getAddressDTO().getZipcode());
		assertEquals(doctor1.getAddress().getCity(), doctorDTO.getAddressDTO().getCity());
		assertEquals(doctor1.getAddress().getCountry(), doctorDTO.getAddressDTO().getCountry());
		assertEquals(doctor1.getId(), doctorDTO.getId());
		assertEquals(doctor1.getFirstname(), doctorDTO.getFirstname());
		assertEquals(doctor1.getLastname(), doctorDTO.getLastname());
		assertEquals(doctor1.getEmail(), doctorDTO.getEmail());
		assertEquals(doctor1.getSpecialties().size(), doctorDTO.getSpecialtiesDTO().size());
		Iterator<Specialty> it = doctor1.getSpecialties().iterator();
		Iterator<SpecialtyDTO> itDTO = doctorDTO.getSpecialtiesDTO().iterator();
		Specialty sp = it.next();
		SpecialtyDTO spDTO = itDTO.next();
		assertEquals(sp.getId(), spDTO.getId());
		assertEquals(sp.getDescription(), spDTO.getDescription());
		sp = it.next();
		spDTO = itDTO.next();
		assertEquals(sp.getId(), spDTO.getId());
		assertEquals(sp.getDescription(), spDTO.getDescription());
		assertNull(doctorDTO.getSecurityCode());
	}

	@Test
	public void testModelMapperPatientFileDTO2PatientFile() {
		patientFileDTO.setId("P001");
		patientFileDTO.setFirstname("Patrick");
		patientFileDTO.setLastname("Dubois");
		patientFileDTO.setDateOfBirth(LocalDate.of(2000, 2, 13));
		patientFileDTO.setPhone("9876543210");
		patientFileDTO.setEmail("patrick.dubois@mail.fr");
		patientFileDTO.setAddressDTO(addressDTO);
		patientFileDTO.setReferringDoctorId("D001");
		patientFileDTO.setSecurityCode("code");

		patientFile = commonModelMapper.map(patientFileDTO, PatientFile.class);

		assertEquals(patientFileDTO.getId(), patientFile.getId());
		assertEquals(patientFileDTO.getFirstname(), patientFile.getFirstname());
		assertEquals(patientFileDTO.getLastname(), patientFile.getLastname());
		assertEquals(patientFileDTO.getDateOfBirth(), patientFile.getDateOfBirth());
		assertEquals(patientFileDTO.getPhone(), patientFile.getPhone());
		assertEquals(patientFileDTO.getEmail(), patientFile.getEmail());
		assertEquals(patientFileDTO.getAddressDTO().getStreet1(), patientFile.getAddress().getStreet1());
		assertEquals(patientFileDTO.getAddressDTO().getZipcode(), patientFile.getAddress().getZipcode());
		assertEquals(patientFileDTO.getAddressDTO().getCity(), patientFile.getAddress().getCity());
		assertEquals(patientFileDTO.getAddressDTO().getCountry(), patientFile.getAddress().getCountry());
		assertEquals(patientFileDTO.getReferringDoctorId(), patientFile.getReferringDoctor().getId());
		assertEquals(patientFileDTO.getSecurityCode(), patientFile.getSecurityCode());
	}

	@Test
	public void testModelMapperPatientFile2PatientFileDTO() {
		patientFile.setFirstname("Patrick");
		patientFile.setLastname("Dubois");
		patientFile.setDateOfBirth(LocalDate.of(2000, 2, 13));
		patientFile.setPhone("9876543210");
		patientFile.setEmail("patrick.dubois@mail.fr");
		patientFile.setAddress(address);
		patientFile.setSecurityCode("code");
		patientFile.setReferringDoctor(doctor1);

		patientFileDTO = patientFileModelMapper.map(patientFile, PatientFileDTO.class);

		assertEquals(patientFile.getId(), patientFileDTO.getId());
		assertEquals(patientFile.getFirstname(), patientFileDTO.getFirstname());
		assertEquals(patientFile.getLastname(), patientFileDTO.getLastname());
		assertEquals(patientFile.getDateOfBirth(), patientFileDTO.getDateOfBirth());
		assertEquals(patientFile.getPhone(), patientFileDTO.getPhone());
		assertEquals(patientFile.getEmail(), patientFileDTO.getEmail());
		assertEquals(patientFile.getAddress().getStreet1(), patientFileDTO.getAddressDTO().getStreet1());
		assertEquals(patientFile.getAddress().getZipcode(), patientFileDTO.getAddressDTO().getZipcode());
		assertEquals(patientFile.getAddress().getCity(), patientFileDTO.getAddressDTO().getCity());
		assertEquals(patientFile.getAddress().getCountry(), patientFileDTO.getAddressDTO().getCountry());
		assertEquals(patientFile.getReferringDoctor().getId(), patientFileDTO.getReferringDoctorId());
		assertEquals(patientFile.getReferringDoctor().getFirstname(), patientFileDTO.getReferringDoctorFirstname());
		assertEquals(patientFile.getReferringDoctor().getLastname(), patientFileDTO.getReferringDoctorLastname());
		assertEquals(
				patientFile.getReferringDoctor().getSpecialties().stream().map(Specialty::getDescription)
						.collect(Collectors.toList()).toString(),
				patientFileDTO.getReferringDoctorSpecialties().toString());
		assertNull(patientFileDTO.getSecurityCode());
	}

	@Test
	public void testModelMapperCorrespondanceDTO2Correspondance() {
		correspondenceDTO.setId(UUID.randomUUID());
		correspondenceDTO.setDateUntil(LocalDate.of(2022, 7, 21));
		correspondenceDTO.setDoctorId("D001");
		correspondenceDTO.setPatientFileId("P001");

		correspondence = commonModelMapper.map(correspondenceDTO, Correspondence.class);

		assertEquals(correspondenceDTO.getId(), correspondence.getId());
		assertEquals(correspondenceDTO.getDateUntil(), correspondence.getDateUntil());
		assertEquals(correspondenceDTO.getDoctorId(), correspondence.getDoctor().getId());
		assertEquals(correspondenceDTO.getPatientFileId(), correspondence.getPatientFile().getId());
	}

	@Test
	public void testModelMapperCorrespondance2CorrespondanceDTO() {
		correspondence.setId(UUID.randomUUID());
		correspondence.setDateUntil(LocalDate.of(2022, 7, 21));
		correspondence.setDoctor(doctor1);
		correspondence.setPatientFile(patientFile);

		correspondenceDTO = commonModelMapper.map(correspondence, CorrespondenceDTO.class);

		assertEquals(correspondence.getId(), correspondenceDTO.getId());
		assertEquals(correspondence.getDateUntil(), correspondenceDTO.getDateUntil());
		assertEquals(correspondence.getPatientFile().getId(), correspondenceDTO.getPatientFileId());
		assertEquals(correspondence.getDoctor().getId(), correspondenceDTO.getDoctorId());
		assertEquals(correspondence.getDoctor().getFirstname(), correspondenceDTO.getDoctorFirstname());
		assertEquals(correspondence.getDoctor().getLastname(), correspondenceDTO.getDoctorLastname());
		assertEquals(correspondence.getDoctor().getSpecialties().stream().map(Specialty::getDescription)
				.collect(Collectors.toList()), correspondenceDTO.getDoctorSpecialties());
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

		mail = commonModelMapper.map(mailDTO, Mail.class);

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

		mailDTO = commonModelMapper.map(mail, MailDTO.class);

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

		diagnosis = commonModelMapper.map(diagnosisDTO, Diagnosis.class);

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

		diagnosisDTO = diagnosisModelMapper.map(diagnosis, DiagnosisDTO.class);

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

		act = commonModelMapper.map(actDTO, Act.class);

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

		actDTO = actModelMapper.map(act, ActDTO.class);

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

		symptom = commonModelMapper.map(symptomDTO, Symptom.class);

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

		symptomDTO = commonModelMapper.map(symptom, SymptomDTO.class);

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
