package fr.cnam.stefangeorgesco.dmp.api;

import fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondenceDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileItemDTO;
import fr.cnam.stefangeorgesco.dmp.domain.service.CorrespondenceService;
import fr.cnam.stefangeorgesco.dmp.domain.service.PatientFileItemService;
import fr.cnam.stefangeorgesco.dmp.domain.service.PatientFileService;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CreateException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DeleteException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.UpdateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Contrôleur REST dédié aux dossiers patients et objets rattachés.
 * 
 * @author Stéfan Georgesco
 * 
 */
@RestController
public class PatientFileItemController {

	private final PatientFileService patientFileService;

	private final CorrespondenceService correspondenceService;

	private final PatientFileItemService patientFileItemService;

	public PatientFileItemController(PatientFileService patientFileService,
                                     CorrespondenceService correspondenceService,
                                     PatientFileItemService patientFileItemService) {
		this.patientFileService = patientFileService;
		this.correspondenceService = correspondenceService;
		this.patientFileItemService = patientFileItemService;
	}

	/**
	 * Gestionnaire des requêtes GET de récupération des éléments médicaux associées
	 * au dossier patient correspondant à l'utilisateur connecté (authentifié).
	 * 
	 * @param principal l'utilisateur authentifié.
	 * @return la liste (List) d'objets
	 *         {@link PatientFileItemDTO}
	 *         représentant les éléments médicaux demandés, encapsulée dans un objet
	 *         org.springframework.http.ResponseEntity.
	 */
	@GetMapping("/patient-file/details/item")
	public ResponseEntity<List<PatientFileItemDTO>> findPatientPatientFileItems(Principal principal) {

		String userId = principal.getName();

		return ResponseEntity.ok(patientFileItemService.findPatientFileItemsByPatientFileId(userId));
	}

	/**
	 * Gestionnaire des requêtes POST de création d'un élément médical associée au
	 * dossier patient désigné par son identifiant. L'utilisateur doit être le
	 * médecin référent ou un médecin correspondant (correspondance en cours de
	 * validité).
	 * 
	 * @param patientFileItemDTO l'objet
	 *                           {@link PatientFileItemDTO}
	 *                           représentant l'élément médical à créer.
	 * @param id                 l'identifiant du dossier patient, fourni en
	 *                           variable de chemin.
	 * @param principal          l'utilisateur authentifié.
	 * @return l'objet
	 *         {@link PatientFileItemDTO}
	 *         représentant l'élément médical créé, encapsulé dans un objet
	 *         org.springframework.http.ResponseEntity avec le statut
	 *         {@link HttpStatus#CREATED} en cas de succès.
	 * @throws FinderException le compte utilisateur n'a pas été trouvé, ou le
	 *                         dossier patient n'a pas été trouvé, ou l'utilisateur
	 *                         n'est pas le médecin référent ou correspondant.
	 * @throws CreateException l'élément médical n'a pas pu être créé.
	 */
	@PostMapping("/patient-file/{id}/item")
	public ResponseEntity<PatientFileItemDTO> createPatientFileItem(
			@Valid @RequestBody PatientFileItemDTO patientFileItemDTO, @PathVariable String id, Principal principal)
			throws FinderException, CreateException {

		String userId = principal.getName();

		String referringDoctorId = patientFileService.findPatientFile(id).getReferringDoctorId();

		List<CorrespondenceDTO> correspondencesDTO = correspondenceService.findCorrespondencesByPatientFileId(id);

		LocalDate now = LocalDate.now();

		boolean userIsReferringDoctor = userId.equals(referringDoctorId);

		boolean userIsCorrespondingDoctor = correspondencesDTO.stream()
				.filter(correspondence -> !correspondence.getDateUntil().isBefore(now))
				.map(CorrespondenceDTO::getDoctorId).collect(Collectors.toList()).contains(userId);

		if (!userIsReferringDoctor && !userIsCorrespondingDoctor) {
			throw new FinderException("L'utilisateur n'est pas le médecin référent ou correspondant.");
		}

		patientFileItemDTO.setAuthoringDoctorId(userId);
		patientFileItemDTO.setPatientFileId(id);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(patientFileItemService.createPatientFileItem(patientFileItemDTO));
	}

