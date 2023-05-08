package fr.cnam.stefangeorgesco.dmp.domain.dao;

import fr.cnam.stefangeorgesco.dmp.domain.model.Symptom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SymptomDAO extends JpaRepository<Symptom, UUID> {
}
