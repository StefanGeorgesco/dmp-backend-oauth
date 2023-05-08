package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.service.UserServiceImpl;
import fr.cnam.stefangeorgesco.dmp.domain.dao.*;
import fr.cnam.stefangeorgesco.dmp.domain.dto.*;
import fr.cnam.stefangeorgesco.dmp.domain.model.*;
import fr.cnam.stefangeorgesco.dmp.exception.domain.*;
import fr.cnam.stefangeorgesco.dmp.utils.SecurityCodeGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Classe de service pour la gestion des dossiers patients et objets rattachés.
 * 
 * @author Stéfan Georgesco
 *
 */
@Service
public class PatientFileService {

	private final RnippServiceImpl rnippService;

	private final UserServiceImpl userService;

	private final PatientFileDAO patientFileDAO;

	private final FileDAO fileDAO;

	private final DoctorDAO doctorDAO;

	private final CorrespondenceDAO correspondenceDAO;

	private final DiseaseDAO diseaseDAO;

	private final MedicalActDAO medicalActDAO;

	private final PatientFileItemDAO patientFileItemDAO;

	private final ModelMapper commonModelMapper;

	private final ModelMapper patientFileModelMapper;

	private final MapperServiceImpl mapperService;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public PatientFileService(RnippServiceImpl rnippService, UserServiceImpl userService, PatientFileDAO patientFileDAO, BCryptPasswordEncoder bCryptPasswordEncoder, MapperServiceImpl mapperService, FileDAO fileDAO, DoctorDAO doctorDAO, CorrespondenceDAO correspondenceDAO, DiseaseDAO diseaseDAO, MedicalActDAO medicalActDAO, PatientFileItemDAO patientFileItemDAO, ModelMapper commonModelMapper, ModelMapper patientFileModelMapper) {
		this.rnippService = rnippService;
		this.userService = userService;
		this.patientFileDAO = patientFileDAO;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.mapperService = mapperService;
		this.fileDAO = fileDAO;
		this.doctorDAO = doctorDAO;
		this.correspondenceDAO = correspondenceDAO;
		this.diseaseDAO = diseaseDAO;
		this.medicalActDAO = medicalActDAO;
		this.patientFileItemDAO = patientFileItemDAO;
		this.commonModelMapper = commonModelMapper;
		this.patientFileModelMapper = patientFileModelMapper;
	}

	/**
	 * Service de création d'un dossier patient. Le service vérifie qu'un dossier
	 * avec le même identifiant n'existe pas.
	 * 
	 * @param patientFileDTO l'objet
	 *                       {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
	 *                       représentant le dossier patient à créer.
	 * @return un objet
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
	 *         représentant le dossier patient créé.
	 * @throws CheckException  le dossier patient n'existe pas au RNIPP.
	 * @throws CreateException le dossier patient n'a pas pu être créé.
	 */
	public PatientFileDTO createPatientFile(PatientFileDTO patientFileDTO) throws CheckException, CreateException {

		rnippService.checkPatientData(patientFileDTO);

		if (fileDAO.existsById(patientFileDTO.getId())) {
			throw new DuplicateKeyException("Un dossier avec cet identifiant existe déjà.");
		}

		patientFileDTO.setSecurityCode(SecurityCodeGenerator.generateCode());

		PatientFile patientFile = commonModelMapper.map(patientFileDTO, PatientFile.class);

		patientFile.setSecurityCode(bCryptPasswordEncoder.encode(patientFile.getSecurityCode()));

		try {
			patientFileDAO.save(patientFile);
		} catch (Exception e) {
			throw new CreateException("Le dossier patient n'a pas pu être créé.");
		}

		return patientFileDTO;
	}

	/**
	 * Service de recherche d'un dossier patient par son identifiant.
	 * 
	 * @param id l'identifiant du dossier recherché.
	 * @return un objet
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
	 *         représentant le dossier patient trouvé.
	 * @throws FinderException dossier patient non trouvé.
	 */
	public PatientFileDTO findPatientFile(String id) throws FinderException {
		Optional<PatientFile> optionalPatientFile = patientFileDAO.findById(id);

		if (optionalPatientFile.isPresent()) {
			return patientFileModelMapper.map(optionalPatientFile.get(), PatientFileDTO.class);
		} else {
			throw new FinderException("Dossier patient non trouvé.");
		}
	}

