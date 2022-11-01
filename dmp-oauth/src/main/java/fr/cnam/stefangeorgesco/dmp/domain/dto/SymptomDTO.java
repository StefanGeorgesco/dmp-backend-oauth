package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.constraints.NotBlank;

/**
 * Objet de transfert de données représentant un symptôme observé par un
 * médecin.
 * 
 * @author Stéfan Georgesco
 *
 */
public class SymptomDTO extends PatientFileItemDTO {

	/**
	 * Description du symptôme.
	 */
	@NotBlank(message = "La description est obligatoire.")
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
