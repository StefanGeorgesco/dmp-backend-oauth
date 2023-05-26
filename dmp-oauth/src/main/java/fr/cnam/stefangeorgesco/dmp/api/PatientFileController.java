package fr.cnam.stefangeorgesco.dmp.api;

import fr.cnam.stefangeorgesco.dmp.domain.dto.*;
import fr.cnam.stefangeorgesco.dmp.domain.service.PatientFileService;
import fr.cnam.stefangeorgesco.dmp.domain.service.PatientFileServiceImpl;
import fr.cnam.stefangeorgesco.dmp.exception.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * Contrôleur REST dédié aux dossiers patients et objets rattachés.
 * 
 * @author Stéfan Georgesco
 * 
 */
@RestController
public class PatientFileController {

	private final PatientFileService patientFileService;

	public PatientFileController(PatientFileService patientFileService) {
		this.patientFileService = patientFileService;
	}

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
	 * @throws CheckException  le dossier patient n'existe pas au RNIPP.
	 * @throws CreateException le dossier patient n'a pas pu être créé.
	 */
	@PostMapping("/patient-file")
	public ResponseEntity<PatientFileDTO> createPatientFile(@Valid @RequestBody PatientFileDTO patientFileDTO,
			Principal principal) throws CheckException, CreateException {

		patientFileDTO.setReferringDoctorId(principal.getName());

		return ResponseEntity.status(HttpStatus.CREATED).body(patientFileService.createPatientFile(patientFileDTO));
	}

	/**
	 * Gestionnaire des requêtes PUT de modification du dossier patient
	 * correspondant à l'utilisateur connecté (authentifié).
	 * 
	 * @param patientFileDTO l'objet
	 *                       {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
	 *                       représentant le dossier patient à modifier, et les données à modifier.
	 * @param principal      l'utilisateur authentifié.
	 * @return l'objet {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
	 *         représentant le dossier patient modifié, encapsulé dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws UpdateException le dossier patient n'a pas pu être modifié.
	 * @see PatientFileServiceImpl#updatePatientFile(PatientFileDTO)
	 */
	@PutMapping("/patient-file/details")
	public ResponseEntity<PatientFileDTO> updatePatientFile(@Valid @RequestBody PatientFileDTO patientFileDTO,
			Principal principal) throws UpdateException {

		patientFileDTO.setId(principal.getName());

		return ResponseEntity.ok(patientFileService.updatePatientFile(patientFileDTO));
	}

	/**
	 * Gestionnaire des requêtes GET de consultation du dossier patient
	 * correspondant à l'utilisateur connecté (authentifié).
	 * 
	 * @param principal l'utilisateur authentifié.
	 * @return l'objet {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
	 *         représentant le dossier patient consulté, encapsulé dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException le compte utilisateur n'a pas été trouvé.
	 */
	@GetMapping("/patient-file/details")
	public ResponseEntity<PatientFileDTO> getPatientFileDetails(Principal principal) throws FinderException {

		return ResponseEntity.ok(patientFileService.findPatientFile(principal.getName()));
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
	 * patient désigné par son identifiant.
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
}
