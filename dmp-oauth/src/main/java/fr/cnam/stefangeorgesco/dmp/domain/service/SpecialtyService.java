package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;

import java.util.List;

public interface SpecialtyService {
    SpecialtyDTO findSpecialty(String id) throws FinderException;

    List<SpecialtyDTO> findSpecialtiesByIdOrDescription(String q);

    List<SpecialtyDTO> findAllSpecialties();
}
