package fr.cnam.stefangeorgesco.dmp.api;

import java.security.Principal;
import java.util.List;

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
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO;
import fr.cnam.stefangeorgesco.dmp.domain.service.DoctorService;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CreateException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DeleteException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.UpdateException;

/**
 * Contrôleur REST dédié aux médecins et aux spécialités des médecins.
 * 
 * @author Stéfan Georgesco
 * 
 */
@RestController
public class DoctorController {

	@Autowired
	UserService userService;

	@Autowired
	DoctorService doctorService;

	/**
	 * Gestionnaire des requêtes POST de création des dossiers de médecins.
	 * 
	 * @param doctorDTO l'objet
	 *                  {@link fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO}
	 *                  représentant le dossier de médecin à créer.
	 * @return l'objet {@link fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO}
	 *         représentant le dossier de médecin créé, encapsulé dans un objet
	 *         org.springframework.http.ResponseEntity avec le statut
	 *         {@link org.springframework.http.HttpStatus#CREATED} en cas de succès.
	 * @throws FinderException une spécialité du dossier de médecin n'existe pas.
	 * @throws CreateException un dossier avec le même identifiant existe déjà.
	 */
	@PostMapping("/doctor")
	public ResponseEntity<DoctorDTO> createDoctor(@Valid @RequestBody DoctorDTO doctorDTO)
			throws FinderException, CreateException {

		return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.createDoctor(doctorDTO));
	}

	/**
	 * Gestionnaire des requêtes PUT de modification du dossier de médecin
	 * correspondant à l'utilisateur connecté (authentifé).
	 * 
	 * @param doctorDTO l'objet
	 *                  {@link fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO}
	 *                  représentant le dossier de médecin à modifier et les données
	 *                  à modifier.
	 * @param principal l'utilisateur authentifié.
	 * @return l'objet {@link fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO}
	 *         représentant le dossier de médecin modifié, encapsulé dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException le compte utilisateur n'a pas été trouvé.
	 * @throws UpdateException le dossier de médecin n'a pas pu être modifié.
	 * @see fr.cnam.stefangeorgesco.dmp.domain.service.DoctorService#updateDoctor(DoctorDTO)
	 */
	@PutMapping("/doctor/details")
	public ResponseEntity<DoctorDTO> updateDoctor(@Valid @RequestBody DoctorDTO doctorDTO, Principal principal)
			throws FinderException, UpdateException {

		UserDTO userDTO = userService.findUserByUsername(principal.getName());

		doctorDTO.setId(userDTO.getId());

		return ResponseEntity.ok(doctorService.updateDoctor(doctorDTO));
	}

	/**
	 * Gestionnaire des requêtes GET de consultation du dossier de médecin
	 * correspondant à l'utilisateur connecté (authentifé).
	 * 
	 * @param principal l'utilisateur authentifié.
	 * @return l'objet {@link fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO}
	 *         représentant le dossier de médecin consulté, encapsulé dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException le compte utilisateur n'a pas été trouvé.
	 */
	@GetMapping("/doctor/details")
	public ResponseEntity<DoctorDTO> getDoctorDetails(Principal principal) throws FinderException {

		UserDTO userDTO = userService.findUserByUsername(principal.getName());

		return ResponseEntity.ok(doctorService.findDoctor(userDTO.getId()));
	}

	/**
	 * Gestionnaire des requêtes GET de consultation du dossier de médecin désigné
	 * par son identifiant.
	 * 
	 * @param id l'identifiant du dossier de médecin, fourni en variable de chemin.
	 * @return l'objet {@link fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO}
	 *         représentant le dossier de médecin consulté, encapsulé dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException le dossier de médecin n'a pas été trouvé.
	 */
	@GetMapping("/doctor/{id}")
	public ResponseEntity<DoctorDTO> getDoctor(@PathVariable String id) throws FinderException {

		return ResponseEntity.ok(doctorService.findDoctor(id));
	}

	/**
	 * Gestionnaire des requêtes DELETE de suppression du dossier de médecin désigné
	 * par son identifiant.
	 * 
	 * @param id l'identifiant du dossier de médecin, fourni en variable de chemin.
	 * @return une réponse {@link RestResponse} encapsulée dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws DeleteException Le dossier de médecin n'a pas pu être supprimé.
	 */
	@DeleteMapping("/doctor/{id}")
	public ResponseEntity<RestResponse> deleteDoctor(@PathVariable String id) throws DeleteException {

		doctorService.deleteDoctor(id);

		RestResponse response = new RestResponse(HttpStatus.OK.value(), "Le dossier de médecin a bien été supprimé.");

		return ResponseEntity.ok(response);
	}

	/**
	 * Gestionnaire des requêtes GET de récupération des dossiers de médecins
	 * trouvés par une recherche à partir d'une chaîne de caractères.
	 * 
	 * @param q la chaîne de caractère (String) utilisée pour la recherche, fournie
	 *          en paramètre de requête.
	 * @return une liste ({@link java.util.List}) d'objets
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO} représentant
	 *         le résultat de la recherche, encapsulée dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @see fr.cnam.stefangeorgesco.dmp.domain.service.DoctorService#findDoctorsByIdOrFirstnameOrLastname(String)
	 */
	@GetMapping("/doctor")
	public ResponseEntity<List<DoctorDTO>> findDoctorsByIdOrFirstnameOrLastname(@RequestParam String q) {

		return ResponseEntity.ok(doctorService.findDoctorsByIdOrFirstnameOrLastname(q));
	}

	/**
	 * Gestionnaire des requêtes GET de consultation d'une spécialité médicale
	 * désignée par son identifiant.
	 * 
	 * @param id l'identifiant de la spécialité, fourni en variable de chemin.
	 * @return un objet {@link fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO}
	 *         représentant la spécialité consultée, encapsulé dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException spécialité non trouvée.
	 */
	@GetMapping("/specialty/{id}")
	public ResponseEntity<SpecialtyDTO> getSpecialty(@PathVariable String id) throws FinderException {

		return ResponseEntity.ok(doctorService.findSpecialty(id));
	}

	/**
	 * Gestionnaire des requêtes GET de récupération des spécialités trouvées par
	 * une recherche à partir d'une chaîne de caractères.
	 * 
	 * @param q la chaîne de caractère (String) utilisée pour la recherche, fournie
	 *          en paramètre de requête.
	 * @return une liste ({@link java.util.List}) d'objets
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO}
	 *         représentant le résultat de la recherche, encapsulée dans un objet
	 *         org.springframework.http.ResponseEntity.
	 */
	@GetMapping("/specialty")
	public ResponseEntity<List<SpecialtyDTO>> getSpecialties(@RequestParam(required = false) String q) {

		if (q == null) {
			return ResponseEntity.ok(doctorService.findAllSpecialties());
		} else {
			return ResponseEntity.ok(doctorService.findSpecialtiesByIdOrDescription(q));
		}
	}

}
