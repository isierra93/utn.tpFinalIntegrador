package utn.trabajoPracticoIntegrador.entities;

public abstract class BaseEntity {
    private Long id;
    private boolean eliminado;

    public BaseEntity() {
    }

    public BaseEntity(Long id, boolean eliminado) {
        this.id = id;
        this.eliminado = eliminado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                ", eliminado=" + eliminado +
                '}';
    }
}
