package fr.cnam.stefangeorgesco.dmp.api;

/**
 * Encapsulation des réponses REST lorsqu'aucun objet DTO ne doit pas être retourné.
 * 
 * @author Stéfan Georgesco
 * 
 */
public class RestResponse {

	/**
	 * Statut HTTP de réponse à la requête.
	 */
	private int status;

	/**
	 * Texte de réponse.
	 */
	private String message;

	public RestResponse() {
		super();
	}

	public RestResponse(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}