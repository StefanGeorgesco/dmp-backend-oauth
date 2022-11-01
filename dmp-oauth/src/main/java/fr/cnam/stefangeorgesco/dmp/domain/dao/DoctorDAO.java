package fr.cnam.stefangeorgesco.dmp.domain.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;

/**
 * Repository pour les objets
 * {@link fr.cnam.stefangeorgesco.dmp.domain.model.Doctor}
 * 
 * @author Stéfan Georgesco
 *
 */
public interface DoctorDAO extends CrudRepository<Doctor, String> {

	/**
	 * Recherche les dossiers de médecins par recherche insensible à la casse de la
	 * présence d'une sous-chaîne dans l'identifiant du dossier ou dans le prénom ou
	 * dans le nom du médecin.
	 * 
	 * @param keyword la sous-chaîne de recherche.
	 * @return un {@link java.lang.Iterable} d'objets
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.model.Doctor} représentant
	 *         les dossiers de médecins trouvés.
	 */
	@Query("select distinct doctor from Doctor doctor "
			+ "where lower(doctor.id) like lower(concat('%', :keyword,'%')) "
			+ "or lower(doctor.firstname) like lower(concat('%', :keyword,'%')) "
			+ "or lower(doctor.lastname) like lower(concat('%', :keyword,'%'))")
	public Iterable<Doctor> findByIdOrFirstnameOrLastname(@Param("keyword") String keyword);

}
