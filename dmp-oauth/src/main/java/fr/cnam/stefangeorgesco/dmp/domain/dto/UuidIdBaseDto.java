package fr.cnam.stefangeorgesco.dmp.domain.dto;

import java.util.UUID;

public abstract class UuidIdBaseDto extends BaseDto {

    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
