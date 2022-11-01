package fr.cnam.stefangeorgesco.dmp.domain.service;

import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cnam.stefangeorgesco.dmp.domain.dto.ActDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DiagnosisDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.MailDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileItemDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PrescriptionDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.SymptomDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Act;
import fr.cnam.stefangeorgesco.dmp.domain.model.Diagnosis;
import fr.cnam.stefangeorgesco.dmp.domain.model.Mail;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFileItem;
import fr.cnam.stefangeorgesco.dmp.domain.model.Prescription;
import fr.cnam.stefangeorgesco.dmp.domain.model.Symptom;

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

	@Autowired
	private ModelMapper commonModelMapper;

	@Autowired
	private ModelMapper diagnosisModelMapper;

	@Autowired
	private ModelMapper actModelMapper;

	private Map<Class<? extends PatientFileItem>, Class<? extends PatientFileItemDTO>> returnDTOTypes;

	private Map<Class<? extends PatientFileItemDTO>, Class<? extends PatientFileItem>> returnEntityTypes;

	private ModelMapper modelMapper;

	public MapperService() {

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

		modelMapper = commonModelMapper;

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
