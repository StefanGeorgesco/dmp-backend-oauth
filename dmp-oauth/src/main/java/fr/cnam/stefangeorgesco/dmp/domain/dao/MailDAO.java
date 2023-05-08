package fr.cnam.stefangeorgesco.dmp.domain.dao;

import fr.cnam.stefangeorgesco.dmp.domain.model.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MailDAO extends JpaRepository<Mail, UUID> {
}
