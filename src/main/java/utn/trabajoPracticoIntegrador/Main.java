package utn.trabajoPracticoIntegrador;

import java.sql.Connection;
import java.sql.SQLException;
import utn.trabajoPracticoIntegrador.config.DatabaseConnection;
import utn.trabajoPracticoIntegrador.service.AppMenu;

public class Main {
    public static void main(String[] args) {
        AppMenu app = new AppMenu();
        app.run();
    }
}