package fr.cnam.stefangeorgesco.dmp.exception.domain;

/**
 * Classe abstraite parente des classes d'exception applicatives.
 * 
 * @author Stéfan Georgesco
 *
 */
@SuppressWarnings("serial")
public abstract class ApplicationException extends Exception {

	protected ApplicationException(final String message) {
		super(message);
	}

}
