package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dto.MedicalActDTO;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;

import java.util.List;

public interface MedicalActService {
    MedicalActDTO findMedicalAct(String id) throws FinderException;

    List<MedicalActDTO> findMedicalActsByIdOrDescription(String q, int limit);
}
