package fr.cnam.stefangeorgesco.dmp.domain.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import fr.cnam.stefangeorgesco.dmp.domain.model.MedicalAct;

/**
 * Repository pour les objets
 * {@link fr.cnam.stefangeorgesco.dmp.domain.model.MedicalAct}
 * 
 * @author Stéfan Georgesco
 *
 */
public interface MedicalActDAO extends CrudRepository<MedicalAct, String> {

	/**
	 * Recherche les actes médicaux par recherche insensible à la casse de la
	 * présence d'une sous-chaîne dans l'identifiant ou dans la description de
	 * l'acte médical.
	 * 
	 * @param keyword la sous-chaîne de recherche.
	 * @param limit   le nombre maximum d'enregistrement retournés.
	 * @return un {@link java.lang.Iterable} d'objets
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.model.MedicalAct}
	 *         représentant les maladies trouvées.
	 */
	@Query(nativeQuery = true, value = "select distinct * from t_medical_act "
			+ "where lower(t_medical_act.id) like lower(concat('%', :keyword,'%')) "
			+ "or lower(t_medical_act.description) like lower(concat('%', :keyword,'%')) limit :limit")
	public Iterable<MedicalAct> findByIdOrDescription(@Param("keyword") String keyword, @Param("limit") int limit);

}
