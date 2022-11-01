package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.constraints.NotBlank;

/**
 * Objet de transfert de données représentant une maladie de la nomenclature CIM
 * 10.
 * 
 * @author Stéfan Georgesco
 *
 */
public class DiseaseDTO {

	/**
	 * Identifiant, champ 'Code du diagnostic' de la nomenclature CIM 10
	 */
	@NotBlank(message = "L'identifiant est obligatoire.")
	private String id;

	/**
	 * Champ 'Libellé long' de la nomenclature CIM 10
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
