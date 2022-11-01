package fr.cnam.stefangeorgesco.dmp.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.service.UserService;
import fr.cnam.stefangeorgesco.dmp.domain.dao.DoctorDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.FileDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.SpecialtyDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CreateException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DeleteException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.UpdateException;
import fr.cnam.stefangeorgesco.dmp.utils.SecurityCodeGenerator;

/**
 * Classe de service pour la gestion des dossiers de médecins et des spécialités
 * médicales.
 * 
 * @author Stéfan Georgesco
 *
 */
@Service
public class DoctorService {

	@Autowired
	private UserService userService;

	@Autowired
	private FileDAO fileDAO;

	@Autowired
	private DoctorDAO doctorDAO;

	@Autowired
	private ModelMapper commonModelMapper;

	@Autowired
	private ModelMapper doctorModelMapper;

	@Autowired
	private SpecialtyDAO specialtyDAO;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	/**
	 * Service de création d'un dossier de médecin. Le service vérifie qu'un dossier
	 * avec le même identifiant n'existe pas et que les spécialités existent.
	 * 
	 * @param doctorDTO l'objet
	 *                  {@link fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO}
	 *                  représentant le dossier de médecin à créer.
	 * @return un objet {@link fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO}
	 *         représentant le dossier de médecin créé.
	 * @throws FinderException une spécialité du dossier de médecin n'existe pas.
	 * @throws CreateException un dossier avec le même identifiant existe déjà.
	 */
	public DoctorDTO createDoctor(DoctorDTO doctorDTO) throws FinderException, CreateException {

		if (fileDAO.existsById(doctorDTO.getId())) {
			throw new DuplicateKeyException("Un dossier avec cet identifiant existe déjà.");
		}

		doctorDTO.setSecurityCode(SecurityCodeGenerator.generateCode());

		for (SpecialtyDTO specialtyDTO : doctorDTO.getSpecialtiesDTO()) {
			Optional<Specialty> optionalSpecialty = specialtyDAO.findById(specialtyDTO.getId());

			if (optionalSpecialty.isPresent()) {
				specialtyDTO.setDescription(optionalSpecialty.get().getDescription());
			} else {
				throw new FinderException("La spécialité n'existe pas.");
			}
		}

		Doctor doctor = commonModelMapper.map(doctorDTO, Doctor.class);

		doctor.setSecurityCode(bCryptPasswordEncoder.encode(doctorDTO.getSecurityCode()));

		try {
			doctorDAO.save(doctor);
		} catch (Exception e) {
			throw new CreateException("Le dossier de médecin n'a pas pu être créé.");
		}

		return doctorDTO;
	}

	/**
	 * Service de recherche d'un dossier de médecin par son identifiant.
	 * 
	 * @param id l'identifiant du dossier recherché.
	 * @return un objet {@link fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO}
	 *         représentant le dossier de médecin trouvé.
	 * @throws FinderException le dossier de médecin n'a pas été trouvé.
	 */
	public DoctorDTO findDoctor(String id) throws FinderException {

		Optional<Doctor> optionalDoctor = doctorDAO.findById(id);

		if (optionalDoctor.isPresent()) {
			return doctorModelMapper.map(optionalDoctor.get(), DoctorDTO.class);
		} else {
			throw new FinderException("Le dossier de médecin n'a pas été trouvé.");
		}

	}

	/**
	 * Service de recherche de dossiers de médecins à partir d'une chaîne de
	 * caractères.
	 * 
	 * @param q la chaîne de caractères de recherche.
	 * @return une liste ({@link java.util.List}) d'objets
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO} représentant
	 *         les dossiers trouvés.
	 */
	public List<DoctorDTO> findDoctorsByIdOrFirstnameOrLastname(String q) {

		if ("".equals(q)) {
			return new ArrayList<DoctorDTO>();
		}

		Iterable<Doctor> doctors = doctorDAO.findByIdOrFirstnameOrLastname(q);

		List<DoctorDTO> doctorsDTO = ((List<Doctor>) doctors).stream()
				.map(doctor -> doctorModelMapper.map(doctor, DoctorDTO.class)).collect(Collectors.toList());

		return doctorsDTO;
	}

