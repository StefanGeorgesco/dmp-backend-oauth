package fr.cnam.stefangeorgesco.dmp.domain.dto;

/**
 * Objet de transfert de données représentant un acte médical de la nomenclature
 * CCAM.
 * 
 * @author Stéfan Georgesco
 *
 */
public class MedicalActDTO extends StringIdBaseDto {

	/**
	 * Identifiant, champ 'Code' de la nomenclature CCAM
	 * Hérité de StringIdBaseEntity
	 */

	/**
	 * Champ 'Texte' de la nomenclature CCAM
	 */
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
