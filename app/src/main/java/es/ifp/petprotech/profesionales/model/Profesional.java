package es.ifp.petprotech.profesionales.model;

import es.ifp.petprotech.bd.Entidad;
import es.ifp.petprotech.info.model.Horario;
import es.ifp.petprotech.info.model.InfoContacto;

public abstract class Profesional extends Entidad {

    private final String nombre;
    private final String apellidos;
    private final InfoContacto infoContato;
    private final Profesion[] profesiones;
    private final Horario horario;

    protected Profesional(Builder<?> builder) {
        nombre = builder.nombre;
        apellidos = builder.apellidos;
        horario = builder.horario;
        infoContato = builder.infoContato;
        profesiones = builder.profesiones;
    }

    public static Builder<ProfesionalSimple> nuevo() {
        return new ProfesionalSimple.Builder();
    }

    private static class ProfesionalSimple extends Profesional{
        protected ProfesionalSimple(Builder builder) {
            super(builder);
        }

        private static final class Builder extends Profesional.Builder<ProfesionalSimple> {
            @Override
            public ProfesionalSimple build() {
                return new ProfesionalSimple(this);
            }
        }
    }

    public static abstract class Builder<T extends Profesional> {
        private String nombre;
        private String apellidos;
        private InfoContacto infoContato;
        private Profesion[] profesiones;
        private Horario horario;

        protected Builder() {}

        public Builder<T> nombreYApellidos(String nombre, String apellidos) {
            this.nombre = nombre;
            this.apellidos = apellidos;
            return this;
        }

        public Builder<T> infoContato(InfoContacto infoContato) {
            this.infoContato = infoContato;
            return this;
        }

        public Builder<T> profesiones(Profesion... profesiones) {
            this.profesiones = profesiones;
            return this;
        }

        public Builder<T> horario(Horario horario) {
            this.horario = horario;
            return this;
        }

        public abstract T build();
    }
}
