DROP TABLE IF EXISTS `t_patient_file_item`;

CREATE TABLE `t_patient_file_item` (
  `id` varchar(255) NOT NULL,
  `date` date NOT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `authoring_doctor_id` varchar(255) NOT NULL,
  `patient_file_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_authoring_doctor_id` (`authoring_doctor_id`),
  KEY `FKmv9senx13a9vhmy2xhe7m6t1e` (`patient_file_id`),
  CONSTRAINT `FK_doctor_patient_file_item` FOREIGN KEY (`authoring_doctor_id`) REFERENCES `t_doctor` (`id`),
  CONSTRAINT `FK_patient_file_patient_file_item` FOREIGN KEY (`patient_file_id`) REFERENCES `t_patient_file` (`id`)
) ENGINE=InnoDB;

INSERT INTO `t_patient_file_item` VALUES
('1b57e70f-8eb0-4a97-99c6-5d44f138c22c', '2021-10-11', 'Praesent varius est eget risus rhoncus eleifend.', 'D001', 'P005'),
('707b71f1-0bbd-46ec-b79c-c9717bd6b2cd', '2019-03-23', 'Phasellus id ligula nec risus vehicula rhoncus.', 'D009', 'P007'),
('3ab3d311-585c-498e-aaca-728c00beb86e', '2021-02-03', 'Sed euismod felis et leo vestibulum, nec faucibus sapien pellentesque.', 'D004', 'P001'),
('31571533-a9d4-4b10-ac46-8afe0247e6cd', '2020-12-26', 'Ut tincidunt risus sed ipsum egestas, aliquam facilisis velit commodo.', 'D002', 'P004'),
('142763cf-6eeb-47a5-b8f8-8ec85f0025c4', '2022-05-07', 'Ut posuere quam in placerat gravida.', 'D001', 'P013'),
('f6de43eb-d86b-49bd-8a57-bb005fc5b062', '2019-01-17', 'Duis lobortis arcu sed enim fermentum fermentum.', 'D005', 'P012'),
('cf708537-91cc-4e03-8979-c7988234d375', '2021-07-30', 'Curabitur sit amet felis aliquam, sodales justo quis, ornare tellus.', 'D006', 'P011'),
('736bab44-5c6d-4beb-9e1e-5be4f3b082a3', '2020-11-16', 'Mauris lacinia nibh sed blandit ultricies.', 'D013', 'P009'),
('643bce5b-9c74-4e6f-8ae2-5809a31bedb4', '2022-08-01', 'Vivamus faucibus enim efficitur tellus egestas, sed condimentum erat porta.', 'D012', 'P006'),
('8dddad2f-b9e0-48fb-be56-298c9c512015', '2019-04-06', 'Donec tincidunt risus nec massa scelerisque eleifend.', 'D011', 'P010'),
('b087dee3-b0bc-4b3e-9e34-56f14a2c9511', '2021-09-20', 'Etiam pharetra risus sed bibendum vulputate.', 'D010', 'P008'),
('c793da7f-5ca8-41f5-a0f0-1cc77b34b6fe', '2020-06-29', 'Etiam at ex quis lacus hendrerit egestas.', 'D007', 'P005'),
('aab462f4-04ab-47d9-8ab5-3229404e8c13', '2021-10-28', 'Quisque sagittis ante eget arcu faucibus dictum.', 'D008', 'P007'),
('eaf55efd-c8bf-4f70-a6a4-60ab2b1d7824', '2019-03-15', 'Mauris vitae ante sed ex dignissim euismod.', 'D009', 'P001'),
('a46b49de-de29-4912-a13e-62c0cf995317', '2021-02-12', 'Nullam eget risus ac tortor mollis malesuada sit amet eu sem.', 'D004', 'P004'),
('9babb2b2-21b5-4490-95da-4a33db04eff4', '2020-12-02', 'Quisque cursus quam in sollicitudin imperdiet.', 'D002', 'P013'),
('b7bdb6e4-da4b-47f9-9b67-cfd67567aaca', '2022-05-31', 'Cras eget risus eget ante cursus finibus.', 'D001', 'P012'),
('bb2bb081-fcd1-41eb-a054-91d6ae433a2c', '2019-01-22', 'Nulla quis diam congue, volutpat lectus tempus, pellentesque risus.', 'D005', 'P011'),
('b0a5e8d0-4ba7-491e-b109-3e0620a3b0c4', '2021-07-09', 'Nam eget nibh nec urna finibus feugiat.', 'D006', 'P009'),
('4a3e7cc1-2298-4ff0-9d7a-1d0f4377e24a', '2020-11-10', 'Vivamus eleifend ipsum ut urna auctor, sed maximus justo scelerisque.', 'D013', 'P006'),
('d4383a18-231e-4f8a-9bca-17de42d98968', '2022-08-04', 'Sed condimentum massa et metus blandit feugiat.', 'D012', 'P010'),
('4b2d490e-2024-4a63-a5f9-97dbcdc8d823', '2019-04-27', 'Integer eu nulla nec lorem tempus tempor ut at lacus.', 'D011', 'P008'),
('83b2f235-f183-4144-8eb8-2e4cac07d434', '2021-09-24', 'Suspendisse et tortor ut ligula consequat rhoncus.', 'D010', 'P005'),
('c4846634-e4a0-41eb-b9b0-778cfbf31642', '2020-06-05', 'Aliquam at arcu dapibus, luctus ante et, volutpat sem.', 'D007', 'P007'),
('bde02c78-723b-416e-ad87-3a917a11cf5f', '2021-10-13', 'Proin nec urna a dui eleifend eleifend non vitae erat.', 'D008', 'P001'),
('849031b7-eb23-49e8-995a-8c5077e86e02', '2019-03-25', 'Fusce sagittis urna eget bibendum varius.', 'D009', 'P004'),
('788c2cb1-400d-4125-9924-bb105efbced7', '2021-02-21', 'Aenean quis elit at nulla volutpat aliquam.', 'D004', 'P013'),
('42b68587-6756-4d4c-9429-eda577d6ffee', '2020-12-08', 'Quisque tristique mi eget euismod blandit.', 'D002', 'P012'),
('62f0ab70-f9ef-4bde-a9c3-a63aa15f4631', '2022-05-18', 'Nullam nec mi et odio bibendum venenatis nec ac mauris.', 'D001', 'P011'),
('5b0ce20b-4ecf-4b23-ae8b-24fb085767ff', '2019-01-14', 'Sed tincidunt tellus vel justo convallis euismod.', 'D005', 'P009'),
('150d4d20-b686-41c4-8b41-04d876462dcb', '2021-07-19', 'Vivamus sit amet sapien condimentum, lobortis lacus non, tristique urna.', 'D006', 'P006'),
('7f765c81-109f-48f8-bad0-3e3c9e0bb8f7', '2020-11-11', 'Pellentesque pulvinar tortor eget dui varius, vel fringilla elit ultricies.', 'D013', 'P010'),
('3acac16c-a6cd-4be0-82ad-0fc6d0cc7a61', '2021-08-23', 'Proin gravida ligula ac dapibus mollis.', 'D012', 'P008'),
('ad377732-f2f2-4be8-84b9-537a2f919635', '2019-04-03', 'Nullam vestibulum orci ut pretium porta.', 'D011', 'P005'),
('bd3fb440-2bbd-4aaa-9e7f-3fef7e72916c', '2021-09-26', 'Donec a tellus non sapien dignissim ultricies non vitae nunc.', 'D010', 'P007'),
('72cdad41-1c2c-4f08-b6dc-71dd66b0f5b0', '2020-06-07', 'Mauris quis felis eu nisi sollicitudin sollicitudin non nec eros.', 'D007', 'P001'),
('18d302d8-a079-4752-b4e7-be02a6ec9991', '2021-10-17', 'Donec ac erat eu mi venenatis vulputate eget quis libero.', 'D008', 'P004'),
('8b2ff04d-198d-433a-b36f-b4b1f5f4a195', '2019-03-30', 'Integer bibendum ante a lectus tristique, euismod vulputate lorem gravida.', 'D009', 'P013'),
('b3b56f38-3405-4d53-a286-71dee9cfca6a', '2021-02-16', 'Nulla viverra nisi vitae arcu varius, eu auctor nulla sodales.', 'D004', 'P012'),
('65f20273-feb3-4a22-80d9-90b756dca5ab', '2020-12-01', 'Proin tincidunt dolor a risus ultrices, ac lacinia diam condimentum.', 'D002', 'P011'),
('1857ab96-2629-4290-a9c3-a18d476f1d2b', '2022-05-06', 'Mauris eget ex non elit fermentum bibendum eu sit amet ligula.', 'D001', 'P009'),
('dfd08a78-a082-4c1f-9d3f-3836465a73cd', '2019-01-20', 'Nunc euismod mi ut posuere tristique.', 'D005', 'P006'),
('8684ae68-38eb-46ee-9856-e8617a42d648', '2021-07-29', 'Fusce sed turpis eu leo mollis ultrices.', 'D006', 'P010'),
('a070afff-23f9-4efc-9b7f-91cd09ba08a5', '2020-11-28', 'Maecenas vel risus id purus ultrices vulputate quis et risus.', 'D013', 'P008'),
('8682091d-376f-4229-8f8b-ae03cc2c3c24', '2021-08-15', 'Cras ac arcu a magna tempor tempor eu nec sem.', 'D012', 'P005'),
('66a923b3-a7ca-44ca-8fd9-40f47ffa3b3c', '2019-04-12', 'Integer congue magna non metus ultricies vehicula.', 'D011', 'P007'),
('9a59e70b-a843-4a17-9384-5c118ab9a707', '2021-09-02', 'Nam bibendum risus in metus condimentum, eget hendrerit lectus vulputate.', 'D010', 'P001'),
('73a2c66e-8a32-40fc-96f2-48222e1814b5', '2020-07-01', 'Maecenas fermentum nibh non mi commodo venenatis.', 'D007', 'P004'),
('0b5ae054-c0f2-4cfa-ab56-e360f90519b5', '2021-10-22', 'Donec consequat quam sed erat condimentum, vel blandit nibh auctor.', 'D008', 'P013'),
('71823f86-6bfd-4d62-a83c-9c0b33c355f6', '2019-03-09', 'Etiam ut tellus ac justo iaculis fermentum.', 'D009', 'P012'),
('a2751d5c-8bb4-4b0e-b6d5-d3df7eb7a37d', '2021-02-10', 'Quisque vitae turpis sollicitudin lacus imperdiet rhoncus non non magna.', 'D004', 'P011'),
('8fbed7ca-75d3-4e10-835d-b97a7e604eec', '2020-12-04', 'Praesent rutrum augue eu aliquam dictum.', 'D002', 'P009'),
('7f331dd1-0950-4991-964c-2383ba92699e', '2022-05-27', 'Sed eget sem sit amet nisi rhoncus rutrum.', 'D001', 'P006'),
('5a6dfb97-52c1-4d24-bda4-b08251a9150c', '2019-01-24', 'Donec interdum neque tempor, semper erat sed, mollis justo.', 'D005', 'P010'),
('a3c3b562-8b87-42de-be73-13862451933e', '2021-07-05', 'Integer dapibus augue quis sodales ultrices.', 'D006', 'P008'),
('e9e3b05c-6ff4-4d27-8d1e-ef978163f1e3', '2020-11-13', 'Etiam convallis leo quis viverra aliquam.', 'D013', 'P005'),
('ce8d561d-ffe4-4495-81a3-eb3b95b10474', '2021-08-25', 'Donec lacinia lacus non urna feugiat vehicula.', 'D012', 'P007'),
('c18db363-8069-4b61-9d39-321859316c7d', '2019-04-21', 'Vivamus lacinia urna quis enim ultrices, sed venenatis ipsum laoreet.', 'D011', 'P001'),
('ebada6b3-4191-4b24-86cf-f88426fd61b1', '2021-09-08', 'Quisque ut lectus in libero cursus eleifend.', 'D010', 'P004'),
('ff39ec29-11ee-4ee8-bce7-a97216eff1bd', '2020-06-18', 'Suspendisse vehicula ante sit amet leo gravida, eu vestibulum metus bibendum.', 'D007', 'P013'),
('86c16f7b-9d9d-4e6d-a887-0e497c8bbe8b', '2021-10-14', 'Praesent cursus dolor ut nibh ullamcorper consectetur.', 'D008', 'P012'),
('df94df69-b823-466f-be66-12f048e0ce46', '2019-03-19', 'Aliquam faucibus lectus ut ex suscipit, sit amet ultrices odio lacinia.', 'D009', 'P011'),
('9b647283-e52e-4951-a979-e52cc6b221a7', '2021-02-11', 'Aenean in est eu sapien varius iaculis eu ac lacus.', 'D004', 'P009'),
('69a80238-0d85-4169-b295-c0851bd44f0f', '2020-12-23', 'Pellentesque a eros iaculis, placerat ipsum sed, sodales ex.', 'D002', 'P006'),
('44c1e292-9d00-484c-b9ff-ff2c6d903bbd', '2022-05-03', 'Integer non lorem ut nibh pulvinar mollis.', 'D001', 'P010'),
('bab52f72-82b8-4b36-8da1-ce82d61a83c9', '2019-01-26', 'Pellentesque pulvinar eros a nunc mattis, eu tempus sapien luctus.', 'D005', 'P008'),
('8cd2d329-ad34-41dc-9b62-8cd66083fc33', '2021-07-07', 'Nullam vestibulum turpis aliquet eros sagittis, eu varius augue congue.', 'D006', 'P005'),
('2d8a6ee3-a349-4106-804c-6956e9acf156', '2020-11-17', 'Curabitur ac felis vitae lectus luctus cursus vitae non ante.', 'D013', 'P007'),
('22300513-e865-4f60-bf1e-8606691d24db', '2021-08-30', 'Morbi dictum purus sed tellus condimentum consectetur.', 'D012', 'P001'),
('57ad62b6-4fa1-46e3-93fb-99e43b67d74e', '2019-04-16', 'Aenean egestas leo ac aliquet tincidunt.', 'D011', 'P004'),
('5c9fe905-b471-49c7-81db-64bd2f3cc5c3', '2021-09-01', 'Vestibulum ac dui pretium, venenatis odio at, ullamcorper odio.', 'D010', 'P013'),
('48a32c91-8d2d-40b8-bd49-155f8e9c666b', '2020-06-06', 'Nunc laoreet felis a dignissim consequat.', 'D007', 'P012'),
('241a677a-5b3d-47cc-a8bd-a603cb993035', '2021-10-20', 'Sed sed lectus id urna condimentum aliquam.', 'D008', 'P011'),
('714751b8-dbc4-49d1-b679-15d08a5b5e0f', '2019-03-29', 'Aliquam nec augue vel sem congue tempor.', 'D009', 'P009'),
('3f519dd2-ca6e-4076-924d-983bd4272257', '2021-02-28', 'Etiam efficitur lorem non urna commodo dictum.', 'D004', 'P006'),
('61a30190-1c5f-4870-abe2-5a18c3643cc7', '2020-12-15', 'In eget neque at diam consequat ultricies eget eget elit.', 'D002', 'P010'),
('ef0fe410-fca7-4092-9b25-9e4e75dc6d59', '2022-05-12', 'Vestibulum ut justo non nisl vulputate elementum vitae ut mi.', 'D001', 'P008'),
('1f889473-c2d0-4b36-9e98-900643938cc3', '2019-01-02', 'Nam vitae sem vitae mi ornare pretium.', 'D005', 'P005'),
('b3bb91c8-c5ea-4722-8f76-b4869553dd3a', '2021-07-31', 'Sed eu purus eget risus lobortis vestibulum.', 'D006', 'P007'),
('312533c5-0344-43a2-8b26-ef9af85219ef', '2020-11-22', 'Integer porttitor leo a hendrerit cursus.', 'D013', 'P001'),
('2949a262-7e7e-40d1-8896-a46b7001a709', '2021-08-09', 'Donec vel sapien eu augue suscipit mollis.', 'D012', 'P004'),
('cb92a46b-5862-439c-9916-3d1c8fd1dd39', '2019-04-10', 'Phasellus sodales augue eget lectus porta feugiat.', 'D011', 'P013'),
('d2afe536-df79-41f4-b9be-06cf4d6de7d8', '2021-09-04', 'Morbi ut risus commodo, blandit risus eget, pharetra nibh.', 'D010', 'P012'),
('4be3a09d-d5c3-4fd2-9789-070046e8cc42', '2020-06-27', 'Maecenas vitae ex eu dui aliquam faucibus nec id velit.', 'D007', 'P011'),
('0231fb3b-795b-4955-a55a-2f490e4df6ea', '2021-10-24', 'Etiam bibendum ante in mi dictum ornare.', 'D008', 'P009'),
('1ccb8280-7013-4a5e-a9ac-a90108f039c8', '2019-03-05', 'Vivamus quis felis vestibulum, auctor risus id, feugiat erat.', 'D009', 'P006'),
('27e4287e-ae8b-4f70-aaeb-39b71a735a9f', '2021-02-13', 'Nullam consectetur ipsum eu diam suscipit gravida.', 'D004', 'P010'),
('71f16ef3-1b17-431f-b7a0-f1c89c10bca1', '2020-12-25', 'Cras in erat et orci finibus accumsan non ut ante.', 'D002', 'P008'),
('25f57a12-7035-407d-9fbc-f0b0beecd311', '2022-05-21', 'Ut in augue vitae sapien aliquam feugiat nec cursus quam.', 'D001', 'P005'),
('631460b1-4327-4358-bbaa-bde06d0b494e', '2019-01-08', 'Curabitur hendrerit ex non imperdiet tincidunt.', 'D005', 'P007'),
('39d9d0f4-d2b0-442c-8326-d6663a717aaf', '2021-07-18', 'Cras in erat nec leo ullamcorper suscipit.', 'D006', 'P001'),
('3933c480-aed5-4d83-8ef1-8646affe3bf6', '2020-11-14', 'Nullam non leo vitae nulla sodales lobortis.', 'D013', 'P004'),
('29574db3-44c6-4dc2-9789-15b7d03da27b', '2021-08-19', 'Nam ornare eros sed odio maximus, eu consequat tortor sodales.', 'D012', 'P013'),
('f3246078-7756-429c-a49e-2cc885cb1605', '2019-04-11', 'Aliquam vitae augue vitae dolor maximus vulputate id ac nibh.', 'D011', 'P012'),
('dd76168d-ceed-4486-b4bb-43bcb4bd05ce', '2021-09-23', 'Vestibulum vitae libero sit amet nibh molestie tincidunt.', 'D010', 'P011'),
('54aa4a61-5da9-4a02-8e53-003049d26db7', '2020-06-03', 'Morbi venenatis tellus ac odio viverra cursus.', 'D007', 'P009'),
('e62bca05-27ef-4516-a0a5-36c2f25464c9', '2021-10-26', 'Vestibulum aliquam magna vel massa laoreet ornare facilisis ut ex.', 'D008', 'P006'),
('137b673f-9ee8-4789-9f49-9e55f094c15a', '2019-03-07', 'Sed euismod ligula at tortor molestie porttitor.', 'D009', 'P010'),
('681b3a1c-38c2-408e-8ea9-be6a9345f070', '2021-02-17', 'Quisque non nibh vitae massa feugiat porttitor.', 'D004', 'P008'),
('e61db19c-0d6f-420d-9d33-1ee6edeb426c', '2020-12-30', 'Sed vitae est a mi cursus suscipit ut at mi.', 'D002', 'P005');


