DROP TABLE IF EXISTS `t_file`;

CREATE TABLE `t_file` (
  `id` VARCHAR(255) NOT NULL,
  `firstname` VARCHAR(255) NOT NULL,
  `lastname` VARCHAR(255) NOT NULL,
  `phone` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `street1` VARCHAR(255) NOT NULL,
  `street2` VARCHAR(255) NOT NULL,
  `city` VARCHAR(255) NOT NULL,
  `state` VARCHAR(255) NOT NULL,
  `zipcode` VARCHAR(255) NOT NULL,
  `country` VARCHAR(255) NOT NULL,
  `security_code` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

INSERT INTO `t_file` VALUES
('D001', 'John', 'Smith', '0123456789', 'john.smith@doctors.com', '1 baker street', '', 'London', '', '99999', 'United Kingdom', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG'),
('P001', 'Jean', 'Martin', '0101010101', 'jean.martin@free.fr', '1 rue de la Paix', '', 'Paris', '', '75001', 'France', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG'),
('D002', 'Marc', 'Dupont', '9999999999', 'marc.dupont@docteurs.fr', '15 rue de Vaugirard', '', 'Paris', '', '75015', 'France', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG'),
('D004', 'Leah', 'Little', '061-074-7258', 'leah.little@example.com', '2727 The Avenue', '', 'Nenagh', '', '47362', 'Ireland', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG'),
('D005', 'Vivienne', 'Roy', '077 793 50 38', 'vivienne.roy@example.com', '1661 Rue du Village', '', 'Lichtensteig', '', '2126', 'Switzerland', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG'),
('D006', 'Vukašin', 'Adamović', '016-3028-125', 'vukasin.adamovic@example.com', '6351 Bresjačka', '', 'Senta', '', '95114', 'Serbia', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG'),
('D007', 'Eugenio', 'Olvera', '(615) 831 6362', 'eugenio.olvera@example.com', '3234 Circunvalación Bhután', '', 'Comalcalco', '', '47519', 'Mexico', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG'),
('D008', 'Darko', 'RatkovićRistić', '025-7372-402', 'darko.ratkovicristic@example.com', '1607 Dragiše Kašikovića', '', 'Boljevac', '', '14541', 'Serbia', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG'),
('D009', 'Peppi', 'Seppanen', '09-720-887', 'peppi.seppanen@example.com', '7237 Hämeenkatu', '', 'Lempäälä', '', '34397', 'Finland', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG'),
('D010', 'Rachel', 'Richards', '013873 97383', 'rachel.richards@example.com', '6294 St. John’S Road', '', 'Cambridge', '', 'WJ1 8LF', 'United Kingdom', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG'),
('D011', 'Bill', 'Carlson', '071-524-4134', 'bill.carlson@example.com', '2581 Mill Lane', '', 'Trim', '', '15049', 'Ireland', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG'),
('D012', 'Melquisedeque', 'Nascimento', '(21) 5214-4747', 'melquisedeque.nascimento@example.com', '2155 Rua Santa Luzia ', '', 'Olinda', '', '51415', 'Brazil', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG'),
('D013', 'Luke', 'Hansen', '061-855-6303', 'luke.hansen@example.com', '1201 Grange Road', '', 'Gorey', '', '71364', 'Ireland', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG'),
('P004', 'Roelf', 'Verwijmeren', '(089) 8518423', 'roelf.verwijmeren@example.com', '7481 Keerderstraat', '', 'Spijk Gld', '', '4951 XC', 'Netherlands', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG'),
('P005', 'Martha', 'Welch', '00-1044-2420', 'martha.welch@example.com', '2186 Blossom Hill Rd', '', 'Dubbo', '', '1178', 'Australia', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG'),
('P006', 'Lyubomila', 'Kalnickiy', '(098) A93-0296', 'lyubomila.kalnickiy@example.com', '1406 Mikoli Gastello', '', 'Irpin', '', '45431', 'Ukraine', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG'),
('P007', 'Phoebe', 'Clarke', '(183)-791-1522', 'phoebe.clarke@example.com', '5100 Weymouth Road', '', 'Wellington', '', '54665', 'New Zealand', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG'),
('P008', 'Sami', 'Mjønes', '32959974', 'sami.mjones@example.com', '7467 Midtoddveien', '', 'Vear', '', '9365', 'Norway', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG'),
('P009', 'Alvaro', 'Canales', '(666) 146 7433', 'alvaro.canales@example.com', '1413 Circunvalación Tamaulipas', '', 'Morelos Cañada', '', '85598', 'Mexico', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG'),
('P010', 'Susan', 'Edwards', '031-909-2200', 'susan.edwards@example.com', '4577 Patrick Street', '', 'Wexford', '', '11051', 'Ireland', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG'),
('P011', 'Marie-Louise', 'Leroux', '077 177 95 66', 'marie-louise.leroux@example.com', '2556 Rue Dumenge', '', 'Savigny', '', '8618', 'Switzerland', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG'),
('P012', 'Julio', 'Bernard', '077 202 55 32', 'julio.bernard@example.com', '1492 Place des 44 Enfants D Izieu', '', 'Kernenried', '', '8314', 'Switzerland', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG'),
('P013', 'Magnus', 'Jensen', '19776685', 'magnus.jensen@example.com', '165 Bakkevænget', '', 'Stenderup', '', '19960', 'Denmark', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG'),
('P014', 'Fatih', 'Toraman', '(608)-401-2304', 'fatih.toraman@example.com', '2051 Şehitler Cd', '', 'Bingöl', '', '63992', 'Turkey', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG');

DROP TABLE IF EXISTS `t_doctor`;

CREATE TABLE `t_doctor` (
  `id` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_file_doctor` FOREIGN KEY (`id`) REFERENCES `t_file` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

INSERT INTO `t_doctor` VALUES
('D001'),
('D002'),
('D004'),
('D005'),
('D006'),
('D007'),
('D008'),
('D009'),
('D010'),
('D011'),
('D012'),
('D013');

DROP TABLE IF EXISTS `t_doctor_specialty`;

CREATE TABLE `t_doctor_specialty` (
  `doctor_id` VARCHAR(255) NOT NULL,
  `specialty_id` VARCHAR(255) NOT NULL,
  KEY `FK_specialty_id` (`specialty_id`),
  KEY `FK_doctor_id` (`doctor_id`),
  CONSTRAINT `FK_doctor_doctor_specialty` FOREIGN KEY (`doctor_id`) REFERENCES `t_doctor` (`id`),
  CONSTRAINT `FK_specialty_doctor_specialty` FOREIGN KEY (`specialty_id`) REFERENCES `t_specialty` (`id`)
) ENGINE=InnoDB;

INSERT INTO t_doctor_specialty(doctor_id, specialty_id) VALUES
('D001', 'S001'),
('D001', 'S024'),
('D002', 'S012'),
('D002', 'S013'),
('D004', 'S017'),
('D005', 'S045'),
('D006', 'S032'),
('D007', 'S027'),
('D007', 'S028'),
('D008', 'S002'),
('D009', 'S044'),
('D010', 'S039'),
('D011', 'S035'),
('D012', 'S011'),
('D012', 'S012'),
('D013', 'S012');

DROP TABLE IF EXISTS `t_patient_file`;

CREATE TABLE `t_patient_file` (
  `id` VARCHAR(255) NOT NULL,
  `date_of_birth` DATE NOT NULL,
  `referring_doctor_id` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_referring_doctor_id` (`referring_doctor_id`),
  CONSTRAINT `FK_doctor_patient_file` FOREIGN KEY (`referring_doctor_id`) REFERENCES `t_doctor` (`id`),
  CONSTRAINT `FK_file_patient_file` FOREIGN KEY (`id`) REFERENCES `t_file` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

INSERT INTO `t_patient_file` VALUES
('P001', '1995-05-15', 'D001'),
('P004', '1978-02-26', 'D004'),
('P005', '1974-07-16', 'D001'),
('P006', '1994-05-05', 'D004'),
('P007', '1991-07-28', 'D005'),
('P008', '1977-12-14', 'D004'),
('P009', '2000-01-01', 'D005'),
('P010', '1946-02-17', 'D004'),
('P011', '2000-11-30', 'D005'),
('P012', '1962-06-29', 'D004'),
('P013', '1986-02-27', 'D005'),
('P014', '1999-05-22', 'D001');
