package fr.cnam.stefangeorgesco.dmp.domain.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Entité représentant un dossier patient.
 * 
 * @author Stéfan Georgesco
 *
 */
@Entity
@Table(name = "t_patient_file")
@OnDelete(action = OnDeleteAction.CASCADE)
public class PatientFile extends File {

	/**
	 * Date de naissance du patient.
	 */
	@NotNull(message = "La date de naissance est obligatoire.")
	@PastOrPresent(message = "La date de naissance doit être dans le passé ou aujourd'hui.")
	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	/**
	 * Médecin référent.
	 */
	@ManyToOne
	@JoinColumn(name = "referring_doctor_id")
	@NotNull(message = "Le médecin référent est obligatoire.")
	private Doctor referringDoctor;

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Doctor getReferringDoctor() {
		return referringDoctor;
	}

	public void setReferringDoctor(Doctor referringDoctor) {
		this.referringDoctor = referringDoctor;
	}

}
