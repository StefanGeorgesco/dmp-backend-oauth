package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dao.*;
import fr.cnam.stefangeorgesco.dmp.domain.dto.*;
import fr.cnam.stefangeorgesco.dmp.domain.model.*;
import fr.cnam.stefangeorgesco.dmp.exception.domain.*;
import org.springframework.stereotype.Service;

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
public class PatientFileItemServiceImpl implements PatientFileItemService {

	private final PatientFileItemDAO patientFileItemDAO;

	private final MapperService mapperService;

	public PatientFileItemServiceImpl(PatientFileItemDAO patientFileItemDAO, MapperService mapperService) {
		this.patientFileItemDAO = patientFileItemDAO;
		this.mapperService = mapperService;
	}

	/**
	 * Service de création d'un élément médical.
	 * 
	 * @param patientFileItemDTO l'objet
	 *                           {@link PatientFileItemDTO}
	 *                           représentant l'élément médical à créer.
	 * @return un objet
	 *         {@link PatientFileItemDTO}
	 *         représentant l'élément médical créé.
	 * @throws CreateException l'élément médical n'a pas pu être créé.
	 */
	@Override
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
	 *         {@link PatientFileItemDTO}
	 *         représentant l'élément médical trouvé.
	 * @throws FinderException élément médical non trouvé.
	 */
	@Override
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
	 * @return une liste ({@link List}) d'objets
	 *         {@link PatientFileItemDTO}
	 *         représentant les éléments médicaux demandés.
	 */
	@Override
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
	 *                           {@link PatientFileItemDTO}
	 *                           représentant l'élément médical à modifier.
	 * @return un objet
	 *         {@link PatientFileItemDTO}
	 *         représentant l'élément médical modifié.
	 * @throws FinderException l'élément médical n'a pas été trouvé.
	 * @throws UpdateException le type de l'élément médical est incorrect ou
	 *                         l'élément médical n'a pas pu être modifié.
	 */
	@Override
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
	@Override
	public void deletePatientFileItem(UUID uuid) {

		patientFileItemDAO.deleteById(uuid);
	}
}
