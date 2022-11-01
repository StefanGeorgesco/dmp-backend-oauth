package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.constraints.NotBlank;

/**
 * Objet de transfert de données représentant une spécialité médicale.
 * 
 * @author Stéfan Georgesco
 *
 */
public class SpecialtyDTO {

	/**
	 * Identifiant.
	 */
	@NotBlank(message = "L'identifiant est obligatoire.")
	private String id;

	/**
	 * Libellé.
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
