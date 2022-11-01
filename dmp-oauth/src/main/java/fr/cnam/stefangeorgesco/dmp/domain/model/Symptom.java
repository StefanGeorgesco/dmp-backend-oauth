package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Entité représentant un symptôme observé par un médecin.
 * 
 * @author Stéfan Georgesco
 *
 */
@Entity
@Table(name = "t_symptom")
@OnDelete(action = OnDeleteAction.CASCADE)
public class Symptom extends PatientFileItem {

	/**
	 * Description du symptôme.
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

}
