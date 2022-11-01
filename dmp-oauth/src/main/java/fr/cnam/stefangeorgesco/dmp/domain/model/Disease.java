package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * Entité représentant une maladie de la nomenclature CIM 10.
 * 
 * @author Stéfan Georgesco
 *
 */
@Entity
@Table(name = "t_disease")
public class Disease {

	/**
	 * Identifiant, champ 'Code du diagnostic' de la nomenclature CIM 10
	 */
	@Id
	@NotBlank(message = "L'identifiant est obligatoire.")
	private String id;

	/**
	 * Champ 'Libellé long' de la nomenclature CIM 10
	 */
	@Column(length = 320)
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
