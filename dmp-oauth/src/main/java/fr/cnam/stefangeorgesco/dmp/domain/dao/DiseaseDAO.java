package fr.cnam.stefangeorgesco.dmp.domain.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import fr.cnam.stefangeorgesco.dmp.domain.model.Disease;

/**
 * Repository pour les objets
 * {@link fr.cnam.stefangeorgesco.dmp.domain.model.Disease}
 * 
 * @author Stéfan Georgesco
 *
 */
public interface DiseaseDAO extends CrudRepository<Disease, String> {

	/**
	 * Recherche les maladies par recherche insensible à la casse de la présence
	 * d'une sous-chaîne dans l'identifiant ou dans la description de la maladie.
	 * 
	 * @param keyword la sous-chaîne de recherche.
	 * @param limit   le nombre maximum d'enregistrement retournés.
	 * @return un {@link java.lang.Iterable} d'objets
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.model.Disease} représentant
	 *         les maladies trouvées.
	 */
	@Query(nativeQuery = true, value = "select distinct * from t_disease "
			+ "where lower(t_disease.id) like lower(concat('%', :keyword,'%')) "
			+ "or lower(t_disease.description) like lower(concat('%', :keyword,'%')) limit :limit")
	public Iterable<Disease> findByIdOrDescription(@Param("keyword") String keyword, @Param("limit") int limit);

}
