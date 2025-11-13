package utn.trabajoPracticoIntegrador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestHistoriaClinica {
    public static void main(String[] args) {

        try (Connection conn = utn.trabajoPracticoIntegrador.config.DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("✅ Conexión establecida con éxito.");

                // 🔹 Crear y ejecutar consulta SQL con PreparedStatement
                String sql = "SELECT * FROM historiaclinica";
                try (PreparedStatement pstmt = conn.prepareStatement(sql);
                     ResultSet rs = pstmt.executeQuery()) {
                    System.out.println("📋 Listado de historias clinicas:");
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String nroHistoria = rs.getString("nroHistoria");
                        String grupoSanguineo = rs.getString("grupoSanguineo");
                        String antecedentes = rs.getString("antecedentes");
                        String medicacionActual = rs.getString("medicacionActual");
                        String observaciones = rs.getString("observaciones");
                        System.out.println("ID: " + id + ", " +
                                "Numero: " + nroHistoria + ", " +
                                "Grupo Sanguineo: " + grupoSanguineo + ", " +
                                "Antecedentes: " + antecedentes + ", " +
                                "Medicacion actual: " + medicacionActual + ", " +
                                "Observaciones: " + observaciones );
                    }
                }
            } else {
                System.out.println("❌ No se pudo establecer la conexión.");
            }
        } catch (SQLException e) {
            // 🔹 Manejo de errores en la conexión a la base de datos
            System.err.println("⚠️ Error al conectar a la base de datos: " + e.getMessage());
            e.printStackTrace(); // Imprime el stack trace completo para depuración
        }
    }
}
