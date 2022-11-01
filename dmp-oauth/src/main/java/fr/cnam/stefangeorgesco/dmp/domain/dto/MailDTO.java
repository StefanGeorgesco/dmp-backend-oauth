package fr.cnam.stefangeorgesco.dmp.domain.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

/**
 * Objet de transfert de données représentant un courrier adressé par le médecin
 * référent à un autre médecin.
 * 
 * @author Stéfan Georgesco
 *
 */
public class MailDTO extends PatientFileItemDTO {

	/**
	 * Texte du courrier.
	 */
	@NotBlank(message = "Le texte du courrier est obligatoire.")
	private String text;

	/**
	 * Identifiant du médecin destinataire.
	 */
	@NotBlank(message = "L'identifiant du médecin destinataire est obligatoire.")
	private String recipientDoctorId;

	/**
	 * Prénom du médecin destinataire.
	 */
	private String recipientDoctorFirstname;

	/**
	 * Nom du médecin destinataire.
	 */
	private String recipientDoctorLastname;

	/**
	 * Spécialités médicales du médecin destinataire (descriptions seulement).
	 */
	private List<String> recipientDoctorSpecialties;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getRecipientDoctorId() {
		return recipientDoctorId;
	}

	public void setRecipientDoctorId(String recipientDoctorId) {
		this.recipientDoctorId = recipientDoctorId;
	}

	public String getRecipientDoctorFirstname() {
		return recipientDoctorFirstname;
	}

	public void setRecipientDoctorFirstname(String recipientDoctorFirstname) {
		this.recipientDoctorFirstname = recipientDoctorFirstname;
	}

	public String getRecipientDoctorLastname() {
		return recipientDoctorLastname;
	}

	public void setRecipientDoctorLastname(String recipientDoctorLastname) {
		this.recipientDoctorLastname = recipientDoctorLastname;
	}

	public List<String> getRecipientDoctorSpecialties() {
		return recipientDoctorSpecialties;
	}

	public void setRecipientDoctorSpecialties(List<String> recipientDoctorSpecialties) {
		this.recipientDoctorSpecialties = recipientDoctorSpecialties;
	}

}
