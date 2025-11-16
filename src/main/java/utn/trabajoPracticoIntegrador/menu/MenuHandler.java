package utn.trabajoPracticoIntegrador.menu;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import utn.trabajoPracticoIntegrador.entities.GrupoSanguineo;
import utn.trabajoPracticoIntegrador.entities.HistoriaClinica;
import utn.trabajoPracticoIntegrador.entities.Paciente;
import utn.trabajoPracticoIntegrador.service.HistoriaClinicaService;
import utn.trabajoPracticoIntegrador.service.PacienteService;

public class MenuHandler {

    private final Scanner scanner;
    private final PacienteService pacienteService;
    private final HistoriaClinicaService historiaClinicaService;

    /**
     * Constructor con inyección de dependencias.
     */
    public MenuHandler(Scanner scanner, PacienteService pacienteService, HistoriaClinicaService historiaClinicaService) {
        if (scanner == null || pacienteService == null || historiaClinicaService == null) {
            throw new IllegalArgumentException("Las dependencias no pueden ser null");
        }
        this.scanner = scanner;
        this.pacienteService = pacienteService;
        this.historiaClinicaService = historiaClinicaService;
    }

    /**
     * Opción 1: Crear Paciente y su Historia
     */
    public void crearPacienteConHistoria() {
        System.out.println("\n--- Creando Nuevo Paciente y su Historia Clínica ---");
        try {
            // Pedimos datos Paciente
            System.out.print("Nombre del Paciente: ");
            String nombre = scanner.nextLine().trim();
            System.out.print("Apellido del Paciente: ");
            String apellido = scanner.nextLine().trim();
            System.out.print("DNI del Paciente: ");
            String dni = scanner.nextLine().trim().toUpperCase();
            System.out.print("Fecha de Nacimiento (AAAA-MM-DD): ");
            LocalDate fechaNac = LocalDate.parse(scanner.nextLine().trim());
            
            // Pedimos datos Historia
            System.out.print("Nro. de Historia (ej: HC-1001): ");
            String nroHistoria = scanner.nextLine().trim().toUpperCase();
            System.out.print("Grupo Sanguíneo (A+, O-, AB+, etc.): ");
            GrupoSanguineo gs = GrupoSanguineo.fromValue(scanner.nextLine().trim().toUpperCase());
            System.out.print("Antecedentes (opcional): ");
            String antecedentes = scanner.nextLine().trim();
            System.out.print("Medicación Actual (opcional): ");
            String medicacion = scanner.nextLine().trim();
            System.out.print("Observaciones (opcional): ");
            String observaciones = scanner.nextLine().trim();

            // Creamos los objetos
            HistoriaClinica nuevaHistoria = new HistoriaClinica(null, false, nroHistoria, gs, antecedentes, medicacion, observaciones);
            Paciente nuevoPaciente = new Paciente(null, false, nombre, apellido, dni, fechaNac, nuevaHistoria); // Asociamos la historia

            // Llamamos al SERVICIO transaccional
            // (El método se llama 'insertar' en la nueva plantilla)
            pacienteService.insertar(nuevoPaciente);

            System.out.println("-------------------------------------------------");
            System.out.println("¡ÉXITO! Paciente y Historia creados correctamente.");
            System.out.println("ID del Paciente: " + nuevoPaciente.getId());
            System.out.println("ID de la Historia: " + nuevaHistoria.getId());
            System.out.println("-------------------------------------------------");

        } catch (DateTimeParseException e) {
            System.err.println("Error de Formato: La fecha debe ser AAAA-MM-DD.");
        } catch (IllegalArgumentException e) { // Captura validaciones de service y enums
            System.err.println("Error de Formato: " + e.getMessage());
        } catch (Exception e) { // Captura SQLException y otras
            System.err.println("-------------------------------------------------");
            System.err.println("ERROR AL CREAR: " + e.getMessage());
            System.err.println("-------------------------------------------------");
        }
    }

