package fr.cnam.stefangeorgesco.dmp.authentication.domain.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;

/**
 * Repository pour les objets
 * {@link fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User}
 * 
 * @author Stéfan Georgesco
 *
 */
public interface UserDAO extends CrudRepository<User, String> {

	Optional<User> findByUsername(String username);

	boolean existsByUsername(String username);

}
