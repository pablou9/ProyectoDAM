package es.ifp.petprotech.info.model;

import java.time.LocalTime;

public class HorarioDia {

    private final LocalTime comienza;
    private final LocalTime descanso;
    private final LocalTime termina;

    public HorarioDia(LocalTime comienza, LocalTime termina) {
        this(comienza, termina, null);
    }

    public HorarioDia(LocalTime comienza, LocalTime termina, LocalTime descanso) {
        this.comienza = comienza;
        this.descanso = descanso;
        this.termina = termina;
    }

    public LocalTime getComienza() {
        return comienza;
    }
    public LocalTime getDescanso() {
        return descanso;
    }
    public LocalTime getTermina() {
        return termina;
    }
}
