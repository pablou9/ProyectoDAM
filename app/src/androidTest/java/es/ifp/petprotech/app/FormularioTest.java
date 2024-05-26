package es.ifp.petprotech.app;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.android.material.textfield.TextInputLayout;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Map;

import es.ifp.petprotech.R;
import es.ifp.petprotech.app.datos.CampoFormulario;
import es.ifp.petprotech.app.datos.formulario.Formulario;
import es.ifp.petprotech.mascotas.views.AnadirEntidadesActivity;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FormularioTest {

    private static final String TAG = "FormularioTest";

    @Rule
    public ActivityScenarioRule<AnadirEntidadesActivity> rule =
            new ActivityScenarioRule<>(AnadirEntidadesActivity.class);

    private Formulario formulario;
    private LinearLayout layout;


    @Before
    public void init() {
        rule.getScenario().onActivity(activity -> {
            formulario = new Formulario();
            layout = new LinearLayout(activity);
        });
    }

    @Test
    public void anadeUnTextInputLayoutPorCadaCampo() {
        anadirCampos(layout, "Campo 1", "Campo 2", "Campo 3");
        Assert.assertEquals(3, layout.getChildCount());
    }

    @Test
    public void anadeElNombreDelCampoComoTagEnCadaInput() {
        String[] nombres = new String[]{"Campo 1", "Campo 2", "Campo 3"};
        anadirCampos(layout, nombres);

        for(int i = 0; i < layout.getChildCount(); i++) {
            Assert.assertEquals(nombres[i], layout.getChildAt(i).getTag());
        }
    }

    @Test
    public void anadeLosInputsEnElMismoOrdenQueSeAnadenLosCampos() {
        String[] nombres = new String[]{"Campo 1", "Campo 2", "Campo 3"};
        anadirCampos(layout, nombres);

        for(int i = 0; i < layout.getChildCount(); i++) {
            Assert.assertEquals(nombres[i], layout.getChildAt(i).getTag());
        }
    }

    @Test
    public void lanzaExcepcionSiSeAnadenDosCamposConElMismoNombre() {
        Assert.assertThrows(IllegalArgumentException.class,
                () -> anadirCampos(layout, "Campo 1", "Campo 2", "Campo 1"));
    }

    @Test
    public void deshabilitaCadaCampoAlAsignarHabilitadoComoFalso() {
        formulario.anadirCampos(layout,
                new CampoFormulario("Campo 1"),
                new CampoFormulario("Campo 2"),
                new CampoFormulario("Campo 3", "",
                    new CampoFormulario.AccionCampoFormulario((p) -> null, R.drawable.alerts)));

        formulario.setHabilitado(false);

        assertTodosLosHijosDeshabilitados(layout);
    }

    @Test
    public void cambiaElHintDeUnCampo() {
        formulario.anadirCampos(layout,
            new CampoFormulario("Campo 1", "Hint 1"),
            new CampoFormulario("Campo 2", "Hint 2"),
            new CampoFormulario("Campo 3", "Hint 3"));

        formulario.cambiarHint("Campo 1", "Otro Hint 1");
        formulario.cambiarHint("Campo 2", "Otro Hint 2");
        formulario.cambiarHint("Campo 3", "Otro Hint 3");

        TextInputLayout input1 = (TextInputLayout) layout.getChildAt(0);
        TextInputLayout input2 = (TextInputLayout) layout.getChildAt(1);
        TextInputLayout input3 = (TextInputLayout) layout.getChildAt(2);

        Assert.assertEquals("Otro Hint 1", input1.getHint());
        Assert.assertEquals("Otro Hint 2", input2.getHint());
        Assert.assertEquals("Otro Hint 3", input3.getHint());
    }

    @Test
    public void muestraErrores() {
        anadirCampos(layout, "Campo 1", "Campo 2", "Campo 3");

        Context context = layout.getContext();
        String error2 = context.getString(R.string.error_campo_obligatorio);
        String error3 = context.getString(R.string.error_chip_invalido);

        formulario.mostarErrores(context, Map.of(
            "Campo 2", R.string.error_campo_obligatorio,
            "Campo 3", R.string.error_chip_invalido
        ));

        TextInputLayout input1 = (TextInputLayout) layout.getChildAt(0);
        TextInputLayout input2 = (TextInputLayout) layout.getChildAt(1);
        TextInputLayout input3 = (TextInputLayout) layout.getChildAt(2);

        Assert.assertNull(input1.getError());
        Assert.assertEquals(error2, input2.getError());
        Assert.assertEquals(error3, input3.getError());
    }

    @Test
    public void registraInteraccionDelUsuario() {
        CampoFormulario[] campos = producirCampos("Campo 1", "Campo 2", "Campo 3");

        formulario.anadirCampos(layout, campos);

        // Da focus al primer input y escribe en el mismo
        TextInputLayout primerInput = layout.findViewById(campos[0].getId());
        primerInput.requestFocus();
        primerInput.getEditText().setText("Text");

        // Mueve el focus a otro input
        View segundoInput = layout.findViewById(campos[1].getId());
        segundoInput.requestFocus();

        // Espera por todos los handlers de eventos
        rule.getScenario().onActivity(activity ->
                Assert.assertTrue(formulario.usuarioHaInteractuado()));
    }

    @Test
    public void remueveInteraccionDelUsuarioSiSeVacianTodos() {
        CampoFormulario[] campos = producirCampos("Campo 1", "Campo 2", "Campo 3");

        formulario.anadirCampos(layout, campos);

        // Da focus al primer input y escribe en el mismo
        TextInputLayout primerInput = layout.findViewById(campos[0].getId());
        primerInput.requestFocus();
        primerInput.getEditText().setText("Text");

        // Vacia el primer input y da focus al segundo
        TextInputLayout segundoInput = layout.findViewById(campos[0].getId());
        primerInput.getEditText().setText("");
        segundoInput.requestFocus();

        // Espera por todos los handlers de eventos
        rule.getScenario().onActivity(activity ->
                Assert.assertFalse(formulario.usuarioHaInteractuado()));
    }

    private CampoFormulario[] producirCampos(String... campos) {
        return Arrays
                .stream(campos)
                .map(CampoFormulario::new)
                .toArray(CampoFormulario[]::new);
    }

    private void anadirCampos(LinearLayout layout, String... campos) {
        formulario.anadirCampos(layout, producirCampos(campos));
    }

    private void assertTodosLosHijosDeshabilitados(ViewGroup layout) {
        for(int i = 0; i < layout.getChildCount(); i++) {
            View view = layout.getChildAt(i);

            Log.d(TAG, "iterarTodosLosHijos: " + view);

            if (view instanceof TextInputLayout) {
                Assert.assertFalse(view.isEnabled());
            }
            else if (view instanceof ViewGroup) {
                assertTodosLosHijosDeshabilitados((ViewGroup) view);
            }
            else {
                Assert.assertFalse(view.isClickable());
            }
        }
    }
}
