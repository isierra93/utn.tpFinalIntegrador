package utn.trabajoPracticoIntegrador.controller;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utn.trabajoPracticoIntegrador.dao.HistoriaClinicaDao;
import utn.trabajoPracticoIntegrador.dao.PacienteDao;
import utn.trabajoPracticoIntegrador.entities.Paciente;
import utn.trabajoPracticoIntegrador.service.HistoriaClinicaService;
import utn.trabajoPracticoIntegrador.service.PacienteService;

import java.io.IOException;
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
        }
    }

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
//        String nombre = req.getParameter()
//    }
}
