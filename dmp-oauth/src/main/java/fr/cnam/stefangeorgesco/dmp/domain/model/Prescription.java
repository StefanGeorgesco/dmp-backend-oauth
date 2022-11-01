package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Entité représentant une prescription délivrée par un médecin.
 * 
 * @author Stéfan Georgesco
 *
 */
@Entity
@Table(name = "t_prescription")
@OnDelete(action = OnDeleteAction.CASCADE)
public class Prescription extends PatientFileItem {

	/**
	 * Contenu de la prescription.
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
