package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dao.*;
import fr.cnam.stefangeorgesco.dmp.domain.dto.*;
import fr.cnam.stefangeorgesco.dmp.domain.model.*;
import fr.cnam.stefangeorgesco.dmp.exception.domain.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Classe de service pour la gestion des dossiers patients et objets rattachés.
 * 
 * @author Stéfan Georgesco
 *
 */
@Service
public class DiseaseServiceImpl implements DiseaseService {

	private final DiseaseDAO diseaseDAO;

	private final ModelMapper commonModelMapper;

	public DiseaseServiceImpl(DiseaseDAO diseaseDAO, ModelMapper commonModelMapper) {
		this.diseaseDAO = diseaseDAO;
		this.commonModelMapper = commonModelMapper;
	}

	/**
	 * Service de recherche d'une maladie par son identifiant.
	 * 
	 * @param id l'identifiant de la maladie recherchée.
	 * @return un objet {@link DiseaseDTO}
	 *         représentant la maladie trouvée.
	 * @throws FinderException maladie non trouvée.
	 */
	@Override
	public DiseaseDTO findDisease(String id) throws FinderException {

		Optional<Disease> optionalDisease = diseaseDAO.findById(id);

		if (optionalDisease.isPresent()) {
			return commonModelMapper.map(optionalDisease.get(), DiseaseDTO.class);
		} else {
			throw new FinderException("Maladie non trouvée.");
		}
	}

	/**
	 * Service de recherche de maladies à partir d'une chaîne de caractères.
	 *
	 * @param q     la chaîne de caractères de recherche.
	 * @param limit le nombre maximum d'objets récupérés.
	 * @return une liste ({@link List}) d'objets
	 *         {@link DiseaseDTO}
	 *         représentant le résultat de la recherche.
	 */
	@Override
	public List<DiseaseDTO> findDiseasesByIdOrDescription(String q, int limit) {

		if ("".equals(q)) {
			return new ArrayList<>();
		}

		Iterable<Disease> diseases = diseaseDAO.findByIdOrDescription(q, limit);

		return ((List<Disease>) diseases).stream()
				.map(disease -> commonModelMapper.map(disease, DiseaseDTO.class)).collect(Collectors.toList());
	}
}
