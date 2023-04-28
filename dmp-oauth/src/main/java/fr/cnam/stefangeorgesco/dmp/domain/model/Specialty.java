package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * Entité représentant une spécialité médicale.
 * 
 * @author Stéfan Georgesco
 *
 */
@Entity
@Table(name = "t_specialty")
public class Specialty extends StringIdBaseEntity {

	/**
	 * Libellé.
	 */
	@NotBlank(message = "La description est obligatoire.")
	private String description;

	/**
	 * Représente la spécialité sous forme {@link java.lang.String}
	 */
	@Override
	public String toString() {
		return this.description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Specialty)) return false;
		if (!super.equals(o)) return false;

		Specialty specialty = (Specialty) o;

		return getDescription() != null ? getDescription().equals(specialty.getDescription()) : specialty.getDescription() == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
		return result;
	}
}
