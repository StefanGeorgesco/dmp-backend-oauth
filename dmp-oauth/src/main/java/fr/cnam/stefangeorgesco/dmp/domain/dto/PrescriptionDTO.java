package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.constraints.NotBlank;

/**
 * Objet de transfert de données représentant une prescription délivrée par un
 * médecin.
 * 
 * @author Stéfan Georgesco
 *
 */
public class PrescriptionDTO extends PatientFileItemDTO {

	/**
	 * Contenu de la prescription.
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
