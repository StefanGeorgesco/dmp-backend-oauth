package fr.cnam.stefangeorgesco.dmp.api;

import fr.cnam.stefangeorgesco.dmp.domain.dto.MedicalActDTO;
import fr.cnam.stefangeorgesco.dmp.domain.service.MedicalActService;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Contrôleur REST dédié aux dossiers patients et objets rattachés.
 * 
 * @author Stéfan Georgesco
 * 
 */
@RestController
public class MedicalActController {

	private final MedicalActService medicalActService;

	public MedicalActController(MedicalActService medicalActService) {
		this.medicalActService = medicalActService;
	}

	/**
	 * Gestionnaire des requêtes GET de consultation d'un acte médical désignée par
	 * son identifiant.
	 * 
	 * @param id l'identifiant de l'élément médical, fourni en variable de chemin.
	 * @return un objet {@link MedicalActDTO}
	 *         représentant l'acte médical consulté, encapsulé dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException acte médical non trouvé.
	 */
	@GetMapping("/medical-act/{id}")
	public ResponseEntity<MedicalActDTO> getMedicalAct(@PathVariable String id) throws FinderException {

		return ResponseEntity.ok(medicalActService.findMedicalAct(id));
	}

	/**
	 * Gestionnaire des requêtes GET de récupération des actes médicaux trouvés par
	 * une recherche à partir d'une chaîne de caractères.
	 * 
	 * @param q     la chaîne de caractère (String) utilisée pour la recherche,
	 *              fournie en paramètre de requête.
	 * @param limit le nombre maximum d'objets récupérés, paramètre de requête
	 *              optionnel. Il est fixé à 30 par défaut.
	 * @return une liste ({@link List}) d'objets
	 *         {@link MedicalActDTO}
	 *         représentant le résultat de la recherche, encapsulée dans un objet
	 *         org.springframework.http.ResponseEntity.
	 */
	@GetMapping("/medical-act")
	public ResponseEntity<List<MedicalActDTO>> getMedicalActs(@RequestParam String q,
			@RequestParam(required = false, defaultValue = "30") int limit) {

		return ResponseEntity.ok(medicalActService.findMedicalActsByIdOrDescription(q, limit));
	}
}
