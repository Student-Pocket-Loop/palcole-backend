-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema palcole
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `palcole` ;

-- -----------------------------------------------------
-- Schema palcole
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `palcole` DEFAULT CHARACTER SET utf8 ;
SHOW WARNINGS;
USE `palcole` ;

-- -----------------------------------------------------
-- Table `palcole`.`roles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `palcole`.`roles` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `palcole`.`roles` (
  `rol_id` INT NOT NULL AUTO_INCREMENT,
  `rol_name` VARCHAR(5) NOT NULL,
  PRIMARY KEY (`rol_id`))
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE UNIQUE INDEX `rol_name_UNIQUE` ON `palcole`.`roles` (`rol_name` ASC) VISIBLE;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `palcole`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `palcole`.`users` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `palcole`.`users` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(20) NOT NULL,
  `email` VARCHAR(90) NOT NULL,
  `password` CHAR(60) NOT NULL,
  `create_date` DATETIME NOT NULL,
  `account_expiration_date` DATETIME,
  `is_locked` TINYINT(1),
  `credentials_expiration_date` DATETIME,
  `is_enabled` TINYINT(1),
  `roles_rol_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `roles_rol_id`),
  CONSTRAINT `fk_users_roles`
    FOREIGN KEY (`roles_rol_id`)
    REFERENCES `palcole`.`roles` (`rol_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE UNIQUE INDEX `username_UNIQUE` ON `palcole`.`users` (`username` ASC) VISIBLE;

SHOW WARNINGS;
CREATE UNIQUE INDEX `email_UNIQUE` ON `palcole`.`users` (`email` ASC) VISIBLE;

SHOW WARNINGS;
CREATE INDEX `fk_users_roles_idx` ON `palcole`.`users` (`roles_rol_id` ASC) VISIBLE;

SHOW WARNINGS;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
