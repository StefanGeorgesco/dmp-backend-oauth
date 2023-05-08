DROP DATABASE IF EXISTS `dmp-oauth`;
DROP USER IF EXISTS `dmp-oauth-admin`@`%`;
DROP USER IF EXISTS `dmp-oauth-user`@`%`;
CREATE DATABASE IF NOT EXISTS `dmp-oauth` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS `dmp-oauth-admin`@`%` IDENTIFIED WITH mysql_native_password BY 'password';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, REFERENCES, INDEX, ALTER, EXECUTE, CREATE VIEW, SHOW VIEW,
    CREATE ROUTINE, ALTER ROUTINE, EVENT, TRIGGER ON `dmp-oauth`.* TO `dmp-oauth-admin`@`%`;
CREATE USER IF NOT EXISTS `dmp-oauth-user`@`%` IDENTIFIED WITH mysql_native_password BY 'password';
GRANT SELECT, INSERT, UPDATE, DELETE, SHOW VIEW ON `dmp-oauth`.* TO `dmp-oauth-user`@`%`;
FLUSH PRIVILEGES;