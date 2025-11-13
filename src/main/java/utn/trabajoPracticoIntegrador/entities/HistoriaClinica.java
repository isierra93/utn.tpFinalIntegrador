package utn.trabajoPracticoIntegrador.entities;

public class HistoriaClinica extends BaseEntity{
    private String nroHistoria;
    private GrupoSanguineo grupoSanguineo;
    private String antecedentes;
    private String medicacionActual;
    private String observaciones;

    public HistoriaClinica() {
    }

    public HistoriaClinica(Long id, boolean eliminado, String nroHistoria, GrupoSanguineo grupoSanguineo, String antecedentes, String medicacionActual, String observaciones) {
        super(id, eliminado);
        this.nroHistoria = nroHistoria;
        this.grupoSanguineo = grupoSanguineo;
        this.antecedentes = antecedentes;
        this.medicacionActual = medicacionActual;
        this.observaciones = observaciones;
    }

    public String getNroHistoria() {
        return nroHistoria;
    }

    public void setNroHistoria(String nroHistoria) {
        this.nroHistoria = nroHistoria;
    }

    public GrupoSanguineo getGrupoSanguineo() {
        return grupoSanguineo;
    }

    public void setGrupoSanguineo(GrupoSanguineo grupoSanguineo) {
        this.grupoSanguineo = grupoSanguineo;
    }

    public String getAntecedentes() {
        return antecedentes;
    }

    public void setAntecedentes(String antecedentes) {
        this.antecedentes = antecedentes;
    }

    public String getMedicacionActual() {
        return medicacionActual;
    }

    public void setMedicacionActual(String medicacionActual) {
        this.medicacionActual = medicacionActual;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

@Override
public String toString() {
    return String.format(
        """
        -----------------------------
        HISTORIA CLÍNICA
        -----------------------------
        Número: %s
        Grupo Sanguíneo: %s
        Antecedentes: %s
        Medicación Actual: %s
        Observaciones: %s
        -----------------------------
        """,
        nroHistoria,
        grupoSanguineo,
        antecedentes != null ? antecedentes : "No registrado",
        medicacionActual != null ? medicacionActual : "No registrada",
        observaciones != null ? observaciones : "No registradas"
    );
}
}
