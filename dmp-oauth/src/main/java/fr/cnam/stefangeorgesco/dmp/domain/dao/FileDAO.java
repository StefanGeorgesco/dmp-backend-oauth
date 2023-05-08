package fr.cnam.stefangeorgesco.dmp.domain.dao;

import fr.cnam.stefangeorgesco.dmp.domain.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository pour les objets
 * {@link fr.cnam.stefangeorgesco.dmp.domain.model.File}
 * 
 * @author Stéfan Georgesco
 *
 */
public interface FileDAO extends JpaRepository<File, String> {

}
