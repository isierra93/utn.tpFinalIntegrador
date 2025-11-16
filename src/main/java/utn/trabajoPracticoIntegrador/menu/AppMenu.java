package utn.trabajoPracticoIntegrador.menu;
import java.util.Scanner;

import utn.trabajoPracticoIntegrador.dao.HistoriaClinicaDao;
import utn.trabajoPracticoIntegrador.dao.PacienteDao;
import utn.trabajoPracticoIntegrador.service.HistoriaClinicaService;
import utn.trabajoPracticoIntegrador.service.PacienteService;

public class AppMenu {

    private final Scanner scanner;
    private final MenuHandler menuHandler; 
    private boolean running;

    /**
     * Constructor que inicializa la aplicación.
     * Implementa Inyección de Dependencias manual
     */
    public AppMenu() {
        this.scanner = new Scanner(System.in);
        // El "Factory Method" crea toda la cadena de dependencias
        this.menuHandler = createMenuHandler();
        this.running = true;
    }

    public void run() {
        while (running) {
            try {
                MenuDisplay.mostrarMenuPrincipal(); // Muestra el menú
                int opcion = Integer.parseInt(scanner.nextLine());
                processOption(opcion); // Procesa la opción
            } catch (NumberFormatException e) {
                System.err.println("Entrada invalida. Por favor, ingrese un numero.");
            }
        }
        scanner.close(); 
    }

    /**
     * Procesa la opción y delega a MenuHandler.
     */
    private void processOption(int opcion) {
        switch (opcion) {
            case 1 -> menuHandler.crearPacienteConHistoria();
            case 2 -> menuHandler.verPacientePorId();
            case 3 -> menuHandler.listarTodosLosPacientes();
            case 4 -> menuHandler.buscarPacientePorDni();
            case 5 -> menuHandler.actualizarPaciente(); // Nueva opción
            case 6 -> menuHandler.eliminarPaciente();
            case 7 -> menuHandler.verHistoriaPorId();
            case 8 -> menuHandler.listarTodasLasHistorias();
            case 0 -> { // 0 para Salir
                System.out.println("Saliendo... ¡Hasta luego!");
                running = false;
            }
            default -> System.err.println("Opcion no valida.");
        }
    }

    private MenuHandler createMenuHandler() {
        // 1. DAOs (Capa más baja)
        HistoriaClinicaDao historiaDao = new HistoriaClinicaDao();
        PacienteDao pacienteDao = new PacienteDao();
        
        // 2. Services (Inyectamos los DAOs)
        HistoriaClinicaService historiaService = new HistoriaClinicaService(historiaDao);
        
        // PacienteService necesita ambos DAOs y el otro Service
        PacienteService pacienteService = new PacienteService(pacienteDao, historiaService, historiaDao);
        
        // 3. Handler (Inyectamos Scanner y Services)
        return new MenuHandler(scanner, pacienteService, historiaService);
    }
}