	/**
	 * Service de modification d'un dossier patient. Les données prises en compte
	 * dans la modification sont le numéro de téléphone, l'adresse email et
	 * l'adresse postale. Les autres données ne sont pas considérées.
	 * 
	 * @param patientFileDTO l'objet
	 *                       {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
	 *                       représentant le dossier patient à modifier et les
	 *                       données modifiées.
	 * @return un objet
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
	 *         représentant le dossier patient modifié.
	 * @throws UpdateException le dossier patient n'a pas pu être modifié.
	 */
	public PatientFileDTO updatePatientFile(PatientFileDTO patientFileDTO) throws UpdateException {

		PatientFile patientFile = patientFileDAO.findById(patientFileDTO.getId()).orElseThrow();

		patientFile.setPhone(patientFileDTO.getPhone());
		patientFile.setEmail(patientFileDTO.getEmail());

		PatientFile mappedPatientFile = commonModelMapper.map(patientFileDTO, PatientFile.class);

		patientFile.setAddress(mappedPatientFile.getAddress());

		try {
			patientFile = patientFileDAO.save(patientFile);
		} catch (Exception e) {
			throw new UpdateException("Le dossier patient n'a pas pu être modifié.");
		}

		UserDTO userDTO = new UserDTO();
		userDTO.setId(patientFileDTO.getId());
		userDTO.setFirstname(patientFileDTO.getFirstname());
		userDTO.setLastname(patientFileDTO.getLastname());
		userDTO.setEmail(patientFileDTO.getEmail());
		userService.updateUser(userDTO);

		return patientFileModelMapper.map(patientFile, PatientFileDTO.class);
	}

	/**
	 * Service de modification du médecin référent d'un dossier patient. Le service
	 * vérifie si le dossier patient et le dossier de médecin du nouveau médecin
	 * référent existent.
	 * 
	 * @param patientFileDTO l'objet
	 *                       {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
	 *                       représentant le dossier patient dont on souhaite
	 *                       modifier le médecin référent, comportant dans son
	 *                       attribut referringDoctorId l'identifiant du dossier de
	 *                       médecin du nouveau médecin référent.
	 * @return l'objet {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
	 *         représentant le dossier patient modifié.
	 * @throws FinderException le dossier patient n'a pas été trouvé ou bien le dossier
	 *                         de médecin référent n'a pas été trouvé.
	 * @throws UpdateException le dossier patient n'a pas pu être modifié.
	 */
	public PatientFileDTO updateReferringDoctor(PatientFileDTO patientFileDTO) throws FinderException, UpdateException {

		Optional<PatientFile> optionalPatientFile = patientFileDAO.findById(patientFileDTO.getId());

		if (optionalPatientFile.isEmpty()) {
			throw new FinderException("Dossier patient non trouvé.");
		}

		PatientFile patientFile = optionalPatientFile.get();

		Optional<Doctor> optionalDoctor = doctorDAO.findById(patientFileDTO.getReferringDoctorId());

		if (optionalDoctor.isEmpty()) {
			throw new FinderException("Dossier de médecin non trouvé.");
		}

		Doctor doctor = optionalDoctor.get();
		patientFile.setReferringDoctor(doctor);

		try {
			patientFile = patientFileDAO.save(patientFile);
		} catch (Exception e) {
			throw new UpdateException("Le dossier patient n'a pas pu être modifié (médecin référent).");
		}

		return patientFileModelMapper.map(patientFile, PatientFileDTO.class);
	}

	/**
	 * Service de recherche de dossiers patients à partir d'une chaîne de
	 * caractères.
	 * 
	 * @param q la chaîne de caractères de recherche.
	 * @return une liste ({@link java.util.List}) d'objets
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
	 *         représentant les dossiers trouvés.
	 */
	public List<PatientFileDTO> findPatientFilesByIdOrFirstnameOrLastname(String q) {

		if ("".equals(q)) {
			return new ArrayList<>();
		}

		Iterable<PatientFile> patientFiles = patientFileDAO.findByIdOrFirstnameOrLastname(q);

		return ((List<PatientFile>) patientFiles).stream()
				.map(patientFile -> patientFileModelMapper.map(patientFile, PatientFileDTO.class))
				.collect(Collectors.toList());
	}

