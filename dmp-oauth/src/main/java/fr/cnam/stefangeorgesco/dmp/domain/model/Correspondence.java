package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Entité représentant une correspondance avec un médecin.
 * 
 * @author Stéfan Georgesco
 *
 */
@Entity
@Table(name = "t_correspondence")
public class Correspondence extends UuidIdBaseEntity {

	/**
	 * Date limite d'effet de la correspondance.
	 */
	@Column(name = "date_until")
	@NotNull(message = "La date de la correspondance est obligatoire.")
	@Future(message = "La date de la correspondance doit être dans le futur.")
	private LocalDate dateUntil;

	/**
	 * Médecin correspondant.
	 */
	@ManyToOne
	@JoinColumn(name = "doctor_id")
	@NotNull(message = "L'identifiant du médecin est obligatoire.")
	private Doctor doctor;

	/**
	 * Dossier patient auquel la correspondance est associée.
	 */
	@ManyToOne
	@JoinColumn(name = "patient_file_id")
	@NotNull(message = "Le dossier patient est obligatoire.")
	private PatientFile patientFile;

	public LocalDate getDateUntil() {
		return dateUntil;
	}

	public void setDateUntil(LocalDate dateUntil) {
		this.dateUntil = dateUntil;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
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
		if (!(o instanceof Correspondence)) return false;
		if (!super.equals(o)) return false;

		Correspondence that = (Correspondence) o;

		if (getDateUntil() != null ? !getDateUntil().equals(that.getDateUntil()) : that.getDateUntil() != null)
			return false;
		if (getDoctor() != null ? !getDoctor().equals(that.getDoctor()) : that.getDoctor() != null) return false;
		return getPatientFile() != null ? getPatientFile().equals(that.getPatientFile()) : that.getPatientFile() == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (getDateUntil() != null ? getDateUntil().hashCode() : 0);
		result = 31 * result + (getDoctor() != null ? getDoctor().hashCode() : 0);
		result = 31 * result + (getPatientFile() != null ? getPatientFile().hashCode() : 0);
		return result;
	}
}
