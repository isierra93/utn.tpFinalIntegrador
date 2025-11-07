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
 nroHistoria VARCHAR(60) NOT NULL,
 grupoSanguineo VARCHAR(3) NOT NULL,
 antecedentes VARCHAR(100) NULL,
 medicacionActual VARCHAR(200) NULL,
 observaciones VARCHAR(200) NULL,
 PRIMARY KEY (id),
 UNIQUE (nroHistoria),
 CHECK (grupoSanguineo IN ('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'))
);

CREATE TABLE db_integrador.paciente (
 id BIGINT NOT NULL AUTO_INCREMENT,
 eliminado boolean DEFAULT FALSE,
 nombre VARCHAR(60) NOT NULL,
 apellido VARCHAR(60) NOT NULL,
 dni VARCHAR(60) NOT NULL UNIQUE,
 fechaNacimiento DATE NOT NULL,
 -- CAMPO AÑADIDO PARA LA RELACIÓN 1:1 --
 historia_clinica_id BIGINT NULL UNIQUE,

 PRIMARY KEY (id),

 -- RESTRICCIÓN DE CLAVE FORÁNEA (LA ASOCIACIÓN) --
 CONSTRAINT fk_paciente_hclinica
   FOREIGN KEY (historia_clinica_id)
   REFERENCES db_integrador.historiaclinica(id)
);

-- ---
-- 3. CREACIÓN DEL USUARIO Y PERMISOS
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


-- (Opcional) Insertar un par de ejemplos

-- ---
-- HISTORIAS CLINICAS
-- ---

-- Historia Clínica 1

INSERT INTO db_integrador.historiaclinica
(nroHistoria, grupoSanguineo, antecedentes, medicacionActual, observaciones)
VALUES
('HC-1001', 'A+', 'Alergia al polen', 'Loratadina 10mg', 'Control estacional requerido');

-- Historia Clínica 2
INSERT INTO db_integrador.historiaclinica
(nroHistoria, grupoSanguineo, antecedentes, medicacionActual, observaciones)
VALUES
('HC-1002', 'O-', 'Hipertensión', 'Enalapril 20mg', 'Dieta baja en sodio');

-- Historia Clínica 3
INSERT INTO db_integrador.historiaclinica
(nroHistoria, grupoSanguineo, antecedentes, medicacionActual, observaciones)
VALUES
('HC-1003', 'AB+', 'Asma leve', 'Salbutamol SOS', NULL);

-- Historia Clínica 4
INSERT INTO db_integrador.historiaclinica
(nroHistoria, grupoSanguineo, antecedentes, medicacionActual, observaciones)
VALUES
('HC-1004', 'B-', 'Cirugía de apéndice (2015)', NULL, 'Paciente saludable');

-- Historia Clínica 5
INSERT INTO db_integrador.historiaclinica
(nroHistoria, grupoSanguineo, antecedentes, medicacionActual, observaciones)
VALUES
('HC-1005', 'O+', NULL, 'Complejo vitamínico', 'Chequeo general anual');

-- ---
-- PACIENTES
-- ---

-- Paciente 1 (vinculado a HC-1001, ID: 1)
INSERT INTO db_integrador.paciente
(nombre, apellido, dni, fechaNacimiento, historia_clinica_id)
VALUES
('Carlos', 'Sánchez', '30123456', '1980-05-15', 1);

-- Paciente 2 (vinculado a HC-1002, ID: 2)
INSERT INTO db_integrador.paciente
(nombre, apellido, dni, fechaNacimiento, historia_clinica_id)
VALUES
('Ana', 'Gómez', '32987654', '1985-11-20', 2);

-- Paciente 3 (vinculado a HC-1003, ID: 3)
INSERT INTO db_integrador.paciente
(nombre, apellido, dni, fechaNacimiento, historia_clinica_id)
VALUES
('Juan', 'Pérez', '40111222', '1998-02-10', 3);

-- Paciente 4 (vinculado a HC-1004, ID: 4)
INSERT INTO db_integrador.paciente
(nombre, apellido, dni, fechaNacimiento, historia_clinica_id)
VALUES
('María', 'López', '35888999', '1990-07-30', 4);

-- Paciente 5 (vinculado a HC-1005, ID: 5)
INSERT INTO db_integrador.paciente
(nombre, apellido, dni, fechaNacimiento, historia_clinica_id)
VALUES
('Luis', 'Martínez', '28777333', '1975-12-01', 5);