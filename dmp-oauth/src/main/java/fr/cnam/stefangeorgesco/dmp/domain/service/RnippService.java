package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;

import javax.validation.Valid;

public interface RnippService {
    void checkPatientData(@Valid PatientFileDTO patientFileDTO) throws CheckException;
}
