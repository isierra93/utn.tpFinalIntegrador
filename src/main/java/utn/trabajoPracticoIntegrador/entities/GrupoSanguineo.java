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

    private final String valorSanguineo;

    GrupoSanguineo(String s) {
        this.valorSanguineo = s;
    }

    public String getGrupoSanguineo(){
        return valorSanguineo;
    }

    public static GrupoSanguineo buscarPorValor(String valor) {

        for(GrupoSanguineo grupoSanguineo : GrupoSanguineo.values()) {
            if (grupoSanguineo.getGrupoSanguineo().equals(valor)  ){
                return grupoSanguineo;
            }
        }
        throw new IllegalArgumentException("Valor de grupo sanguíneo no válido: '" + valor + "'");
    }
}
