package es.ifp.petprotech.bd;

import org.junit.Assert;
import org.junit.Test;

public class EntidadTest {

    @Test
    public void crearUnaEntidadConIdMenorQueUno_lanzaExcepcion() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new PruebaEntidad(0));
    }

    @Test
    public void asignarUnaIdMenorQueUno_lanzaExcepcion() {
        PruebaEntidad entidad = new PruebaEntidad();
        Assert.assertThrows(IllegalArgumentException.class, () -> entidad.setId(0));
    }

    @Test
    public void asignarUnaIdAUnaEntidadConIdPreviamienteAsignada_lanzaExcepcion() {
        PruebaEntidad entidad = new PruebaEntidad(1);
        Assert.assertThrows(IllegalStateException.class, () -> entidad.setId(2));
    }

    private static final class PruebaEntidad extends Entidad {
        public PruebaEntidad() {}

        public PruebaEntidad(long id) {
            super(id);
        }
    }
}
