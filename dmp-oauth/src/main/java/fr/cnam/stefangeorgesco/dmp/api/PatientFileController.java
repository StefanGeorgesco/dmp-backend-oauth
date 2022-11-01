package fr.cnam.stefangeorgesco.dmp.api;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.service.UserService;
import fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondenceDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DiseaseDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.MedicalActDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileItemDTO;
import fr.cnam.stefangeorgesco.dmp.domain.service.PatientFileService;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CreateException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DeleteException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.UpdateException;

/**
 * Contrôleur REST dédié aux dossiers patients et objets rattachés.
 * 
 * @author Stéfan Georgesco
 * 
 */
@RestController
public class PatientFileController {

	@Autowired
	private UserService userService;

	@Autowired
	private PatientFileService patientFileService;

	/**
	 * Gestionnaire des requêtes POST de création des dossiers patients.
	 * 
	 * @param patientFileDTO l'objet
	 *                       {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
	 *                       représentant le dossier patient à créer. Le médecin
	 *                       référent du dossier patient est initialisé avec le
	 *                       dossier de médecin associé à l'utilisateur connecté
	 *                       (authentifié).
	 * @param principal      l'utilisateur authentifié.
	 * @return l'objet {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
	 *         représentant le dossier patient créé, encapsulé dans un objet
	 *         org.springframework.http.ResponseEntity avec le statut
	 *         {@link org.springframework.http.HttpStatus#CREATED} en cas de succès.
	 * @throws FinderException le compte utilisateur n'a pas été trouvé.
	 * @throws CheckException  le dossier patient n'existe pas au RNIPP.
	 * @throws CreateException le dossier patient n'a pas pu être créé.
	 */
	@PostMapping("/patient-file")
	public ResponseEntity<PatientFileDTO> createPatientFile(@Valid @RequestBody PatientFileDTO patientFileDTO,
			Principal principal) throws FinderException, CheckException, CreateException {

		UserDTO userDTO = userService.findUserByUsername(principal.getName());

		patientFileDTO.setReferringDoctorId(userDTO.getId());

		return ResponseEntity.status(HttpStatus.CREATED).body(patientFileService.createPatientFile(patientFileDTO));
	}

	/**
	 * Gestionnaire des requêtes PUT de modification du dossier patient
	 * correspondant à l'utilisateur connecté (authentifé).
	 * 
	 * @param patientFileDTO l'objet
	 *                       {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
	 *                       représentant le dossier patient à modifier et les
	 *                       données à modifier.
	 * @param principal      l'utilisateur authentifié.
	 * @return l'objet {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
	 *         représentant le dossier patient modifié, encapsulé dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException le compte utilisateur n'a pas été trouvé.
	 * @throws UpdateException le dossier patient n'a pas pu être modifié.
	 * @see PatientFileService#updatePatientFile(PatientFileDTO)
	 */
	@PutMapping("/patient-file/details")
	public ResponseEntity<PatientFileDTO> updatePatientFile(@Valid @RequestBody PatientFileDTO patientFileDTO,
			Principal principal) throws FinderException, UpdateException {

		UserDTO userDTO = userService.findUserByUsername(principal.getName());

		patientFileDTO.setId(userDTO.getId());

		return ResponseEntity.ok(patientFileService.updatePatientFile(patientFileDTO));
	}

	/**
	 * Gestionnaire des requêtes GET de consultation du dossier patient
	 * correspondant à l'utilisateur connecté (authentifé).
	 * 
	 * @param principal l'utilisateur authentifié.
	 * @return l'objet {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
	 *         représentant le dossier patient consulté, encapsulé dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException le compte utilisateur n'a pas été trouvé.
	 */
	@GetMapping("/patient-file/details")
	public ResponseEntity<PatientFileDTO> getPatientFileDetails(Principal principal) throws FinderException {

		UserDTO userDTO = userService.findUserByUsername(principal.getName());

		return ResponseEntity.ok(patientFileService.findPatientFile(userDTO.getId()));
	}

