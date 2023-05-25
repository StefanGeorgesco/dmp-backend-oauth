package fr.cnam.stefangeorgesco.dmp.domain.service;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.service.UserService;
import fr.cnam.stefangeorgesco.dmp.domain.dao.*;
import fr.cnam.stefangeorgesco.dmp.domain.dto.*;
import fr.cnam.stefangeorgesco.dmp.domain.model.*;
import fr.cnam.stefangeorgesco.dmp.exception.domain.*;
import fr.cnam.stefangeorgesco.dmp.utils.SecurityCodeGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Classe de service pour la gestion des dossiers patients et objets rattachés.
 *
 * @author Stéfan Georgesco
 */
@Service
public class PatientFileServiceImpl implements PatientFileService {

    private final RnippService rnippService;

    private final UserService userService;

    private final PatientFileDAO patientFileDAO;

    private final FileDAO fileDAO;

    private final DoctorDAO doctorDAO;

    private final CorrespondenceDAO correspondenceDAO;

    private final PatientFileItemDAO patientFileItemDAO;

    private final ModelMapper commonModelMapper;

    private final ModelMapper patientFileModelMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public PatientFileServiceImpl(
            RnippService rnippService,
            UserService userService,
            PatientFileDAO patientFileDAO,
            FileDAO fileDAO,
            DoctorDAO doctorDAO,
            CorrespondenceDAO correspondenceDAO,
            PatientFileItemDAO patientFileItemDAO,
            ModelMapper commonModelMapper,
            ModelMapper patientFileModelMapper,
            BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        this.rnippService = rnippService;
        this.userService = userService;
        this.patientFileDAO = patientFileDAO;
        this.fileDAO = fileDAO;
        this.doctorDAO = doctorDAO;
        this.correspondenceDAO = correspondenceDAO;
        this.patientFileItemDAO = patientFileItemDAO;
        this.commonModelMapper = commonModelMapper;
        this.patientFileModelMapper = patientFileModelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * Service de création d'un dossier patient. Le service vérifie qu'un dossier
     * avec le même identifiant n'existe pas.
     *
     * @param patientFileDTO l'objet
     *                       {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
     *                       représentant le dossier patient à créer.
     * @return un objet
     * {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
     * représentant le dossier patient créé.
     * @throws CheckException  le dossier patient n'existe pas au RNIPP.
     * @throws CreateException le dossier patient n'a pas pu être créé.
     */
    @Override
    public PatientFileDTO createPatientFile(PatientFileDTO patientFileDTO) throws CheckException, CreateException {

        rnippService.checkPatientData(patientFileDTO);

        if (fileDAO.existsById(patientFileDTO.getId())) {
            throw new DuplicateKeyException("Un dossier avec cet identifiant existe déjà.");
        }

        patientFileDTO.setSecurityCode(SecurityCodeGenerator.generateCode());

        PatientFile patientFile = commonModelMapper.map(patientFileDTO, PatientFile.class);

        patientFile.setSecurityCode(bCryptPasswordEncoder.encode(patientFile.getSecurityCode()));

        try {
            patientFileDAO.save(patientFile);
        } catch (Exception e) {
            throw new CreateException("Le dossier patient n'a pas pu être créé.");
        }

        return patientFileDTO;
    }

    /**
     * Service de recherche d'un dossier patient par son identifiant.
     *
     * @param id l'identifiant du dossier recherché.
     * @return un objet
     * {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
     * représentant le dossier patient trouvé.
     * @throws FinderException dossier patient non trouvé.
     */
    @Override
    public PatientFileDTO findPatientFile(String id) throws FinderException {
        Optional<PatientFile> optionalPatientFile = patientFileDAO.findById(id);

        if (optionalPatientFile.isPresent()) {
            return patientFileModelMapper.map(optionalPatientFile.get(), PatientFileDTO.class);
        } else {
            throw new FinderException("Dossier patient non trouvé.");
        }
    }

    /**
     * Service de modification d'un dossier patient. Les données prises en compte
     * dans la modification sont le numéro de téléphone, l'adresse email et
     * l'adresse postale. Les autres données ne sont pas considérées.
     *
     * @param patientFileDTO l'objet
     *                       {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
     *                       représentant le dossier patient à modifier et les
     *                       données modifiées.
     * @return un objet
     * {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
     * représentant le dossier patient modifié.
     * @throws UpdateException le dossier patient n'a pas pu être modifié.
     */
    @Override
    public PatientFileDTO updatePatientFile(PatientFileDTO patientFileDTO) throws UpdateException {

        PatientFile patientFile = patientFileDAO.findById(patientFileDTO.getId()).orElseThrow();

        patientFile.setPhone(patientFileDTO.getPhone());
        patientFile.setEmail(patientFileDTO.getEmail());

        PatientFile mappedPatientFile = commonModelMapper.map(patientFileDTO, PatientFile.class);

        patientFile.setAddress(mappedPatientFile.getAddress());

        try {
            patientFile = patientFileDAO.save(patientFile);
        } catch (Exception e) {
            throw new UpdateException("Le dossier patient n'a pas pu être modifié.");
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(patientFileDTO.getId());
        userDTO.setFirstname(patientFileDTO.getFirstname());
        userDTO.setLastname(patientFileDTO.getLastname());
        userDTO.setEmail(patientFileDTO.getEmail());
        userService.updateUser(userDTO);

        return patientFileModelMapper.map(patientFile, PatientFileDTO.class);
    }

    /**
     * Service de modification du médecin référent d'un dossier patient. Le service
     * vérifie si le dossier patient et le dossier de médecin du nouveau médecin
     * référent existent.
     *
     * @param patientFileDTO l'objet
     *                       {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
     *                       représentant le dossier patient dont on souhaite
     *                       modifier le médecin référent, comportant dans son
     *                       attribut referringDoctorId l'identifiant du dossier de
     *                       médecin du nouveau médecin référent.
     * @return l'objet {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
     * représentant le dossier patient modifié.
     * @throws FinderException le dossier patient n'a pas été trouvé ou bien le dossier
     *                         de médecin référent n'a pas été trouvé.
     * @throws UpdateException le dossier patient n'a pas pu être modifié.
     */
    @Override
    public PatientFileDTO updateReferringDoctor(PatientFileDTO patientFileDTO) throws FinderException, UpdateException {

        Optional<PatientFile> optionalPatientFile = patientFileDAO.findById(patientFileDTO.getId());

        if (optionalPatientFile.isEmpty()) {
            throw new FinderException("Dossier patient non trouvé.");
        }

        PatientFile patientFile = optionalPatientFile.get();

        Optional<Doctor> optionalDoctor = doctorDAO.findById(patientFileDTO.getReferringDoctorId());

        if (optionalDoctor.isEmpty()) {
            throw new FinderException("Dossier de médecin non trouvé.");
        }

        Doctor doctor = optionalDoctor.get();
        patientFile.setReferringDoctor(doctor);

        try {
            patientFile = patientFileDAO.save(patientFile);
        } catch (Exception e) {
            throw new UpdateException("Le dossier patient n'a pas pu être modifié (médecin référent).");
        }

        return patientFileModelMapper.map(patientFile, PatientFileDTO.class);
    }

    /**
     * Service de recherche de dossiers patients à partir d'une chaîne de
     * caractères.
     *
     * @param q la chaîne de caractères de recherche.
     * @return une liste ({@link java.util.List}) d'objets
     * {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
     * représentant les dossiers trouvés.
     */
    @Override
    public List<PatientFileDTO> findPatientFilesByIdOrFirstnameOrLastname(String q) {

        if ("".equals(q)) {
            return new ArrayList<>();
        }

        Iterable<PatientFile> patientFiles = patientFileDAO.findByIdOrFirstnameOrLastname(q);

        return ((List<PatientFile>) patientFiles).stream()
                .map(patientFile -> patientFileModelMapper.map(patientFile, PatientFileDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Service de suppression d'un dossier patient désigné par son identifiant. Les
     * éventuelles correspondances, éléments médicaux et compte utilisateur associés
     * au dossier sont également supprimés.
     *
     * @param patientFileId l'identifiant du dossier à supprimer.
     * @throws DeleteException le dossier patient n'a pas pu être supprimé.
     */
    @Override
    public void deletePatientFile(String patientFileId) throws DeleteException {

        correspondenceDAO.deleteAllByPatientFileId(patientFileId);
        patientFileItemDAO.deleteAllByPatientFileId(patientFileId);

        try {
            patientFileDAO.deleteById(patientFileId);
        } catch (Exception e) {
            throw new DeleteException("Le dossier patient n'a pas pu être supprimé.");
        }

        try {
            userService.deleteUser(patientFileId);
        } catch (DeleteException e) {
            System.out.println("Pas de compte utilisateur associé au dossier patient supprimé");
        }
    }
}
