package es.ifp.petprotech.mascotas.model;

import static es.ifp.petprotech.app.util.StringUtils.notNull;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import es.ifp.petprotech.bd.Entidad;
import es.ifp.petprotech.veterinarios.model.Veterinario;

public class Mascota extends Entidad {

    public static final int DIGITOS_NUMERO_CHIP = 15;

    private final String nombre;
    private final String especie;
    private final String raza;
    private final LocalDate fechaNacimiento;
    private final String numeroChip;
    private final Veterinario veterinario;

    private Uri foto;

    private Mascota(Builder builder) {
        validarMascota(builder);

        nombre = builder.nombre;
        especie = builder.especie;
        raza = builder.raza;
        fechaNacimiento = builder.fechaNacimiento;
        numeroChip = builder.numeroChip;
        veterinario = builder.veterinario;
    }

    private void validarMascota(Builder builder) {
        if (isEmpty(builder.nombre))
            throw new IllegalArgumentException("Debes introducir un nombre de mascota");

        if (isEmpty(builder.especie))
            throw new IllegalArgumentException("Debes introducir una especie");

        if (builder.fechaNacimiento != null && builder.fechaNacimiento.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser en el futuro");

        String chip = notNull(builder.numeroChip);

        if (!chip.isBlank() && chip.length() != DIGITOS_NUMERO_CHIP)
            throw new IllegalArgumentException("El número de chip debe contener 15 dígitos");
    }

    public Uri getFoto() {
        return foto;
    }

    public void setFoto(Uri foto) {
        this.foto = foto;
    }

    private boolean isEmpty(String s) {
        return s == null || s.isBlank();
    }

    public String getNombre() {
        return nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public String getRaza() {
        return raza;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public long getEdad() {
        return ChronoUnit.YEARS.between(fechaNacimiento, LocalTime.now());
    }

    public String getNumeroChip() {
        return numeroChip;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    @NonNull
    @Override
    public String toString() {
        return "Mascota{" +
            "nombre='" + nombre + '\'' +
            ", especie='" + especie + '\'' +
            ", raza='" + raza + '\'' +
            ", fechaNacimiento=" + fechaNacimiento +
            ", numeroChip='" + numeroChip + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mascota mascota = (Mascota) o;
        return Objects.equals(nombre, mascota.nombre) && Objects.equals(especie, mascota.especie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, especie);
    }

    public static Builder nuevaMascota() {
        return new Builder();
    }

    public static class Builder {
        private String nombre;
        private String especie;
        private String raza;
        private LocalDate fechaNacimiento;
        private String numeroChip;
        private Veterinario veterinario;

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }
        public Builder especie(String especie) {
            this.especie = especie;
            return this;
        }
        public Builder raza(String raza) {
            this.raza = raza;
            return this;
        }
        public Builder fechaNacimiento(LocalDate fechaNacimiento) {
            this.fechaNacimiento = fechaNacimiento;
            return this;
        }
        public Builder chip(String numeroChip) {
            this.numeroChip = numeroChip;
            return this;
        }
        public Builder veterinario(Veterinario veterinario) {
            this.veterinario = veterinario;
            return this;
        }
        public Mascota build() {
            return new Mascota(this);
        }
    }
}
