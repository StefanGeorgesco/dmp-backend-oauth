package fr.cnam.stefangeorgesco.dmp.domain.dao;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFileItem;

/**
 * Repository pour les objets
 * {@link fr.cnam.stefangeorgesco.dmp.domain.model.PatientFileItem}
 * 
 * @author Stéfan Georgesco
 *
 */
@Transactional
public interface PatientFileItemDAO extends CrudRepository<PatientFileItem, UUID> {

	Iterable<PatientFileItem> findByPatientFileId(String patientFileId);

	int deleteAllByPatientFileId(String patientFileId);

}
