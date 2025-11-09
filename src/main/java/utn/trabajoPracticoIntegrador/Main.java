package utn.trabajoPracticoIntegrador;

import java.sql.Connection;
import java.sql.SQLException;
import utn.trabajoPracticoIntegrador.config.DatabaseConnection;

public class Main {
    public static void main(String[] args) {
        System.out.println("Intentando conectar a la base de datos...");

        try {
            // Llama a tu clase de conexión
            Connection conn = DatabaseConnection.getConnection();

            if (conn != null) {
                System.out.println("======================================");
                System.out.println("¡CONEXIÓN EXITOSA!");
                System.out.println("======================================");
                conn.close();
            }
        } catch (SQLException e) {
            // Si algo falló, imprime el error
            System.err.println("======================================");
            System.err.println("ERROR: NO SE PUDO CONECTAR A LA DB");
            System.err.println("======================================");
            e.printStackTrace(); 
        }
    }
}