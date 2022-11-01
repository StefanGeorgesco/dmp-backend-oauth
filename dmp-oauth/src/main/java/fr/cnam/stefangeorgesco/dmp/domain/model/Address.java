package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.Embeddable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Entité représentant une adresse postale.
 * 
 * @author Stéfan Georgesco
 *
 */
@Embeddable
public class Address {

	/**
	 * Première ligne d'adresse.
	 */
	@NotBlank(message = "Champ 'street1' invalide.")
	private String street1;

	/**
	 * Seconde ligne d'adresse.
	 */
	@NotNull(message = "Le champ 'street2' ne doit pas être 'null'.")
	private String street2 = "";

	/**
	 * Ville.
	 */
	@NotBlank(message = "Champ 'city' invalide.")
	private String city;

	/**
	 * Etat ou province.
	 */
	@NotNull(message = "Le champ 'state' ne doit pas être 'null'.")
	private String state = "";

	/**
	 * Code postal.
	 */
	@NotBlank(message = "Champ 'zipcode' invalide.")
	private String zipcode;

	/**
	 * Pays.
	 */
	@NotBlank(message = "Champ 'country' invalide.")
	private String country;

	public String getStreet1() {
		return street1;
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
