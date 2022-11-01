package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.constraints.NotBlank;

/**
 * Objet de transfert de données représentant un acte médical de la nomenclature
 * CCAM.
 * 
 * @author Stéfan Georgesco
 *
 */
public class MedicalActDTO {

	/**
	 * Identifiant, champ 'Code' de la nomenclature CCAM
	 */
	@NotBlank(message = "L'identifiant est obligatoire.")
	private String id;

	/**
	 * Champ 'Texte' de la nomenclature CCAM
	 */
	private String description;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
