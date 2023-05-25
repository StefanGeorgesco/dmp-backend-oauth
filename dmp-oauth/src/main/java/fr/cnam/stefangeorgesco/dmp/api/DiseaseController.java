package fr.cnam.stefangeorgesco.dmp.api;

import fr.cnam.stefangeorgesco.dmp.domain.dto.DiseaseDTO;
import fr.cnam.stefangeorgesco.dmp.domain.service.DiseaseService;
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
public class DiseaseController {

	private final DiseaseService diseaseService;

	public DiseaseController(DiseaseService diseaseService) {
		this.diseaseService = diseaseService;
	}

	/**
	 * Gestionnaire des requêtes GET de consultation d'une maladie désignée par son
	 * identifiant.
	 * 
	 * @param id l'identifiant de la maladie, fourni en variable de chemin.
	 * @return un objet {@link DiseaseDTO}
	 *         représentant la maladie consultée, encapsulé dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException maladie non trouvée.
	 */
	@GetMapping("/disease/{id}")
	public ResponseEntity<DiseaseDTO> getDisease(@PathVariable String id) throws FinderException {

		return ResponseEntity.ok(diseaseService.findDisease(id));
	}

	/**
	 * Gestionnaire des requêtes GET de récupération des maladies trouvées par une
	 * recherche à partir d'une chaîne de caractères.
	 * 
	 * @param q     la chaîne de caractère (String) utilisée pour la recherche,
	 *              fournie en paramètre de requête.
	 * @param limit le nombre maximum d'objets récupérés, paramètre de requête
	 *              optionnel. Il est fixé à 30 par défaut.
	 * @return une liste ({@link List}) d'objets
	 *         {@link DiseaseDTO}
	 *         représentant le résultat de la recherche, encapsulée dans un objet
	 *         org.springframework.http.ResponseEntity.
	 */
	@GetMapping("/disease")
	public ResponseEntity<List<DiseaseDTO>> getDiseases(@RequestParam String q,
			@RequestParam(required = false, defaultValue = "30") int limit) {

		return ResponseEntity.ok(diseaseService.findDiseasesByIdOrDescription(q, limit));
	}
}
