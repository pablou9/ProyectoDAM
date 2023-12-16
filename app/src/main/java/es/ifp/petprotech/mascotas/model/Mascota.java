package es.ifp.petprotech.mascotas.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import es.ifp.petprotech.bd.Entidad;

public class Mascota extends Entidad {

    public static final int DIGITOS_NUMERO_CHIP = 15;

    private final String nombre;
    private final String especie;
    private final String familia;
    private final String raza;
    private final LocalDateTime fechaNacimiento;
    private final String numeroChip;

    public static Builder nuevaMascota() {
        return new Builder();
    }

    private Mascota(Builder builder) {
        validarMascota(builder);

        nombre = builder.nombre;
        especie = builder.especie;
        familia = builder.familia;
        raza = builder.raza;
        fechaNacimiento = builder.fechaNacimiento;
        numeroChip = builder.numeroChip;
    }

    private void validarMascota(Builder builder) {
        if (isEmpty(builder.nombre))
            throw new IllegalArgumentException("Debes introducir un nombre de mascota");

        if (isEmpty(builder.especie))
            throw new IllegalArgumentException("Debes introducir una especie");

        if (isEmpty(builder.familia))
            throw new IllegalArgumentException("Debes introducir una familia");

        if (builder.fechaNacimiento == null)
            throw new IllegalArgumentException("Debes introducir una edad y no puede ser menor a 0");

        if (builder.fechaNacimiento.isAfter(LocalDateTime.now()))
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser en el futuro");

        if (isEmpty(builder.numeroChip))
            throw new IllegalArgumentException("Debes introducir un número de chip");

        if (builder.numeroChip.length() != DIGITOS_NUMERO_CHIP)
            throw new IllegalArgumentException("El número de chip debe contener 15 dígitos");
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

    public String getFamilia() {
        return familia;
    }

    public String getRaza() {
        return raza;
    }

    public LocalDateTime getFechaNacimiento() {
        return fechaNacimiento;
    }

    public long getEdad() {
        return ChronoUnit.YEARS.between(fechaNacimiento, LocalDateTime.now());
    }

    public String getNumeroChip() {
        return numeroChip;
    }

    public static class Builder {
        private String nombre;
        private String especie;
        private String familia;
        private String raza;
        private LocalDateTime fechaNacimiento;
        private String numeroChip;

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }
        public Builder especie(String especie) {
            this.especie = especie;
            return this;
        }
        public Builder familia(String familia) {
            this.familia = familia;
            return this;
        }
        public Builder raza(String raza) {
            this.raza = raza;
            return this;
        }
        public Builder fechaNacimiento(LocalDateTime fechaNacimiento) {
            this.fechaNacimiento = fechaNacimiento;
            return this;
        }
        public Builder chip(String numeroChip) {
            this.numeroChip = numeroChip;
            return this;
        }
        public Mascota build() {
            return new Mascota(this);
        }
    }
}
