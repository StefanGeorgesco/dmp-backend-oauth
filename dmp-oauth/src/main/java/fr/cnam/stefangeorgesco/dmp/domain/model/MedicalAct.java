package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
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
public class MedicalAct extends StringIdBaseEntity {

	/**
	 * Identifiant, champ 'Code' de la nomenclature CCAM
	 * Hérité de StringIdBaseEntity
	 */

	/**
	 * Champ 'Texte' de la nomenclature CCAM
	 */
	@Column(length = 800)
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
		if (!(o instanceof MedicalAct)) return false;
		if (!super.equals(o)) return false;

		MedicalAct that = (MedicalAct) o;

		return getDescription() != null ? getDescription().equals(that.getDescription()) : that.getDescription() == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
		return result;
	}
}
