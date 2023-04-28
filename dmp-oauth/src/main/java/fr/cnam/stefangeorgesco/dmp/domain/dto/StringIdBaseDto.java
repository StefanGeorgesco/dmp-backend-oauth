package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.constraints.NotBlank;

public class StringIdBaseDto extends BaseDto {

    @NotBlank(message = "L'identifiant est obligatoire.")
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
