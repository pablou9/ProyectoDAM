package es.ifp.petprotech.veterinarios.viewmodels;

import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.DIRECCION_CENTRO;
import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.EMAIL_CENTRO;
import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.NOMBRE;
import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.NOMBRE_CENTRO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import es.ifp.petprotech.R;
import es.ifp.petprotech.app.datos.formulario.ValoresFormulario;
import es.ifp.petprotech.bd.MockRepositorio;
import es.ifp.petprotech.centros.model.CentroProfesional;
import es.ifp.petprotech.mascotas.model.Mascota;

public class AnadirVeterinarioViewModelTest {

    private AnadirVeterinarioViewModel viewModel;

    @Before
    public void init() {
        viewModel = new AnadirVeterinarioViewModel(new MockRepositorio<>());
    }

    @Test
    public void anadeUnaCentroVeterinarioAlRepositorioAlInvocarCrearCentroVeterinario() {
        List<CentroProfesional> antes = viewModel.todosLosCentrosVeterinarios();

        Assert.assertTrue(antes.isEmpty());

        Mascota mascota = Mascota.nuevaMascota().nombre("Luna").especie("Perro").build();
        mascota.setId(1L);

        viewModel.crearCentroVeterinario(new ValoresFormulario(Map.of(
                NOMBRE_CENTRO.nombre(), "CentroProfesional",
                DIRECCION_CENTRO.nombre(), "Direccion",
                EMAIL_CENTRO.nombre(), ".com")),
                new HashMap<>(),
                mascota
        );

        List<CentroProfesional> clinicas = viewModel.todosLosCentrosVeterinarios();
        Assert.assertEquals(1, clinicas.size());

        CentroProfesional clinica = clinicas.get(0);
        Assert.assertEquals("CentroProfesional", clinica.getNombre());
        Assert.assertEquals("Direccion", clinica.getDireccion());
        Assert.assertEquals(".com", mascota.getRaza());
    }

    @Test
    public void produceErroresAlValidarCamposInvalidos() {
        Map<String,Integer> errores = viewModel.validarInput(new ValoresFormulario(
            Map.of(NOMBRE.nombre(), ""))).getErrores();

        Assert.assertEquals(R.string.error_campo_obligatorio,
                (int) Objects.requireNonNull(errores.get(NOMBRE.nombre())));
    }
}
