package fr.cnam.stefangeorgesco.dmp.domain.dto;

/**
 * Objet de transfert de données représentant une spécialité médicale.
 * 
 * @author Stéfan Georgesco
 *
 */
public class SpecialtyDTO extends StringIdBaseDto {

	/**
	 * Libellé.
	 */
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
