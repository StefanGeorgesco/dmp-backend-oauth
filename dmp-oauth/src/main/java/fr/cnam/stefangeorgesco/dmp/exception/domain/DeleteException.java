package fr.cnam.stefangeorgesco.dmp.exception.domain;

/**
 * Classe d'exception applicative pour les erreurs de suppression.
 * 
 * @author Stéfan Georgesco
 *
 */
public class DeleteException extends ApplicationException {

	public DeleteException(String message) {
		super(message);
	}

}
