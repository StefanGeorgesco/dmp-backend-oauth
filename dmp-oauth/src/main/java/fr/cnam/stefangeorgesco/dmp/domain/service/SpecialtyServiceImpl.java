package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dao.SpecialtyDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Classe de service pour la gestion des spécialités médicales.
 * 
 * @author Stéfan Georgesco
 *
 */
@Service
public class SpecialtyServiceImpl implements SpecialtyService {

	private final ModelMapper commonModelMapper;

	private final SpecialtyDAO specialtyDAO;

	public SpecialtyServiceImpl(ModelMapper commonModelMapper, SpecialtyDAO specialtyDAO) {
		this.commonModelMapper = commonModelMapper;
		this.specialtyDAO = specialtyDAO;
	}

	/**
	 * Service de recherche d'une spécialité médicale par son identifiant.
	 *
	 * @param id l'identifiant de la spécialité recherchée.
	 * @return un objet {@link SpecialtyDTO}
	 *         représentant la specialité trouvée.
	 * @throws FinderException spécialité non trouvée.
	 */
	@Override
	public SpecialtyDTO findSpecialty(String id) throws FinderException {

		Optional<Specialty> optionalSpecialty = specialtyDAO.findById(id);

		if (optionalSpecialty.isPresent()) {
			return commonModelMapper.map(optionalSpecialty.get(), SpecialtyDTO.class);
		} else {
			throw new FinderException("Spécialité non trouvée.");
		}

	}

	/**
	 * Service de recherche de spécialités à partir d'une chaîne de caractères.
	 *
	 * @param q la chaîne de caractères de recherche.
	 * @return une liste ({@link List}) d'objets
	 *         {@link SpecialtyDTO}
	 *         représentant les spécialités trouvées.
	 */
	@Override
	public List<SpecialtyDTO> findSpecialtiesByIdOrDescription(String q) {

		if ("".equals(q)) {
			return new ArrayList<>();
		}

		Iterable<Specialty> specialties = specialtyDAO.findByIdOrDescription(q);

		return ((List<Specialty>) specialties).stream()
				.map(specialty -> commonModelMapper.map(specialty, SpecialtyDTO.class)).collect(Collectors.toList());
	}

	/**
	 * Service de récupération de toutes les spécialités médicales.
	 *
	 * @return une liste ({@link List}) d'objets
	 *         {@link SpecialtyDTO}
	 *         représentant troutes les spécialités.
	 */
	@Override
	public List<SpecialtyDTO> findAllSpecialties() {
		List<Specialty> specialties = specialtyDAO.findAll();

		return specialties.stream()
				.map(specialty -> commonModelMapper.map(specialty, SpecialtyDTO.class)).collect(Collectors.toList());
	}

}
