package utn.trabajoPracticoIntegrador.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    //Datos de la conexión
    private static final String URL = "jdbc:mysql://localhost:3306/db_integrador";
    private static final String USER = "usuario_hospital";
    private static final String PASSWORD = "abc123";

    static {
        try {
            //Carga el driver JDBC de MYSQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            //Lanza una excepción si no esta disponible el driver
            throw new RuntimeException("Error: No se encontró el driver JDBC.", e);
        }
    }

    /**
     * Método para obtener una conexión con la base de datos
     * @return Connection si la conexión es exitosa.
     * @throws SQLException Si hay un error en la conexión.
     */
    public static Connection getConnection() throws SQLException {
        if (URL == null || URL.isEmpty() || USER == null || USER.isEmpty() || PASSWORD == null || PASSWORD.isEmpty()){
            throw new SQLException("Configuracion de la base de datos incompleta o invalida.");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


}
