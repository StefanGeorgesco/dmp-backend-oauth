package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CreateException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DeleteException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.UpdateException;

import java.util.List;

public interface DoctorService {
    DoctorDTO createDoctor(DoctorDTO doctorDTO) throws FinderException, CreateException;

    DoctorDTO findDoctor(String id) throws FinderException;

    List<DoctorDTO> findDoctorsByIdOrFirstnameOrLastname(String q);

    DoctorDTO updateDoctor(DoctorDTO doctorDTO) throws UpdateException;

    void deleteDoctor(String id) throws DeleteException;
}
