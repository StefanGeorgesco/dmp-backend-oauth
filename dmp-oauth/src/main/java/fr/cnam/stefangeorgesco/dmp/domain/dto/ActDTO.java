package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Objet de transfert de données représentant un acte dispensé par un médecin.
 * 
 * @author Stéfan Georgesco
 *
 */
public class ActDTO extends PatientFileItemDTO {

	/**
	 * Acte médical de la nomenclature correspondant à l'acte dispensé.
	 */
	@NotNull(message = "L'acte médical est obligatoire.")
	@Valid
	@JsonProperty("medicalAct")
	private MedicalActDTO medicalActDTO;

	public MedicalActDTO getMedicalActDTO() {
		return medicalActDTO;
	}

	public void setMedicalActDTO(MedicalActDTO medicalActDTO) {
		this.medicalActDTO = medicalActDTO;
	}

}
