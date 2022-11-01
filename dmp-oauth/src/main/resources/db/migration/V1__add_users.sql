DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id` VARCHAR(255) NOT NULL,
  `username` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_username_user` (`username`)
) ENGINE=InnoDB;

INSERT INTO `t_user` VALUES
('A001','admin'),
('D001','john'),
('D002','marco'),
('P001','jean');
