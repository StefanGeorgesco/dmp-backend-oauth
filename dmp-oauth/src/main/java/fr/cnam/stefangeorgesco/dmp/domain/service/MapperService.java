package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dto.*;
import fr.cnam.stefangeorgesco.dmp.domain.model.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe de service pour la conversion des objets
 * {@link fr.cnam.stefangeorgesco.dmp.domain.model.PatientFileItem} en objets
 * {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileItemDTO} et
 * vice-versa.
 * 
 * @author Stéfan Georgesco
 *
 */
@Service
public class MapperService {

	private final ModelMapper commonModelMapper;

	private final ModelMapper diagnosisModelMapper;

	private final ModelMapper actModelMapper;

	private final Map<Class<? extends PatientFileItem>, Class<? extends PatientFileItemDTO>> returnDTOTypes;

	private final Map<Class<? extends PatientFileItemDTO>, Class<? extends PatientFileItem>> returnEntityTypes;

	public MapperService(ModelMapper commonModelMapper, ModelMapper diagnosisModelMapper, ModelMapper actModelMapper) {

		returnDTOTypes = new HashMap<>();

		returnDTOTypes.put(Mail.class, MailDTO.class);
		returnDTOTypes.put(Diagnosis.class, DiagnosisDTO.class);
		returnDTOTypes.put(Act.class, ActDTO.class);
		returnDTOTypes.put(Prescription.class, PrescriptionDTO.class);
		returnDTOTypes.put(Symptom.class, SymptomDTO.class);

		returnEntityTypes = new HashMap<>();

		returnEntityTypes.put(MailDTO.class, Mail.class);
		returnEntityTypes.put(DiagnosisDTO.class, Diagnosis.class);
		returnEntityTypes.put(ActDTO.class, Act.class);
		returnEntityTypes.put(PrescriptionDTO.class, Prescription.class);
		returnEntityTypes.put(SymptomDTO.class, Symptom.class);

		this.commonModelMapper = commonModelMapper;
		this.diagnosisModelMapper = diagnosisModelMapper;
		this.actModelMapper = actModelMapper;
	}

	/**
	 * Service de conversion des objets
	 * {@link fr.cnam.stefangeorgesco.dmp.domain.model.PatientFileItem} en objets
	 * {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileItemDTO}.
	 * 
	 * @param item l'objet
	 *             {@link fr.cnam.stefangeorgesco.dmp.domain.model.PatientFileItem}
	 *             à convertir.
	 * @return l'objet
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileItemDTO}
	 *         résultant de la conversion.
	 */
	public PatientFileItemDTO mapToDTO(PatientFileItem item) {

		ModelMapper modelMapper = commonModelMapper;

		if (item instanceof Diagnosis) {
			modelMapper = diagnosisModelMapper;
		} else if (item instanceof Act) {
			modelMapper = actModelMapper;
		}

		return modelMapper.map(item, returnDTOTypes.get(item.getClass()));
	}

	/**
	 * Service de conversion des objets
	 * {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileItemDTO} en objets
	 * {@link fr.cnam.stefangeorgesco.dmp.domain.model.PatientFileItem}.
	 * 
	 * @param itemDTO l'objet
	 *                {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileItemDTO}
	 *                à convertir.
	 * @return l'objet
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.model.PatientFileItem}
	 *         résultant de la conversion.
	 */
	public PatientFileItem mapToEntity(PatientFileItemDTO itemDTO) {

		return commonModelMapper.map(itemDTO, returnEntityTypes.get(itemDTO.getClass()));
	}
}
