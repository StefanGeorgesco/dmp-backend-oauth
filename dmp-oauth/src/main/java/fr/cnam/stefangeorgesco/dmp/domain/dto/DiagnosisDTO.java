package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Objet de transfert de données représentant un diagnostic réalisé par un
 * médecin.
 * 
 * @author Stéfan Georgesco
 *
 */
public class DiagnosisDTO extends PatientFileItemDTO {

	/**
	 * Maladie de la nomenclature diagnostiquée.
	 */
	@NotNull(message = "La maladie est obligatoire.")
	@Valid
	@JsonProperty("disease")
	private DiseaseDTO diseaseDTO;

	public DiseaseDTO getDiseaseDTO() {
		return diseaseDTO;
	}

	public void setDiseaseDTO(DiseaseDTO diseaseDTO) {
		this.diseaseDTO = diseaseDTO;
	}

}
