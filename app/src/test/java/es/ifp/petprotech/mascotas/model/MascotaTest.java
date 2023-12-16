package es.ifp.petprotech.mascotas.model;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class MascotaTest {

    private static final int LONGITUD_CHIP = 15;
    private static final String CHIP = "C".repeat(LONGITUD_CHIP);

    @Test
    public void crearUnaMascotaConParametrosCorrectos() {
        LocalDateTime now = LocalDateTime.now();
        Mascota mascota = builderValido(now).build();

        Assert.assertEquals(mascota.getNombre(), "nombre");
        Assert.assertEquals(mascota.getFechaNacimiento(), now);
        Assert.assertEquals(mascota.getFamilia(), "familia");
        Assert.assertEquals(mascota.getEspecie(), "especie");
        Assert.assertEquals(mascota.getRaza(), "raza");
        Assert.assertEquals(mascota.getNumeroChip(), CHIP);
    }

    /** @noinspection unchecked*/
    @Test
    public void crearMascotaSinParametrosObligatorios_lanzaExcepcion() {

        Consumer<Mascota.Builder> assertion = builder ->
                Assert.assertThrows(IllegalArgumentException.class, builder::build);

        mockBuilding(assertion,
                //sin nombre
                () -> Mascota.nuevaMascota().especie("e").familia("f").fechaNacimiento(LocalDateTime.now()).chip(CHIP),
                builder -> builder.nombre(null),
                builder -> builder.nombre(""),
                // sin familia
                builder -> Mascota.nuevaMascota().nombre("n").especie("e").chip(CHIP).fechaNacimiento(LocalDateTime.now()),
                builder -> builder.familia(null),
                builder -> builder.familia(""),
                // sin especie
                builder -> Mascota.nuevaMascota().nombre("n").familia("f").chip(CHIP).fechaNacimiento(LocalDateTime.now()),
                builder -> builder.especie(null),
                builder -> builder.especie(""),
                // sin fechaNacimiento
                builder -> Mascota.nuevaMascota().nombre("n").especie("e").familia("f").chip(CHIP),
                builder -> builder.fechaNacimiento(null),
                // sin chip
                builder -> Mascota.nuevaMascota().especie("e").familia("f").fechaNacimiento(LocalDateTime.now()),
                builder -> builder.chip(null),
                builder -> builder.chip(""));
    }

    @Test
    public void crearMascotaConChipInvalido_lanzaExcepcion() {
        Assert.assertThrows(IllegalArgumentException.class,
                () -> builderValido().fechaNacimiento(LocalDateTime.now()).build());
    }

    private void mockBuilding(
            Consumer<Mascota.Builder> assertion,
            Supplier<Mascota.Builder> primerPaso,
            Function<Mascota.Builder, Mascota.Builder>... pasos) {

        Mascota.Builder builder = primerPaso.get();
        assertion.accept(builder);

        for (Function<Mascota.Builder,Mascota.Builder> paso : pasos) {
            builder = paso.apply(builder);
            assertion.accept(builder);
        }
    }

    private Mascota.Builder builderValido() {
        return builderValido(LocalDateTime.now());
    }

    private Mascota.Builder builderValido(LocalDateTime now) {
        return Mascota
            .nuevaMascota()
            .nombre("nombre")
            .fechaNacimiento(now)
            .familia("familia")
            .especie("especie")
            .raza("raza")
            .chip(CHIP);
    }
}
