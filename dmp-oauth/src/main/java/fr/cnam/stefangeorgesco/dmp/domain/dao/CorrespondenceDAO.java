package fr.cnam.stefangeorgesco.dmp.domain.dao;

import fr.cnam.stefangeorgesco.dmp.domain.model.Correspondence;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.UUID;

/**
 * Repository pour les objets
 * {@link fr.cnam.stefangeorgesco.dmp.domain.model.Correspondence}
 * 
 * @author Stéfan Georgesco
 *
 */
@Transactional
public interface CorrespondenceDAO extends JpaRepository<Correspondence, UUID> {

	Iterable<Correspondence> findByPatientFileId(String patientFileId);

	int deleteAllByPatientFileId(String patientFileId);

}