    /**
     * Opción 2: Ver Paciente por ID
     */
    public void verPacientePorId() {
        System.out.println("\n--- Ver Paciente por ID ---");
        try {
            System.out.print("Ingrese el ID del Paciente: ");
            long id = Long.parseLong(scanner.nextLine()); // Usamos long
            Paciente p = pacienteService.getById(id);

            if (p != null) {
                System.out.println("Resultado: \n" + p.toString());
            } else {
                System.err.println("No se encontró ningún paciente con el ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: El ID debe ser un número.");
        } catch (Exception e) {
            System.err.println("Error de Base de Datos: " + e.getMessage());
        }
    }

    /**
     * Opción 3: Listar todos los Pacientes
     */
    public void listarTodosLosPacientes() {
        System.out.println("\n--- Listado de Pacientes ---");
        try {
            List<Paciente> pacientes = pacienteService.getAll();
            if (pacientes.isEmpty()) {
                System.out.println("No hay pacientes registrados.");
            } else {
                pacientes.forEach(p -> System.out.println(p.toString()));
            }
        } catch (Exception e) {
            System.err.println("Error de Base de Datos: " + e.getMessage());
        }
    }

    /**
     * Opción 4: Buscar Paciente por DNI
     */
    public void buscarPacientePorDni() {
        System.out.println("\n--- Buscar Paciente por DNI ---");
        try {
            System.out.print("Ingrese el DNI a buscar: ");
            String dni = scanner.nextLine().trim().toUpperCase();
            Paciente p = pacienteService.getByDni(dni); 

            if (p != null) {
                System.out.println("Resultado: \n" + p.toString());
            } else {
                System.err.println("No se encontró ningún paciente con el DNI: " + dni);
            }
        } catch (Exception e) {
            System.err.println("Error de Base de Datos: " + e.getMessage());
        }
    }
    
    /**
     * Opción 5: Actualizar Paciente
     */
    public void actualizarPaciente() {
        System.out.println("\n--- Actualizar Paciente ---");
        try {
            System.out.print("Ingrese el ID del Paciente a actualizar: ");
            long id = Long.parseLong(scanner.nextLine());
            Paciente p = pacienteService.getById(id);

            if (p == null) {
                System.err.println("No se encontró ningún paciente con el ID: " + id);
                return;
            }

            System.out.println("Datos actuales: " + p);
            System.out.println("Ingrese los nuevos datos (deje en blanco para mantener el valor actual):");

            // Lógica "Enter para mantener"
            System.out.print("Nuevo nombre (" + p.getNombre() + "): ");
            String nombre = scanner.nextLine().trim();
            if (!nombre.isEmpty()) {
                p.setNombre(nombre);
            }

            System.out.print("Nuevo apellido (" + p.getApellido() + "): ");
            String apellido = scanner.nextLine().trim();
            if (!apellido.isEmpty()) {
                p.setApellido(apellido);
            }

            System.out.print("Nuevo DNI (" + p.getDni() + "): ");
            String dni = scanner.nextLine().trim().toUpperCase();
            if (!dni.isEmpty()) {
                p.setDni(dni);
            }
            
            System.out.print("Nueva Fecha de Nacimiento (AAAA-MM-DD) (" + p.getFechaNacimiento() + "): ");
            String fechaStr = scanner.nextLine().trim();
            if (!fechaStr.isEmpty()) {
                p.setFechaNacimiento(LocalDate.parse(fechaStr));
            }
            
            pacienteService.actualizar(p);
            System.out.println("¡ÉXITO! Paciente actualizado correctamente.");
            actualizarHistoriaClinica(p.getHistoriaClinica().getId());
        } catch (NumberFormatException e) {
            System.err.println("Error: El ID debe ser un número.");
        } catch (DateTimeParseException e) {
            System.err.println("Error de Formato: La fecha debe ser AAAA-MM-DD.");
        } catch (Exception e) { // Captura errores de Service y DB
            System.err.println("Error al actualizar paciente: " + e.getMessage());
        }
    }


    /**
     * Opción 6: Eliminar Paciente (lógico)
     */
    public void eliminarPaciente() {
        System.out.println("\n--- Eliminar Paciente (Baja Lógica) ---");
        try {
            System.out.print("Ingrese el ID del Paciente a eliminar: ");
            long id = Long.parseLong(scanner.nextLine());
            Paciente p = pacienteService.getById(id);
            if (p == null) {
                System.err.println("Error: No se encontró ningún paciente con el ID: " + id);
                return;
            }
            
            System.out.print("¿Está seguro que desea eliminar a " + p.getNombre() + " " + p.getApellido() + "? (S/N): ");
            String confirmacion = scanner.nextLine().trim().toUpperCase();
            
            if (confirmacion.equalsIgnoreCase("S")) {
                pacienteService.eliminar(id);
                historiaClinicaService.eliminar(p.getHistoriaClinica().getId());
                System.out.println("¡ÉXITO! Paciente eliminado correctamente.");
            } else {
                System.out.println("Operación cancelada.");
            }
            
        } catch (NumberFormatException e) {
            System.err.println("Error: El ID debe ser un número.");
        } catch (Exception e) {
            System.err.println("Error de Base de Datos: " + e.getMessage());
        }
    }

    /**
     * Opción 7: Ver Historia Clínica por ID
     */
    public void verHistoriaPorId() {
        System.out.println("\n--- Ver Historia Clínica por ID ---");
        try {
            System.out.print("Ingrese el ID de la Historia: ");
            long id = Long.parseLong(scanner.nextLine());
            HistoriaClinica hc = historiaClinicaService.getById(id);

            if (hc != null) {
                System.out.println("Resultado: \n" + hc.toString());
            } else {
                System.err.println("No se encontró ninguna historia con el ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: El ID debe ser un número.");
        } catch (Exception e) {
            System.err.println("Error de Base de Datos: " + e.getMessage());
        }
    }

    /**
     * Opción 8: Listar todas las Historias Clínicas
     */
    public void listarTodasLasHistorias() {
        System.out.println("\n--- Listado de Historias Clínicas ---");
        try {
            List<HistoriaClinica> historias = historiaClinicaService.getAll();
            if (historias.isEmpty()) {
                System.out.println("No hay historias registradas.");
            } else {
                historias.forEach(hc -> System.out.println(hc.toString()));
            }
        } catch (Exception e) {
            System.err.println("Error de Base de Datos: " + e.getMessage());
        }
    }

    /**
     * Opción 8: Actualizar Historia Clinica
     */

    public void actualizarHistoriaClinica(Long id) {
        System.out.println("\n--- Actualizar Historia Clinica ---");
        try {

            HistoriaClinica historiaClinica = historiaClinicaService.getById(id);

            if (historiaClinica == null) {
                System.err.println("No se encontró ningúna historia clinica con el ID: " + id);
                return;
            }

            System.out.println("Datos actuales: " + historiaClinica);
            System.out.println("Ingrese los nuevos datos (deje en blanco para mantener el valor actual):");

            // Lógica "Enter para mantener"
            System.out.print("Nuevo Numero Historia (" + historiaClinica.getNroHistoria() + "): ");
            String nroHistoria = scanner.nextLine().trim();
            if (!nroHistoria.isEmpty()) {
                historiaClinica.setNroHistoria(nroHistoria);
            }

            //Verificar si esto anda
            System.out.print("Nuevo Grupo Sanguineo (" + historiaClinica.getGrupoSanguineo() + "): ");
            String grupoSanguineo = scanner.nextLine().trim();
            if (!grupoSanguineo.isEmpty()) {
                GrupoSanguineo gs = GrupoSanguineo.fromValue(grupoSanguineo);
                historiaClinica.setGrupoSanguineo(gs);
            }

            System.out.print("Nuevo antecedentes (" + historiaClinica.getAntecedentes() + "): ");
            String antecedentes = scanner.nextLine().trim().toUpperCase();
            if (!antecedentes.isEmpty()) {
                historiaClinica.setAntecedentes(antecedentes);
            }

            System.out.print("Nueva Medicacion: (" + historiaClinica.getMedicacionActual() + "): ");
            String medicacion = scanner.nextLine().trim();
            if (!medicacion.isEmpty()) {
                historiaClinica.setMedicacionActual(medicacion);
            }

            System.out.print("Nuevas observaciones: (" + historiaClinica.getObservaciones() + "): ");
            String obs = scanner.nextLine().trim();
            if (!obs.isEmpty()) {
                historiaClinica.setObservaciones(obs);
            }

            historiaClinicaService.actualizar(historiaClinica);
            System.out.println("¡ÉXITO! Historia Clinica actualizada correctamente.");

        } catch (NumberFormatException e) {
            System.err.println("Error: El ID debe ser un número.");
        }
        catch (Exception e) { // Captura errores de Service y DB
            System.err.println("Error al actualizar historia clinica: " + e.getMessage());
        }
    }

}
