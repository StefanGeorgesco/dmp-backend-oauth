package fr.cnam.stefangeorgesco.dmp.authentication.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

/**
 * Entité du modèle d'authentification représentant l'utilisateur.
 * 
 * @author Stéfan Georgesco
 *
 */
@Entity
@Table(name = "t_user")
public class User implements IUser {

	/**
	 * L'identifiant est identique à celui du dossier de médecin ou du dossier
	 * patient auquel l'utilisateur est associé (propriétaire du dossier) et permet
	 * cette association.
	 */
	@Id
	@NotBlank(message = "L'identifiant est obligatoire.")
	private String id;

	/**
	 * Nom d'utilisateur pour l'identification et l'authentification de
	 * l'utilisateur.
	 */
	@Column(unique = true)
	@NotBlank(message = "Le non utilisateur est obligatoire.")
	private String username;

	@Transient
	private String password;

	/**
	 * Ce code généré lors de la création du dossier de médecin ou du dossier
	 * patient auquel l'utilisateur veut s'associer permet d'authentifier
	 * l'utilisateur lors de la création de son compte utilisateur et de valider
	 * cette association. Il n'est pas conservé dans la persistance.
	 */
	@Transient
	private String securityCode;

	public User() {
		super();
	}

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

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

}
