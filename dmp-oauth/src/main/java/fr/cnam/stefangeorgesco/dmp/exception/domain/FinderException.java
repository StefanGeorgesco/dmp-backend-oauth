package fr.cnam.stefangeorgesco.dmp.exception.domain;

/**
 * Classe d'exception applicative pour les erreurs de recherche.
 * 
 * @author Stéfan Georgesco
 *
 */
@SuppressWarnings("serial")
public class FinderException extends ApplicationException {

	public FinderException(String message) {
		super(message);
	}

}
