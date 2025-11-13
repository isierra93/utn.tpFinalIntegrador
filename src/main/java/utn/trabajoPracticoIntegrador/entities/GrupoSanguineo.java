package utn.trabajoPracticoIntegrador.entities;

public enum GrupoSanguineo {
    A_POSITIVO("A+"),
    A_NEGATIVO("A-"),
    B_POSITIVO("B+"),
    B_NEGATIVO("B-"),
    AB_POSITIVO("AB+"),
    AB_NEGATIVO("AB-"),
    O_POSITIVO("O+"),
    O_NEGATIVO("O-")
    ;

// 2. Campo para guardar el valor de la DB
    private final String valorDb;

    // 3. Constructor
    GrupoSanguineo(String valorDb) {
        this.valorDb = valorDb;
    }

    // 4. Getter para guardar en la DB (lo usará el DAO.crear)
    public String getValorDb() {
        return valorDb;
    }

    // 5. Método "inteligente" para buscar (¡Este reemplaza a valueOf!)
    public static GrupoSanguineo fromValue(String texto) {
        for (GrupoSanguineo gs : values()) {
            if (gs.valorDb.equalsIgnoreCase(texto)) {
                return gs;
            }
        }
        // Si no lo encuentra, lanza una excepción (o devuelve null)
        throw new IllegalArgumentException("No se encontró un GrupoSanguineo para el valor: " + texto);
    }
}