/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utn.trabajoPracticoIntegrador.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import utn.trabajoPracticoIntegrador.entities.GrupoSanguineo;
import utn.trabajoPracticoIntegrador.entities.HistoriaClinica;
import utn.trabajoPracticoIntegrador.entities.Paciente;

/**
 *
 * @author Cristian
 */
public class AppMenu {

    // "Dependencias": El menú "usa" los servicios
    private PacienteServiceImpl pacienteService;
    private HistoriaClinicaServiceImpl historiaClinicaService;
    private Scanner scanner;

    // Constructor: Inicializa las dependencias
    public AppMenu() {
        this.pacienteService = new PacienteServiceImpl();
        this.historiaClinicaService = new HistoriaClinicaServiceImpl();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Bucle principal del menú.
     */
    public void mostrarMenuPrincipal() {
        int opcion = 0;
        
        while (opcion != 9) {
            System.out.println("\n--- SISTEMA DE GESTIÓN HOSPITALARIA ---");
            System.out.println("--- GESTIÓN DE PACIENTES ---");
            System.out.println("1. Crear Paciente (con Historia Clínica)");
            System.out.println("2. Ver Paciente por ID");
            System.out.println("3. Listar todos los Pacientes");
            System.out.println("4. Buscar Paciente por DNI"); // Requisito: Búsqueda por campo
            System.out.println("5. Eliminar Paciente (lógico)");
            // Faltaría "6. Actualizar Paciente" (te lo dejo de tarea)
            System.out.println("--- GESTIÓN DE HISTORIAS CLÍNICAS ---");
            System.out.println("7. Ver Historia Clínica por ID");
            System.out.println("8. Listar todas las Historias Clínicas");
            System.out.println("9. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                // Requisito: Manejo robusto de entradas inválidas
                opcion = Integer.parseInt(scanner.nextLine()); 

                switch (opcion) {
                    case 1:
                        crearPacienteConHistoria();
                        break;
                    case 2:
                        verPacientePorId();
                        break;
                    case 3:
                        listarTodosLosPacientes();
                        break;
                    case 4:
                        buscarPacientePorDni();
                        break;
                    case 5:
                        eliminarPaciente();
                        break;
                    case 7:
                        verHistoriaPorId();
                        break;
                    case 8:
                        listarTodasLasHistorias();
                        break;
                    case 9:
                        System.out.println("Saliendo... ¡Hasta luego!");
                        break;
                    default:
                        System.err.println("Opción no válida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                // Requisito: Manejo robusto de parseos numéricos
                System.err.println("Error: Debe ingresar un número válido.");
                opcion = 0; // Resetea la opción para que no salga del bucle
            }
        }
        scanner.close();
    }

    /**
     * Requisito: CRUD Completo - Crear A y B
     */
    private void crearPacienteConHistoria() {
        System.out.println("\n--- Creando Nuevo Paciente y su Historia Clínica ---");
        try {
            // --- Pedir datos del Paciente (A) ---
            System.out.print("Nombre del Paciente: ");
            String nombre = scanner.nextLine();
            System.out.print("Apellido del Paciente: ");
            String apellido = scanner.nextLine();
            
            // Requisito: Convertir a mayúsculas
            System.out.print("DNI del Paciente: ");
            String dni = scanner.nextLine().toUpperCase(); 
            
            // Requisito: Manejo de formatos
            System.out.print("Fecha de Nacimiento (AAAA-MM-DD): ");
            LocalDate fechaNac = LocalDate.parse(scanner.nextLine());

            // --- Pedir datos de la Historia Clínica (B) ---
            System.out.print("Nro. de Historia (ej: HC-1001): ");
            String nroHistoria = scanner.nextLine().toUpperCase();
            
            System.out.print("Grupo Sanguíneo (A+, O-, AB+, etc.): ");
            // Requisito: Convertir a mayúsculas
            GrupoSanguineo gs = GrupoSanguineo.fromValue(scanner.nextLine().toUpperCase());
            
            System.out.print("Antecedentes (opcional): ");
            String antecedentes = scanner.nextLine();
            System.out.print("Medicación Actual (opcional): ");
            String medicacion = scanner.nextLine();
            System.out.print("Observaciones (opcional): ");
            String observaciones = scanner.nextLine();

            // Creamos los objetos
            HistoriaClinica nuevaHistoria = new HistoriaClinica(null, false, nroHistoria, gs, antecedentes, medicacion, observaciones);
            Paciente nuevoPaciente = new Paciente(null, false, nombre, apellido, dni, fechaNac, null); // La historia se asigna en el servicio

            // ¡Llamamos al SERVICIO transaccional!
            pacienteService.crearPacienteYSuHistoria(nuevoPaciente, nuevaHistoria);

            // Requisito: Mensajes claros de éxito
            System.out.println("-------------------------------------------------");
            System.out.println("¡ÉXITO! Paciente y Historia creados correctamente.");
            System.out.println("ID del Paciente: " + nuevoPaciente.getId());
            System.out.println("ID de la Historia: " + nuevaHistoria.getId());
            System.out.println("-------------------------------------------------");

        } catch (DateTimeParseException e) {
            System.err.println("Error de Formato: La fecha debe ser AAAA-MM-DD.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error de Formato: El grupo sanguíneo no es válido.");
        } catch (SQLException | RuntimeException e) {
            // Requisito: Manejo de errores de BD y violaciones de unicidad
            System.err.println("-------------------------------------------------");
            System.err.println("ERROR AL CREAR: " + e.getMessage());
            System.err.println("-------------------------------------------------");
        }
    }

    /**
     * Requisito: CRUD Completo - Leer A por ID
     */
    private void verPacientePorId() {
        System.out.println("\n--- Ver Paciente por ID ---");
        try {
            System.out.print("Ingrese el ID del Paciente: ");
            long id = Long.parseLong(scanner.nextLine());
            
            Paciente p = pacienteService.getById(id);

            // Requisito: Manejo de IDs inexistentes
            if (p != null) {
                System.out.println("Resultado: \n" + p.toString());
            } else {
                System.err.println("No se encontró ningún paciente con el ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: El ID debe ser un número.");
        } catch (SQLException e) {
            System.err.println("Error de Base de Datos: " + e.getMessage());
        }
    }

    /**
     * Requisito: CRUD Completo - Listar A
     */
    private void listarTodosLosPacientes() {
        System.out.println("\n--- Listado de Pacientes ---");
        try {
            List<Paciente> pacientes = pacienteService.getAll();
            if (pacientes.isEmpty()) {
                System.out.println("No hay pacientes registrados.");
            } else {
                pacientes.forEach(p -> System.out.println(p.toString()));
            }
        } catch (SQLException e) {
            System.err.println("Error de Base de Datos: " + e.getMessage());
        }
    }

    /**
     * Requisito: Búsqueda por campo relevante (DNI)
     */
    private void buscarPacientePorDni() {
//        System.out.println("\n--- Buscar Paciente por DNI ---");
//        System.out.print("Ingrese el DNI a buscar: ");
//        String dni = scanner.nextLine().toUpperCase();
//
//        // ¡¡ESTO REQUIERE AÑADIR EL MÉTODO getByDni() A PacienteServiceImpl!!
//        // (Ya lo agregamos al PacienteDao, ahora falta exponerlo en el servicio)
//        
//        /* --- CÓDIGO A AÑADIR en PacienteServiceImpl.java ---
//        public Paciente getByDni(String dni) throws SQLException {
//        try (Connection conn = DatabaseConnection.getConnection()) {
//        return pacienteDao.getByDni(dni, conn); // Reutiliza el método del DAO
//        }
//        }
//        ------------------------------------------------------- */
//        
//        Paciente p = pacienteService.getByDni(dni);
//        if (p != null) {
//            System.out.println("Resultado: \n" + p.toString());
//        } else {
//            System.err.println("No se encontró ningún paciente con el DNI: " + dni);
//        }
    }

    /**
     * Requisito: CRUD Completo - Eliminar A (lógico)
     */
    private void eliminarPaciente() {
        System.out.println("\n--- Eliminar Paciente (Baja Lógica) ---");
        try {
            System.out.print("Ingrese el ID del Paciente a eliminar: ");
            long id = Long.parseLong(scanner.nextLine());

            // Primero verificamos que exista
            Paciente p = pacienteService.getById(id);
            if (p == null) {
                System.err.println("Error: No se encontró ningún paciente con el ID: " + id);
                return; // Salimos del método
            }
            
            // Confirmación
            System.out.print("¿Está seguro que desea eliminar a " + p.getNombre() + " " + p.getApellido() + "? (S/N): ");
            String confirmacion = scanner.nextLine().toUpperCase();
            
            if (confirmacion.equals("S")) {
                pacienteService.eliminar(id);
                System.out.println("¡ÉXITO! Paciente eliminado correctamente.");
            } else {
                System.out.println("Operación cancelada.");
            }
            
        } catch (NumberFormatException e) {
            System.err.println("Error: El ID debe ser un número.");
        } catch (SQLException e) {
            System.err.println("Error de Base de Datos: " + e.getMessage());
        }
    }

    // --- Métodos para Historia Clínica (B) ---

    private void verHistoriaPorId() {
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
        } catch (SQLException e) {
            System.err.println("Error de Base de Datos: " + e.getMessage());
        }
    }

    private void listarTodasLasHistorias() {
        System.out.println("\n--- Listado de Historias Clínicas ---");
        try {
            List<HistoriaClinica> historias = historiaClinicaService.getAll();
            if (historias.isEmpty()) {
                System.out.println("No hay historias registradas.");
            } else {
                historias.forEach(hc -> System.out.println(hc.toString()));
            }
        } catch (SQLException e) {
            System.err.println("Error de Base de Datos: " + e.getMessage());
        }
    }
}
