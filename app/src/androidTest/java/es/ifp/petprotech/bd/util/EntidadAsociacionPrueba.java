package es.ifp.petprotech.bd.util;

import androidx.annotation.NonNull;

import java.util.Objects;

import es.ifp.petprotech.bd.Entidad;

public class EntidadAsociacionPrueba extends Entidad {

    private final String nombre;
    private final int random;
    private final int idForanea;

    public EntidadAsociacionPrueba(long id, String nombre, int random, int idForanea) {
        super(id);
        this.nombre = nombre;
        this.random = random;
        this.idForanea = idForanea;
    }

    public EntidadAsociacionPrueba(long id, String nombre, int random) {
        this(id, nombre, random, 0);
    }

    public EntidadAsociacionPrueba(String nombre, int random, int idForanea) {
        this.nombre = nombre;
        this.random = random;
        this.idForanea = idForanea;
    }

    public EntidadAsociacionPrueba(String nombre, int random) {
        this(nombre, random, 0);
    }

    public String getNombre() {
        return nombre;
    }

    public int getRandom() {
        return random;
    }

    public int getIdForanea() {
        return idForanea;
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
