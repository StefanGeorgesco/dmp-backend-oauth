package fr.cnam.stefangeorgesco.dmp.authentication.domain.service;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;
import fr.cnam.stefangeorgesco.dmp.domain.dao.FileDAO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.File;
import fr.cnam.stefangeorgesco.dmp.exception.domain.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

/**
 * Classe de service pour la gestion des utilisateurs.
 * 
 * @author Stéfan Georgesco
 *
 */
@Service
@Validated
public class UserService {

	private final KeycloakService keycloakService;

	private final FileDAO fileDAO;

	private final ModelMapper commonModelMapper;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserService(KeycloakService keycloakService, FileDAO fileDAO, ModelMapper commonModelMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.keycloakService = keycloakService;
		this.fileDAO = fileDAO;
		this.commonModelMapper = commonModelMapper;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	/**
	 * Service de création d'un compte utilisateur. Le service vérifie qu'un compte
	 * avec le même identifiant ou le même nom d'utilsateur n'existe pas, qu'un
	 * dossier (de médecin ou de patient) avec le même identifiant existe et que les
	 * données fournies concordent avec ce dossier.
	 * 
	 * @param userDTO l'objet
	 *                {@link fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO}
	 *                représentant le compte utilisateur à créer.
	 * @throws FinderException       le dossier n'existe pas.
	 * @throws CheckException        les données ne concordent pas.
	 * @throws CreateException       le compte utilisateur n'a pas pu être créé.
	 * @throws DuplicateKeyException Le compte utilisateur existe déjà ou le nom
	 *                               d'utilisateur existe déjà.
	 */
	public void createUser(UserDTO userDTO)
			throws FinderException, CheckException, CreateException, DuplicateKeyException {
		
		if (keycloakService.userExistsById(userDTO.getId())) {
			throw new DuplicateKeyException("Le compte utilisateur existe déjà.");
		}

		if (keycloakService.userExistsByUsername(userDTO.getUsername())) {
			throw new DuplicateKeyException("Le nom d'utilisateur existe déjà.");
		}

		Optional<File> optionalFile = fileDAO.findById(userDTO.getId());

		if (optionalFile.isEmpty()) {
			throw new FinderException("Le dossier n'existe pas.");
		}

		File file = optionalFile.get();

		User user = commonModelMapper.map(userDTO, User.class);

		file.checkUserData(user, bCryptPasswordEncoder);
		
		userDTO.setEmail(file.getEmail());
		userDTO.setFirstname(file.getFirstname());
		userDTO.setLastname(file.getLastname());

		if (file instanceof Doctor) {
			userDTO.setRole("DOCTOR");
		} else {
			userDTO.setRole("PATIENT");
		}

		try {
			keycloakService.createUser(userDTO);
		} catch (WebClientResponseException e) {
			throw new CreateException("Le compte utilisateur n'a pas pu être créé (erreur webclient Keycloak).");
		}

	}
	
	/**
	 * Service de mise à jour des données de l'utilisateur.
	 * 
	 * @param userDTO l'objet UserDTO contenant les données à mettre à jour.
	 */
	public void updateUser(UserDTO userDTO) {
		try {
			keycloakService.updateUser(userDTO);
		} catch (WebClientResponseException e) {
			System.err.println("L'utilisateur keycloak n'a pas pu être mise à jour.");
		}
	}

	/**
	 * Service de suppression d'un compte utilisateur désigné par son identifiant.
	 * 
	 * @param id l'identifiant du compte utilisateur.
	 * @throws DeleteException le compte utilisateur n'a pas pu être supprimé.
	 */
	public void deleteUser(String id) throws DeleteException {

		if (!keycloakService.userExistsById(id)) {
			throw new DeleteException("Compte utilisateur non trouvé.");
		}

		HttpStatus responseStatus;

		try {
			responseStatus = keycloakService.deleteUser(id);
		} catch (WebClientResponseException e) {
			throw new DeleteException("Le compte utilisateur n'a pas pu être supprimé (erreur webclient Keycloak).");
		}

		if (responseStatus != HttpStatus.NO_CONTENT) {
			throw new DeleteException("Le compte utilisateur n'a pas pu être supprimé (erreur Keycloak, HTTPStatus : "
					+ responseStatus + ").");
		}

	}

}
