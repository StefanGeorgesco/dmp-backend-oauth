package fr.cnam.stefangeorgesco.dmp.exception.domain;

/**
 * Classe d'exception applicative pour les erreurs de vérification.
 * 
 * @author Stéfan Georgesco
 *
 */
@SuppressWarnings("serial")
public class CheckException extends ApplicationException {

	public CheckException(String message) {
		super(message);
	}

}
