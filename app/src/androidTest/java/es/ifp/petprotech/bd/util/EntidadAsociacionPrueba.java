package es.ifp.petprotech.bd.util;

import androidx.annotation.NonNull;

import java.util.Objects;

import es.ifp.petprotech.bd.Entidad;

public class EntidadAsociacionPrueba extends Entidad {

    private final String nombre;
    private final int random;

    public EntidadAsociacionPrueba(long id, String nombre, int random) {
        super(id);
        this.nombre = nombre;
        this.random = random;
    }

    public EntidadAsociacionPrueba(String nombre, int random) {
        this.nombre = nombre;
        this.random = random;
    }

    public String getNombre() {
        return nombre;
    }

    public int getRandom() {
        return random;
    }

    @NonNull
    @Override
    public String toString() {
        return "EntidadAsociacionPrueba{" +
                "nombre='" + nombre + '\'' +
                ", random=" + random +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntidadAsociacionPrueba that = (EntidadAsociacionPrueba) o;
        return random == that.random && Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, random);
    }
}
