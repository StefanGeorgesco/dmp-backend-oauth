package fr.cnam.stefangeorgesco.dmp.domain.model;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Classe abstraite parente de entités représentant les dossiers patients et les
 * dossiers de médecins.
 * 
 * @author Stéfan Georgesco
 *
 */
@Entity
@Table(name = "t_file")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class File extends StringIdBaseEntity {

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
	@Embedded
	@NotNull(message = "L'adresse est obligatoire.")
	@Valid
	protected Address address;

	/**
	 * Code généré lors de la création du dossier, permettant d'authentifier un
	 * utilisateur lors de la création de son compte utilisateur et de valider son
	 * association avec le dossier.
	 */
	@Column(name = "security_code", nullable = false)
	protected String securityCode;

	/**
	 * Vérifie que les données de l'utilisateur concordent avec les données du
	 * dossier.
	 * 
	 * @param user            l'utilisateur.
	 * @param passwordEncoder l'encodeur à utiliser pour vérifier la concordance du
	 *                        code de sécurité de l'utilisateur et du code de
	 *                        sécurité du dossier.
	 * @throws CheckException on a voulu vérifier un utilisateur 'null' ou avec un
	 *                        identifiant 'null' ou avec un code de sécurité 'null',
	 *                        ou les données ne concordent pas.
	 */
	public void checkUserData(User user, PasswordEncoder passwordEncoder) throws CheckException {

		if (user == null) {
			throw new CheckException("Impossible de vérifier un utilisateur 'null'.");
		}

		if (user.getId() == null) {
			throw new CheckException("Impossible de vérifier un utilisateur avec un identifiant 'null'.");
		}

		if (user.getSecurityCode() == null) {
			throw new CheckException("Impossible de vérifier un utilisateur avec un code de sécurité 'null'.");
		}

		if (!user.getId().equals(this.id) || !passwordEncoder.matches(user.getSecurityCode(), this.securityCode)) {
			throw new CheckException("Les données ne correspondent pas.");
		}

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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof File)) return false;
		if (!super.equals(o)) return false;

		File file = (File) o;

		if (getFirstname() != null ? !getFirstname().equals(file.getFirstname()) : file.getFirstname() != null)
			return false;
		if (getLastname() != null ? !getLastname().equals(file.getLastname()) : file.getLastname() != null)
			return false;
		if (getPhone() != null ? !getPhone().equals(file.getPhone()) : file.getPhone() != null) return false;
		if (getEmail() != null ? !getEmail().equals(file.getEmail()) : file.getEmail() != null) return false;
		if (getAddress() != null ? !getAddress().equals(file.getAddress()) : file.getAddress() != null) return false;
		return getSecurityCode() != null ? getSecurityCode().equals(file.getSecurityCode()) : file.getSecurityCode() == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (getFirstname() != null ? getFirstname().hashCode() : 0);
		result = 31 * result + (getLastname() != null ? getLastname().hashCode() : 0);
		result = 31 * result + (getPhone() != null ? getPhone().hashCode() : 0);
		result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
		result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
		result = 31 * result + (getSecurityCode() != null ? getSecurityCode().hashCode() : 0);
		return result;
	}
}
