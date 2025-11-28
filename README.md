# TP Integrador Programación 2 - Gestión de Pacientes

Este repositorio contiene el Trabajo Práctico Integrador para la materia de Programación 2. El proyecto es una aplicación desarrollada en Java 21 (Vanilla) que implementa un sistema CRUD (Crear, Leer, Actualizar, Eliminar) para la gestión de pacientes y sus correspondientes historias clínicas.

La persistencia de los datos se maneja directamente a través de JDBC conectándose a una base de datos MySQL.

## Características Principales

* **Gestión de Pacientes:**
  * Alta (Crear) nuevos pacientes.
  * Baja lógica (Eliminar) pacientes existentes.
  * Modificación (Actualizar) la información de los pacientes.
  * Lectura (Listar y buscar) pacientes en la base de datos.


* **Gestión de Historias Clínicas:**
  * Crear, leer, actualizar y eliminar historias clínicas asociadas a cada paciente.
  

* **Persistencia de Datos:**
    * Conexión directa a la base de datos mediante JDBC.
    * Ejecución de consultas SQL para todas las operaciones.

## Tecnologías Utilizadas

* **Lenguaje:** Java 21
* **Base de Datos:** MySQL
* **Conectividad:** JDBC (Java Database Connectivity)

## Requisitos del Sistema

| Componente        | Versión Requerida      |
|-------------------|------------------------|
| Java JDK          | 21 o superior          |
| MySQL             | 8.4.0 o superior       |
| Sistema Operativo | Windows, Linux o macOS |

Antes de ejecutar el proyecto, necesitarás tener instalado lo siguiente:

* JDK (Java Development Kit) 21 o superior.
* Un servidor de base de datos MySQL (como MySQL Community Server, XAMPP, WAMP, etc.).
* Un IDE de Java (opcional, pero recomendado): IntelliJ IDEA, Apache Netbeans, Eclipse o VS Code con las extensiones de Java.

## Puesta en Marcha

Sigue estos pasos para configurar y ejecutar el proyecto en tu máquina local:

**1. Clonar el Repositorio**

```bash
git clone https://github.com/isierra93/utn.tpFinalIntegrador.git
cd utn.tpFinalIntegrador
```

**2. Configurar y Probar la Conexión**

Por defecto conecta a:
- **Host**: localhost:3306
- **Base de datos**: db_integrador
- **Usuario**: usuario_hospital
- **Contraseña**: abc123

Para cambiar la configuración, usar el archivo dentro de la carpeta resources: db.properties

```bash

./src/main/resources/db.properties

##Datos precargados, se aplican con el init.sql
db.url=jdbc:mysql://127.0.0.1:3306/db_integrador
db.user=usuario_hospital
db.password=abc123

```

El proyecto incluye un script.sql para configurar la base de datos.

Antes de ejecutar la aplicación, asegúrate de dos cosas:

Ejecuta el script.sql en tu servidor MySQL.

Que la configuración de conexión JDBC (URL, usuario, contraseña) en el código Java sea la correcta para tu entorno local.

Para facilitar esta verificación, puedes ejecutar el test TestConexion.java (ubicado en test/java/utn/tpFinalIntegrador). Este test te informará si la conexión es exitosa y si las tablas (del script.sql) se han creado correctamente.

**3. Ejecutar la Aplicación**

Una vez que la conexión esté verificada (idealmente usando el TestConexion.java), puedes compilar y ejecutar el proyecto. Si estás usando un IDE, simplemente busca la clase principal (que contiene el método public static void main(String[] args)) y ejecútala.

---

## Resumen de Operaciones del Menú

| Opción | Operación                           | Handler 
|--------|-------------------------------------|---------|
| 1 | Crear paciente                      | `crearPacienteConHistoria()` 
| 2 | Buscar paciente por ID              | `verPacientePorId()` 
| 3 | Listar todos los pacientes          | `listarTodosLosPacientes()` 
| 4 | Buscar paciente por DNI             | `buscarPacientePorDni()` 
| 5 | Actualizar paciente e historia      | `actualizarPaciente()` 
| 6 | Eliminar paciente e historia        | `eliminarPaciente()`
| 7 | Buscar historia clinica por ID      | `verHistoriaPorId()` 
| 8 | Listar todas las historias clinicas | `listarTodasLasHistorias()`
| 0 | Salir                               | Sets `running = false`

---

Autores

Este proyecto fue desarrollado en grupo por:

- Facundo Archiria
- Nidia Samaniego
- Ivan Sierra
- Cristian Siles

Link video:
https://youtu.be/EZ--aU8dmZU