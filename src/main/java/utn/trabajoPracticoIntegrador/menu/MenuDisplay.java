package utn.trabajoPracticoIntegrador.menu;

public class MenuDisplay {
    
    public static void mostrarMenuPrincipal() {
        System.out.println("\n--- SISTEMA DE GESTIÓN HOSPITALARIA ---");
        System.out.println("--- GESTIÓN DE PACIENTES ---");
        System.out.println("1. Crear Paciente (con Historia Clínica)");
        System.out.println("2. Ver Paciente por ID");
        System.out.println("3. Listar todos los Pacientes");
        System.out.println("4. Buscar Paciente por DNI");
        System.out.println("5. Actualizar Paciente e Historia Clinica");
        System.out.println("6. Eliminar Paciente e Historia Clinica (lógico)");
        System.out.println("--- GESTIÓN DE HISTORIAS CLÍNICAS ---");
        System.out.println("7. Ver Historia Clínica por ID");
        System.out.println("8. Listar todas las Historias Clínicas");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }
}
