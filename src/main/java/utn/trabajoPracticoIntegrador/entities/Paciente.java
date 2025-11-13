package utn.trabajoPracticoIntegrador.entities;

import java.time.LocalDate;

public class Paciente extends BaseEntity{
    private String nombre;
    private String apellido;
    private String dni;
    private LocalDate fechaNacimiento;
    private HistoriaClinica historiaClinica;

    public Paciente() {
    }

    public Paciente(Long id, boolean eliminado, String nombre, String apellido, String dni, LocalDate fechaNacimiento, HistoriaClinica historiaClinica) {
        super(id, eliminado);
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.historiaClinica = historiaClinica;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public HistoriaClinica getHistoriaClinica() {
        return historiaClinica;
    }

    public void setHistoriaClinica(HistoriaClinica historiaClinica) {
        this.historiaClinica = historiaClinica;
    }

@Override
public String toString() {
    return String.format(
        """
        --------------------------
        PACIENTE
        --------------------------
        Nombre: %s %s
        DNI: %s
        Fecha Nacimiento: %s
        Historia Clínica: %s
        Grupo Sanguíneo: %s
        --------------------------
        """,
        nombre,
        apellido,
        dni,
        fechaNacimiento,
        (historiaClinica != null) ? historiaClinica.getNroHistoria() : "No asignada",
        (historiaClinica != null) ? historiaClinica.getGrupoSanguineo() : "No especificado"
    );
}
}
