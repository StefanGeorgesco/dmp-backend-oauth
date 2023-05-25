package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dao.*;
import fr.cnam.stefangeorgesco.dmp.domain.dto.*;
import fr.cnam.stefangeorgesco.dmp.domain.model.*;
import fr.cnam.stefangeorgesco.dmp.exception.domain.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Classe de service pour la gestion des dossiers patients et objets rattachés.
 * 
 * @author Stéfan Georgesco
 *
 */
@Service
public class CorrespondenceServiceImpl implements CorrespondenceService {

	private final CorrespondenceDAO correspondenceDAO;

	private final ModelMapper commonModelMapper;

	public CorrespondenceServiceImpl(CorrespondenceDAO correspondenceDAO, ModelMapper commonModelMapper) {
		this.correspondenceDAO = correspondenceDAO;
		this.commonModelMapper = commonModelMapper;
	}

	/**
	 * Service de création d'une correspondance.
	 * 
	 * @param correspondenceDTO l'objet
	 *                          {@link CorrespondenceDTO}
	 *                          représentant la correspondance à créer.
	 * @return un objet
	 *         {@link CorrespondenceDTO}
	 *         représentant la correspondance créée.
	 * @throws CreateException la correspondance n'a pas pu être créé.
	 */
	@Override
	public CorrespondenceDTO createCorrespondence(CorrespondenceDTO correspondenceDTO) throws CreateException {

		Correspondence correspondence = commonModelMapper.map(correspondenceDTO, Correspondence.class);

		try {
			correspondence = correspondenceDAO.save(correspondence);
		} catch (Exception e) {
			throw new CreateException("La correspondance n'a pas pu être créé.");
		}

		correspondence = correspondenceDAO.findById(correspondence.getId()).orElseThrow();

		return commonModelMapper.map(correspondence, CorrespondenceDTO.class);
	}

	/**
	 * Service de suppression d'une correspondance désignée par son identifiant.
	 * 
	 * @param uuid l'identifiant de la correspondance à supprimer.
	 */
	@Override
	public void deleteCorrespondence(UUID uuid) {

		correspondenceDAO.deleteById(uuid);
	}

	/**
	 * Service de recherche d'une correspondance par son identifiant.
	 * 
	 * @param id l'identifiant de la correspondance recherchée.
	 * @return un objet
	 *         {@link CorrespondenceDTO}
	 *         représentant la correspondance trouvée.
	 * @throws FinderException correspondance non trouvée.
	 */
	@Override
	public CorrespondenceDTO findCorrespondence(String id) throws FinderException {

		Optional<Correspondence> optionalCorrespondence = correspondenceDAO.findById(UUID.fromString(id));

		if (optionalCorrespondence.isPresent()) {
			return commonModelMapper.map(optionalCorrespondence.get(), CorrespondenceDTO.class);
		} else {
			throw new FinderException("Correspondance non trouvée.");
		}
	}

	/**
	 * Service de recherche de toutes les correspondances associées à un dossier
	 * patient.
	 * 
	 * @param patientFileId l'identifiant du dossier patient.
	 * @return une liste ({@link List}) d'objets
	 *         {@link CorrespondenceDTO}
	 *         représentant les correspondances demandées.
	 */
	@Override
	public List<CorrespondenceDTO> findCorrespondencesByPatientFileId(String patientFileId) {

		Iterable<Correspondence> correspondences = correspondenceDAO.findByPatientFileId(patientFileId);

		return ((List<Correspondence>) correspondences).stream()
				.map(correspondence -> commonModelMapper.map(correspondence, CorrespondenceDTO.class))
				.collect(Collectors.toList());
	}
}
