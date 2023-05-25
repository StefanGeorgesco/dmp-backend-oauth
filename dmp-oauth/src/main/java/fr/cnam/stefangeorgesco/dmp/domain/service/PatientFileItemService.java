package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileItemDTO;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CreateException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.UpdateException;

import java.util.List;
import java.util.UUID;

public interface PatientFileItemService {
    PatientFileItemDTO createPatientFileItem(PatientFileItemDTO patientFileItemDTO) throws CreateException;

    PatientFileItemDTO findPatientFileItem(UUID uuid) throws FinderException;

    List<PatientFileItemDTO> findPatientFileItemsByPatientFileId(String patientFileId);

    PatientFileItemDTO updatePatientFileItem(PatientFileItemDTO patientFileItemDTO)
            throws FinderException, UpdateException;

    void deletePatientFileItem(UUID uuid);
}
