package fr.cnam.stefangeorgesco.dmp.domain.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;

/**
 * Repository pour les objets
 * {@link fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile}
 * 
 * @author Stéfan Georgesco
 *
 */
public interface PatientFileDAO extends CrudRepository<PatientFile, String> {

	/**
	 * Recherche les dossiers patients par recherche insensible à la casse de la
	 * présence d'une sous-chaîne dans l'identifiant du dossier ou dans le prénom ou
	 * dans le nom du patient.
	 * 
	 * @param keyword la sous-chaîne de recherche.
	 * @return un {@link java.lang.Iterable} d'objets
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile}
	 *         représentant les dossiers patients trouvés.
	 */
	@Query("select distinct patientFile from PatientFile patientFile "
			+ "where lower(patientFile.id) like lower(concat('%', :keyword,'%')) "
			+ "or lower(patientFile.firstname) like lower(concat('%', :keyword,'%')) "
			+ "or lower(patientFile.lastname) like lower(concat('%', :keyword,'%'))")
	Iterable<PatientFile> findByIdOrFirstnameOrLastname(@Param("keyword") String keyword);

}
