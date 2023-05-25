package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dao.*;
import fr.cnam.stefangeorgesco.dmp.domain.dto.MedicalActDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.MedicalAct;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;
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
public class MedicalActServiceImpl implements MedicalActService {

	private final MedicalActDAO medicalActDAO;

	private final ModelMapper commonModelMapper;

	public MedicalActServiceImpl(MedicalActDAO medicalActDAO, ModelMapper commonModelMapper) {
		this.medicalActDAO = medicalActDAO;
		this.commonModelMapper = commonModelMapper;
	}

	/**
	 * Service de recherche d'un acte médical par son identifiant.
	 * 
	 * @param id l'identifiant de l'acte médical recherché.
	 * @return un objet {@link MedicalActDTO}
	 *         représentant l'acte médical trouvé.
	 * @throws FinderException acte médical non trouvé.
	 */
	@Override
	public MedicalActDTO findMedicalAct(String id) throws FinderException {

		Optional<MedicalAct> optionalMedicalAct = medicalActDAO.findById(id);

		if (optionalMedicalAct.isPresent()) {
			return commonModelMapper.map(optionalMedicalAct.get(), MedicalActDTO.class);
		} else {
			throw new FinderException("Acte médical non trouvé.");
		}
	}

	/**
	 * Service de recherche d'actes médicaux à partir d'une chaîne de caractères.
	 * 
	 * @param q     la chaîne de caractères de recherche.
	 * @param limit le nombre maximum d'objets récupérés.
	 * @return une liste ({@link List}) d'objets
	 *         {@link MedicalActDTO}
	 *         représentant le résultat de la recherche.
	 */
	@Override
	public List<MedicalActDTO> findMedicalActsByIdOrDescription(String q, int limit) {

		if ("".equals(q)) {
			return new ArrayList<>();
		}

		Iterable<MedicalAct> medicalActs = medicalActDAO.findByIdOrDescription(q, limit);

		return ((List<MedicalAct>) medicalActs).stream()
				.map(medicalAct -> commonModelMapper.map(medicalAct, MedicalActDTO.class)).collect(Collectors.toList());
	}
}
