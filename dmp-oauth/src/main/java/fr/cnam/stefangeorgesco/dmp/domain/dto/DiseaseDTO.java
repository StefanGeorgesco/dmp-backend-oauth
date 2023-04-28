package fr.cnam.stefangeorgesco.dmp.domain.dto;

/**
 * Objet de transfert de données représentant une maladie de la nomenclature CIM
 * 10.
 * 
 * @author Stéfan Georgesco
 *
 */
public class DiseaseDTO extends StringIdBaseDto {

	/**
	 * Identifiant, champ 'Code du diagnostic' de la nomenclature CIM 10
	 * Hérité de StringIdBaseEntity
	 */

	/**
	 * Champ 'Libellé long' de la nomenclature CIM 10
	 */
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
