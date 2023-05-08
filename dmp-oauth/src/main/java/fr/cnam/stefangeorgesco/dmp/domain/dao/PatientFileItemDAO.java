package fr.cnam.stefangeorgesco.dmp.domain.dao;

import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFileItem;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.UUID;

/**
 * Repository pour les objets
 * {@link fr.cnam.stefangeorgesco.dmp.domain.model.PatientFileItem}
 * 
 * @author Stéfan Georgesco
 *
 */
@Transactional
public interface PatientFileItemDAO extends JpaRepository<PatientFileItem, UUID> {

	Iterable<PatientFileItem> findByPatientFileId(String patientFileId);

	int deleteAllByPatientFileId(String patientFileId);

}
