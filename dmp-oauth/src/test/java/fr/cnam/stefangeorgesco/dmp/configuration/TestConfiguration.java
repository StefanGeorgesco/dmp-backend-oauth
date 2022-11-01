package fr.cnam.stefangeorgesco.dmp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;
import fr.cnam.stefangeorgesco.dmp.domain.dto.ActDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.AddressDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondenceDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DiagnosisDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DiseaseDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.MailDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.MedicalActDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PrescriptionDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.SymptomDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Act;
import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Correspondence;
import fr.cnam.stefangeorgesco.dmp.domain.model.Diagnosis;
import fr.cnam.stefangeorgesco.dmp.domain.model.Disease;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.Mail;
import fr.cnam.stefangeorgesco.dmp.domain.model.MedicalAct;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import fr.cnam.stefangeorgesco.dmp.domain.model.Prescription;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;
import fr.cnam.stefangeorgesco.dmp.domain.model.Symptom;

@Configuration
public class TestConfiguration {

	@Bean(name = "user")
	@Scope(value = "prototype")
	User getUser() {
		return new User();
	}

	@Bean(name = "doctor")
	@Scope(value = "prototype")
	Doctor getDoctor() {
		return new Doctor();
	}

	@Bean(name = "patientFile")
	@Scope(value = "prototype")
	PatientFile getPatientFile() {
		return new PatientFile();
	}

	@Bean(name = "address")
	@Scope(value = "prototype")
	Address getAddress() {
		return new Address();
	}

	@Bean(name = "correspondance")
	@Scope(value = "prototype")
	Correspondence getCorrespondance() {
		return new Correspondence();
	}

	@Bean(name = "specialty")
	@Scope(value = "prototype")
	Specialty getSpecialty() {
		return new Specialty();
	}

	@Bean(name = "act")
	@Scope(value = "prototype")
	Act getAct() {
		return new Act();
	}

	@Bean(name = "diagnosis")
	@Scope(value = "prototype")
	Diagnosis getDiagnosis() {
		return new Diagnosis();
	}

	@Bean(name = "disease")
	@Scope(value = "prototype")
	Disease getDisease() {
		return new Disease();
	}

	@Bean(name = "mail")
	@Scope(value = "prototype")
	Mail getMail() {
		return new Mail();
	}

	@Bean(name = "medicalAct")
	@Scope(value = "prototype")
	MedicalAct getMedicalAct() {
		return new MedicalAct();
	}

	@Bean(name = "prescription")
	@Scope(value = "prototype")
	Prescription getPrescription() {
		return new Prescription();
	}

	@Bean(name = "symptom")
	@Scope(value = "prototype")
	Symptom getSymptom() {
		return new Symptom();
	}

	@Bean(name = "userDTO")
	@Scope(value = "prototype")
	UserDTO getUserDTO() {
		return new UserDTO();
	}

	@Bean(name = "doctorDTO")
	@Scope(value = "prototype")
	DoctorDTO getDoctorDTO() {
		return new DoctorDTO();
	}

	@Bean(name = "patientFileDTO")
	@Scope(value = "prototype")
	PatientFileDTO getPatientFileDTO() {
		return new PatientFileDTO();
	}

	@Bean(name = "correspondanceDTO")
	@Scope(value = "prototype")
	CorrespondenceDTO getCorrespondanceDTO() {
		return new CorrespondenceDTO();
	}

	@Bean(name = "addressDTO")
	@Scope(value = "prototype")
	AddressDTO getAddressDTO() {
		return new AddressDTO();
	}

	@Bean(name = "specialtyDTO")
	@Scope(value = "prototype")
	SpecialtyDTO getSpecialtyDTO() {
		return new SpecialtyDTO();
	}

	@Bean(name = "actDTO")
	@Scope(value = "prototype")
	ActDTO getActDTO() {
		return new ActDTO();
	}

	@Bean(name = "diagnosisDTO")
	@Scope(value = "prototype")
	DiagnosisDTO getDiagnosisDTO() {
		return new DiagnosisDTO();
	}

	@Bean(name = "diseaseDTO")
	@Scope(value = "prototype")
	DiseaseDTO getDiseaseDTO() {
		return new DiseaseDTO();
	}

	@Bean(name = "mailDTO")
	@Scope(value = "prototype")
	MailDTO getMailDTO() {
		return new MailDTO();
	}

	@Bean(name = "medicalActDTO")
	@Scope(value = "prototype")
	MedicalActDTO getMedicalActDTO() {
		return new MedicalActDTO();
	}

	@Bean(name = "prescriptionDTO")
	@Scope(value = "prototype")
	PrescriptionDTO getPrescriptionDTO() {
		return new PrescriptionDTO();
	}

	@Bean(name = "symptomDTO")
	@Scope(value = "prototype")
	SymptomDTO getSymptomDTO() {
		return new SymptomDTO();
	}

}