DROP TABLE IF EXISTS `t_act`;

CREATE TABLE `t_act` (
  `id` varchar(255) NOT NULL,
  `medical_act_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_medical_act_id` (`medical_act_id`),
  CONSTRAINT `FK_patient_file_item_act` FOREIGN KEY (`id`) REFERENCES `t_patient_file_item` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_medical_act_act` FOREIGN KEY (`medical_act_id`) REFERENCES `t_medical_act` (`id`)
) ENGINE=InnoDB;

INSERT INTO `t_act` VALUES
('1b57e70f-8eb0-4a97-99c6-5d44f138c22c', 'HAFA007'),
('f6de43eb-d86b-49bd-8a57-bb005fc5b062', 'ENNF001'),
('b087dee3-b0bc-4b3e-9e34-56f14a2c9511', 'HBGD065'),
('9babb2b2-21b5-4490-95da-4a33db04eff4', 'JDPA002'),
('d4383a18-231e-4f8a-9bca-17de42d98968', 'JQQM007'),
('849031b7-eb23-49e8-995a-8c5077e86e02', 'JJJA002'),
('150d4d20-b686-41c4-8b41-04d876462dcb', 'HEFA018'),
('72cdad41-1c2c-4f08-b6dc-71dd66b0f5b0', 'HNFA006'),
('1857ab96-2629-4290-a9c3-a18d476f1d2b', 'QAMA005'),
('66a923b3-a7ca-44ca-8fd9-40f47ffa3b3c', 'HPLA005'),
('a2751d5c-8bb4-4b0e-b6d5-d3df7eb7a37d', 'BKHA002'),
('e9e3b05c-6ff4-4d27-8d1e-ef978163f1e3', 'DHFA004'),
('86c16f7b-9d9d-4e6d-a887-0e497c8bbe8b', 'BEEA002'),
('bab52f72-82b8-4b36-8da1-ce82d61a83c9', 'GDNE002'),
('5c9fe905-b471-49c7-81db-64bd2f3cc5c3', 'EBSA002'),
('61a30190-1c5f-4870-abe2-5a18c3643cc7', 'HBLD015'),
('2949a262-7e7e-40d1-8896-a46b7001a709', 'JAQX004'),
('1ccb8280-7013-4a5e-a9ac-a90108f039c8', 'ZZQX172'),
('39d9d0f4-d2b0-442c-8326-d6663a717aaf', 'BGQP006'),
('54aa4a61-5da9-4a02-8e53-003049d26db7', 'AFFA005');


DROP TABLE IF EXISTS `t_diagnosis`;

CREATE TABLE `t_diagnosis` (
  `id` varchar(255) NOT NULL,
  `disease_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_disease_id` (`disease_id`),
  CONSTRAINT `FK_patient_file_item_diagnosis` FOREIGN KEY (`id`) REFERENCES `t_patient_file_item` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_disease_diagnosis` FOREIGN KEY (`disease_id`) REFERENCES `t_disease` (`id`)
) ENGINE=InnoDB;

