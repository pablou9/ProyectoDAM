package es.ifp.petprotech.info.model;

public class Horario {

    private final HorarioDia lunes;
    private final HorarioDia martes;
    private final HorarioDia miercoles;
    private final HorarioDia jueves;
    private final HorarioDia viernes;
    private final HorarioDia sabado;
    private final HorarioDia domingo;

    public Horario(Builder builder) {
        miercoles = builder.miercoles;
        sabado = builder.sabado;
        martes = builder.martes;
        lunes = builder.lunes;
        domingo = builder.domingo;
        viernes = builder.viernes;
        jueves = builder.jueves;
    }

    public static Builder nuevo() {
        return new Builder();
    }

    public static final class Builder {
        private HorarioDia lunes;
        private HorarioDia martes;
        private HorarioDia miercoles;
        private HorarioDia jueves;
        private HorarioDia viernes;
        private HorarioDia sabado;
        private HorarioDia domingo;

        private Builder() {}

        public Builder lunes(HorarioDia lunes) {
            this.lunes = lunes;
            return this;
        }

        public Builder martes(HorarioDia martes) {
            this.martes = martes;
            return this;
        }

        public Builder miercoles(HorarioDia miercoles) {
            this.miercoles = miercoles;
            return this;
        }

        public Builder jueves(HorarioDia jueves) {
            this.jueves = jueves;
            return this;
        }

        public Builder viernes(HorarioDia viernes) {
            this.viernes = viernes;
            return this;
        }

        public Builder sabado(HorarioDia sabado) {
            this.sabado = sabado;
            return this;
        }

        public Builder domingo(HorarioDia domingo) {
            this.domingo = domingo;
            return this;
        }

        public Horario build() {
            return new Horario(this);
        }
    }
}