	/**
	 * Gestionnaire des requêtes GET de récupération des éléments médicaux associés
	 * à un dossier patient désigné par son identifiant. L'utilisateur doit être le
	 * médecin référent ou un médecin correspondant (correspondance en cours de
	 * validité).
	 * 
	 * @param id        : l'identifiant du dossier patient, fourni en variable de
	 *                  chemin.
	 * @param principal l'utilisateur authentifié.
	 * @return la liste (List) d'objets
	 *         {@link PatientFileItemDTO}
	 *         représentant les éléments médicaux demandés, encapsulée dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException le compte utilisateur n'a pas été trouvé, ou le
	 *                         dossier patient n'a pas été trouvé, ou l'utilisateur
	 *                         n'est pas le médecin référent ou correspondant.
	 */
	@GetMapping("/patient-file/{id}/item")
	public ResponseEntity<List<PatientFileItemDTO>> findPatientFileItemsByPatientFileId(@PathVariable String id,
			Principal principal) throws FinderException {

		String userId = principal.getName();

		String referringDoctorId = patientFileService.findPatientFile(id).getReferringDoctorId();

		List<CorrespondenceDTO> correspondencesDTO = correspondenceService.findCorrespondencesByPatientFileId(id);

		LocalDate now = LocalDate.now();

		boolean userIsReferringDoctor = userId.equals(referringDoctorId);

		boolean userIsCorrespondingDoctor = correspondencesDTO.stream()
				.filter(correspondence -> !correspondence.getDateUntil().isBefore(now))
				.map(CorrespondenceDTO::getDoctorId).collect(Collectors.toList()).contains(userId);

		if (!userIsReferringDoctor && !userIsCorrespondingDoctor) {
			throw new FinderException("L'utilisateur n'est pas le médecin référent ou correspondant.");
		}

		return ResponseEntity.ok(patientFileItemService.findPatientFileItemsByPatientFileId(id));
	}

	/**
	 * Gestionnaire des requêtes PUT de modification d'un élément médical désigné
	 * par son identifiant, associé au dossier patient désigné par son identifiant.
	 * L'utilisateur doit être le médecin référent ou un médecin correspondant
	 * (correspondance en cours de validité), et auteur de l'élément médical.
	 * 
	 * @param patientFileItemDTO l'objet
	 *                           {@link PatientFileItemDTO}
	 *                           représentant l'élément médical à modifier.
	 * @param patientFileId      l'identifiant du dossier patient, fourni en
	 *                           variable de chemin.
	 * @param itemId             l'identifiant de l'élément médical, fourni en
	 *                           variable de chemin.
	 * @param principal          l'utilisateur authentifié.
	 * @return l'objet
	 *         {@link PatientFileItemDTO}
	 *         représentant l'élément médical modifié, encapsulé dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException le compte utilisateur n'a pas été trouvé, ou l'élément
	 *                         médical n'a pas été trouvé ou ne correspond pas au
	 *                         dossier patient.
	 * @throws UpdateException l'utilisateur n'est pas l'auteur de l'élément
	 *                         médical.
	 */
	@PutMapping("/patient-file/{patientFileId}/item/{itemId}")
	public ResponseEntity<PatientFileItemDTO> updatePatientFileItem(
			@Valid @RequestBody PatientFileItemDTO patientFileItemDTO, @PathVariable String patientFileId,
			@PathVariable String itemId, Principal principal) throws FinderException, UpdateException {

		String userId = principal.getName();

		UUID patientFileItemId = UUID.fromString(itemId);

		patientFileItemDTO.setId(patientFileItemId);

		PatientFileItemDTO storedPatientFileItemDTO = patientFileItemService.findPatientFileItem(patientFileItemId);

		if (!patientFileId.equals(storedPatientFileItemDTO.getPatientFileId())) {
			throw new FinderException("Elément médical non trouvé pour le dossier patient '" + patientFileId + "'");
		}

		boolean userIsAuthor = userId.equals(storedPatientFileItemDTO.getAuthoringDoctorId());

		if (!userIsAuthor) {
			throw new UpdateException(
					"L'utilisateur n'est pas l'auteur de l'élément médical et ne peut pas le modifier.");
		}

		String referringDoctorId = patientFileService.findPatientFile(patientFileId).getReferringDoctorId();

		List<CorrespondenceDTO> correspondencesDTO = correspondenceService
				.findCorrespondencesByPatientFileId(patientFileId);

		LocalDate now = LocalDate.now();

		boolean userIsReferringDoctor = userId.equals(referringDoctorId);

		boolean userIsCorrespondingDoctor = correspondencesDTO.stream()
				.filter(correspondence -> !correspondence.getDateUntil().isBefore(now))
				.map(CorrespondenceDTO::getDoctorId).collect(Collectors.toList()).contains(userId);

		if (!userIsReferringDoctor && !userIsCorrespondingDoctor) {
			throw new FinderException("L'utilisateur n'est pas le médecin référent ou correspondant.");
		}

		return ResponseEntity.ok(patientFileItemService.updatePatientFileItem(patientFileItemDTO));
	}

