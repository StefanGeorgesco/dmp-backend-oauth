package fr.cnam.stefangeorgesco.dmp.domain.dto;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Objet de transfert de données représentant un dossier de médecin.
 * 
 * @author Stéfan Georgesco
 *
 */
public class DoctorDTO extends FileDTO {

	/**
	 * Spécialités (objets de transfert de données) du médecin.
	 */
	@NotNull(message = "Les spécialités sont obligatoires.")
	@Size(min = 1, message = "Le médecin doit avoir au moins une spécialité.")
	@JsonProperty("specialties")
	private Collection<@Valid SpecialtyDTO> specialtiesDTO;

	public Collection<SpecialtyDTO> getSpecialtiesDTO() {
		return specialtiesDTO;
	}

	public void setSpecialtiesDTO(Collection<SpecialtyDTO> specialtiesDTO) {
		this.specialtiesDTO = specialtiesDTO;
	}

}
