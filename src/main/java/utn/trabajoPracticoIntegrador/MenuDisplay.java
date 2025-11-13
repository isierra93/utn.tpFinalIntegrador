/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utn.trabajoPracticoIntegrador;

/**
 *
 * @author Cristian
 */
public class MenuDisplay {
    
    public static void mostrarMenuPrincipal() {
        System.out.println("\n--- SISTEMA DE GESTIÓN HOSPITALARIA ---");
        System.out.println("--- GESTIÓN DE PACIENTES ---");
        System.out.println("1. Crear Paciente (con Historia Clínica)");
        System.out.println("2. Ver Paciente por ID");
        System.out.println("3. Listar todos los Pacientes");
        System.out.println("4. Buscar Paciente por DNI");
        System.out.println("5. Actualizar Paciente"); // La opción que faltaba
        System.out.println("6. Eliminar Paciente (lógico)");
        System.out.println("--- GESTIÓN DE HISTORIAS CLÍNICAS ---");
        System.out.println("7. Ver Historia Clínica por ID");
        System.out.println("8. Listar todas las Historias Clínicas");
        // Aquí podrías agregar 9. Actualizar HC, 10. Eliminar HC
        System.out.println("0. Salir"); // Usamos 0 para Salir
        System.out.print("Seleccione una opción: ");
    }
}
