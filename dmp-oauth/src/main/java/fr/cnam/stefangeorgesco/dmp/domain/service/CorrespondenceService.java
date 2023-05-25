package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondenceDTO;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CreateException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;

import java.util.List;
import java.util.UUID;

public interface CorrespondenceService {
    CorrespondenceDTO createCorrespondence(CorrespondenceDTO correspondenceDTO) throws CreateException;

    void deleteCorrespondence(UUID uuid);

    CorrespondenceDTO findCorrespondence(String id) throws FinderException;

    List<CorrespondenceDTO> findCorrespondencesByPatientFileId(String patientFileId);
}
