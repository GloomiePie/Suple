
DROP DATABASE IF EXISTS `ProyectoIntegrador` ;

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE DATABASE IF NOT EXISTS `ProyectoIntegrador` DEFAULT CHARACTER SET utf8 ;
USE `ProyectoIntegrador` ;

-- -----------------------------------------------------
-- Table `Movie`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Movie`;
CREATE TABLE IF NOT EXISTS `Movie` (
  `id` INT AUTO_INCREMENT NOT NULL,
  `budget` BIGINT NULL,
  `homepage` VARCHAR(250) NULL,
  `id_original` INT NOT NULL,
  `original_language` VARCHAR(250) NULL,
  `original_title` VARCHAR(205) NULL,
  `overview` LONGTEXT NULL,
  `popularity` DOUBLE NULL,
  `release_date` VARCHAR(250) NULL,
  `revenue` BIGINT NULL,
  `runtime` DOUBLE NULL,
  `status` VARCHAR(250) NULL,
  `tagline` LONGTEXT NULL,
  `title` VARCHAR(250) NULL,
  `vote_average` DOUBLE NULL,
  `vote_count` INT NULL,
  PRIMARY KEY (`id`));
  
  ALTER TABLE movie ADD INDEX `id`(`id_original`);

-- Eliminado director
-- -----------------------------------------------------
-- Table `Genres`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Genres`; 
CREATE TABLE IF NOT EXISTS `Genres` (
  `idMovie` INT NOT NULL,
  `name` VARCHAR(250) NULL,
  FOREIGN KEY (`idMovie`) REFERENCES `Movie`(`id`));
-- -----------------------------------------------------
-- Table `Cast`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Cast`;
CREATE TABLE IF NOT EXISTS `Cast` (
  `id` INT  AUTO_INCREMENT,
  `PersonId` INT,
  `idMovie` INT,
  FOREIGN KEY (`idMovie`) REFERENCES `Movie`(`id`),
  FOREIGN KEY (`PersonId`) REFERENCES `Person`(`id`),
  PRIMARY KEY (`id`)
  );
-- -----------------------------------------------------
-- Table `Director`
-- ----------------------------------------------------

-- -----------------------------------------------------
-- Table `Person`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Person`;
CREATE TABLE IF NOT EXISTS `Person` (
  `id` INT AUTO_INCREMENT NOT NULL,
  `Name` VARCHAR(1024) NOT NULL,
  `Role` VARCHAR(32) NOT NULL,
  PRIMARY KEY(`id`)
  );

-- -----------------------------------------------------
-- Table `KeyWords`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `KeyWords` (
  `idMovie` INT NOT NULL,
  `Name` VARCHAR(250) NULL,
  FOREIGN KEY (`idMovie`) REFERENCES `Movie`(`id`));

-- -----------------------------------------------------
-- Table `production_companies`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `production_companies`;
CREATE TABLE IF NOT EXISTS `production_companies` (
  `idMovie` INT NOT NULL,
  `idCompany` INT NOT NULL,
  FOREIGN KEY (`idMovie`) REFERENCES `Movie`(`id`),
  FOREIGN KEY (`idCompany`) REFERENCES `Companie`(`id`),
  PRIMARY KEY (`idMovie`, `idCompany`)
   );
   
-- -----------------------------------------------------
-- Table `Companie`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Companie`;
CREATE TABLE IF NOT EXISTS `Companie` (
  `id` INT AUTO_INCREMENT NOT NULL,
  `Name` VARCHAR(512),
  `OriginalId` INT,
   PRIMARY KEY (`id`)
   );

-- -----------------------------------------------------
-- Table `spoken_language`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spoken_language`;
CREATE TABLE IF NOT EXISTS `spoken_language` (
  `idMovie` INT NOT NULL,
  `LanguageId` INT NOT NULL,
  FOREIGN KEY (`idMovie`) REFERENCES `Movie`(`id`),
  FOREIGN KEY (`LanguageId`) REFERENCES `Language`(`id`),
  PRIMARY KEY (`LanguageId`, `idMovie`)
  );

-- -----------------------------------------------------
-- Table `Language`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Language`;
CREATE TABLE IF NOT EXISTS `Language` (
  `id` INT AUTO_INCREMENT NOT NULL,
  `iso_639_1` VARCHAR(250) NULL,
  `name` VARCHAR(250) NULL,
  PRIMARY KEY(`id`)
  );
-- -----------------------------------------------------
-- Table `production_countries`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `production_countries` (
  `idMovie` INT NOT NULL,
  `name` VARCHAR(250) NULL,
  `iso_3166_1` VARCHAR(250) NULL,
  FOREIGN KEY (`idMovie`) REFERENCES `Movie`(`id`));

-- -----------------------------------------------------
-- Table `Crew`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Crew` (
  `idMovie` INT NOT NULL,
  `id` INT NULL,
  `name` VARCHAR(250) NULL,
  `gender` VARCHAR(45) NULL,
  `departament` VARCHAR(250) NULL,
  `job` VARCHAR(250) NULL,
  `credit_id` VARCHAR(250) NULL,
  FOREIGN KEY (`idMovie`) REFERENCES `Movie`(`id`));