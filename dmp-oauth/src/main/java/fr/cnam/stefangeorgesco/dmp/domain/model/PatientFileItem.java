package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

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
public abstract class PatientFileItem extends UuidIdBaseEntity {

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PatientFileItem)) return false;
		if (!super.equals(o)) return false;

		PatientFileItem that = (PatientFileItem) o;

		if (getDate() != null ? !getDate().equals(that.getDate()) : that.getDate() != null) return false;
		if (getComments() != null ? !getComments().equals(that.getComments()) : that.getComments() != null)
			return false;
		if (getAuthoringDoctor() != null ? !getAuthoringDoctor().equals(that.getAuthoringDoctor()) : that.getAuthoringDoctor() != null)
			return false;
		return getPatientFile() != null ? getPatientFile().equals(that.getPatientFile()) : that.getPatientFile() == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
		result = 31 * result + (getComments() != null ? getComments().hashCode() : 0);
		result = 31 * result + (getAuthoringDoctor() != null ? getAuthoringDoctor().hashCode() : 0);
		result = 31 * result + (getPatientFile() != null ? getPatientFile().hashCode() : 0);
		return result;
	}
}
