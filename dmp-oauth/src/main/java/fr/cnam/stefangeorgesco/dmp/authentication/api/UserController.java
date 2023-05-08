package fr.cnam.stefangeorgesco.dmp.authentication.api;

import fr.cnam.stefangeorgesco.dmp.api.RestResponse;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.service.UserService;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CreateException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Contrôleur REST dédié à la gestion des utilisateurs.
 * 
 * @author Stéfan Georgesco
 * 
 */
@RestController
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Gestionnaire des requêtes POST de création d'un compte utilisateur.
	 * 
	 * @param userDTO l'objet
	 *                {@link fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO}
	 *                représentant le compte utilisateur à créer.
	 * @return une réponse {@link RestResponse} encapsulée dans un objet
	 *         org.springframework.http.ResponseEntity avec le statut
	 *         {@link org.springframework.http.HttpStatus#CREATED} en cas de succès.
	 * @throws FinderException       le dossier n'existe pas.
	 * @throws CheckException        les données ne concordent pas.
	 * @throws CreateException       le compte utilisateur n'a pas pu être créé.
	 * @throws DuplicateKeyException Le compte utilisateur existe déjà ou le nom
	 *                               d'utilisateur existe déjà.
	 */
	@PostMapping("/user")
	public ResponseEntity<RestResponse> createAccount(@Valid @RequestBody UserDTO userDTO)
			throws FinderException, CheckException, CreateException {

		userService.createUser(userDTO);

		RestResponse response = new RestResponse(HttpStatus.CREATED.value(), "Le compte utilisateur a été créé.");

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

}
