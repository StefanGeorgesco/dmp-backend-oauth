package fr.cnam.stefangeorgesco.dmp.exception.domain;

/**
 * Classe d'exception applicative pour les erreurs de modification.
 * 
 * @author Stéfan Georgesco
 *
 */
public class UpdateException extends ApplicationException {

	public UpdateException(String message) {
		super(message);
	}

}