	/**
	 * Gestionnaire des requêtes GET de récupération des correspondances associées
	 * au dossier patient correspondant à l'utilisateur connecté (authentifé).
	 * 
	 * @param principal l'utilisateur authentifié.
	 * @return la liste (List) d'objets
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondenceDTO}
	 *         représentant les correspondances demandées, encapsulée dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException le compte utilisateur n'a pas été trouvé.
	 */
	@GetMapping("/patient-file/details/correspondence")
	public ResponseEntity<List<CorrespondenceDTO>> findPatientCorrespondences(Principal principal)
			throws FinderException {

		String userId = userService.findUserByUsername(principal.getName()).getId();

		return ResponseEntity.ok(patientFileService.findCorrespondencesByPatientFileId(userId));
	}

	/**
	 * Gestionnaire des requêtes GET de récupération des éléments médicaux associées
	 * au dossier patient correspondant à l'utilisateur connecté (authentifé).
	 * 
	 * @param principal l'utilisateur authentifié.
	 * @return la liste (List) d'objets
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileItemDTO}
	 *         représentant les éléments médicaux demandés, encapsulée dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException le compte utilisateur n'a pas été trouvé.
	 */
	@GetMapping("/patient-file/details/item")
	public ResponseEntity<List<PatientFileItemDTO>> findPatientPatientFileItems(Principal principal)
			throws FinderException {

		String userId = userService.findUserByUsername(principal.getName()).getId();

		return ResponseEntity.ok(patientFileService.findPatientFileItemsByPatientFileId(userId));
	}

	/**
	 * Gestionnaire des requêtes GET de consultation du dossier patient désigné par
	 * son identifiant.
	 * 
	 * @param id l'identifiant du dossier patient, fourni en variable de chemin.
	 * @return l'objet {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
	 *         représentant le dossier patient consulté, encapsulé dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException dossier patient non trouvé.
	 */
	@GetMapping("/patient-file/{id}")
	public ResponseEntity<PatientFileDTO> getPatientFileDetails(@PathVariable String id) throws FinderException {

		return ResponseEntity.ok(patientFileService.findPatientFile(id));
	}

	/**
	 * Gestionnaire des requêtes DELETE de suppression du dossier patient désigné
	 * par son identifiant.
	 * 
	 * @param id l'identifiant du dossier patient, fourni en variable de chemin.
	 * @return une réponse {@link RestResponse} encapsulée dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws DeleteException le dossier patient n'a pas pu être supprimé.
	 */
	@DeleteMapping("/patient-file/{id}")
	public ResponseEntity<RestResponse> deletePatientFile(@PathVariable String id) throws DeleteException {

		patientFileService.deletePatientFile(id);

		RestResponse response = new RestResponse(HttpStatus.OK.value(), "Le dossier patient a bien été supprimé.");

		return ResponseEntity.ok(response);
	}

	/**
	 * Gestionnaire des requêtes PUT de modification du médecin référent du dossier
	 * patient désingé par son identifiant.
	 * 
	 * @param id        l'identifiant du dossier patient, fourni en variable de
	 *                  chemin.
	 * @param doctorDTO le dossier de médecin du nouveau médecin référent.
	 * @return l'objet {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
	 *         représentant le dossier patient modifié, encapsulé dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException dossier patient non trouvé.
	 * @throws UpdateException le dossier patient n'a pas pu être modifié.
	 */
	@PutMapping("/patient-file/{id}/referring-doctor")
	public ResponseEntity<PatientFileDTO> updateReferringDoctor(@PathVariable String id,
			@Valid @RequestBody DoctorDTO doctorDTO) throws FinderException, UpdateException {

		PatientFileDTO patientFileDTO = patientFileService.findPatientFile(id);

		patientFileDTO.setReferringDoctorId(doctorDTO.getId());

		return ResponseEntity.ok(patientFileService.updateReferringDoctor(patientFileDTO));
	}

