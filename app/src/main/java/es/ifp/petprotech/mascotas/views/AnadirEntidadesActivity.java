package es.ifp.petprotech.mascotas.views;

import static es.ifp.petprotech.app.viewmodels.NavegacionAnadirEntidadesViewModel.Pantalla;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import es.ifp.petprotech.R;
import es.ifp.petprotech.app.viewmodels.NavegacionAnadirEntidadesViewModel;
import es.ifp.petprotech.app.views.AnadirEntidadFragment;
import es.ifp.petprotech.veterinarios.views.AnadirVeterinarioFragment;

public class AnadirEntidadesActivity extends AppCompatActivity {
    private static final String TAG = "AnadirEntidadesActivity";

    private NavegacionAnadirEntidadesViewModel viewModel;

    private Button atras;
    private Button siguiente;

    private Pantalla pantallaActual;

    @Override
    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_entidades);

        atras = findViewById(R.id.atras);
        siguiente = findViewById(R.id.siguiente);

        siguiente.setOnClickListener(e -> crearEntidad());
        atras.setOnClickListener(e -> viewModel.atras());

        viewModel = new ViewModelProvider(this).get(NavegacionAnadirEntidadesViewModel.class);

        List<Pantalla> pantallas = extraerPantallas();
        viewModel.anadirPantallas(pantallas);

        Log.d(TAG, "onCreate: PANTALLAS: " + pantallas);

        viewModel.getFinalizado().observe(this, this::handleFinalizado);
        viewModel.getUsuarioHaInteractuado().observe(this, this::handleInteraccion);
        viewModel.getPantallaActual().observe(this, this::cambiarPantalla);
    }

    private List<Pantalla> extraerPantallas() {
        Bundle paquete = getIntent().getBundleExtra("data");

        return paquete == null || paquete.isEmpty()
                ? Pantalla.todas()
                : paquete.keySet()
                .stream()
                .map(k -> (Pantalla) paquete.getSerializable(k))
                .collect(Collectors.toList());
    }

    private void cambiarPantalla(Pantalla pantalla) {
        pantallaActual = pantalla;

        cambiarFragmento(getFragment(pantalla), pantalla.getTag());

        if (viewModel.esPrimeraPantalla(pantalla)) {
            atras.setText(R.string.cancelar);
        }
        else atras.setText(R.string.atras);

        if (viewModel.esUltimaPantalla(pantalla)) {
            siguiente.setText(R.string.finalizar);
        }
        else if (pantalla.sePuedeSaltar()) {
            siguiente.setText(R.string.mas_tarde);
        }
        else siguiente.setText(R.string.siguiente);
    }

    private void crearEntidad() {
        AnadirEntidadFragment fragmentoActivo = Objects.requireNonNull(
            (AnadirEntidadFragment) getSupportFragmentManager()
                    .findFragmentByTag(pantallaActual.getTag()));

        fragmentoActivo.crearEntidad();
    }

    private void cambiarFragmento(@NonNull Fragment fragmento, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container_view, fragmento, tag)
                .commit();
    }

    private void handleFinalizado(boolean finalizado) {
        if (finalizado)
            finish();
    }

    private void handleInteraccion(boolean usuarioHaInteractuado) {
        if (usuarioHaInteractuado && pantallaActual.sePuedeSaltar())
            siguiente.setText(R.string.siguiente);
    }

    private Fragment getFragment(Pantalla pantalla) {
        Log.d(TAG, "getFragment: for pantalla" + pantalla);
        return switch (pantalla) {
            case ANADIR_MASCOTA -> new AnadirMascotaFragment();
            case ANADIR_FOTO_MASCOTA -> new AnadirFotoMascotaFragment();
            case ANADIR_VETERINARIO -> new AnadirVeterinarioFragment();
        };
    }
}