	/**
	 * Gestionnaire des requêtes DELETE de suppression d'un élément médical désigné
	 * par son identifiant, associé au dossier patient désigné par son identifiant.
	 * L'utilisateur doit être le médecin référent ou un médecin correspondant
	 * (correspondance en cours de validité), et auteur de l'élément médical.
	 * 
	 * @param patientFileId l'identifiant du dossier patient, fourni en variable de
	 *                      chemin.
	 * @param itemId        l'identifiant de l'élément médical, fourni en variable
	 *                      de chemin.
	 * @param principal     l'utilisateur authentifié.
	 * @return une réponse {@link RestResponse} encapsulée dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException le compte utilisateur n'a pas été trouvé, ou le
	 *                         dossier patient n'a pas été trouvé, ou l'élément
	 *                         médical n'a pas été trouvé ou ne correspond pas au
	 *                         dossier patient.
	 * @throws DeleteException l'utilisateur n'est pas le médecin référent ou
	 *                         correspondant ou il n'est pas l'auteur de l'élément
	 *                         médical.
	 */
	@DeleteMapping("/patient-file/{patientFileId}/item/{itemId}")
	public ResponseEntity<RestResponse> deletePatientFileItem(@PathVariable String patientFileId,
			@PathVariable String itemId, Principal principal) throws FinderException, DeleteException {

		String userId = principal.getName();

		UUID patientFileItemId = UUID.fromString(itemId);

		PatientFileItemDTO storedPatientFileItemDTO = patientFileItemService.findPatientFileItem(patientFileItemId);

		if (!patientFileId.equals(storedPatientFileItemDTO.getPatientFileId())) {
			throw new FinderException("Elément médical non trouvé pour le dossier patient '" + patientFileId + "'");
		}

		boolean userIsAuthor = userId.equals(storedPatientFileItemDTO.getAuthoringDoctorId());

		if (!userIsAuthor) {
			throw new DeleteException(
					"L'utilisateur n'est pas l'auteur de l'élément médical et ne peut pas le supprimer.");
		}

		String referringDoctorId = patientFileService.findPatientFile(patientFileId).getReferringDoctorId();

		List<CorrespondenceDTO> correspondencesDTO = correspondenceService
				.findCorrespondencesByPatientFileId(patientFileId);

		LocalDate now = LocalDate.now();

		boolean userIsReferringDoctor = userId.equals(referringDoctorId);

		boolean userIsCorrespondingDoctor = correspondencesDTO.stream()
				.filter(correspondence -> !correspondence.getDateUntil().isBefore(now))
				.map(CorrespondenceDTO::getDoctorId).collect(Collectors.toList()).contains(userId);

		if (!userIsReferringDoctor && !userIsCorrespondingDoctor) {
			throw new DeleteException("L'utilisateur n'est pas le médecin référent ou correspondant.");
		}

		patientFileItemService.deletePatientFileItem(patientFileItemId);

		RestResponse response = new RestResponse(HttpStatus.OK.value(), "L'élément médical a bien été supprimé.");

		return ResponseEntity.ok(response);
	}
}
