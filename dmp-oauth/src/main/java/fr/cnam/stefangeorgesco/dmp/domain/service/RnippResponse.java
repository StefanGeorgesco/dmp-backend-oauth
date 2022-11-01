package fr.cnam.stefangeorgesco.dmp.domain.service;

/**
 * Encapsulation des données de la réponse du service REST RNIPP.
 * 
 * @author Stéfan Georgesco
 * 
 */
public class RnippResponse {

	/**
	 * Résultat de la vérification RNIPP.
	 */
	private boolean result;

	/**
	 * Message de la réponse.
	 */
	private String message;

	public RnippResponse() {
		super();
	}

	public RnippResponse(boolean result, String message) {
		super();
		this.result = result;
		this.message = message;
	}

	public boolean getResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}