	/**
	 * Service de création d'une correspondance.
	 * 
	 * @param correspondenceDTO l'objet
	 *                          {@link fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondenceDTO}
	 *                          représentant la correspondance à créer.
	 * @return un objet
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondenceDTO}
	 *         représentant la correspondance créée.
	 * @throws CreateException la correspondance n'a pas pu être créé.
	 */
	public CorrespondenceDTO createCorrespondence(CorrespondenceDTO correspondenceDTO) throws CreateException {

		Correspondence correspondence = commonModelMapper.map(correspondenceDTO, Correspondence.class);

		try {
			correspondence = correspondenceDAO.save(correspondence);
		} catch (Exception e) {
			throw new CreateException("La correspondance n'a pas pu être créé.");
		}

		correspondence = correspondenceDAO.findById(correspondence.getId()).orElseThrow();

		return commonModelMapper.map(correspondence, CorrespondenceDTO.class);
	}

	/**
	 * Service de suppression d'une correspondance désignée par son identifiant.
	 * 
	 * @param uuid l'identifiant de la correspondance à supprimer.
	 */
	public void deleteCorrespondence(UUID uuid) {

		correspondenceDAO.deleteById(uuid);
	}

	/**
	 * Service de recherche d'une correspondance par son identifiant.
	 * 
	 * @param id l'identifiant de la correspondance recherchée.
	 * @return un objet
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondenceDTO}
	 *         représentant la correspondance trouvée.
	 * @throws FinderException correspondance non trouvée.
	 */
	public CorrespondenceDTO findCorrespondence(String id) throws FinderException {

		Optional<Correspondence> optionalCorrespondence = correspondenceDAO.findById(UUID.fromString(id));

		if (optionalCorrespondence.isPresent()) {
			return commonModelMapper.map(optionalCorrespondence.get(), CorrespondenceDTO.class);
		} else {
			throw new FinderException("Correspondance non trouvée.");
		}
	}

	/**
	 * Service de recherche de toutes les correspondances associées à un dossier
	 * patient.
	 * 
	 * @param patientFileId l'identifiant du dossier patient.
	 * @return une liste ({@link java.util.List}) d'objets
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondenceDTO}
	 *         représentant les correspondances demandées.
	 */
	public List<CorrespondenceDTO> findCorrespondencesByPatientFileId(String patientFileId) {

		Iterable<Correspondence> correspondences = correspondenceDAO.findByPatientFileId(patientFileId);

		return ((List<Correspondence>) correspondences).stream()
				.map(correspondence -> commonModelMapper.map(correspondence, CorrespondenceDTO.class))
				.collect(Collectors.toList());
	}

	/**
	 * Service de recherche d'une maladie par son identifiant.
	 * 
	 * @param id l'identifiant de la maladie recherchée.
	 * @return un objet {@link fr.cnam.stefangeorgesco.dmp.domain.dto.DiseaseDTO}
	 *         représentant la maladie trouvée.
	 * @throws FinderException maladie non trouvée.
	 */
	public DiseaseDTO findDisease(String id) throws FinderException {

		Optional<Disease> optionalDisease = diseaseDAO.findById(id);

		if (optionalDisease.isPresent()) {
			return commonModelMapper.map(optionalDisease.get(), DiseaseDTO.class);
		} else {
			throw new FinderException("Maladie non trouvée.");
		}
	}

	/**
	 * Service de recherche d'un acte médical par son identifiant.
	 * 
	 * @param id l'identifiant de l'acte médical recherché.
	 * @return un objet {@link fr.cnam.stefangeorgesco.dmp.domain.dto.MedicalActDTO}
	 *         représentant l'acte médical trouvé.
	 * @throws FinderException acte médical non trouvé.
	 */
	public MedicalActDTO findMedicalAct(String id) throws FinderException {

		Optional<MedicalAct> optionalMedicalAct = medicalActDAO.findById(id);

		if (optionalMedicalAct.isPresent()) {
			return commonModelMapper.map(optionalMedicalAct.get(), MedicalActDTO.class);
		} else {
			throw new FinderException("Acte médical non trouvé.");
		}
	}

