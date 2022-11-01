package fr.cnam.stefangeorgesco.dmp.domain.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Objet de transfert de données représentant une correspondance avec un
 * médecin.
 * 
 * @author Stéfan Georgesco
 *
 */
public class CorrespondenceDTO {

	private UUID id;

	/**
	 * Date limite d'effet de la correspondance.
	 */
	@NotNull(message = "La date de la correspondance est obligatoire.")
	@Future(message = "La date de la correspondance doit être dans le futur.")
	private LocalDate dateUntil;

	/**
	 * Identifiant du médecin correspondant.
	 */
	@NotBlank(message = "Identifiant du médecin est obligatoire.")
	private String doctorId;

	/**
	 * Prénom du médecin correspondant.
	 */
	private String doctorFirstname;

	/**
	 * Nom du médecin correspondant.
	 */
	private String doctorLastname;

	/**
	 * Spécialités médicales du médecin correspondant (descriptions seulement).
	 */
	private List<String> doctorSpecialties;

	/**
	 * Identifiant du dossier patient auquel la correspondance est associée.
	 */
	private String patientFileId;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public LocalDate getDateUntil() {
		return dateUntil;
	}

	public void setDateUntil(LocalDate dateUntil) {
		this.dateUntil = dateUntil;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorFirstname() {
		return doctorFirstname;
	}

	public void setDoctorFirstname(String doctorFirstname) {
		this.doctorFirstname = doctorFirstname;
	}

	public String getDoctorLastname() {
		return doctorLastname;
	}

	public void setDoctorLastname(String doctorLastname) {
		this.doctorLastname = doctorLastname;
	}

	public List<String> getDoctorSpecialties() {
		return doctorSpecialties;
	}

	public void setDoctorSpecialties(List<String> doctorSpecialties) {
		this.doctorSpecialties = doctorSpecialties;
	}

	public String getPatientFileId() {
		return patientFileId;
	}

	public void setPatientFileId(String patientFileId) {
		this.patientFileId = patientFileId;
	}

}
