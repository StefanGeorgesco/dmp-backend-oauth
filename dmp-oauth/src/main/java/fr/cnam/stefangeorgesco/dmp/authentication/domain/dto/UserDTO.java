package fr.cnam.stefangeorgesco.dmp.authentication.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Objet de transfert de données représentant un utilisateur.
 * 
 * @author Stéfan Georgesco
 *
 */
public class UserDTO {

	/**
	 * L'identifiant est identique à celui du dossier de médecin ou du dossier
	 * patient auquel l'utilisateur est associé (propriétaire du dossier) et permet
	 * cette association.
	 */
	@NotBlank(message = "L'identifiant est obligatoire.")
	private String id;

	/**
	 * Nom d'utilisateur pour l'identification et l'authentification de
	 * l'utilisateur.
	 */
	@NotBlank(message = "Le non utilisateur est obligatoire.")
	private String username;

	@NotBlank(message = "Le mot de passe est obligatoire.")
	@Size(min = 4, message = "Le mot de passe doit contenir au moins 4 caractères.")
	private String password;
	
	private String role;

	/**
	 * Ce code généré lors de la création du dossier de médecin ou du dossier
	 * patient auquel l'utilisateur veut s'associer permet d'authentifier
	 * l'utilisateur lors de la création de son compte utilisateur et de valider
	 * cette association.
	 */
	private String securityCode;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

}
