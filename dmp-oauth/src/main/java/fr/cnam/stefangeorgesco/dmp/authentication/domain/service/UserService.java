package fr.cnam.stefangeorgesco.dmp.authentication.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dao.UserDAO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;
import fr.cnam.stefangeorgesco.dmp.domain.dao.FileDAO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.File;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CreateException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DeleteException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;

/**
 * Classe de service pour la gestion des utilisateurs.
 * 
 * @author Stéfan Georgesco
 *
 */
@Service
@Validated
public class UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private FileDAO fileDAO;

	@Autowired
	ModelMapper commonModelMapper;

	@Autowired
	ModelMapper userModelMapper;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

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

		if (userDAO.existsById(userDTO.getId())) {
			throw new DuplicateKeyException("Le compte utilisateur existe déjà.");
		}

		if (userDAO.existsByUsername(userDTO.getUsername())) {
			throw new DuplicateKeyException("Le nom d'utilisateur existe déjà.");
		}
		;

		Optional<File> optionalFile = fileDAO.findById(userDTO.getId());

		if (optionalFile.isEmpty()) {
			throw new FinderException("Le dossier n'existe pas.");
		}

		File file = optionalFile.get();

		User user = commonModelMapper.map(userDTO, User.class);

		file.checkUserData(user, bCryptPasswordEncoder);

		if (file instanceof Doctor) {
			//user.setRole(IUser.ROLE_DOCTOR);
		} else {
			//user.setRole(IUser.ROLE_PATIENT);
		}

		try {
			userDAO.save(user);
		} catch (RuntimeException e) {
			throw new CreateException("Le compte utilisateur n'a pas pu être créé.");
		}
	}

	/**
	 * Service de recherche d'un compte utilisateur par nom d'utilisateur.
	 * 
	 * @param username le nom d'utilisateur du compte recherché.
	 * @return l'objet
	 *         {@link fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO}
	 *         représentant le compte utilisateur recherché, encapsulé dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException le compte utilisateur n'a pas été trouvé.
	 */
	public UserDTO findUserByUsername(String username) throws FinderException {
		Optional<User> optionalUser = userDAO.findByUsername(username);

		if (optionalUser.isPresent()) {
			return userModelMapper.map(optionalUser.get(), UserDTO.class);
		} else {
			throw new FinderException("Compte utilisateur non trouvé.");
		}

	}

	/**
	 * Service de suppression d'un compte utilisateur désigné par son identifiant.
	 * 
	 * @param id l'identifiant du compte utilisateur.
	 * @throws DeleteException le compte utilisateur n'a pas pu être supprimé.
	 */
	public void deleteUser(String id) throws DeleteException {
		try {
			userDAO.deleteById(id);
		} catch (Exception e) {
			throw new DeleteException("Le compte utilisateur n'a pas pu être supprimé.");
		}
	}

}
