-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema fpchess
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema fpchess
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `fpchess` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `fpchess` ;

-- -----------------------------------------------------
-- Table `fpchess`.`Piece`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fpchess`.`Piece` ;

CREATE TABLE IF NOT EXISTS `fpchess`.`Piece` (
  `X` INT NOT NULL,
  `Y` INT NOT NULL,
  `Type` INT NOT NULL,
  `PlayerNum` INT NOT NULL,
  PRIMARY KEY (`X`, `PlayerNum`, `Type`, `Y`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
