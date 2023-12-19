package es.ifp.petprotech.veterinarios.model;

import es.ifp.petprotech.profesionales.model.Profesional;

public class Veterinario extends Profesional {

    private final String numeroColegiado;

    public Veterinario(Builder builder) {
        super(builder);
        this.numeroColegiado = builder.numeroColegiado;
    }

    public String getNumeroColegiado() {
        return numeroColegiado;
    }

    private static class Builder extends Profesional.Builder<Veterinario> {
        private String numeroColegiado;

        public Builder numeroColegiado(String numeroColegiado) {
            this.numeroColegiado = numeroColegiado;
            return this;
        }

        @Override
        public Veterinario build() {
            return new Veterinario(this);
        }
    }
}
