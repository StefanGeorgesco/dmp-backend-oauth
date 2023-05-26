package fr.cnam.stefangeorgesco.dmp.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnam.stefangeorgesco.dmp.domain.dao.DoctorDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.PatientFileItemDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.*;
import fr.cnam.stefangeorgesco.dmp.domain.model.Act;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.Mail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
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
public class PatientFileItemControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private DoctorDAO doctorDAO;

	@Autowired
	private PatientFileItemDAO patientFileItemDAO;

	private MedicalActDTO medicalActDTO;

	private ActDTO actDTO;

	private MailDTO mailDTO;

	private DiseaseDTO diseaseDTO;

	private DiagnosisDTO diagnosisDTO;

	private Doctor authoringDoctor;

	private long count;

	private UUID uuid;

	private String comment;

	private String text;

	@BeforeEach
	public void setup() {
		comment = "A comment";
		text = "A text";
		medicalActDTO = new MedicalActDTO();
		actDTO = new ActDTO();
		mailDTO = new MailDTO();
		diseaseDTO = new DiseaseDTO();
		diagnosisDTO = new DiagnosisDTO();
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testCreateActSuccessUserIsReferringDoctor() throws Exception {

		count = patientFileItemDAO.count();

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");
		actDTO.setDate(now);
		actDTO.setComments("comments on this act");
		actDTO.setMedicalActDTO(medicalActDTO);

		assertNull(actDTO.getId());
		assertNull(actDTO.getAuthoringDoctorId());
		assertNull(actDTO.getPatientFileId());

		authoringDoctor = doctorDAO.findById("D001").orElseThrow();

		mockMvc.perform(post("/patient-file/P001/item").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO))).andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.@type", is("act")))
				.andExpect(jsonPath("$.id", hasLength(36))).andExpect(jsonPath("$.date", is(now.toString())))
				.andExpect(jsonPath("$.comments", is(actDTO.getComments())))
				.andExpect(jsonPath("$.authoringDoctorId", is(authoringDoctor.getId())))
				.andExpect(jsonPath("$.authoringDoctorFirstname", is(authoringDoctor.getFirstname())))
				.andExpect(jsonPath("$.authoringDoctorLastname", is(authoringDoctor.getLastname())))
				.andExpect(jsonPath("$.medicalAct.id", is(actDTO.getMedicalActDTO().getId())))
				.andExpect(jsonPath("$.medicalAct.description",
						is("Hémostase gingivoalvéolaire secondaire à une avulsion dentaire")))
				.andExpect(jsonPath("$.patientFileId", is("P001")));

		assertEquals(count + 1, patientFileItemDAO.count());
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testCreateActSuccessUserIsActiveCorrespondingDoctor() throws Exception {

		count = patientFileItemDAO.count();

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");
		actDTO.setDate(now);
		actDTO.setComments("comments on this act");
		actDTO.setAuthoringDoctorId("D001");
		actDTO.setMedicalActDTO(medicalActDTO);

		assertNull(actDTO.getId());
		assertNull(actDTO.getPatientFileId());

		authoringDoctor = doctorDAO.findById(actDTO.getAuthoringDoctorId()).orElseThrow();

		mockMvc.perform(post("/patient-file/P006/item").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO))).andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.@type", is("act")))
				.andExpect(jsonPath("$.id", hasLength(36))).andExpect(jsonPath("$.date", is(now.toString())))
				.andExpect(jsonPath("$.comments", is(actDTO.getComments())))
				.andExpect(jsonPath("$.authoringDoctorId", is(authoringDoctor.getId())))
				.andExpect(jsonPath("$.authoringDoctorFirstname", is(authoringDoctor.getFirstname())))
				.andExpect(jsonPath("$.authoringDoctorLastname", is(authoringDoctor.getLastname())))
				.andExpect(jsonPath("$.medicalAct.id", is(actDTO.getMedicalActDTO().getId())))
				.andExpect(jsonPath("$.medicalAct.description",
						is("Hémostase gingivoalvéolaire secondaire à une avulsion dentaire")))
				.andExpect(jsonPath("$.patientFileId", is("P006")));

		assertEquals(count + 1, patientFileItemDAO.count());
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testCreateActFailureUserCorrespondanceExpired() throws Exception {

		count = patientFileItemDAO.count();

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");
		actDTO.setDate(now);
		actDTO.setComments("comments on this act");
		actDTO.setAuthoringDoctorId("D001");
		actDTO.setMedicalActDTO(medicalActDTO);

		mockMvc.perform(post("/patient-file/P013/item").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO))).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("L'utilisateur n'est pas le médecin référent ou correspondant.")));

		assertEquals(count, patientFileItemDAO.count());
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testCreateActFailureUserIsNotReferringNorCorrespondingDoctor() throws Exception {

		count = patientFileItemDAO.count();

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");
		actDTO.setDate(now);
		actDTO.setComments("comments on this act");
		actDTO.setAuthoringDoctorId("D001");
		actDTO.setMedicalActDTO(medicalActDTO);

		mockMvc.perform(post("/patient-file/P004/item").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO))).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("L'utilisateur n'est pas le médecin référent ou correspondant.")));

		assertEquals(count, patientFileItemDAO.count());
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testCreateActFailurePatientFileDoesNotExist() throws Exception {

		count = patientFileItemDAO.count();

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");
		actDTO.setDate(now);
		actDTO.setComments("comments on this act");
		actDTO.setAuthoringDoctorId("D001");
		actDTO.setMedicalActDTO(medicalActDTO);

		mockMvc.perform(post("/patient-file/P002/item").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO))).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("Dossier patient non trouvé.")));

		assertEquals(count, patientFileItemDAO.count());
	}

	@Test
	@WithMockUser(username="P001",roles={"PATIENT"})
	public void testCreatePatientFileItemFailureBadRolePatient() throws Exception {

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");
		actDTO.setDate(now);
		actDTO.setComments("comments on this act");
		actDTO.setAuthoringDoctorId("D001");
		actDTO.setMedicalActDTO(medicalActDTO);

		mockMvc.perform(post("/patient-file/P001/item").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO))).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testCreatePatientFileItemFailureBadRoleAdmin() throws Exception {

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");
		actDTO.setDate(now);
		actDTO.setComments("comments on this act");
		actDTO.setAuthoringDoctorId("D001");
		actDTO.setMedicalActDTO(medicalActDTO);

		mockMvc.perform(post("/patient-file/P001/item").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO))).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testCreatePatientFileItemFailureUnauthenticatedUser() throws Exception {

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");
		actDTO.setDate(now);
		actDTO.setComments("comments on this act");
		actDTO.setAuthoringDoctorId("D001");
		actDTO.setMedicalActDTO(medicalActDTO);

		mockMvc.perform(post("/patient-file/P001/item").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO))).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testUpdatePatientFileItemSuccessUserIsReferringAndAuthor() throws Exception {

		uuid = UUID.fromString("1b57e70f-8eb0-4a97-99c6-5d44f138c22c");

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");

		actDTO.setId(UUID.randomUUID()); // not taken into account
		actDTO.setDate(now); // can not be updated
		actDTO.setComments(comment);
		actDTO.setMedicalActDTO(medicalActDTO);
		actDTO.setAuthoringDoctorId("D002"); // can not be updated
		actDTO.setPatientFileId("P002"); // can not be updated

		authoringDoctor = doctorDAO.findById("D001").orElseThrow();

		mockMvc.perform(put("/patient-file/P005/item/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.@type", is("act")))
				.andExpect(jsonPath("$.id", hasLength(36))).andExpect(jsonPath("$.date", is("2021-10-11")))
				.andExpect(jsonPath("$.comments", is(actDTO.getComments())))
				.andExpect(jsonPath("$.authoringDoctorId", is(authoringDoctor.getId())))
				.andExpect(jsonPath("$.authoringDoctorFirstname", is(authoringDoctor.getFirstname())))
				.andExpect(jsonPath("$.authoringDoctorLastname", is(authoringDoctor.getLastname())))
				.andExpect(jsonPath("$.medicalAct.id", is(actDTO.getMedicalActDTO().getId())))
				.andExpect(jsonPath("$.medicalAct.description",
						is("Hémostase gingivoalvéolaire secondaire à une avulsion dentaire")))
				.andExpect(jsonPath("$.patientFileId", is("P005")));

		Act act = (Act) patientFileItemDAO.findById(uuid).orElseThrow();

		assertEquals(comment, act.getComments());
		assertEquals("2021-10-11", act.getDate().toString());
		assertEquals(authoringDoctor.getId(), act.getAuthoringDoctor().getId());
		assertEquals("HBSD001", act.getMedicalAct().getId());
		assertEquals("Hémostase gingivoalvéolaire secondaire à une avulsion dentaire",
				act.getMedicalAct().getDescription());
		assertEquals("P005", act.getPatientFile().getId());
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testUpdatePatientFileItemSuccessUserIsActiveCorrespondentAndAuthor() throws Exception {

		uuid = UUID.fromString("7f331dd1-0950-4991-964c-2383ba92699e");

		LocalDate now = LocalDate.now();

		mailDTO.setId(UUID.randomUUID()); // not taken into account
		mailDTO.setDate(now); // can not be updated
		mailDTO.setComments(comment);
		mailDTO.setText(text);
		mailDTO.setRecipientDoctorId("D004");
		mailDTO.setAuthoringDoctorId("D002"); // can not be updated
		mailDTO.setPatientFileId("P002"); // can not be updated

		authoringDoctor = doctorDAO.findById("D001").orElseThrow();

		mockMvc.perform(put("/patient-file/P006/item/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(mailDTO))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.@type", is("mail")))
				.andExpect(jsonPath("$.id", hasLength(36))).andExpect(jsonPath("$.date", is("2022-05-27")))
				.andExpect(jsonPath("$.comments", is(mailDTO.getComments())))
				.andExpect(jsonPath("$.authoringDoctorId", is(authoringDoctor.getId())))
				.andExpect(jsonPath("$.authoringDoctorFirstname", is(authoringDoctor.getFirstname())))
				.andExpect(jsonPath("$.authoringDoctorLastname", is(authoringDoctor.getLastname())))
				.andExpect(jsonPath("$.text", is(text))).andExpect(jsonPath("$.recipientDoctorId", is("D004")))
				.andExpect(jsonPath("$.recipientDoctorFirstname", is("Leah")))
				.andExpect(jsonPath("$.recipientDoctorLastname", is("Little")))
				.andExpect(jsonPath("$.recipientDoctorSpecialties", hasSize(1)))
				.andExpect(jsonPath("$.recipientDoctorSpecialties[0]", is("gériatrie")))
				.andExpect(jsonPath("$.patientFileId", is("P006")));

		Mail mail = (Mail) patientFileItemDAO.findById(uuid).orElseThrow();

		assertEquals(comment, mail.getComments());
		assertEquals("2022-05-27", mail.getDate().toString());
		assertEquals(text, mail.getText());
		assertEquals("D004", mail.getRecipientDoctor().getId());
		assertEquals("Leah", mail.getRecipientDoctor().getFirstname());
		assertEquals("Little", mail.getRecipientDoctor().getLastname());
		assertEquals("gériatrie", mail.getRecipientDoctor().getSpecialties().iterator().next().getDescription());
		assertEquals(authoringDoctor.getId(), mail.getAuthoringDoctor().getId());
		assertEquals("P006", mail.getPatientFile().getId());
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testUpdatePatientFileItemUserIsReferringAndAuthorFailureWrongPayloadType() throws Exception {

		uuid = UUID.fromString("1b57e70f-8eb0-4a97-99c6-5d44f138c22c"); // type Act

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");

		mailDTO.setId(UUID.randomUUID());
		mailDTO.setDate(now);
		mailDTO.setComments(comment);
		mailDTO.setText(text);
		mailDTO.setRecipientDoctorId("D004");
		mailDTO.setAuthoringDoctorId("D002");
		mailDTO.setPatientFileId("P002");

		mockMvc.perform(put("/patient-file/P005/item/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(mailDTO))).andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("Le type de l'élément médical est incorrect.")));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testUpdatePatientFileItemUserIsReferringAndAuthorFailurePatientFileItemDoesNotExist() throws Exception {

		uuid = UUID.fromString("1b57e70f-8eb0-4a97-99c6-5d44f138c22"); // does not exist

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");

		actDTO.setId(UUID.randomUUID()); // not taken into account
		actDTO.setDate(now); // can not be updated
		actDTO.setComments(comment);
		actDTO.setMedicalActDTO(medicalActDTO);
		actDTO.setAuthoringDoctorId("D002"); // can not be updated
		actDTO.setPatientFileId("P002"); // can not be updated

		mockMvc.perform(put("/patient-file/P005/item/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO))).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("Elément médical non trouvé.")));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testUpdatePatientFileItemFailureUserIsReferringButNotAuthor() throws Exception {

		uuid = UUID.fromString("c793da7f-5ca8-41f5-a0f0-1cc77b34b6fe");

		LocalDate now = LocalDate.now();

		diseaseDTO.setId("J010");

		diagnosisDTO.setId(UUID.randomUUID());
		diagnosisDTO.setDate(now);
		diagnosisDTO.setComments(comment);
		diagnosisDTO.setDiseaseDTO(diseaseDTO);
		diagnosisDTO.setAuthoringDoctorId("D002");
		diagnosisDTO.setPatientFileId("P002");

		mockMvc.perform(put("/patient-file/P005/item/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(diagnosisDTO))).andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.message",
						is("L'utilisateur n'est pas l'auteur de l'élément médical et ne peut pas le modifier.")));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testUpdatePatientFileItemFailureUserIsActiveCorrespondentButNotAuthor() throws Exception {

		uuid = UUID.fromString("643bce5b-9c74-4e6f-8ae2-5809a31bedb4");

		LocalDate now = LocalDate.now();

		PrescriptionDTO prescriptionDTO = new PrescriptionDTO();
		prescriptionDTO.setId(UUID.randomUUID());
		prescriptionDTO.setDate(now);
		prescriptionDTO.setComments(comment);
		prescriptionDTO.setDescription("prescription description");
		prescriptionDTO.setAuthoringDoctorId("D002");
		prescriptionDTO.setPatientFileId("P002");

		mockMvc.perform(put("/patient-file/P006/item/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(prescriptionDTO))).andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.message",
						is("L'utilisateur n'est pas l'auteur de l'élément médical et ne peut pas le modifier.")));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testUpdatePatientFileItemFailureUserIsCorrespondentAndAuthorButNotActiveCorrespondent()
			throws Exception {

		uuid = UUID.fromString("142763cf-6eeb-47a5-b8f8-8ec85f0025c4");

		LocalDate now = LocalDate.now();

		SymptomDTO symptomDTO = new SymptomDTO();
		symptomDTO.setId(UUID.randomUUID());
		symptomDTO.setDate(now);
		symptomDTO.setComments(comment);
		symptomDTO.setDescription("symptom description");
		symptomDTO.setAuthoringDoctorId("D002");
		symptomDTO.setPatientFileId("P002");

		mockMvc.perform(put("/patient-file/P013/item/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(symptomDTO))).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("L'utilisateur n'est pas le médecin référent ou correspondant.")));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testUpdatePatientFileItemFailureUserIsNotReferringNorCorrespondingDoctor() throws Exception {

		uuid = UUID.fromString("b7bdb6e4-da4b-47f9-9b67-cfd67567aaca");

		LocalDate now = LocalDate.now();

		diseaseDTO.setId("J010");

		diagnosisDTO.setId(UUID.randomUUID());
		diagnosisDTO.setDate(now);
		diagnosisDTO.setComments(comment);
		diagnosisDTO.setDiseaseDTO(diseaseDTO);
		diagnosisDTO.setAuthoringDoctorId("D002");
		diagnosisDTO.setPatientFileId("P002");

		mockMvc.perform(put("/patient-file/P012/item/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(diagnosisDTO))).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("L'utilisateur n'est pas le médecin référent ou correspondant.")));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testUpdatePatientFileItemUserIsReferringAndAuthorFailurePatientFileItemAndPatientFileDontMatch()
			throws Exception {

		uuid = UUID.fromString("1b57e70f-8eb0-4a97-99c6-5d44f138c22c"); // P005

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");

		actDTO.setId(UUID.randomUUID());
		actDTO.setDate(now);
		actDTO.setComments(comment);
		actDTO.setMedicalActDTO(medicalActDTO);
		actDTO.setAuthoringDoctorId("D002");
		actDTO.setPatientFileId("P002");

		mockMvc.perform(put("/patient-file/P001/item/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO))).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("Elément médical non trouvé pour le dossier patient 'P001'")));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testUpdatePatientFileItemFailureBadRoleAdmin() throws Exception {

		uuid = UUID.fromString("1b57e70f-8eb0-4a97-99c6-5d44f138c22c");

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");

		actDTO.setId(UUID.randomUUID());
		actDTO.setDate(now);
		actDTO.setComments(comment);
		actDTO.setMedicalActDTO(medicalActDTO);
		actDTO.setAuthoringDoctorId("D002");
		actDTO.setPatientFileId("P002");

		mockMvc.perform(put("/patient-file/P005/item/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO))).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username="P001",roles={"PATIENT"})
	public void testUpdatePatientFileItemFailureBadRolePatient() throws Exception {

		uuid = UUID.fromString("1b57e70f-8eb0-4a97-99c6-5d44f138c22c");

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");

		actDTO.setId(UUID.randomUUID());
		actDTO.setDate(now);
		actDTO.setComments(comment);
		actDTO.setMedicalActDTO(medicalActDTO);
		actDTO.setAuthoringDoctorId("D002");
		actDTO.setPatientFileId("P002");

		mockMvc.perform(put("/patient-file/P005/item/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO))).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testUpdatePatientFileItemFailureUnauthenticatedUser() throws Exception {

		uuid = UUID.fromString("1b57e70f-8eb0-4a97-99c6-5d44f138c22c");

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");

		actDTO.setId(UUID.randomUUID());
		actDTO.setDate(now);
		actDTO.setComments(comment);
		actDTO.setMedicalActDTO(medicalActDTO);
		actDTO.setAuthoringDoctorId("D002");
		actDTO.setPatientFileId("P002");

		mockMvc.perform(put("/patient-file/P005/item/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO))).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testDeletePatientFileItemSuccessUserIsReferringAndAuthor() throws Exception {

		count = patientFileItemDAO.count();

		uuid = UUID.fromString("1b57e70f-8eb0-4a97-99c6-5d44f138c22c");

		assertTrue(patientFileItemDAO.existsById(uuid));

		mockMvc.perform(delete("/patient-file/P005/item/" + uuid.toString())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.status", is(200)))
				.andExpect(jsonPath("$.message", is("L'élément médical a bien été supprimé.")));

		assertEquals(count - 1, patientFileItemDAO.count());
		assertFalse(patientFileItemDAO.existsById(uuid));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testDeletePatientFileItemSuccessUserIsActiveCorrespondentAndAuthor() throws Exception {

		count = patientFileItemDAO.count();

		uuid = UUID.fromString("7f331dd1-0950-4991-964c-2383ba92699e");

		assertTrue(patientFileItemDAO.existsById(uuid));

		mockMvc.perform(delete("/patient-file/P006/item/" + uuid.toString())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.status", is(200)))
				.andExpect(jsonPath("$.message", is("L'élément médical a bien été supprimé.")));

		assertEquals(count - 1, patientFileItemDAO.count());
		assertFalse(patientFileItemDAO.existsById(uuid));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testDeletePatientFileItemUserIsReferringAndAuthorFailurePatientFileItemDoesNotExist() throws Exception {

		count = patientFileItemDAO.count();

		uuid = UUID.fromString("1b57e70f-8eb0-4a97-99c6-5d44f138c22"); // does not exist

		assertFalse(patientFileItemDAO.existsById(uuid));

		mockMvc.perform(delete("/patient-file/P005/item/" + uuid.toString())).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("Elément médical non trouvé.")));

		assertEquals(count, patientFileItemDAO.count());
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testDeletePatientFileItemFailureUserIsReferringButNotAuthor() throws Exception {

		count = patientFileItemDAO.count();

		uuid = UUID.fromString("c793da7f-5ca8-41f5-a0f0-1cc77b34b6fe");

		mockMvc.perform(delete("/patient-file/P005/item/" + uuid)).andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.message",
						is("L'utilisateur n'est pas l'auteur de l'élément médical et ne peut pas le supprimer.")));

		assertEquals(count, patientFileItemDAO.count());
		assertTrue(patientFileItemDAO.existsById(uuid));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testDeletePatientFileItemFailureUserIsActiveCorrespondentButNotAuthor() throws Exception {

		count = patientFileItemDAO.count();

		uuid = UUID.fromString("643bce5b-9c74-4e6f-8ae2-5809a31bedb4");

		mockMvc.perform(delete("/patient-file/P006/item/" + uuid)).andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.message",
						is("L'utilisateur n'est pas l'auteur de l'élément médical et ne peut pas le supprimer.")));

		assertEquals(count, patientFileItemDAO.count());
		assertTrue(patientFileItemDAO.existsById(uuid));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testDeletePatientFileItemFailureUserIsCorrespondentAndAuthorButNotActiveCorrespondent()
			throws Exception {

		count = patientFileItemDAO.count();

		uuid = UUID.fromString("142763cf-6eeb-47a5-b8f8-8ec85f0025c4");

		mockMvc.perform(delete("/patient-file/P013/item/" + uuid)).andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("L'utilisateur n'est pas le médecin référent ou correspondant.")));

		assertEquals(count, patientFileItemDAO.count());
		assertTrue(patientFileItemDAO.existsById(uuid));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testDeletePatientFileItemFailureUserIsNotReferringNorCorrespondingDoctor() throws Exception {

		count = patientFileItemDAO.count();

		uuid = UUID.fromString("b7bdb6e4-da4b-47f9-9b67-cfd67567aaca");

		mockMvc.perform(delete("/patient-file/P012/item/" + uuid)).andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("L'utilisateur n'est pas le médecin référent ou correspondant.")));

		assertEquals(count, patientFileItemDAO.count());
		assertTrue(patientFileItemDAO.existsById(uuid));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testDeletePatientFileItemUserIsReferringAndAuthorFailurePatientFileItemAndPatientFileDontMatch()
			throws Exception {

		count = patientFileItemDAO.count();

		uuid = UUID.fromString("1b57e70f-8eb0-4a97-99c6-5d44f138c22c"); // P005

		mockMvc.perform(delete("/patient-file/P001/item/" + uuid)).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("Elément médical non trouvé pour le dossier patient 'P001'")));

		assertEquals(count, patientFileItemDAO.count());
		assertTrue(patientFileItemDAO.existsById(uuid));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testDeletePatientFileItemFailureBadRoleAdmin() throws Exception {

		uuid = UUID.fromString("1b57e70f-8eb0-4a97-99c6-5d44f138c22c");

		mockMvc.perform(delete("/patient-file/P005/item/" + uuid)).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username="P001",roles={"PATIENT"})
	public void testDeletePatientFileItemFailureBadRolePatient() throws Exception {

		uuid = UUID.fromString("1b57e70f-8eb0-4a97-99c6-5d44f138c22c");

		mockMvc.perform(delete("/patient-file/P005/item/" + uuid)).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testDeletePatientFileItemFailureUnauthenticatedUser() throws Exception {

		uuid = UUID.fromString("1b57e70f-8eb0-4a97-99c6-5d44f138c22c");

		mockMvc.perform(delete("/patient-file/P005/item/" + uuid)).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testFindPatientFileItemsByPatientFileIdSuccessUserIsReferringDoctor() throws Exception {

		mockMvc.perform(get("/patient-file/P005/item")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(10)))
				.andExpect(jsonPath("$[1].@type", is("diagnosis")));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testFindPatientFileItemsByPatientFileIdSuccessUserIsActiveCorrespondingDoctor() throws Exception {

		mockMvc.perform(get("/patient-file/P006/item")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(9)))
				.andExpect(jsonPath("$[1].@type", is("symptom")));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testFindPatientFileItemsByPatientFileIdFailureCorrespondenceExpired() throws Exception {

		mockMvc.perform(get("/patient-file/P013/item")).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("L'utilisateur n'est pas le médecin référent ou correspondant.")));
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"})
	public void testFindPatientFileItemsByPatientFileIdFailureUserIsNotReferringNorCorrespondingDoctor()
			throws Exception {

		mockMvc.perform(get("/patient-file/P012/item")).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("L'utilisateur n'est pas le médecin référent ou correspondant.")));
	}

	@Test
	@WithMockUser(username="P001",roles={"PATIENT"})
	public void testFindPatientFileItemsByPatientFileIdFailureBadRolePatient() throws Exception {

		mockMvc.perform(get("/patient-file/P005/item")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testFindPatientFileItemsByPatientFileIdFailureBadRoleAdmin() throws Exception {

		mockMvc.perform(get("/patient-file/P005/item")).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testFindPatientFileItemsByPatientFileIdFailureUnauthenticatedUser() throws Exception {

		mockMvc.perform(get("/patient-file/P005/item")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(username="P001",roles={"PATIENT"}) // P001, ROLE_DOCTOR
	public void testFindPatientPatientFileItemsSuccess() throws Exception {

		mockMvc.perform(get("/patient-file/details/item")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(9)))
				.andExpect(jsonPath("$[1].@type", is("prescription")));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testFindPatientPatientFileItemsFailureBadRoleAdmin() throws Exception {

		mockMvc.perform(get("/patient-file/details/item")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username="D001",roles={"DOCTOR"}) // ROLE_DOCTOR
	public void testFindPatientPatientFileItemsFailureBadRoleDoctor() throws Exception {

		mockMvc.perform(get("/patient-file/details/item")).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testFindPatientPatientFileItemsFailureUnauthenticatedUser() throws Exception {

		mockMvc.perform(get("/patient-file/details/item")).andExpect(status().isUnauthorized());
	}
}
