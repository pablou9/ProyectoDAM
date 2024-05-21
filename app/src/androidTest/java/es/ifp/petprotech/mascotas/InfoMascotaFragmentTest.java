package es.ifp.petprotech.mascotas;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;

import static es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel.CamposInfoMascota.*;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.ifp.petprotech.R;
import es.ifp.petprotech.mascotas.views.AnadirMascotaActivity;
import es.ifp.petprotech.mascotas.views.InfoMascotaFragment;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class InfoMascotaFragmentTest {

    @Rule
    public ActivityScenarioRule<AnadirMascotaActivity> rule =
        new ActivityScenarioRule<>(AnadirMascotaActivity.class);

    private FragmentScenario<InfoMascotaFragment> scenario;

    @Before
    public void init() {
        scenario = FragmentScenario.launchInContainer(
                InfoMascotaFragment.class, null, androidx.appcompat.R.style.Theme_AppCompat);
    }

    @After
    public void cleanUp() {
        if (scenario != null)
            scenario.close();
    }

    @Test
    public void elTextoDelBotonSiguienteMuestraSiguienteInicialmente() {
        onView(withId(R.id.siguiente)).check(matches(withText(containsString("Siguiente"))));
    }

    @Test
    public void elFormularioDeMascotaSeEncuentranDeshabilitadoAlLanzarLaActividad() {
        scenario.onFragment(fragmento ->
                Assert.assertFalse(fragmento.getFormulario().estaHabilitado()));
    }

    @Test
    public void habilitaLosCamposDeMascotaAlElegirElTipoDeMascota() {
        TiposMascota form = new TiposMascota();

        for (int tipoMascotaId : form.tipos) {
            onView(withId(tipoMascotaId)).perform(click());

            scenario.onFragment(fragmento ->
                Assert.assertTrue(fragmento.getFormulario().estaHabilitado()));

            scenario.recreate();
        }
    }

    private static final String TAG = "InfoMascotaFragmentTest";

    @Test
    public void cambiaElTextoDelBotonSiguienteAlElegirElTipoDeMascota() {
        TiposMascota form = new TiposMascota();

        for (int tipoMascotaId : form.tipos) {
            onView(withId(tipoMascotaId)).perform(click());

            scenario.onFragment(fragmento -> {
                View view = Objects.requireNonNull(fragmento.getView());
                Button siguiente = view.findViewById(R.id.siguiente);
                String texto = siguiente.getText().toString();
                Assert.assertTrue(texto.contains("Siguiente"));
            });

            Log.d(TAG, "cambiaElTextoDelBotonSiguienteAlElegirElTipoDeMascota: recreando...");
            scenario.recreate();
        }
    }

    @Test
    public void asignaElHintDeRazaOEspecieARazaAlHacerClickEnGatoOPerro() {
        onView(withId(R.id.perro_icon)).perform(click());
        onView(CoreMatchers
            .allOf(
                isDescendantOfA(withTagValue(is(RAZA_ESPECIE))),
                withClassName(endsWith("EditText")))).check(matches(withHint(R.string.raza)));

        onView(withId(R.id.gato_icon)).perform(click());
        onView(CoreMatchers
            .allOf(
                isDescendantOfA(withTagValue(is(RAZA_ESPECIE))),
                withClassName(endsWith("EditText")))).check(matches(withHint(R.string.raza)));
    }

    @Test
    public void asignaElHintDeRazaOEspecieAEspecieAlHacerClickEnTiposDistintosDeGatoOPerro() {
        onView(withId(R.id.ave_icon)).perform(click());
        onView(CoreMatchers
            .allOf(
                isDescendantOfA(withTagValue(is(RAZA_ESPECIE))),
                withClassName(endsWith("EditText")))).check(matches(withHint(R.string.especie)));

        onView(withId(R.id.roedor_icon)).perform(click());
        onView(CoreMatchers
            .allOf(
                isDescendantOfA(withTagValue(is(RAZA_ESPECIE))),
                withClassName(endsWith("EditText")))).check(matches(withHint(R.string.especie)));

        onView(withId(R.id.reptil_icon)).perform(click());
        onView(CoreMatchers
            .allOf(
                isDescendantOfA(withTagValue(is(RAZA_ESPECIE))),
                withClassName(endsWith("EditText")))).check(matches(withHint(R.string.especie)));

        onView(withId(R.id.otro_icon)).perform(click());
        onView(CoreMatchers
            .allOf(
                isDescendantOfA(withTagValue(is(RAZA_ESPECIE))),
                withClassName(endsWith("EditText")))).check(matches(withHint(R.string.especie)));
    }

    private static final class TiposMascota {
        private final List<Integer> tipos = new ArrayList<>();

        public TiposMascota() {
            tipos.add(R.id.perro_icon);
            tipos.add(R.id.gato_icon);
            tipos.add(R.id.ave_icon);
            tipos.add(R.id.roedor_icon);
            tipos.add(R.id.reptil_icon);
            tipos.add(R.id.otro_icon);

        }
    }

}
