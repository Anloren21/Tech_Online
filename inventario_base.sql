-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema inventario_HW
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema inventario_HW
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `inventario_HW` DEFAULT CHARACTER SET utf8 ;
USE `inventario_HW` ;

-- -----------------------------------------------------
-- Table `inventario_HW`.`productos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `inventario_HW`.`productos` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(50) NOT NULL,
  `precio` DECIMAL(40,2) NOT NULL,
  `stock` INT NOT NULL,
  `descripcion` VARCHAR(50) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `inventario_HW`.`ventas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `inventario_HW`.`ventas` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `cantidad` INT NOT NULL,
  `fecha` DATETIME NOT NULL,
  `productos_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_ventas_productos_idx` (`productos_id` ASC) VISIBLE,
  CONSTRAINT `fk_ventas_productos`
    FOREIGN KEY (`productos_id`)
    REFERENCES `inventario_HW`.`productos` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