	/**
	 * Service de modification d'un dossier de médecin.
	 * 
	 * @param doctorDTO l'objet
	 *                  {@link fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO}
	 *                  représentant le dossier de médecin à modifier et les données
	 *                  modifiées.
	 * @return un objet {@link fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO}
	 *         représentant le dossier de médecin modifié.
	 * @throws UpdateException le dossier de médecin n'a pas pu être modifié.
	 */
	public DoctorDTO updateDoctor(DoctorDTO doctorDTO) throws UpdateException {

		Doctor doctor = doctorDAO.findById(doctorDTO.getId()).get();

		doctor.setPhone(doctorDTO.getPhone());
		doctor.setEmail(doctorDTO.getEmail());

		Doctor mappedDoctor = commonModelMapper.map(doctorDTO, Doctor.class);

		doctor.setAddress(mappedDoctor.getAddress());

		try {
			doctorDAO.save(doctor);
		} catch (Exception e) {
			throw new UpdateException("Le dossier de médecin n'a pas pu être modifié.");
		}

		DoctorDTO response = doctorModelMapper.map(doctor, DoctorDTO.class);

		return response;
	}

	/**
	 * Service de suppression d'un dossier de médecin désigné par son identifiant.
	 * L'éventuel compte utilisateur associé au dossier est également supprimé.
	 * 
	 * @param id l'identifiant du dossier à supprimer.
	 * @throws DeleteException Le dossier de médecin n'a pas pu être supprimé.
	 */
	public void deleteDoctor(String id) throws DeleteException {

		try {
			doctorDAO.deleteById(id);
		} catch (Exception e) {
			throw new DeleteException("Le dossier de médecin n'a pas pu être supprimé.");
		}

		try {
			userService.deleteUser(id);
		} catch (DeleteException e) {
			System.out.println("Pas de compte utilisateur associé au dossier de médecin supprimé.");
		}

	}

	/**
	 * Service de recherche d'une spécialité médicale par son identifiant.
	 * 
	 * @param id l'identifiant de la spécialité recherchée.
	 * @return un objet {@link fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO}
	 *         représentant la specialité trouvée.
	 * @throws FinderException spécialité non trouvée.
	 */
	public SpecialtyDTO findSpecialty(String id) throws FinderException {

		Optional<Specialty> optionalSpecialty = specialtyDAO.findById(id);

		if (optionalSpecialty.isPresent()) {
			return commonModelMapper.map(optionalSpecialty.get(), SpecialtyDTO.class);
		} else {
			throw new FinderException("Spécialité non trouvée.");
		}

	}

	/**
	 * Service de recherche de spécialités à partir d'une chaîne de caractères.
	 * 
	 * @param q la chaîne de caractères de recherche.
	 * @return une liste ({@link java.util.List}) d'objets
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO}
	 *         représentant les spécialités trouvées.
	 */
	public List<SpecialtyDTO> findSpecialtiesByIdOrDescription(String q) {

		if ("".equals(q)) {
			return new ArrayList<>();
		}

		Iterable<Specialty> specialties = specialtyDAO.findByIdOrDescription(q);

		List<SpecialtyDTO> specialtiesDTO = ((List<Specialty>) specialties).stream()
				.map(specialty -> commonModelMapper.map(specialty, SpecialtyDTO.class)).collect(Collectors.toList());

		return specialtiesDTO;
	}

	/**
	 * Service de récupération de toutes les spécialités médicales.
	 * 
	 * @return une liste ({@link java.util.List}) d'objets
	 *         {@link fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO}
	 *         représentant troutes les spécialités.
	 */
	public List<SpecialtyDTO> findAllSpecialties() {
		Iterable<Specialty> specialties = specialtyDAO.findAll();

		List<SpecialtyDTO> specialtiesDTO = ((List<Specialty>) specialties).stream()
				.map(specialty -> commonModelMapper.map(specialty, SpecialtyDTO.class)).collect(Collectors.toList());

		return specialtiesDTO;
	}

}
