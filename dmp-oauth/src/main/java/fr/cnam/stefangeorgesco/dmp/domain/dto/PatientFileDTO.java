package fr.cnam.stefangeorgesco.dmp.domain.dto;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Objet de transfert de données représentant un dossier patient.
 * 
 * @author Stéfan Georgesco
 *
 */
public class PatientFileDTO extends FileDTO {

	/**
	 * Date de naissance du patient.
	 */
	@NotNull(message = "La date de naissance est obligatoire.")
	@PastOrPresent(message = "La date de naissance doit être dans le passé ou aujourd'hui.")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDate dateOfBirth;

	/**
	 * Identifiant du médecin référent.
	 */
	private String referringDoctorId;

	/**
	 * Prénom du médecin référent.
	 */
	private String referringDoctorFirstname;

	/**
	 * Nom du médecin référent.
	 */
	private String referringDoctorLastname;

	/**
	 * Spécialités médicales du médecin référent (descriptions seulement).
	 */
	private List<String> referringDoctorSpecialties;

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getReferringDoctorId() {
		return referringDoctorId;
	}

	public void setReferringDoctorId(String referringDoctorId) {
		this.referringDoctorId = referringDoctorId;
	}

	public String getReferringDoctorFirstname() {
		return referringDoctorFirstname;
	}

	public void setReferringDoctorFirstname(String referringDoctorFirstname) {
		this.referringDoctorFirstname = referringDoctorFirstname;
	}

	public String getReferringDoctorLastname() {
		return referringDoctorLastname;
	}

	public void setReferringDoctorLastname(String referringDoctorLastname) {
		this.referringDoctorLastname = referringDoctorLastname;
	}

	public List<String> getReferringDoctorSpecialties() {
		return referringDoctorSpecialties;
	}

	public void setReferringDoctorSpecialties(List<String> referringDoctorSpecialties) {
		this.referringDoctorSpecialties = referringDoctorSpecialties;
	}
}
