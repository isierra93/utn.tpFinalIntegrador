package utn.trabajoPracticoIntegrador.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utn.trabajoPracticoIntegrador.dao.HistoriaClinicaDao;
import utn.trabajoPracticoIntegrador.dao.PacienteDao;
import utn.trabajoPracticoIntegrador.entities.GrupoSanguineo;
import utn.trabajoPracticoIntegrador.entities.HistoriaClinica;
import utn.trabajoPracticoIntegrador.entities.Paciente;
import utn.trabajoPracticoIntegrador.service.HistoriaClinicaService;
import utn.trabajoPracticoIntegrador.service.PacienteService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "PacienteServlet", urlPatterns = {"/pacientes"})
public class PacienteServlet extends HttpServlet {

    private PacienteService pacienteService;

    @Override
    public void init() {
        pacienteService = new PacienteService(new PacienteDao(), new HistoriaClinicaService(new HistoriaClinicaDao()), new HistoriaClinicaDao());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String accion = req.getParameter("accion");

        if (accion == null){
            accion = "listar";
        }

        switch (accion){
            case "nuevo":
                req.getRequestDispatcher("crearPacientes.jsp").forward(req, res);
                break;

            case "listar":
                List<Paciente> pacientes;
                try {
                    pacientes = pacienteService.getAll();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                req.setAttribute("listaPacientes", pacientes);
                req.getRequestDispatcher("pacientes.jsp").forward(req, res);
                break;

            case "editar":
                Long id = Long.parseLong(req.getParameter("id"));
                Paciente paciente = null;
                try {
                    paciente = pacienteService.getById(id);

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                if (paciente == null) {
                res.sendRedirect("pacientes");
                }

                req.setAttribute("paciente", paciente);
                req.getRequestDispatcher("crearPacientes.jsp").forward(req, res);

                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        //Datos del Paciente
        String idStr = req.getParameter("id");
        String nombre = req.getParameter("nombre");
        String apellido = req.getParameter("apellido");
        String dni = req.getParameter("dni");
        String fechaNacimiento = req.getParameter("fechaNacimiento");

        //Datos de la HistoriaClinica
        String nroHistoria = nombre.substring(0, 2).toUpperCase() + "-" + dni.substring(0, 4);
        GrupoSanguineo grupoSanguineo = GrupoSanguineo.fromValue(req.getParameter("grupoSanguineo"));
        String antecedentes = req.getParameter("antecedentes");
        String medicacionActual = req.getParameter("medicacionActual");
        String observaciones = req.getParameter("observaciones");


        try {
            if (idStr == null || idStr.isEmpty()) {
                HistoriaClinica historiaClinica = new HistoriaClinica(null, false, nroHistoria, grupoSanguineo, antecedentes, medicacionActual, observaciones);
                Paciente paciente = new Paciente(null, false, nombre, apellido, dni, LocalDate.parse(fechaNacimiento) , historiaClinica );
                pacienteService.insertar(paciente);
            }else {
                Long id = Long.parseLong(idStr);
                Long idHistoria = Long.parseLong(req.getParameter("idHistoria"));
                HistoriaClinica historiaClinica = new HistoriaClinica(idHistoria, false, nroHistoria, grupoSanguineo, antecedentes, medicacionActual, observaciones);
                Paciente paciente = new Paciente(id, false, nombre, apellido, dni, LocalDate.parse(fechaNacimiento) , historiaClinica );

                pacienteService.actualizar(paciente);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        res.sendRedirect("pacientes");
    }
}
