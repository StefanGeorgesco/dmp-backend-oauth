package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
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
public class Disease extends StringIdBaseEntity {

	/**
	 * Identifiant, champ 'Code du diagnostic' de la nomenclature CIM 10
	 * Hérité de StringIdBaseEntity
	 */

	/**
	 * Champ 'Libellé long' de la nomenclature CIM 10
	 */
	@Column(length = 320)
	@NotBlank(message = "La description est obligatoire.")
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Disease)) return false;
		if (!super.equals(o)) return false;

		Disease disease = (Disease) o;

		return getDescription() != null ? getDescription().equals(disease.getDescription()) : disease.getDescription() == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
		return result;
	}
}
