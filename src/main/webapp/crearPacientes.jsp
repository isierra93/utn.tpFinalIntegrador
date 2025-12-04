<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Nuevo Paciente</title>
</head>
<body>
    <h1>Registrar Nuevo Paciente</h1>

    <form action="pacientes" method="post">
        <input type="hidden" name="accion" value="guardar">
        <input type="hidden" name="id" value="${paciente.id}">
        <input type="hidden" name="idHistoria" value="${paciente.historiaClinica.id}">

        <h5>Datos Paciente:</h5>
        <label for="nombre">Nombre:</label>
        <input value="${paciente.nombre}" type="text" id="nombre" name="nombre" required><br><br>

        <label for="apellido">Apellido:</label>
        <input value="${paciente.apellido}" type="text" id="apellido" name="apellido" required><br><br>

        <label for="dni">DNI:</label>
        <input value="${paciente.dni}" type="text" id="dni" name="dni" required><br><br>

        <label for="fechaNacimiento">Fecha de Nacimiento:</label>
        <input value="${paciente.fechaNacimiento}" type="date" id="fechaNacimiento" name="fechaNacimiento" required><br><br>

        <h5>Datos Historia clinica:</h5>
        <label for="grupoSanguineo">Grupo Sanguineo:</label>

        <input type="radio" id="grupoSanguineo" name="grupoSanguineo" ${paciente.historiaClinica.grupoSanguineo.valorDb == 'A+' ? 'checked' : ''} value="A+"> A +
        <input type="radio" id="grupoSanguineo" name="grupoSanguineo" ${paciente.historiaClinica.grupoSanguineo.valorDb == 'A-' ? 'checked' : ''} value="A-"> A -
        <input type="radio" id="grupoSanguineo" name="grupoSanguineo" ${paciente.historiaClinica.grupoSanguineo.valorDb == 'B+' ? 'checked' : ''} value="B+"> B +
        <input type="radio" id="grupoSanguineo" name="grupoSanguineo" ${paciente.historiaClinica.grupoSanguineo.valorDb == 'B-' ? 'checked' : ''} value="B-"> B -
        <input type="radio" id="grupoSanguineo" name="grupoSanguineo" ${paciente.historiaClinica.grupoSanguineo.valorDb == 'AB+' ? 'checked' : ''} value="AB+"> AB +
        <input type="radio" id="grupoSanguineo" name="grupoSanguineo" ${paciente.historiaClinica.grupoSanguineo.valorDb == 'AB-' ? 'checked' : ''} value="AB-"> AB -
        <input type="radio" id="grupoSanguineo" name="grupoSanguineo" ${paciente.historiaClinica.grupoSanguineo.valorDb == 'O+' ? 'checked' : ''} value="O+"> O +
        <input type="radio" id="grupoSanguineo" name="grupoSanguineo" ${paciente.historiaClinica.grupoSanguineo.valorDb == 'O-' ? 'checked' : ''} value="O-"> O -

        <br><br>
        <label for="antecedentes">Antecedentes:</label>
        <input value="${paciente.historiaClinica.antecedentes}" type="text" id="antecedentes" name="antecedentes" ><br><br>

        <label for="medicacionActual">Medicacion actual:</label>
        <input value="${paciente.historiaClinica.medicacionActual}" type="text" id="medicacionActual" name="medicacionActual" ><br><br>

        <label for="observaciones">Observaciones:</label>
        <input value="${paciente.historiaClinica.observaciones}" type="text" id="observaciones" name="observaciones" ><br><br>

        <button type="submit">Guardar Paciente</button>
    </form>

    <br>
    <a href="pacientes">Volver a la lista</a>
</body>
</html>