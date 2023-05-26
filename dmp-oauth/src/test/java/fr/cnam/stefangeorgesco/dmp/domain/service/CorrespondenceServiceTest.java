package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dao.CorrespondenceDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondenceDTO;
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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CorrespondenceServiceTest {

	@MockBean
	private CorrespondenceDAO correspondenceDAO;

	@Autowired
	private CorrespondenceService correspondenceService;

	private final ArgumentCaptor<Correspondence> correspondenceCaptor = ArgumentCaptor.forClass(Correspondence.class);

	private CorrespondenceDTO correspondenceDTO;

	private CorrespondenceDTO correspondenceDTOResponse;

	private Correspondence persistentCorrespondence;

	private UUID uuid;

	private Doctor doctor;

	private PatientFile patientFile1;

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

		doctor = new Doctor();
		doctor.setId("D001");

		Doctor doctor2 = new Doctor();
		doctor2.setId("D002");
		doctor2.setFirstname("firstname");
		doctor2.setLastname("lastname");
		doctor2.setSpecialties(List.of(specialty1, specialty2));

		patientFile1 = new PatientFile();
		patientFile1.setId("ID_1");
		patientFile1.setFirstname("firstname_1");
		patientFile1.setLastname("lastname_1");
		patientFile1.setDateOfBirth(LocalDate.of(2000, 2, 13));
		patientFile1.setAddress(address);
		patientFile1.setSecurityCode("securityCode_1");
		patientFile1.setReferringDoctor(doctor);

		persistentCorrespondence = new Correspondence();
		persistentCorrespondence.setDateUntil(LocalDate.of(2022, 7, 29));
		persistentCorrespondence.setDoctor(doctor2);
		persistentCorrespondence.setPatientFile(patientFile1);

		uuid = UUID.randomUUID();
	}

	@Test
	public void testCreateCorrespondenceSuccess() {

		correspondenceDTO = new CorrespondenceDTO();
		correspondenceDTO.setDateUntil(LocalDate.now().plusDays(1));
		correspondenceDTO.setDoctorId("D002");
		correspondenceDTO.setPatientFileId("ID_1");

		uuid = UUID.randomUUID();
		persistentCorrespondence.setId(uuid);
		persistentCorrespondence.setDateUntil(correspondenceDTO.getDateUntil());

		when(correspondenceDAO.save(correspondenceCaptor.capture())).thenReturn(persistentCorrespondence);
		when(correspondenceDAO.findById(any(UUID.class))).thenReturn(Optional.of(persistentCorrespondence));

		correspondenceDTOResponse = assertDoesNotThrow(
				() -> correspondenceService.createCorrespondence(correspondenceDTO));

		verify(correspondenceDAO, times(1)).save(any(Correspondence.class));

		Correspondence savedCorrespondence = correspondenceCaptor.getValue();

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

		correspondenceService.deleteCorrespondence(uuid);

		verify(correspondenceDAO, times(1)).deleteById(uuid);
	}

	@Test
	public void testFindCorrespondenceSuccess() {
		uuid = UUID.randomUUID();

		persistentCorrespondence.setId(uuid);

		when(correspondenceDAO.findById(uuid)).thenReturn(Optional.of(persistentCorrespondence));

		correspondenceDTOResponse = assertDoesNotThrow(() -> correspondenceService.findCorrespondence(uuid.toString()));

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
	public void testFindCorrespondenceFailureCorrespondenceDoesNotExist() {
		uuid = UUID.randomUUID();

		when(correspondenceDAO.findById(uuid)).thenReturn(Optional.empty());

		FinderException ex = assertThrows(FinderException.class,
				() -> correspondenceService.findCorrespondence(uuid.toString()));

		verify(correspondenceDAO, times(1)).findById(uuid);

		assertEquals("Correspondance non trouvée.", ex.getMessage());
	}

	@Test
	public void testFindCorrespondencesByPatientFileIdFound3() {

		Correspondence foundCorrespondence = new Correspondence();
		foundCorrespondence.setId(UUID.randomUUID());
		foundCorrespondence.setDateUntil(LocalDate.of(2022, 8, 1));
		foundCorrespondence.setDoctor(doctor);
		foundCorrespondence.setPatientFile(patientFile1);

		when(correspondenceDAO.findByPatientFileId("P001"))
				.thenReturn(List.of(foundCorrespondence, foundCorrespondence));

		List<CorrespondenceDTO> correspondencesDTO = correspondenceService.findCorrespondencesByPatientFileId("P001");

		verify(correspondenceDAO, times(1)).findByPatientFileId("P001");

		assertEquals(2, correspondencesDTO.size());
		assertEquals(foundCorrespondence.getId(), correspondencesDTO.get(0).getId());
		assertEquals("2022-08-01", correspondencesDTO.get(0).getDateUntil().toString());
		assertEquals("D001", correspondencesDTO.get(0).getDoctorId());
		assertEquals("ID_1", correspondencesDTO.get(0).getPatientFileId());
	}

	@Test
	public void testFindCorrespondencesByPatientFileIdFound0() {

		when(correspondenceDAO.findByPatientFileId("P055")).thenReturn(List.of());

		List<CorrespondenceDTO> correspondencesDTO = correspondenceService.findCorrespondencesByPatientFileId("P055");

		verify(correspondenceDAO, times(1)).findByPatientFileId("P055");

		assertEquals(0, correspondencesDTO.size());
	}
}
