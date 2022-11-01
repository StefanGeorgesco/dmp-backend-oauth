package fr.cnam.stefangeorgesco.dmp.domain.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Classe abstraite parente des objets de transfert de données représentant les
 * éléments médicaux des dossiers patients.
 * 
 * @author Stéfan Georgesco
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({ @JsonSubTypes.Type(value = ActDTO.class, name = "act"),
		@JsonSubTypes.Type(value = DiagnosisDTO.class, name = "diagnosis"),
		@JsonSubTypes.Type(value = MailDTO.class, name = "mail"),
		@JsonSubTypes.Type(value = PrescriptionDTO.class, name = "prescription"),
		@JsonSubTypes.Type(value = SymptomDTO.class, name = "symptom") })
public abstract class PatientFileItemDTO {

	/**
	 * Identifiant
	 */
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
	 * Identifiant du médecin auteur de l'élément médical.
	 */
	protected String authoringDoctorId;

	/**
	 * Prénom du médecin auteur de l'élément médical.
	 */
	protected String authoringDoctorFirstname;

	/**
	 * Nom du médecin auteur de l'élément médical.
	 */
	protected String authoringDoctorLastname;

	/**
	 * Spécialités médicales du médecin auteur de l'élément médical (descriptions
	 * seulement).
	 */
	protected List<String> authoringDoctorSpecialties;

	/**
	 * Identifiant du dossier patient auquel l'élément médical est associé.
	 */
	protected String patientFileId;

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

	public String getAuthoringDoctorId() {
		return authoringDoctorId;
	}

	public void setAuthoringDoctorId(String authoringDoctorId) {
		this.authoringDoctorId = authoringDoctorId;
	}

	public String getAuthoringDoctorFirstname() {
		return authoringDoctorFirstname;
	}

	public void setAuthoringDoctorFirstname(String authoringDoctorFirstname) {
		this.authoringDoctorFirstname = authoringDoctorFirstname;
	}

	public String getAuthoringDoctorLastname() {
		return authoringDoctorLastname;
	}

	public void setAuthoringDoctorLastname(String authoringDoctorLastname) {
		this.authoringDoctorLastname = authoringDoctorLastname;
	}

	public List<String> getAuthoringDoctorSpecialties() {
		return authoringDoctorSpecialties;
	}

	public void setAuthoringDoctorSpecialties(List<String> authoringDoctorSpecialties) {
		this.authoringDoctorSpecialties = authoringDoctorSpecialties;
	}

	public String getPatientFileId() {
		return patientFileId;
	}

	public void setPatientFileId(String patientFileId) {
		this.patientFileId = patientFileId;
	}

}
