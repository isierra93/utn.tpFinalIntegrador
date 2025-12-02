<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Lista de Pacientes</title>
</head>
<body>
    <h1>Pacientes Registrados</h1>
    <a href="pacientes?accion=nuevo">Agregar Paciente</a>
    <table border="1">
        <thead>
            <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Apellido</th>
                <th>DNI</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${listaPacientes}" var="p">
                <tr>
                    <td>${p.id}</td>
                    <td>${p.nombre}</td>
                    <td>${p.apellido}</td>
                    <td>${p.dni}</td>
                    <td>
                        <a href="pacientes?accion=editar&id=${p.id}">Editar</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>