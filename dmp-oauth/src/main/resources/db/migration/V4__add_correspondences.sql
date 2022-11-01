DROP TABLE IF EXISTS `t_correspondence`;

CREATE TABLE `t_correspondence` (
  `id` VARCHAR(255) NOT NULL,
  `date_until` DATE NOT NULL,
  `doctor_id` VARCHAR(255) NOT NULL,
  `patient_file_id` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_doctor_id` (`doctor_id`),
  KEY `FK_patient_file_id` (`patient_file_id`),
  CONSTRAINT `FK_patient_file_correspondence` FOREIGN KEY (`patient_file_id`) REFERENCES `t_patient_file` (`id`),
  CONSTRAINT `FK_doctor_correspondence` FOREIGN KEY (`doctor_id`) REFERENCES `t_doctor` (`id`)
) ENGINE=InnoDB;

INSERT INTO `t_correspondence` VALUES
('5b17ffa7-81e2-43ac-9246-7cab5b2f0f6b', '2023-05-02', 'D002', 'P001'),
('a376a45f-17d3-4b75-ad08-6b1da02616b6', '2022-01-05', 'D002', 'P004'),
('531c2161-5507-485f-8a23-de65416b4644', '2023-04-12', 'D004', 'P005'),
('680e2054-b757-4d1e-94dc-1baeb0dbedf8', '2022-11-30', 'D005', 'P006'),
('119b2f84-80ee-4d83-adb1-d788d85c76fd', '2023-10-31', 'D006', 'P007'),
('e1eb3425-d257-4c5e-8600-b125731c458c', '2023-01-01', 'D007', 'P001'),
('2454a376-9f8c-4e01-ab07-87b3cb992ae3', '2021-03-22', 'D008', 'P011'),
('f549abff-1a11-4d9e-9cea-fcbef29eb35c', '2023-06-15', 'D009', 'P012'),
('77aaab4e-6b17-4c3a-88c0-b007122db4bc', '2022-07-12', 'D001', 'P013'),
('ef11c7e1-1694-474e-8d34-eb0a1677b140', '2023-04-24', 'D011', 'P001'),
('3d80bbeb-997e-4354-82d3-68cea80256d6', '2023-08-14', 'D012', 'P004'),
('1bd7028d-c850-4439-8cd3-8a38fb9365a2', '2022-02-07', 'D013', 'P013'),
('1e5670ac-3c72-4671-a22f-94a57ab72982', '2022-11-03', 'D004', 'P009'),
('7fd83511-1709-44b4-b60d-b100093402cb', '2023-12-12', 'D005', 'P008'),
('8ea37abc-052d-4bc3-9aa1-f9a47e366e11', '2023-05-07', 'D001', 'P006'),
('49fc5d3c-843e-4dbb-8cef-23ac5a042539', '2021-05-07', 'D005', 'P001'),
('0a70521f-1725-46fa-b55b-3fa54a2755a6', '2021-11-15', 'D010', 'P005'),
('fd56e9ea-ce99-4c2d-8cd7-7b2229cfc355', '2022-03-27', 'D007', 'P006'),
('2c8870d5-e063-4d09-b534-6d346b516db1', '2022-06-03', 'D008', 'P014'),
('f9516df6-769a-45ee-bfce-f64dde632a38', '2022-11-24', 'D006', 'P014');
