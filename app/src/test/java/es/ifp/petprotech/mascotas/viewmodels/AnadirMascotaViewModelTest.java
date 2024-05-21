package es.ifp.petprotech.mascotas.viewmodels;

import static org.mockito.Mockito.times;
import static es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel.CamposInfoMascota.CHIP;
import static es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel.CamposInfoMascota.FECHA_NACIMIENTO;
import static es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel.CamposInfoMascota.NOMBRE;
import static es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel.CamposInfoMascota.RAZA_ESPECIE;

import android.content.SharedPreferences;
import android.net.Uri;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import es.ifp.petprotech.R;
import es.ifp.petprotech.app.datos.SharedPreferencesRepositorio;
import es.ifp.petprotech.app.datos.formulario.ValoresFormulario;
import es.ifp.petprotech.bd.MockRepositorio;
import es.ifp.petprotech.mascotas.model.Mascota;

@RunWith(MockitoJUnitRunner.class)
public class AnadirMascotaViewModelTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    private AnadirMascotaViewModel viewModel;

    private SharedPreferences.Editor editor;

    @Before
    public void init() {
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        editor = Mockito.mock(SharedPreferences.Editor.class);

        Mockito.when(sharedPreferences.edit()).thenReturn(editor);

        viewModel = new AnadirMascotaViewModel(
            new MockRepositorio<>(),
                SharedPreferencesRepositorio.instancia(sharedPreferences));
    }

    @Test
    public void anadeUnaMascotaAlRepositorioAlInvocarAnadirMascota() {
        List<Mascota> antes = viewModel.todasLasMascotas();

        Assert.assertTrue(antes.isEmpty());

        viewModel.setTipoMascota(AnadirMascotaViewModel.TipoMascota.PERRO);
        viewModel.crearMascota(new ValoresFormulario(Map.of(
                NOMBRE.nombre(), "Mascota",
                FECHA_NACIMIENTO.nombre(), "25/04/2024",
                RAZA_ESPECIE.nombre(), "Mutt",
                CHIP.nombre(), "C".repeat(Mascota.DIGITOS_NUMERO_CHIP))
        ));

        List<Mascota> mascotas = viewModel.todasLasMascotas();
        Assert.assertEquals(1, mascotas.size());

        Mascota mascota = mascotas.get(0);
        Assert.assertEquals("Mascota", mascota.getNombre());
        Assert.assertEquals("Perro", mascota.getEspecie());
        Assert.assertEquals("Mutt", mascota.getRaza());
    }

    @Test
    public void produceErroresAlValidarCamposInvalidos() {
       Map<String,Integer> errores = viewModel.validarInput(new ValoresFormulario(
            Map.of(
                NOMBRE.nombre(), "",
                FECHA_NACIMIENTO.nombre(), "25/04/2026",
                RAZA_ESPECIE.nombre(), "",
                CHIP.nombre(), "C".repeat(Mascota.DIGITOS_NUMERO_CHIP - 1)))).getErrores();

        Assert.assertEquals(R.string.error_campo_obligatorio, (int) Objects.requireNonNull(errores.get(NOMBRE.nombre())));
        Assert.assertEquals(R.string.error_nacimiento_futuro, (int) Objects.requireNonNull(errores.get(FECHA_NACIMIENTO.nombre())));
        Assert.assertEquals(R.string.error_campo_obligatorio, (int) Objects.requireNonNull(errores.get(RAZA_ESPECIE.nombre())));
        Assert.assertEquals(R.string.error_chip_invalido, (int) Objects.requireNonNull(errores.get(CHIP.nombre())));
    }

    @Test
    public void guardaElUriDeLaFotoDeLaMascotaEnLasSharedPreferences() {
        viewModel.setFotoMascota(Mockito.mock(Uri.class));
        Mockito.verify(editor, times(1)).apply();
    }
}
