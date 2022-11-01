package fr.cnam.stefangeorgesco.dmp.domain.dao;

import org.springframework.data.repository.CrudRepository;

import fr.cnam.stefangeorgesco.dmp.domain.model.File;

/**
 * Repository pour les objets
 * {@link fr.cnam.stefangeorgesco.dmp.domain.model.File}
 * 
 * @author Stéfan Georgesco
 *
 */
public interface FileDAO extends CrudRepository<File, String> {

}
