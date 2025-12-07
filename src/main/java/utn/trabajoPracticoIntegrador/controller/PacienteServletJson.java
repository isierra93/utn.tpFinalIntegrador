package utn.trabajoPracticoIntegrador.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "PacienteServletJson", urlPatterns = {"/api/pacientes"})
public class PacienteServletJson extends HttpServlet {

    private PacienteService pacienteService;
    private ObjectMapper mapper;

    @Override
    public void init() {
        pacienteService = new PacienteService(new PacienteDao(), new HistoriaClinicaService(new HistoriaClinicaDao()), new HistoriaClinicaDao());
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        List<Paciente> listaPacientes;

        try {
            listaPacientes = pacienteService.getAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        res.setContentType("application/json;charset=UTF-8");

        mapper.writeValue(res.getWriter() , listaPacientes);

    }


}
