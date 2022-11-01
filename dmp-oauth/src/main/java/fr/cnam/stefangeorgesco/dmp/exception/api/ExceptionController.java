package fr.cnam.stefangeorgesco.dmp.exception.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.cnam.stefangeorgesco.dmp.api.RestResponse;
import fr.cnam.stefangeorgesco.dmp.exception.domain.ApplicationException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DeleteException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.UpdateException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;

/**
 * Classe de gestion des exceptions fournissant des réponses REST aux requêtes
 * en erreur.
 * 
 * @author Stéfan Georgesco
 * 
 */
@ControllerAdvice
@RestController
public class ExceptionController {

	private static Map<Class<? extends Throwable>, HttpStatus> map = new HashMap<>();

	static {
		map.put(DuplicateKeyException.class, HttpStatus.CONFLICT);
		map.put(FinderException.class, HttpStatus.NOT_FOUND);
		map.put(UpdateException.class, HttpStatus.CONFLICT);
		map.put(DeleteException.class, HttpStatus.CONFLICT);
	}

	/**
	 * Gestionnaire des erreurs de validation des objets de transfert de données
	 * transmis dans le corps des requêtes REST.
	 * 
	 * @param ex l'exception
	 *           {@link org.springframework.web.bind.MethodArgumentNotValidException}
	 *           levée par une ou plusieurs erreurs de validation d'un objet de
	 *           transfert de données.
	 * @return un objet {@link java.util.Map} donnant un texte d'erreur pour chaque
	 *         attribut de l'objet de transfert de données n'ayant pas respecté les
	 *         contraintes de validation.
	 */
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField().replaceAll("(DTO|\\[|\\])", "").replaceAll("[.]", "_");
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}

	/**
	 * Gestionnaire des exceptions applicatives.
	 * 
	 * @param ex l'exception
	 *           {@link fr.cnam.stefangeorgesco.dmp.exception.domain.ApplicationException}
	 *           levée par l'application.
	 * @return une réponse {@link fr.cnam.stefangeorgesco.dmp.api.RestResponse}
	 *         encapsulée dans un objet org.springframework.http.ResponseEntity.
	 */
	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<RestResponse> handleApplicationException(ApplicationException ex) {
		HttpStatus status = HttpStatus.BAD_REQUEST;

		if (map.get(ex.getClass()) != null) {
			status = map.get(ex.getClass());
		}

		RestResponse response = new RestResponse();
		response.setStatus(status.value());
		response.setMessage(ex.getMessage());

		return ResponseEntity.status(status).body(response);
	}

	/**
	 * Gestionnaire des autres exceptions.
	 * 
	 * @param ex l'exception {@link java.lang.Exception}
	 * @return une réponse {@link fr.cnam.stefangeorgesco.dmp.api.RestResponse}
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public RestResponse handleUnknownException(Exception ex) {
		RestResponse response = new RestResponse();
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.setMessage(ex.getMessage());

		return response;
	}

}
