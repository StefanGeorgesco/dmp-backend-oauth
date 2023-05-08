package fr.cnam.stefangeorgesco.dmp.exception.domain;

/**
 * Classe d'exception applicative pour les erreurs impliquant un doublon
 * d'identifiant.
 * 
 * @author Stéfan Georgesco
 *
 */
public class DuplicateKeyException extends CreateException {

	public DuplicateKeyException(String message) {
		super(message);
	}

}
