package utn.trabajoPracticoIntegrador;

import utn.trabajoPracticoIntegrador.menu.AppMenu;

public class Main {
    public static void main(String[] args) {
        AppMenu app = new AppMenu();
        app.run();
    }
}
//Agregue llamado a historiaclinicaservice en menugandler - line 227
//GenericDAO - actualizar y eliminar no se utiliza
//Al cargar un paciente con dato null
//Opcion para Modificar historia clinica