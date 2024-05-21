package es.ifp.petprotech.veterinarios.model;

import java.util.ArrayList;
import java.util.List;

import es.ifp.petprotech.bd.Entidad;
import es.ifp.petprotech.centros.model.CentroProfesional;
import es.ifp.petprotech.mascotas.model.Mascota;

public class Veterinario extends Entidad {

    private final String nombre;
    private final CentroProfesional centro;
    private final String especialidad;
    private final List<Mascota> mascotas = new ArrayList<>();

    public Veterinario(Builder builder) {
        validarVeterinario(builder);

        this.nombre = builder.nombre;
        this.centro = builder.centro;
        this.especialidad = builder.especialidad;
    }

    private void validarVeterinario(Builder builder) {
        if (isEmpty(builder.nombre))
            throw new IllegalArgumentException("Debes introducir un nombre de veterinario");
    }

    private boolean isEmpty(String s) {
        return s == null || s.isBlank();
    }

    public String getNombre() {
        return nombre;
    }

    public CentroProfesional getCentro() {
        return centro;
    }

    public static Veterinario.Builder nuevoVeterinario() {
        return new Veterinario.Builder();
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setMascotas(List<Mascota> mascotas) {
       this.mascotas.addAll(mascotas);
    }

    public void anadirMascota(Mascota mascota) {
        if (mascotas.contains(mascota))
            return;

        mascotas.add(mascota);
    }

    public void quitarMascota(Mascota mascota) {
        mascotas.remove(mascota);
    }

    public List<Mascota> getMascotas() {
        return mascotas;
    }

    public static class Builder {
        private String nombre;
        private String especialidad;
        private CentroProfesional centro;

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }
        public Builder especialidad(String especialidad) {
            this.especialidad = especialidad;
            return this;
        }
        public Builder centro(CentroProfesional centro) {
            this.centro = centro;
            return this;
        }

        public Veterinario build() {
            return new Veterinario(this);
        }
    }
}
