package fr.cnam.stefangeorgesco.dmp.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;
import fr.cnam.stefangeorgesco.dmp.domain.dto.ActDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DiagnosisDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Act;
import fr.cnam.stefangeorgesco.dmp.domain.model.Diagnosis;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;

/**
 * Classe de configuration des mappeurs de conversion entre les objets entités
 * et les objets de transfert de données.
 * 
 * @author Stéfan Georgesco
 *
 */
@Configuration
public class MapperConfig {

	/**
	 * Mappeur à utiliser pour tous les cas ne nécessitant pas un mappeur
	 * spécifique.
	 * 
	 * @return le bean mappeur
	 */
	@Bean
	public ModelMapper commonModelMapper() {
		return new ModelMapper();
	}

	/**
	 * Mappeur spécifique de conversion d'un objet
	 * {@link fr.cnam.stefangeorgesco.dmp.domain.model.Doctor} en objet
	 * {@link fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO}
	 * 
	 * @return le bean mappeur
	 */
	@Bean
	public ModelMapper doctorModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<Doctor, DoctorDTO> typeMap = modelMapper.createTypeMap(Doctor.class, DoctorDTO.class);
		modelMapper.getConfiguration().setSkipNullEnabled(true);
		typeMap.addMappings(mapper -> mapper.skip(DoctorDTO::setSecurityCode));
		typeMap.addMapping(src -> src.getSpecialties(), DoctorDTO::setSpecialtiesDTO);
		typeMap.addMapping(src -> src.getAddress(), DoctorDTO::setAddressDTO);

		return modelMapper;
	}

	/**
	 * Mappeur spécifique de conversion d'un objet
	 * {@link fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile} en objet
	 * {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
	 * 
	 * @return le bean mappeur
	 */
	@Bean
	public ModelMapper patientFileModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<PatientFile, PatientFileDTO> typeMap = modelMapper.createTypeMap(PatientFile.class,
				PatientFileDTO.class);
		modelMapper.getConfiguration().setSkipNullEnabled(true);
		typeMap.addMappings(mapper -> mapper.skip(PatientFileDTO::setSecurityCode));
		typeMap.addMapping(src -> src.getAddress(), PatientFileDTO::setAddressDTO);

		return modelMapper;
	}

	/**
	 * Mappeur spécifique de conversion d'un objet
	 * {@link fr.cnam.stefangeorgesco.dmp.domain.model.Diagnosis} en objet
	 * {@link fr.cnam.stefangeorgesco.dmp.domain.dto.DiagnosisDTO}
	 * 
	 * @return le bean mappeur
	 */
	@Bean
	public ModelMapper diagnosisModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<Diagnosis, DiagnosisDTO> typeMap = modelMapper.createTypeMap(Diagnosis.class, DiagnosisDTO.class);
		typeMap.addMapping(src -> src.getDisease(), DiagnosisDTO::setDiseaseDTO);

		return modelMapper;
	}

	/**
	 * Mappeur spécifique de conversion d'un objet
	 * {@link fr.cnam.stefangeorgesco.dmp.domain.model.Act} en objet
	 * {@link fr.cnam.stefangeorgesco.dmp.domain.dto.ActDTO}
	 * 
	 * @return le bean mappeur
	 */
	@Bean
	public ModelMapper actModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<Act, ActDTO> typeMap = modelMapper.createTypeMap(Act.class, ActDTO.class);
		typeMap.addMapping(src -> src.getMedicalAct(), ActDTO::setMedicalActDTO);

		return modelMapper;
	}

	/**
	 * Mappeur spécifique de conversion d'un objet
	 * {@link fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User} en objet
	 * {@link fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO}
	 * 
	 * @return le bean mappeur
	 */
	@Bean
	public ModelMapper userModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<User, UserDTO> typeMap = modelMapper.createTypeMap(User.class, UserDTO.class);
		modelMapper.getConfiguration().setSkipNullEnabled(true);
		typeMap.addMappings(mapper -> mapper.skip(UserDTO::setPassword));
		typeMap.addMappings(mapper -> mapper.skip(UserDTO::setSecurityCode));

		return modelMapper;
	}

}