	/**
	 * Gestionnaire des requêtes GET de récupération des dossiers patient trouvés
	 * par une recherche à partir d'une chaîne de caractères.
	 * 
	 * @param q la chaîne de caractère (String) utilisée pour la recherche, fournie
	 *          en paramètre de requête.
	 * @return une liste ({@link java.util.List}) d'objets
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
	 *         représentant le résultat de la recherche, encapsulée dans un objet
	 *         org.springframework.http.ResponseEntity.
	 */
	@GetMapping("/patient-file")
	public ResponseEntity<List<PatientFileDTO>> findPatientFilesByIdOrFirstnameOrLastname(@RequestParam String q) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(patientFileService.findPatientFilesByIdOrFirstnameOrLastname(q));
	}

	/**
	 * Gestionnaire des requêtes POST de création d'un correspondance associée au
	 * dossier patient désigné par son identifiant. L'utilisateur doit être le
	 * médecin référent et il ne peut pas créer une correspondance pour lui-même.
	 * 
	 * @param correspondenceDTO l'objet
	 *                          {@link fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondenceDTO}
	 *                          représentant la correspondance à créer.
	 * @param id                l'identifiant du dossier patient, fourni en variable
	 *                          de chemin.
	 * @param principal         l'utilisateur authentifié.
	 * @return l'objet
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondenceDTO}
	 *         représentant la correspondance créée, encapsulé dans un objet
	 *         org.springframework.http.ResponseEntity avec le statut
	 *         {@link org.springframework.http.HttpStatus#CREATED} en cas de succès.
	 * @throws FinderException le compte utilisateur n'a pas été trouvé ou le
	 *                         dossier patient n'a pas été trouvé.
	 * @throws CreateException l'utilisateur n'est pas le médecin référent ou on a
	 *                         voulu créer une correspondance pour le médecin
	 *                         référent ou la correspondance n'a pas pu être créé.
	 */
	@PostMapping("/patient-file/{id}/correspondence")
	public ResponseEntity<CorrespondenceDTO> createCorrespondence(
			@Valid @RequestBody CorrespondenceDTO correspondenceDTO, @PathVariable String id, Principal principal)
			throws FinderException, CreateException {

		UserDTO userDTO = userService.findUserByUsername(principal.getName());

		PatientFileDTO patientFileDTO = patientFileService.findPatientFile(id);

		if (!userDTO.getId().equals(patientFileDTO.getReferringDoctorId())) {
			throw new CreateException("L'utilisateur n'est pas le médecin référent.");
		}

		if (userDTO.getId().equals(correspondenceDTO.getDoctorId())) {
			throw new CreateException("Impossible de créer une correspondance pour le médecin référent.");
		}

		correspondenceDTO.setPatientFileId(patientFileDTO.getId());

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(patientFileService.createCorrespondence(correspondenceDTO));

	}

	/**
	 * Gestionnaire des requêtes POST de création d'un élément médical associée au
	 * dossier patient désigné par son identifiant. L'utilisateur doit être le
	 * médecin référent ou un médecin correspondant (correspondance en cours de
	 * validité).
	 * 
	 * @param patientFileItemDTO l'objet
	 *                           {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileItemDTO}
	 *                           représentant l'élément médical à créer.
	 * @param id                 l'identifiant du dossier patient, fourni en
	 *                           variable de chemin.
	 * @param principal          l'utilisateur authentifié.
	 * @return l'objet
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileItemDTO}
	 *         représentant l'élément médical créé, encapsulé dans un objet
	 *         org.springframework.http.ResponseEntity avec le statut
	 *         {@link org.springframework.http.HttpStatus#CREATED} en cas de succès.
	 * @throws FinderException le compte utilisateur n'a pas été trouvé ou le
	 *                         dossier patient n'a pas été trouvé ou l'utilisateur
	 *                         n'est pas le médecin référent ou correspondant.
	 * @throws CreateException l'élément médical n'a pas pu être créé.
	 */
	@PostMapping("/patient-file/{id}/item")
	public ResponseEntity<PatientFileItemDTO> createPatientFileItem(
			@Valid @RequestBody PatientFileItemDTO patientFileItemDTO, @PathVariable String id, Principal principal)
			throws FinderException, CreateException {

		String userId = userService.findUserByUsername(principal.getName()).getId();

		String referringDoctorId = patientFileService.findPatientFile(id).getReferringDoctorId();

		List<CorrespondenceDTO> correspondencesDTO = patientFileService.findCorrespondencesByPatientFileId(id);

		LocalDate now = LocalDate.now();

		boolean userIsReferringDoctor = userId.equals(referringDoctorId);

		boolean userIsCorrespondingDoctor = correspondencesDTO.stream()
				.filter(correspondence -> correspondence.getDateUntil().compareTo(now) >= 0)
				.map(CorrespondenceDTO::getDoctorId).collect(Collectors.toList()).contains(userId);

		if (!userIsReferringDoctor && !userIsCorrespondingDoctor) {
			throw new FinderException("L'utilisateur n'est pas le médecin référent ou correspondant.");
		}

		patientFileItemDTO.setAuthoringDoctorId(userId);
		patientFileItemDTO.setPatientFileId(id);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(patientFileService.createPatientFileItem(patientFileItemDTO));
	}

	/**
	 * Gestionnaire des requêtes GET de récupération des correspondances associées à
	 * un dossier patient désigné par son identifiant. L'utilisateur doit être le
	 * médecin référent ou un médecin correspondant (correspondance en cours de
	 * validité).
	 * 
	 * @param id        : l'identifiant du dossier patient, fourni en variable de
	 *                  chemin.
	 * @param principal l'utilisateur authentifié.
	 * @return la liste (List) d'objets
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondenceDTO}
	 *         représentant les correspondances demandées, encapsulée dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException le compte utilisateur n'a pas été trouvé ou le
	 *                         dossier patient n'a pas été trouvé ou l'utilisateur
	 *                         n'est pas le médecin référent ou correspondant.
	 */
	@GetMapping("/patient-file/{id}/correspondence")
	public ResponseEntity<List<CorrespondenceDTO>> findCorrespondencesByPatientFileId(@PathVariable String id,
			Principal principal) throws FinderException {

		String userId = userService.findUserByUsername(principal.getName()).getId();

		String referringDoctorId = patientFileService.findPatientFile(id).getReferringDoctorId();

		List<CorrespondenceDTO> correspondencesDTO = patientFileService.findCorrespondencesByPatientFileId(id);

		LocalDate now = LocalDate.now();

		boolean userIsReferringDoctor = userId.equals(referringDoctorId);

		boolean userIsCorrespondingDoctor = correspondencesDTO.stream()
				.filter(correspondence -> correspondence.getDateUntil().compareTo(now) >= 0)
				.map(CorrespondenceDTO::getDoctorId).collect(Collectors.toList()).contains(userId);

		if (!userIsReferringDoctor && !userIsCorrespondingDoctor) {
			throw new FinderException("L'utilisateur n'est pas le médecin référent ou correspondant.");
		}

		return ResponseEntity.ok(correspondencesDTO);

	}

	/**
	 * Gestionnaire des requêtes GET de récupération des éléménts médicaux associées
	 * à un dossier patient désigné par son identifiant. L'utilisateur doit être le
	 * médecin référent ou un médecin correspondant (correspondance en cours de
	 * validité).
	 * 
	 * @param id        : l'identifiant du dossier patient, fourni en variable de
	 *                  chemin.
	 * @param principal l'utilisateur authentifié.
	 * @return la liste (List) d'objets
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileItemDTO}
	 *         représentant les éléments médicaux demandés, encapsulée dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException le compte utilisateur n'a pas été trouvé ou le
	 *                         dossier patient n'a pas été trouvé ou l'utilisateur
	 *                         n'est pas le médecin référent ou correspondant.
	 */
	@GetMapping("/patient-file/{id}/item")
	public ResponseEntity<List<PatientFileItemDTO>> findPatientFileItemsByPatientFileId(@PathVariable String id,
			Principal principal) throws FinderException {

		String userId = userService.findUserByUsername(principal.getName()).getId();

		String referringDoctorId = patientFileService.findPatientFile(id).getReferringDoctorId();

		List<CorrespondenceDTO> correspondencesDTO = patientFileService.findCorrespondencesByPatientFileId(id);

		LocalDate now = LocalDate.now();

		boolean userIsReferringDoctor = userId.equals(referringDoctorId);

		boolean userIsCorrespondingDoctor = correspondencesDTO.stream()
				.filter(correspondence -> correspondence.getDateUntil().compareTo(now) >= 0)
				.map(CorrespondenceDTO::getDoctorId).collect(Collectors.toList()).contains(userId);

		if (!userIsReferringDoctor && !userIsCorrespondingDoctor) {
			throw new FinderException("L'utilisateur n'est pas le médecin référent ou correspondant.");
		}

		return ResponseEntity.ok(patientFileService.findPatientFileItemsByPatientFileId(id));
	}

	/**
	 * Gestionnaire des requêtes PUT de modification d'un élémént médical désigné
	 * par son identifiant, associé au dossier patient désigné par son identifiant.
	 * L'utilisateur doit être le médecin référent ou un médecin correspondant
	 * (correspondance en cours de validité), et auteur de l'élément médical.
	 * 
	 * @param patientFileItemDTO l'objet
	 *                           {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileItemDTO}
	 *                           représentant l'élément médical à modifier.
	 * @param patienfFileId      l'identifiant du dossier patient, fourni en
	 *                           variable de chemin.
	 * @param itemId             l'identifiant de l'élément médical, fourni en
	 *                           variable de chemin.
	 * @param principal          l'utilisateur authentifié.
	 * @return l'objet
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileItemDTO}
	 *         représentant l'élément médical modifié, encapsulé dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException le compte utilisateur n'a pas été trouvé ou l'élément
	 *                         médical n'a pas été trouvé ou ne correspond pas au
	 *                         dossier patient.
	 * @throws UpdateException l'utilisateur n'est pas l'auteur de l'élément
	 *                         médical.
	 */
	@PutMapping("/patient-file/{patienfFileId}/item/{itemId}")
	public ResponseEntity<PatientFileItemDTO> updatePatientFileItem(
			@Valid @RequestBody PatientFileItemDTO patientFileItemDTO, @PathVariable String patienfFileId,
			@PathVariable String itemId, Principal principal) throws FinderException, UpdateException {

		String userId = userService.findUserByUsername(principal.getName()).getId();

		UUID patientFileItemId = UUID.fromString(itemId);

		patientFileItemDTO.setId(patientFileItemId);

		PatientFileItemDTO storedPatientFileItemDTO = patientFileService.findPatientFileItem(patientFileItemId);

		if (!patienfFileId.equals(storedPatientFileItemDTO.getPatientFileId())) {
			throw new FinderException("Elément médical non trouvé pour le dossier patient '" + patienfFileId + "'");
		}

		boolean userIsAuthor = userId.equals(storedPatientFileItemDTO.getAuthoringDoctorId());

		if (!userIsAuthor) {
			throw new UpdateException(
					"L'utilisateur n'est pas l'auteur de l'élément médical et ne peut pas le modifier.");
		}

		String referringDoctorId = patientFileService.findPatientFile(patienfFileId).getReferringDoctorId();

		List<CorrespondenceDTO> correspondencesDTO = patientFileService
				.findCorrespondencesByPatientFileId(patienfFileId);

		LocalDate now = LocalDate.now();

		boolean userIsReferringDoctor = userId.equals(referringDoctorId);

		boolean userIsCorrespondingDoctor = correspondencesDTO.stream()
				.filter(correspondence -> correspondence.getDateUntil().compareTo(now) >= 0)
				.map(CorrespondenceDTO::getDoctorId).collect(Collectors.toList()).contains(userId);

		if (!userIsReferringDoctor && !userIsCorrespondingDoctor) {
			throw new FinderException("L'utilisateur n'est pas le médecin référent ou correspondant.");
		}

		return ResponseEntity.ok(patientFileService.updatePatientFileItem(patientFileItemDTO));
	}

	/**
	 * Gestionnaire des requêtes DELETE de suppression d'un correspondance désingée
	 * par son identifiant, associée au dossier patient désigné par son identifiant.
	 * L'utilisateur doit être le médecin référent.
	 * 
	 * @param patientFileId    l'identifiant du dossier patient, fourni en variable
	 *                         de chemin.
	 * @param correspondenceId l'identifiant de la correspondance, fournie en
	 *                         variable de chemin.
	 * @param principal        l'utilisateur authentifié.
	 * @return une réponse {@link RestResponse} encapsulée dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException le compte utilisateur n'a pas été trouvé ou le
	 *                         dossier patient n'a pas été trouvé ou la
	 *                         correspondance n'a pas été trouvée ou ne correspond
	 *                         pas au dossier patient.
	 * @throws DeleteException l'utilisateur n'est pas le médecin référent.
	 */
	@DeleteMapping("/patient-file/{patientFileId}/correspondence/{correspondenceId}")
	public ResponseEntity<RestResponse> deleteCorrespondence(@PathVariable String patientFileId,
			@PathVariable String correspondenceId, Principal principal) throws FinderException, DeleteException {

		UserDTO userDTO = userService.findUserByUsername(principal.getName());

		PatientFileDTO patientFileDTO = patientFileService.findPatientFile(patientFileId);

		if (!userDTO.getId().equals(patientFileDTO.getReferringDoctorId())) {
			throw new DeleteException("L'utilisateur n'est pas le médecin référent.");
		}

		CorrespondenceDTO storedCorrespondenceDTO = patientFileService.findCorrespondence(correspondenceId);

		if (!patientFileId.equals(storedCorrespondenceDTO.getPatientFileId())) {
			throw new FinderException("Correspondance non trouvée pour le dossier patient '" + patientFileId + "'");
		}

		patientFileService.deleteCorrespondence(UUID.fromString(correspondenceId));

		RestResponse response = new RestResponse(HttpStatus.OK.value(), "La correspondance a bien été supprimée.");

		return ResponseEntity.ok(response);
	}

	/**
	 * Gestionnaire des requêtes DELETE de suppression d'un élémént médical désigné
	 * par son identifiant, associé au dossier patient désigné par son identifiant.
	 * L'utilisateur doit être le médecin référent ou un médecin correspondant
	 * (correspondance en cours de validité), et auteur de l'élément médical.
	 * 
	 * @param patienfFileId l'identifiant du dossier patient, fourni en variable de
	 *                      chemin.
	 * @param itemId        l'identifiant de l'élément médical, fourni en variable
	 *                      de chemin.
	 * @param principal     l'utilisateur authentifié.
	 * @return une réponse {@link RestResponse} encapsulée dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException le compte utilisateur n'a pas été trouvé ou le
	 *                         dossier patient n'a pas été trouvé ou l'élément
	 *                         médical n'a pas été trouvé ou ne correspond pas au
	 *                         dossier patient.
	 * @throws DeleteException l'utilisateur n'est pas le médecin référent ou
	 *                         correspondant ou il n'est pas l'auteur de l'élément
	 *                         médical.
	 */
	@DeleteMapping("/patient-file/{patienfFileId}/item/{itemId}")
	public ResponseEntity<RestResponse> deletePatientFileItem(@PathVariable String patienfFileId,
			@PathVariable String itemId, Principal principal) throws FinderException, DeleteException {

		String userId = userService.findUserByUsername(principal.getName()).getId();

		UUID patientFileItemId = UUID.fromString(itemId);

		PatientFileItemDTO storedPatientFileItemDTO = patientFileService.findPatientFileItem(patientFileItemId);

		if (!patienfFileId.equals(storedPatientFileItemDTO.getPatientFileId())) {
			throw new FinderException("Elément médical non trouvé pour le dossier patient '" + patienfFileId + "'");
		}

		boolean userIsAuthor = userId.equals(storedPatientFileItemDTO.getAuthoringDoctorId());

		if (!userIsAuthor) {
			throw new DeleteException(
					"L'utilisateur n'est pas l'auteur de l'élément médical et ne peut pas le supprimer.");
		}

		String referringDoctorId = patientFileService.findPatientFile(patienfFileId).getReferringDoctorId();

		List<CorrespondenceDTO> correspondencesDTO = patientFileService
				.findCorrespondencesByPatientFileId(patienfFileId);

		LocalDate now = LocalDate.now();

		boolean userIsReferringDoctor = userId.equals(referringDoctorId);

		boolean userIsCorrespondingDoctor = correspondencesDTO.stream()
				.filter(correspondence -> correspondence.getDateUntil().compareTo(now) >= 0)
				.map(CorrespondenceDTO::getDoctorId).collect(Collectors.toList()).contains(userId);

		if (!userIsReferringDoctor && !userIsCorrespondingDoctor) {
			throw new DeleteException("L'utilisateur n'est pas le médecin référent ou correspondant.");
		}

		patientFileService.deletePatientFileItem(patientFileItemId);

		RestResponse response = new RestResponse(HttpStatus.OK.value(), "L'élément médical a bien été supprimé.");

		return ResponseEntity.ok(response);

	}

	/**
	 * Gestionnaire des requêtes GET de consultation d'une maladie désignée par son
	 * identifiant.
	 * 
	 * @param id l'identifiant de la maladie, fourni en variable de chemin.
	 * @return un objet {@link fr.cnam.stefangeorgesco.dmp.domain.dto.DiseaseDTO}
	 *         représentant la maladie consultée, encapsulé dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException maladie non trouvée.
	 */
	@GetMapping("/disease/{id}")
	public ResponseEntity<DiseaseDTO> getDisease(@PathVariable String id) throws FinderException {

		return ResponseEntity.ok(patientFileService.findDisease(id));
	}

	/**
	 * Gestionnaire des requêtes GET de récupération des maladies trouvées par une
	 * recherche à partir d'une chaîne de caractères.
	 * 
	 * @param q     la chaîne de caractère (String) utilisée pour la recherche,
	 *              fournie en paramètre de requête.
	 * @param limit le nombre maximum d'objets récupérés, paramètre de requête
	 *              optionel. Il est fixé à 30 par défaut.
	 * @return une liste ({@link java.util.List}) d'objets
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.DiseaseDTO}
	 *         représentant le résultat de la recherche, encapsulée dans un objet
	 *         org.springframework.http.ResponseEntity.
	 */
	@GetMapping("/disease")
	public ResponseEntity<List<DiseaseDTO>> getDiseases(@RequestParam String q,
			@RequestParam(required = false, defaultValue = "30") int limit) {

		return ResponseEntity.ok(patientFileService.findDiseasesByIdOrDescription(q, limit));
	}

	/**
	 * Gestionnaire des requêtes GET de consultation d'un acte médical désignée par
	 * son identifiant.
	 * 
	 * @param id l'identifiant de l'élément médical, fourni en variable de chemin.
	 * @return un objet {@link fr.cnam.stefangeorgesco.dmp.domain.dto.MedicalActDTO}
	 *         représentant l'acte médical consulté, encapsulé dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException acte médical non trouvé.
	 */
	@GetMapping("/medical-act/{id}")
	public ResponseEntity<MedicalActDTO> getMedicalAct(@PathVariable String id) throws FinderException {

		return ResponseEntity.ok(patientFileService.findMedicalAct(id));
	}

	/**
	 * Gestionnaire des requêtes GET de récupération des actes médicaux trouvés par
	 * une recherche à partir d'une chaîne de caractères.
	 * 
	 * @param q     la chaîne de caractère (String) utilisée pour la recherche,
	 *              fournie en paramètre de requête.
	 * @param limit le nombre maximum d'objets récupérés, paramètre de requête
	 *              optionel. Il est fixé à 30 par défaut.
	 * @return une liste ({@link java.util.List}) d'objets
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.MedicalActDTO}
	 *         représentant le résultat de la recherche, encapsulée dans un objet
	 *         org.springframework.http.ResponseEntity.
	 */
	@GetMapping("/medical-act")
	public ResponseEntity<List<MedicalActDTO>> getMedicalActs(@RequestParam String q,
			@RequestParam(required = false, defaultValue = "30") int limit) {

		return ResponseEntity.ok(patientFileService.findMedicalActsByIdOrDescription(q, limit));
	}

}
