package utn.trabajoPracticoIntegrador.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static Properties properties;
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {

            properties = new Properties();
            properties.load(input);
            if (input == null){
                throw new RuntimeException("Error: No se encontró el archivo db.properties");
            }

            URL = properties.getProperty("db.url");
            USER = properties.getProperty("db.user");
            PASSWORD = properties.getProperty("db.password");

        } catch (Exception e) {
            throw new RuntimeException("Error: No se pudo cargar db.properties" , e);
        }
    }

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
     * Metodo para obtener una conexión con la base de datos
     * @return Connection si la conexión es exitosa.
     * @throws SQLException Si hay un error en la configuracion.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
}