	/**
	 * Service de recherche de maladies à partir d'une chaîne de caractères.
	 * 
	 * @param q     la chaîne de caractères de recherche.
	 * @param limit le nombre maximum d'objets récupérés.
	 * @return une liste ({@link java.util.List}) d'objets
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.DiseaseDTO}
	 *         représentant le résultat de la recherche.
	 */
	public List<DiseaseDTO> findDiseasesByIdOrDescription(String q, int limit) {

		if ("".equals(q)) {
			return new ArrayList<>();
		}

		Iterable<Disease> diseases = diseaseDAO.findByIdOrDescription(q, limit);

		return ((List<Disease>) diseases).stream()
				.map(disease -> commonModelMapper.map(disease, DiseaseDTO.class)).collect(Collectors.toList());
	}

	/**
	 * Service de recherche d'actes médicaux à partir d'une chaîne de caractères.
	 * 
	 * @param q     la chaîne de caractères de recherche.
	 * @param limit le nombre maximum d'objets récupérés.
	 * @return une liste ({@link java.util.List}) d'objets
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.MedicalActDTO}
	 *         représentant le résultat de la recherche.
	 */
	public List<MedicalActDTO> findMedicalActsByIdOrDescription(String q, int limit) {

		if ("".equals(q)) {
			return new ArrayList<>();
		}

		Iterable<MedicalAct> medicalActs = medicalActDAO.findByIdOrDescription(q, limit);

		return ((List<MedicalAct>) medicalActs).stream()
				.map(medicalAct -> commonModelMapper.map(medicalAct, MedicalActDTO.class)).collect(Collectors.toList());
	}

	/**
	 * Service de création d'un élément médical.
	 * 
	 * @param patientFileItemDTO l'objet
	 *                           {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileItemDTO}
	 *                           représentant l'élément médical à créer.
	 * @return un objet
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileItemDTO}
	 *         représentant l'élément médical créé.
	 * @throws CreateException l'élément médical n'a pas pu être créé.
	 */
	public PatientFileItemDTO createPatientFileItem(PatientFileItemDTO patientFileItemDTO) throws CreateException {

		PatientFileItem patientFileItem = mapperService.mapToEntity(patientFileItemDTO);

		try {
			patientFileItem = patientFileItemDAO.save(patientFileItem);
		} catch (Exception e) {
			throw new CreateException("L'élément médical n'a pas pu être créé.");
		}

		patientFileItem = patientFileItemDAO.findById(patientFileItem.getId()).orElseThrow();

		return mapperService.mapToDTO(patientFileItem);
	}

	/**
	 * Service de recherche d'un élément médical par son identifiant.
	 * 
	 * @param uuid l'identifiant de l'élément médical recherché.
	 * @return un objet
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileItemDTO}
	 *         représentant l'élément médical trouvé.
	 * @throws FinderException élément médical non trouvé.
	 */
	public PatientFileItemDTO findPatientFileItem(UUID uuid) throws FinderException {

		Optional<PatientFileItem> optionalPatientFileItem = patientFileItemDAO.findById(uuid);

		if (optionalPatientFileItem.isPresent()) {
			return mapperService.mapToDTO(optionalPatientFileItem.get());
		} else {
			throw new FinderException("Elément médical non trouvé.");
		}
	}

	/**
	 * Service de recherche des éléments médicaux associés à un dossier patient.
	 * 
	 * @param patientFileId l'identifiant du dossier patient.
	 * @return une liste ({@link java.util.List}) d'objets
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileItemDTO}
	 *         représentant les éléments médicaux demandés.
	 */
	public List<PatientFileItemDTO> findPatientFileItemsByPatientFileId(String patientFileId) {

		Iterable<PatientFileItem> patientFileItems = patientFileItemDAO.findByPatientFileId(patientFileId);

		return ((List<PatientFileItem>) patientFileItems).stream()
				.map(mapperService::mapToDTO).collect(Collectors.toList());
	}

