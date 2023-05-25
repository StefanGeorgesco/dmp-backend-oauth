package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dto.DiseaseDTO;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;

import java.util.List;

public interface DiseaseService {
    DiseaseDTO findDisease(String id) throws FinderException;

    List<DiseaseDTO> findDiseasesByIdOrDescription(String q, int limit);
}
