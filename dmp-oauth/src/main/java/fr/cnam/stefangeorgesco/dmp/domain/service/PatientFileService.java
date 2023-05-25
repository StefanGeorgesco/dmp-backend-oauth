package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.exception.domain.*;

import java.util.List;

public interface PatientFileService {
    PatientFileDTO createPatientFile(PatientFileDTO patientFileDTO) throws CheckException, CreateException;

    PatientFileDTO findPatientFile(String id) throws FinderException;

    PatientFileDTO updatePatientFile(PatientFileDTO patientFileDTO) throws UpdateException;

    PatientFileDTO updateReferringDoctor(PatientFileDTO patientFileDTO) throws FinderException, UpdateException;

    List<PatientFileDTO> findPatientFilesByIdOrFirstnameOrLastname(String q);

    void deletePatientFile(String patientFileId) throws DeleteException;
}