	/**
	 * Service de modification d'un élément médical. Les données prises en compte
	 * dans la modification sont les commentaires, l'acte médical pour un acte, la
	 * maladie pour un diagnostic, le texte du courrier et le médecin destinataire
	 * pour un courrier et la description pour une prescription ou un symptôme. Les
	 * autres données ne sont pas considérées.
	 * 
	 * @param patientFileItemDTO l'objet
	 *                           {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileItemDTO}
	 *                           représentant l'élément médical à modifier.
	 * @return un objet
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileItemDTO}
	 *         représentant l'élément médical modifié.
	 * @throws FinderException l'élément médical n'a pas été trouvé.
	 * @throws UpdateException le type de l'élément médical est incorrect ou
	 *                         l'élément médical n'a pas pu être modifié.
	 */
	public PatientFileItemDTO updatePatientFileItem(PatientFileItemDTO patientFileItemDTO)
			throws FinderException, UpdateException {

		Optional<PatientFileItem> optionalPatientFileItem = patientFileItemDAO.findById(patientFileItemDTO.getId());

		if (optionalPatientFileItem.isEmpty()) {
			throw new FinderException("Elément médical non trouvé.");
		}

		PatientFileItem patientFileItem = optionalPatientFileItem.get();

		patientFileItem.setComments(patientFileItemDTO.getComments());

		if (patientFileItemDTO instanceof ActDTO && patientFileItem instanceof Act) {
			((Act) patientFileItem).getMedicalAct().setId(((ActDTO) patientFileItemDTO).getMedicalActDTO().getId());
		} else if (patientFileItemDTO instanceof DiagnosisDTO && patientFileItem instanceof Diagnosis) {
			((Diagnosis) patientFileItem).getDisease()
					.setId(((DiagnosisDTO) patientFileItemDTO).getDiseaseDTO().getId());
		} else if (patientFileItemDTO instanceof MailDTO && patientFileItem instanceof Mail) {
			((Mail) patientFileItem).setText(((MailDTO) patientFileItemDTO).getText());
			((Mail) patientFileItem).getRecipientDoctor().setId(((MailDTO) patientFileItemDTO).getRecipientDoctorId());
		} else if (patientFileItemDTO instanceof PrescriptionDTO && patientFileItem instanceof Prescription) {
			((Prescription) patientFileItem).setDescription(((PrescriptionDTO) patientFileItemDTO).getDescription());
		} else if (patientFileItemDTO instanceof SymptomDTO && patientFileItem instanceof Symptom) {
			((Symptom) patientFileItem).setDescription(((SymptomDTO) patientFileItemDTO).getDescription());
		} else {
			throw new UpdateException("Le type de l'élément médical est incorrect.");
		}

		try {
			patientFileItem = patientFileItemDAO.save(patientFileItem);
		} catch (Exception e) {
			throw new UpdateException("L'élément médical n'a pas pu être modifié.");
		}

		return mapperService.mapToDTO(patientFileItem);
	}

	/**
	 * Service de suppression d'un élément médical désigné par son identifiant.
	 * 
	 * @param uuid l'identifiant de l'élément médical à supprimer.
	 */
	public void deletePatientFileItem(UUID uuid) {

		patientFileItemDAO.deleteById(uuid);
	}

	/**
	 * Service de suppression d'un dossier patient désigné par son identifiant. Les
	 * éventuelles correspondances, éléments médicaux et compte utilisateur associés
	 * au dossier sont également supprimés.
	 * 
	 * @param patientFileId l'identifiant du dossier à supprimer.
	 * @throws DeleteException le dossier patient n'a pas pu être supprimé.
	 */
	public void deletePatientFile(String patientFileId) throws DeleteException {

		correspondenceDAO.deleteAllByPatientFileId(patientFileId);
		patientFileItemDAO.deleteAllByPatientFileId(patientFileId);

		try {
			patientFileDAO.deleteById(patientFileId);
		} catch (Exception e) {
			throw new DeleteException("Le dossier patient n'a pas pu être supprimé.");
		}

		try {
			userService.deleteUser(patientFileId);
		} catch (DeleteException e) {
			System.out.println("Pas de compte utilisateur associé au dossier patient supprimé");
		}
	}

}
