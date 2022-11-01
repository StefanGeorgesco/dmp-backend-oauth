package fr.cnam.stefangeorgesco.dmp.domain.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;

/**
 * Repository pour les objets
 * {@link fr.cnam.stefangeorgesco.dmp.domain.model.Specialty}
 * 
 * @author Stéfan Georgesco
 *
 */
public interface SpecialtyDAO extends CrudRepository<Specialty, String> {

	/**
	 * Recherche les spécialités médicales par recherche insensible à la casse de la
	 * présence d'une sous-chaîne dans l'identifiant ou dans la description de la
	 * spécialité médicale.
	 * 
	 * @param keyword la sous-chaîne de recherche.
	 * @return un {@link java.lang.Iterable} d'objets
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.model.Specialty}
	 *         représentant les maladies trouvées.
	 */
	@Query("select distinct specialty from Specialty specialty "
			+ "where lower(specialty.id) like lower(concat('%', :keyword,'%')) "
			+ "or lower(specialty.description) like lower(concat('%', :keyword,'%'))")
	public Iterable<Specialty> findByIdOrDescription(@Param("keyword") String keyword);
}