INSERT INTO `t_diagnosis` VALUES
('707b71f1-0bbd-46ec-b79c-c9717bd6b2cd', 'Z921'),
('cf708537-91cc-4e03-8979-c7988234d375', 'F41'),
('c793da7f-5ca8-41f5-a0f0-1cc77b34b6fe', 'X1938'),
('b7bdb6e4-da4b-47f9-9b67-cfd67567aaca', 'Y085'),
('4b2d490e-2024-4a63-a5f9-97dbcdc8d823', 'X758'),
('788c2cb1-400d-4125-9924-bb105efbced7', 'W9998'),
('7f765c81-109f-48f8-bad0-3e3c9e0bb8f7', 'V6342'),
('18d302d8-a079-4752-b4e7-be02a6ec9991', 'V7242'),
('dfd08a78-a082-4c1f-9d3f-3836465a73cd', 'Q221'),
('9a59e70b-a843-4a17-9384-5c118ab9a707', 'M231'),
('8fbed7ca-75d3-4e10-835d-b97a7e604eec', 'X47869'),
('ce8d561d-ffe4-4495-81a3-eb3b95b10474', 'N03'),
('df94df69-b823-466f-be66-12f048e0ce46', 'V3550'),
('8cd2d329-ad34-41dc-9b62-8cd66083fc33', 'W0973'),
('48a32c91-8d2d-40b8-bd49-155f8e9c666b', 'Y3048'),
('ef0fe410-fca7-4092-9b25-9e4e75dc6d59', 'X67232'),
('cb92a46b-5862-439c-9916-3d1c8fd1dd39', 'G114'),
('27e4287e-ae8b-4f70-aaeb-39b71a735a9f', 'X7989'),
('3933c480-aed5-4d83-8ef1-8646affe3bf6', 'V731'),
('e62bca05-27ef-4516-a0a5-36c2f25464c9', 'W26031');


