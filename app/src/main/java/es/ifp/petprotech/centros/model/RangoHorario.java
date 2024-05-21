package es.ifp.petprotech.centros.model;

import java.time.LocalTime;

import es.ifp.petprotech.bd.Entidad;

public class RangoHorario extends Entidad {

    private final LocalTime apertura;
    private final LocalTime cierre;

    public RangoHorario(LocalTime apertura, LocalTime cierre) {
        this.apertura = apertura;
        this.cierre = cierre;
    }

    public LocalTime getApertura() {
        return apertura;
    }

    public LocalTime getCierre() {
        return cierre;
    }
}
