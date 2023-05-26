package fr.cnam.stefangeorgesco.dmp.api;

import fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondenceDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.domain.service.CorrespondenceService;
import fr.cnam.stefangeorgesco.dmp.domain.service.PatientFileService;
import fr.cnam.stefangeorgesco.dmp.exception.domain.*;
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
public class CorrespondenceController {

	private final PatientFileService patientFileService;

	private final CorrespondenceService correspondenceService;

	public CorrespondenceController(PatientFileService patientFileService,
									CorrespondenceService correspondenceService) {
		this.patientFileService = patientFileService;
		this.correspondenceService = correspondenceService;
	}

	/**
	 * Gestionnaire des requêtes GET de récupération des correspondances associées
	 * au dossier patient correspondant à l'utilisateur connecté (authentifié).
	 * 
	 * @param principal l'utilisateur authentifié.
	 * @return la liste (List) d'objets
	 *         {@link CorrespondenceDTO}
	 *         représentant les correspondances demandées, encapsulée dans un objet
	 *         org.springframework.http.ResponseEntity.
	 */
	@GetMapping("/patient-file/details/correspondence")
	public ResponseEntity<List<CorrespondenceDTO>> findPatientCorrespondences(Principal principal) {

		String userId = principal.getName();

		return ResponseEntity.ok(correspondenceService.findCorrespondencesByPatientFileId(userId));
	}

	/**
	 * Gestionnaire des requêtes POST de création d'une correspondance associée au
	 * dossier patient désigné par son identifiant. L'utilisateur doit être le
	 * médecin référent et il ne peut pas créer une correspondance pour lui-même.
	 * 
	 * @param correspondenceDTO l'objet
	 *                          {@link CorrespondenceDTO}
	 *                          représentant la correspondance à créer.
	 * @param id                l'identifiant du dossier patient, fourni en variable
	 *                          de chemin.
	 * @param principal         l'utilisateur authentifié.
	 * @return l'objet
	 *         {@link CorrespondenceDTO}
	 *         représentant la correspondance créée, encapsulé dans un objet
	 *         org.springframework.http.ResponseEntity avec le statut
	 *         {@link HttpStatus#CREATED} en cas de succès.
	 * @throws FinderException le compte utilisateur n'a pas été trouvé, ou le
	 *                         dossier patient n'a pas été trouvé.
	 * @throws CreateException l'utilisateur n'est pas le médecin référent ou on a
	 *                         voulu créer une correspondance pour le médecin
	 *                         référent ou la correspondance n'a pas pu être créé.
	 */
	@PostMapping("/patient-file/{id}/correspondence")
	public ResponseEntity<CorrespondenceDTO> createCorrespondence(
			@Valid @RequestBody CorrespondenceDTO correspondenceDTO, @PathVariable String id, Principal principal)
			throws FinderException, CreateException {

		String userId = principal.getName();

		PatientFileDTO patientFileDTO = patientFileService.findPatientFile(id);

		if (!userId.equals(patientFileDTO.getReferringDoctorId())) {
			throw new CreateException("L'utilisateur n'est pas le médecin référent.");
		}

		if (userId.equals(correspondenceDTO.getDoctorId())) {
			throw new CreateException("Impossible de créer une correspondance pour le médecin référent.");
		}

		correspondenceDTO.setPatientFileId(patientFileDTO.getId());

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(correspondenceService.createCorrespondence(correspondenceDTO));

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
	 *         {@link CorrespondenceDTO}
	 *         représentant les correspondances demandées, encapsulée dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException le compte utilisateur n'a pas été trouvé, ou le
	 *                         dossier patient n'a pas été trouvé, ou l'utilisateur
	 *                         n'est pas le médecin référent ou correspondant.
	 */
	@GetMapping("/patient-file/{id}/correspondence")
	public ResponseEntity<List<CorrespondenceDTO>> findCorrespondencesByPatientFileId(@PathVariable String id,
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

		return ResponseEntity.ok(correspondencesDTO);

	}

	/**
	 * Gestionnaire des requêtes DELETE de suppression d'une correspondance désignée
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
	 * @throws FinderException le compte utilisateur n'a pas été trouvé, ou le
	 *                         dossier patient n'a pas été trouvé, ou la
	 *                         correspondance n'a pas été trouvée ou ne correspond
	 *                         pas au dossier patient.
	 * @throws DeleteException l'utilisateur n'est pas le médecin référent.
	 */
	@DeleteMapping("/patient-file/{patientFileId}/correspondence/{correspondenceId}")
	public ResponseEntity<RestResponse> deleteCorrespondence(@PathVariable String patientFileId,
			@PathVariable String correspondenceId, Principal principal) throws FinderException, DeleteException {

		String userId = principal.getName();

		PatientFileDTO patientFileDTO = patientFileService.findPatientFile(patientFileId);

		if (!userId.equals(patientFileDTO.getReferringDoctorId())) {
			throw new DeleteException("L'utilisateur n'est pas le médecin référent.");
		}

		CorrespondenceDTO storedCorrespondenceDTO = correspondenceService.findCorrespondence(correspondenceId);

		if (!patientFileId.equals(storedCorrespondenceDTO.getPatientFileId())) {
			throw new FinderException("Correspondance non trouvée pour le dossier patient '" + patientFileId + "'");
		}

		correspondenceService.deleteCorrespondence(UUID.fromString(correspondenceId));

		RestResponse response = new RestResponse(HttpStatus.OK.value(), "La correspondance a bien été supprimée.");

		return ResponseEntity.ok(response);
	}
}
