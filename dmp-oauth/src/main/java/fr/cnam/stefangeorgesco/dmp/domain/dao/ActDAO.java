package fr.cnam.stefangeorgesco.dmp.domain.dao;

import fr.cnam.stefangeorgesco.dmp.domain.model.Act;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ActDAO extends JpaRepository<Act, UUID> {

}
