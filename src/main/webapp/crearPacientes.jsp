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

        <label for="nombre">Nombre:</label>
        <input type="text" id="nombre" name="nombre" required><br><br>

        <label for="apellido">Apellido:</label>
        <input type="text" id="apellido" name="apellido" required><br><br>

        <label for="dni">DNI:</label>
        <input type="text" id="dni" name="dni" required><br><br>

        <label for="fechaNacimiento">Fecha de Nacimiento:</label>
        <input type="date" id="fechaNacimiento" name="fechaNacimiento" required><br><br>

        <button type="submit">Guardar Paciente</button>
    </form>

    <br>
    <a href="pacientes">Volver a la lista</a>
</body>
</html>