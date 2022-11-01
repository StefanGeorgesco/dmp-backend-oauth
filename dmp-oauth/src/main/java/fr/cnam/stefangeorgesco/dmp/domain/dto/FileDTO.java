package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe abstraite parente des objets de transfert de données représentant les
 * dossiers patients et les dossiers de médecins.
 * 
 * @author Stéfan Georgesco
 *
 */
public abstract class FileDTO {

	@NotBlank(message = "L'identifiant est obligatoire.")
	protected String id;

	/**
	 * Prénom du patient ou du médecin.
	 */
	@NotBlank(message = "Le prénom est obligatoire.")
	protected String firstname;

	/**
	 * Nom du patient ou du médecin.
	 */
	@NotBlank(message = "Le nom est obligatoire.")
	protected String lastname;

	/**
	 * Numéro de téléphone du patient ou du médecin.
	 */
	@NotBlank(message = "Le numéro de téléphone est obligatoire.")
	protected String phone;

	/**
	 * Adresse email du patient ou du médecin.
	 */
	@NotBlank(message = "L'adresse email est obligatoire.")
	@Email(message = "L'adresse email doit être fournie et respecter le format.")
	protected String email;

	/**
	 * Adresse postale du patient ou du médecin.
	 */
	@NotNull(message = "L'adresse est obligatoire.")
	@Valid
	@JsonProperty("address")
	protected AddressDTO addressDTO;

	/**
	 * Code généré lors de la création du dossier (entité), permettant
	 * d'authentifier un utilisateur lors de la création de son compte utilisateur
	 * et de valider son association avec le dossier.
	 */
	protected String securityCode;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public AddressDTO getAddressDTO() {
		return addressDTO;
	}

	public void setAddressDTO(AddressDTO addressDTO) {
		this.addressDTO = addressDTO;
	}

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

}
