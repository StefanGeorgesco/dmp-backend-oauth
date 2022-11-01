package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * Entité représentant un acte médical de la nomenclature CCAM.
 * 
 * @author Stéfan Georgesco
 *
 */
@Entity
@Table(name = "t_medical_act")
public class MedicalAct {

	/**
	 * Identifiant, champ 'Code' de la nomenclature CCAM
	 */
	@Id
	@NotBlank(message = "L'identifiant est obligatoire.")
	private String id;

	/**
	 * Champ 'Texte' de la nomenclature CCAM
	 */
	@Column(length = 800)
	@NotBlank(message = "La description est obligatoire.")
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
