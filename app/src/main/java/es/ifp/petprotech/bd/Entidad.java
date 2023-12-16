package es.ifp.petprotech.bd;

public abstract class Entidad {

    private long id;
    public final long getId() {
        return id;
    }
    public final void setId(long id) {
        if (id < 1)
            throw new IllegalArgumentException("Valor de id invalido: " + id);

        if (this.id > 0)
            throw new IllegalArgumentException("Esta entidad ya tiene un valor de id asignado");

        this.id = id;
    }
}
