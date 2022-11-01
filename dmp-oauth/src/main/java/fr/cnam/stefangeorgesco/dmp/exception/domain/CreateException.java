package fr.cnam.stefangeorgesco.dmp.exception.domain;

/**
 * Classe d'exception applicative pour les erreurs de création.
 * 
 * @author Stéfan Georgesco
 *
 */
@SuppressWarnings("serial")
public class CreateException extends ApplicationException {

	public CreateException(String message) {
		super(message);
	}

}
