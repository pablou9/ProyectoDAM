package es.ifp.petprotech.bd;

import org.junit.Assert;
import org.junit.Test;

public class EntidadTest {

    private static class EntidadImplPrueba extends Entidad {}

    @Test
    public void asignarUnaIdMenorQueUno_lanzaExcepcion() {
        Entidad entidad = new EntidadImplPrueba();
        Assert.assertThrows(IllegalArgumentException.class, () -> entidad.setId(0));
    }

    @Test
    public void asignarUnaIdAUnaEntidadConIdPreviamienteAsignada_lanzaExcepcion() {
        Entidad entidad = new EntidadImplPrueba();
        entidad.setId(1);

        Assert.assertThrows(IllegalArgumentException.class, () -> entidad.setId(2));
    }
}
