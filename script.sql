/*
==============================================================
 SCRIPT DE CREACIÓN DE BASE DE DATOS Y USUARIO
 PROYECTO: Aplicación de Hospital (Java)
 Base de datos: db_integrador
 Usuario: usuario_hospital
==============================================================
*/

-- ---
-- 0. PREPARACIÓN
-- ---
-- Borra la base de datos si ya existe, para empezar de cero
DROP DATABASE IF EXISTS db_integrador;

-- Borra el usuario si ya existe
DROP USER IF EXISTS 'usuario_hospital'@'localhost';

-- ---
-- 1. CREACIÓN DE LA BASE DE DATOS
-- ---
CREATE DATABASE db_integrador
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Seleccionar la base de datos para trabajar en ella
USE db_integrador;

-- ---
-- 2. CREACIÓN DE LA TABLAS
-- ---

-- Primero debe existir historiaClinica ya que pacientes la va a referenciar.
CREATE TABLE db_integrador.historiaclinica (
 id BIGINT NOT NULL AUTO_INCREMENT,
 eliminado boolean DEFAULT FALSE,
 nroHistoria VARCHAR(20) NOT NULL UNIQUE,
 grupoSanguineo VARCHAR(3) NOT NULL,
 antecedentes VARCHAR(100) NULL,
 medicacionActual VARCHAR(200) NULL,
 observaciones VARCHAR(200) NULL,
 PRIMARY KEY (id),
 CHECK (grupoSanguineo IN ('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'))
);

CREATE TABLE db_integrador.paciente (
 id BIGINT NOT NULL AUTO_INCREMENT,
 eliminado boolean DEFAULT FALSE,
 nombre VARCHAR(80) NOT NULL,
 apellido VARCHAR(80) NOT NULL,
 dni VARCHAR(15) NOT NULL UNIQUE,
 fechaNacimiento DATE NOT NULL,
 -- CAMPO AÑADIDO PARA LA RELACIÓN 1:1 --
 historia_clinica_id BIGINT NULL UNIQUE,

 PRIMARY KEY (id),

 -- RESTRICCIÓN DE CLAVE FORÁNEA (LA ASOCIACIÓN) --
 CONSTRAINT fk_paciente_hclinica
   FOREIGN KEY (historia_clinica_id)
   REFERENCES db_integrador.historiaclinica(id)
   ON DELETE CASCADE
);

-- ---
-- 3. CREACIÓN DE TRIGGERS
-- ---

-- Cambiamos el delimitador estándar (;) para poder escribir el
-- cuerpo del trigger, que usa (;) internamente.

DROP TRIGGER IF EXISTS trg_paciente_check_insert;
DROP TRIGGER IF EXISTS trg_paciente_check_update;

DELIMITER $$

-- Trigger para INSERCIONES (valida fecha Y campos vacíos)
CREATE TRIGGER trg_paciente_check_insert
BEFORE INSERT ON paciente
FOR EACH ROW
BEGIN
  -- 1. Validación de fecha de nacimiento
  IF NEW.fechaNacimiento > CURDATE() THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Error: La fecha de nacimiento no puede ser una fecha futura.';

  -- 2. Validación de strings vacíos
  -- Usamos TRIM() para que ' ' (un espacio) también cuente como vacío
  ELSEIF TRIM(NEW.nombre) = '' OR TRIM(NEW.apellido) = '' OR TRIM(NEW.dni) = '' THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Error: El nombre, apellido y DNI no pueden estar vacíos.';
  END IF;
END$$

-- Trigger para ACTUALIZACIONES (valida fecha Y campos vacíos)
CREATE TRIGGER trg_paciente_check_update
BEFORE UPDATE ON paciente
FOR EACH ROW
BEGIN
  -- 1. Validación de fecha de nacimiento
  IF NEW.fechaNacimiento > CURDATE() THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Error: La fecha de nacimiento no puede ser una fecha futura.';

  -- 2. Validación de strings vacíos
  ELSEIF TRIM(NEW.nombre) = '' OR TRIM(NEW.apellido) = '' OR TRIM(NEW.dni) = '' THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Error: El nombre, apellido y DNI no pueden estar vacíos.';
  END IF;
END$$

-- Volvemos a poner el delimitador estándar
DELIMITER ;

-- ---
-- 4. CREACIÓN DEL USUARIO Y PERMISOS
-- ---

-- Crear el usuario que solo se conectará localmente
CREATE USER 'usuario_hospital'@'localhost'
IDENTIFIED BY 'abc123';

-- Dar permisos de CRUD (Crear, Leer, Actualizar, Borrar)
-- ÚNICAMENTE sobre todas las tablas (.*) de 'db_integrador'
GRANT SELECT, INSERT, UPDATE, DELETE
ON db_integrador.* TO 'usuario_hospital'@'localhost';

-- Aplicar los cambios de privilegios
FLUSH PRIVILEGES;


-- Seleccionar la base de datos para trabajar en ella
USE db_integrador;

-- ---
-- 5. INSERCIÓN DE DATOS DE PRUEBA
-- ---

-- Paciente 1: Juan Perez
-- Primero creamos su historia clínica
INSERT INTO db_integrador.historiaclinica
  (nroHistoria, grupoSanguineo, antecedentes, medicacionActual)
VALUES
  ('HC1001', 'O+', 'Alergia al polen', 'Loratadina 10mg');

-- Ahora creamos al paciente, vinculándolo con la historia clínica
-- recién creada usando LAST_INSERT_ID()
INSERT INTO db_integrador.paciente
  (nombre, apellido, dni, fechaNacimiento, historia_clinica_id)
VALUES
  ('Juan', 'Perez', '30111222', '1985-04-12', LAST_INSERT_ID());


-- Paciente 2: Maria Garcia
INSERT INTO db_integrador.historiaclinica
  (nroHistoria, grupoSanguineo, observaciones)
VALUES
  ('HC1002', 'A-', 'Cirugía de apéndice en 2010');

INSERT INTO db_integrador.paciente
  (nombre, apellido, dni, fechaNacimiento, historia_clinica_id)
VALUES
  ('Maria', 'Garcia', '32333444', '1990-11-30', LAST_INSERT_ID());

-- Paciente 3: Carlos Lopez (Ejemplo de campos nulos)
INSERT INTO db_integrador.historiaclinica
  (nroHistoria, grupoSanguineo)
VALUES
  ('HC1003', 'B+');

INSERT INTO db_integrador.paciente
  (nombre, apellido, dni, fechaNacimiento, historia_clinica_id)
VALUES
  ('Carlos', 'Lopez', '28555666', '1980-01-20', LAST_INSERT_ID());