DROP TABLE IF EXISTS `t_mail`;

CREATE TABLE `t_mail` (
  `id` varchar(255) NOT NULL,
  `text` varchar(1000) DEFAULT NULL,
  `recipient_doctor_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_recipient_doctor_id` (`recipient_doctor_id`),
  CONSTRAINT `FK_patient_file_item_mail` FOREIGN KEY (`id`) REFERENCES `t_patient_file_item` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_doctor_mail` FOREIGN KEY (`recipient_doctor_id`) REFERENCES `t_doctor` (`id`)
) ENGINE=InnoDB;

INSERT INTO `t_mail` VALUES
('3ab3d311-585c-498e-aaca-728c00beb86e', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', 'D006'),
('736bab44-5c6d-4beb-9e1e-5be4f3b082a3', 'Sed quis risus a turpis blandit efficitur.', 'D013'),
('aab462f4-04ab-47d9-8ab5-3229404e8c13', 'Vivamus eget dui nec ligula imperdiet tempus.', 'D012'),
('bb2bb081-fcd1-41eb-a054-91d6ae433a2c', 'Sed ut nunc commodo, tristique nisl eu, laoreet nulla.', 'D011'),
('83b2f235-f183-4144-8eb8-2e4cac07d434', 'Nullam tempor felis eget varius efficitur.', 'D010'),
('42b68587-6756-4d4c-9429-eda577d6ffee', 'Mauris ultricies tellus at quam finibus imperdiet.', 'D007'),
('3acac16c-a6cd-4be0-82ad-0fc6d0cc7a61', 'Cras sed nunc ac ex lacinia auctor eget viverra ipsum.', 'D008'),
('8b2ff04d-198d-433a-b36f-b4b1f5f4a195', 'Ut id tortor id magna commodo auctor id ac mi.', 'D009'),
('8684ae68-38eb-46ee-9856-e8617a42d648', 'Nulla et neque at est consectetur scelerisque.', 'D004'),
('73a2c66e-8a32-40fc-96f2-48222e1814b5', 'Nullam accumsan nulla ac sem fringilla, ac ullamcorper lectus tristique.', 'D002'),
('7f331dd1-0950-4991-964c-2383ba92699e', 'Vestibulum ut lacus a dui ornare faucibus.', 'D002'),
('c18db363-8069-4b61-9d39-321859316c7d', 'Cras dapibus purus ut aliquet sodales.', 'D005'),
('9b647283-e52e-4951-a979-e52cc6b221a7', 'Nam tincidunt magna vestibulum ex finibus tempor et vitae ante.', 'D006'),
('2d8a6ee3-a349-4106-804c-6956e9acf156', 'Cras sit amet ex vestibulum, aliquet urna id, bibendum lacus.', 'D013'),
('241a677a-5b3d-47cc-a8bd-a603cb993035', 'Aenean aliquet nunc nec tellus aliquet porttitor.', 'D012'),
('1f889473-c2d0-4b36-9e98-900643938cc3', 'Sed quis orci aliquet, lacinia erat non, pulvinar ante.', 'D011'),
('d2afe536-df79-41f4-b9be-06cf4d6de7d8', 'Vivamus eu justo vitae ante sagittis imperdiet aliquet at lacus.', 'D010'),
('71f16ef3-1b17-431f-b7a0-f1c89c10bca1', 'Etiam sit amet tortor varius, ultrices sapien at, sagittis leo.', 'D007'),
('29574db3-44c6-4dc2-9789-15b7d03da27b', 'Morbi vestibulum lorem sit amet eleifend imperdiet.', 'D008'),
('137b673f-9ee8-4789-9f49-9e55f094c15a', 'Nam eu diam ac mi consequat eleifend.', 'D009');


DROP TABLE IF EXISTS `t_prescription`;

CREATE TABLE `t_prescription` (
  `id` varchar(255) NOT NULL,
  `description` varchar(800) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_patient_file_item_prescription` FOREIGN KEY (`id`) REFERENCES `t_patient_file_item` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

INSERT INTO `t_prescription` VALUES
('31571533-a9d4-4b10-ac46-8afe0247e6cd', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.'),
('643bce5b-9c74-4e6f-8ae2-5809a31bedb4', 'Donec vitae neque eget lacus bibendum suscipit.'),
('eaf55efd-c8bf-4f70-a6a4-60ab2b1d7824', 'Integer dignissim urna et odio fermentum, vitae sollicitudin enim sodales.'),
('b0a5e8d0-4ba7-491e-b109-3e0620a3b0c4', 'Nullam eu nulla pulvinar, accumsan tortor id, convallis ante.'),
('c4846634-e4a0-41eb-b9b0-778cfbf31642', 'Integer sagittis velit vitae ornare tincidunt.'),
('62f0ab70-f9ef-4bde-a9c3-a63aa15f4631', 'Maecenas ultrices eros sed scelerisque ullamcorper.'),
('ad377732-f2f2-4be8-84b9-537a2f919635', 'Ut et dui at nibh sagittis luctus.'),
('b3b56f38-3405-4d53-a286-71dee9cfca6a', 'Fusce vel ipsum eget mi malesuada pretium.'),
('a070afff-23f9-4efc-9b7f-91cd09ba08a5', 'Pellentesque venenatis massa id dolor gravida, nec vehicula nisi commodo.'),
('0b5ae054-c0f2-4cfa-ab56-e360f90519b5', 'Integer iaculis lorem id nisi mollis ultricies.'),
('5a6dfb97-52c1-4d24-bda4-b08251a9150c', 'Nunc sollicitudin nisl eget purus accumsan finibus.'),
('ebada6b3-4191-4b24-86cf-f88426fd61b1', 'Phasellus in tellus tincidunt lorem sollicitudin tincidunt ut vehicula augue.'),
('69a80238-0d85-4169-b295-c0851bd44f0f', 'Mauris luctus velit imperdiet tortor faucibus lobortis.'),
('22300513-e865-4f60-bf1e-8606691d24db', 'Suspendisse a ex sit amet massa ultricies eleifend in eget orci.'),
('714751b8-dbc4-49d1-b679-15d08a5b5e0f', 'Donec scelerisque sem a justo volutpat pharetra.'),
('b3bb91c8-c5ea-4722-8f76-b4869553dd3a', 'Maecenas suscipit nulla sit amet est efficitur placerat.'),
('4be3a09d-d5c3-4fd2-9789-070046e8cc42', 'Praesent semper magna eget dui vestibulum sagittis ut sit amet quam.'),
('25f57a12-7035-407d-9fbc-f0b0beecd311', 'Proin in massa convallis, tristique risus vitae, vulputate eros.'),
('f3246078-7756-429c-a49e-2cc885cb1605', 'Mauris tempus mauris lobortis, maximus enim et, mattis magna.'),
('681b3a1c-38c2-408e-8ea9-be6a9345f070', 'Quisque non dolor rhoncus, faucibus augue sed, elementum erat.');


DROP TABLE IF EXISTS `t_symptom`;

CREATE TABLE `t_symptom` (
  `id` varchar(255) NOT NULL,
  `description` varchar(800) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_patient_file_item_symptom` FOREIGN KEY (`id`) REFERENCES `t_patient_file_item` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

INSERT INTO `t_symptom` VALUES
('142763cf-6eeb-47a5-b8f8-8ec85f0025c4', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.'),
('8dddad2f-b9e0-48fb-be56-298c9c512015', 'Curabitur ut ante rutrum, tincidunt magna nec, gravida sapien.'),
('a46b49de-de29-4912-a13e-62c0cf995317', 'Suspendisse at nibh lacinia, suscipit nisl eget, dapibus augue.'),
('4a3e7cc1-2298-4ff0-9d7a-1d0f4377e24a', 'Fusce tristique libero eget nibh commodo, ac convallis nunc ullamcorper.'),
('bde02c78-723b-416e-ad87-3a917a11cf5f', 'Donec mattis felis vel arcu faucibus hendrerit sed non enim.'),
('5b0ce20b-4ecf-4b23-ae8b-24fb085767ff', 'Nunc sed mi eget libero cursus maximus molestie in nibh.'),
('bd3fb440-2bbd-4aaa-9e7f-3fef7e72916c', 'Ut imperdiet metus sit amet commodo luctus.'),
('65f20273-feb3-4a22-80d9-90b756dca5ab', 'Ut molestie elit sit amet ante maximus, ac tincidunt nisi mattis.'),
('8682091d-376f-4229-8f8b-ae03cc2c3c24', 'Fusce condimentum neque ac odio vulputate, eget vehicula ex ultricies.'),
('71823f86-6bfd-4d62-a83c-9c0b33c355f6', 'Phasellus a lorem porta, vulputate tortor vel, interdum velit.'),
('a3c3b562-8b87-42de-be73-13862451933e', 'In volutpat turpis in quam lobortis, nec condimentum leo mollis.'),
('ff39ec29-11ee-4ee8-bce7-a97216eff1bd', 'Sed malesuada nulla quis neque pulvinar consequat.'),
('44c1e292-9d00-484c-b9ff-ff2c6d903bbd', 'Curabitur dignissim metus ac felis sagittis, eu consequat erat luctus.'),
('57ad62b6-4fa1-46e3-93fb-99e43b67d74e', 'Proin dictum turpis lobortis enim lacinia, et rhoncus enim posuere.'),
('3f519dd2-ca6e-4076-924d-983bd4272257', 'Duis varius ipsum id nisl interdum, nec luctus leo imperdiet.'),
('312533c5-0344-43a2-8b26-ef9af85219ef', 'Ut sodales risus vitae egestas semper.'),
('0231fb3b-795b-4955-a55a-2f490e4df6ea', 'Nulla lacinia enim id nisi sodales, consequat pharetra ipsum pulvinar.'),
('631460b1-4327-4358-bbaa-bde06d0b494e', 'Ut eu ex ut diam ultricies bibendum sed in erat.'),
('dd76168d-ceed-4486-b4bb-43bcb4bd05ce', 'In at diam in arcu auctor interdum.'),
('e61db19c-0d6f-420d-9d33-1ee6edeb426c', 'Pellentesque aliquet ex vel velit convallis, sit amet dictum arcu fringilla.');
