package es.ifp.petprotech.bd.util;

import java.util.Objects;

import es.ifp.petprotech.bd.Entidad;

public class EntidadDePrueba extends Entidad {

    private String nombre;
    private int edad;
    private boolean prueba;

    public EntidadDePrueba(long id, String nombre, int edad, boolean prueba) {
        super(id);
        this.nombre = nombre;
        this.edad = edad;
        this.prueba = prueba;
    }

    public EntidadDePrueba(String nombre, int edad, boolean prueba) {
        this.nombre = nombre;
        this.edad = edad;
        this.prueba = prueba;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public boolean isPrueba() {
        return prueba;
    }

    public void setPrueba(boolean prueba) {
        this.prueba = prueba;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntidadDePrueba that = (EntidadDePrueba) o;
        return edad == that.edad && prueba == that.prueba && Objects.equals(nombre, that.nombre);
    }

    @Override
    public String toString() {
        return "EntidadDePrueba{" +
            "id=" + getId() +
            ", nombre='" + nombre + '\'' +
            ", edad=" + edad +
            ", prueba=" + prueba +
            '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, edad, prueba);
    }
}
