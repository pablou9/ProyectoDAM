package es.ifp.petprotech.profesionales.model;

public class Profesion {

    private final String nombre;

    public Profesion(String nombre) {
        this.nombre = nombre;
    }

    public static Profesion veterinario() {
        return new Profesion("Veterinario");
    }
}
