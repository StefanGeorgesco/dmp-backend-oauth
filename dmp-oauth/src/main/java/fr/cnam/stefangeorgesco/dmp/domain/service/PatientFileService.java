package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dto.*;
import fr.cnam.stefangeorgesco.dmp.exception.domain.*;

import java.util.List;
import java.util.UUID;

public interface PatientFileService {
    PatientFileDTO createPatientFile(PatientFileDTO patientFileDTO) throws CheckException, CreateException;

    PatientFileDTO findPatientFile(String id) throws FinderException;

    PatientFileDTO updatePatientFile(PatientFileDTO patientFileDTO) throws UpdateException;

    PatientFileDTO updateReferringDoctor(PatientFileDTO patientFileDTO) throws FinderException, UpdateException;

    List<PatientFileDTO> findPatientFilesByIdOrFirstnameOrLastname(String q);

    CorrespondenceDTO createCorrespondence(CorrespondenceDTO correspondenceDTO) throws CreateException;

    void deleteCorrespondence(UUID uuid);

    CorrespondenceDTO findCorrespondence(String id) throws FinderException;

    List<CorrespondenceDTO> findCorrespondencesByPatientFileId(String patientFileId);

    DiseaseDTO findDisease(String id) throws FinderException;

    MedicalActDTO findMedicalAct(String id) throws FinderException;

    List<DiseaseDTO> findDiseasesByIdOrDescription(String q, int limit);

    List<MedicalActDTO> findMedicalActsByIdOrDescription(String q, int limit);

    PatientFileItemDTO createPatientFileItem(PatientFileItemDTO patientFileItemDTO) throws CreateException;

    PatientFileItemDTO findPatientFileItem(UUID uuid) throws FinderException;

    List<PatientFileItemDTO> findPatientFileItemsByPatientFileId(String patientFileId);

    PatientFileItemDTO updatePatientFileItem(PatientFileItemDTO patientFileItemDTO)
            throws FinderException, UpdateException;

    void deletePatientFileItem(UUID uuid);

    void deletePatientFile(String patientFileId) throws DeleteException;
}
