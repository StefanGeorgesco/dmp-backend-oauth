package fr.cnam.stefangeorgesco.dmp.api;

import fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO;
import fr.cnam.stefangeorgesco.dmp.domain.service.SpecialtyService;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Contrôleur REST dédié aux médecins et aux spécialités des médecins.
 * 
 * @author Stéfan Georgesco
 * 
 */
@RestController
public class SpecialtyController {

	private final SpecialtyService specialtyService;

	public SpecialtyController(SpecialtyService specialtyService) {
		this.specialtyService = specialtyService;
	}

	/**
	 * Gestionnaire des requêtes GET de consultation d'une spécialité médicale
	 * désignée par son identifiant.
	 * 
	 * @param id l'identifiant de la spécialité, fourni en variable de chemin.
	 * @return un objet {@link SpecialtyDTO}
	 *         représentant la spécialité consultée, encapsulé dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException spécialité non trouvée.
	 */
	@GetMapping("/specialty/{id}")
	public ResponseEntity<SpecialtyDTO> getSpecialty(@PathVariable String id) throws FinderException {

		return ResponseEntity.ok(specialtyService.findSpecialty(id));
	}

	/**
	 * Gestionnaire des requêtes GET de récupération des spécialités trouvées par
	 * une recherche à partir d'une chaîne de caractères.
	 * 
	 * @param q la chaîne de caractère (String) utilisée pour la recherche, fournie
	 *          en paramètre de requête.
	 * @return une liste ({@link List}) d'objets
	 *         {@link SpecialtyDTO}
	 *         représentant le résultat de la recherche, encapsulée dans un objet
	 *         org.springframework.http.ResponseEntity.
	 */
	@GetMapping("/specialty")
	public ResponseEntity<List<SpecialtyDTO>> getSpecialties(@RequestParam(required = false) String q) {

		if (q == null) {
			return ResponseEntity.ok(specialtyService.findAllSpecialties());
		} else {
			return ResponseEntity.ok(specialtyService.findSpecialtiesByIdOrDescription(q));
		}
	}
}
