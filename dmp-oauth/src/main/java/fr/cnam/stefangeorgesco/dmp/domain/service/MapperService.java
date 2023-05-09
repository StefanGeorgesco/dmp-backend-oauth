package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileItemDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFileItem;

public interface MapperService {
    PatientFileItemDTO mapToDTO(PatientFileItem item);

    PatientFileItem mapToEntity(PatientFileItemDTO itemDTO);
}
