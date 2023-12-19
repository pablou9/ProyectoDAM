package es.ifp.petprotech.info.model;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Horario {

    private final HorarioDia[] horario;
    private final HorarioDia festivos;

    public Horario(Builder builder) {
        horario = builder.horario;
        festivos = builder.festivos;
    }

    public HorarioDia[] getHorario() {
        return horario;
    }
    public HorarioDia getFestivos() {
        return festivos;
    }

    public static Builder nuevo() {
        return new Builder();
    }

    /**
     * Crea un nuevo horario con el mismo horario para cada dia de la semana, excepto los dias de
     * descanso
     * @param horarioFijo El horario que será asignado a cada dia de la semana, excepto los de
     *                    descanso
     * @param diasDescanso Los días de la semana a los que no se le asignará un horario
     * @return Una nueva instancia de Builder
     */
    public static Builder nuevo(HorarioDia horarioFijo, int... diasDescanso) {
        return new Builder();
    }

    public static final class Builder {
        private HorarioDia[] horario = new HorarioDia[7];
        private HorarioDia festivos;

        private Builder() {}

        private Builder(HorarioDia horarioFijo, int... diasDescanso) {
            horario = IntStream.range(0, 8)
                .mapToObj(ind -> Arrays.stream(diasDescanso).anyMatch(descanso -> descanso == ind) ? null : horarioFijo)
                .toArray(HorarioDia[]::new);
        }

        public Builder lunes(HorarioDia lunes) {
            horario[0] = lunes;
            return this;
        }

        public Builder martes(HorarioDia martes) {
            horario[1] = martes;
            return this;
        }

        public Builder miercoles(HorarioDia miercoles) {
            horario[2] = miercoles;
            return this;
        }

        public Builder jueves(HorarioDia jueves) {
            horario[3] = jueves;
            return this;
        }

        public Builder viernes(HorarioDia viernes) {
            horario[4] = viernes;
            return this;
        }

        public Builder sabado(HorarioDia sabado) {
            horario[5] = sabado;
            return this;
        }

        public Builder domingo(HorarioDia domingo) {
            horario[6] = domingo;
            return this;
        }

        public Builder festivos(HorarioDia festivos) {
            this.festivos = festivos;
            return this;
        }

        public Horario build() {
            return new Horario(this);
        }
    }
}
