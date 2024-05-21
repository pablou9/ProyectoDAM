package es.ifp.petprotech.mascotas.model;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class MascotaTest {

    private static final int LONGITUD_CHIP = 15;
    private static final String CHIP = "C".repeat(LONGITUD_CHIP);

    @Test
    public void crearUnaMascotaConParametrosCorrectos() {
        LocalDate now = LocalDate.now();
        Mascota mascota = builderValido(now).build();

        Assert.assertEquals(mascota.getNombre(), "nombre");
        Assert.assertEquals(mascota.getFechaNacimiento(), now);
        Assert.assertEquals(mascota.getEspecie(), "especie");
        Assert.assertEquals(mascota.getRaza(), "raza");
        Assert.assertEquals(mascota.getNumeroChip(), CHIP);
    }

    /** @noinspection unchecked*/
    @Test
    public void crearMascotaSinParametrosObligatorios_lanzaExcepcion() {

        Consumer<Mascota.Builder> assertThrows = builder ->
                Assert.assertThrows(IllegalArgumentException.class, builder::build);

        mockCreacion(assertThrows,
                // sin nombre
                () -> Mascota.nuevaMascota().especie("e").fechaNacimiento(LocalDate.now()).chip(CHIP),
                builder -> builder.nombre(null),
                builder -> builder.nombre(""),
                // sin especie
                builder -> Mascota.nuevaMascota().nombre("n").chip(CHIP).fechaNacimiento(LocalDate.now()),
                builder -> builder.especie(null),
                builder -> builder.especie(""),
                // sin fechaNacimiento
                builder -> Mascota.nuevaMascota().nombre("n").especie("e").chip(CHIP),
                builder -> builder.fechaNacimiento(null),
                // sin chip
                builder -> Mascota.nuevaMascota().especie("e").fechaNacimiento(LocalDate.now()),
                builder -> builder.chip(null),
                builder -> builder.chip(""));
    }

    @Test
    public void crearMascotaConChipInvalido_lanzaExcepcion() {
        Assert.assertThrows(IllegalArgumentException.class,
                /* Chip invalido. El numero de chip debe tener 15 digitos */
                () -> builderValido().chip("123456789").build());
    }

    @Test
    public void crearMascotaConFechaDeNacimientoEnElFuturo_lanzaExcepcion() {
        Assert.assertThrows(IllegalArgumentException.class,
            () -> builderValido().fechaNacimiento(LocalDate.now().plusDays(1)).build());
    }

    @SafeVarargs
    private void mockCreacion(
            Consumer<Mascota.Builder> assertThrows,
            Supplier<Mascota.Builder> primerPaso,
            Function<Mascota.Builder, Mascota.Builder>... pasos)
    {
        Mascota.Builder builder = primerPaso.get();
        assertThrows.accept(builder);

        for (Function<Mascota.Builder,Mascota.Builder> paso : pasos) {
            builder = paso.apply(builder);
            assertThrows.accept(builder);
        }
    }

    private Mascota.Builder builderValido() {
        return builderValido(LocalDate.now());
    }

    private Mascota.Builder builderValido(LocalDate now) {
        return Mascota
            .nuevaMascota()
            .nombre("nombre")
            .fechaNacimiento(now)
            .especie("especie")
            .raza("raza")
            .chip(CHIP);
    }
}
