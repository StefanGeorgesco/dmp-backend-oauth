package fr.cnam.stefangeorgesco.dmp.domain.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.annotations.Type;

/**
 * Classe abstraite parente des entités représentant les éléments médicaux des
 * dossiers patients.
 * 
 * @author Stéfan Georgesco
 *
 */
@Entity
@Table(name = "t_patient_file_item")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PatientFileItem {

	/**
	 * Identifiant
	 */
	@Id
	@GeneratedValue
	@Type(type = "org.hibernate.type.UUIDCharType")
	protected UUID id;

	/**
	 * Date de création de l'élément médical.
	 */
	@NotNull(message = "La date de l'élément médical est obligatoire.")
	@PastOrPresent(message = "La date de l'élément médical doit être dans le passé ou aujourd'hui.")
	protected LocalDate date;

	/**
	 * Commentaires.
	 */
	protected String comments;

	/**
	 * Médecin auteur de l'élément médical.
	 */
	@ManyToOne
	@JoinColumn(name = "authoring_doctor_id")
	@NotNull(message = "Le médecin auteur est obligatoire.")
	protected Doctor authoringDoctor;

	/**
	 * Dossier patient auquel l'élément médical est associé.
	 */
	@ManyToOne
	@JoinColumn(name = "patient_file_id")
	@NotNull(message = "Le dossier patient est obligatoire.")
	protected PatientFile patientFile;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Doctor getAuthoringDoctor() {
		return authoringDoctor;
	}

	public void setAuthoringDoctor(Doctor authoringDoctor) {
		this.authoringDoctor = authoringDoctor;
	}

	public PatientFile getPatientFile() {
		return patientFile;
	}

	public void setPatientFile(PatientFile patientFile) {
		this.patientFile = patientFile;
	}

}
