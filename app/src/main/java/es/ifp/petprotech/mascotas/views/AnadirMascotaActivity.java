package es.ifp.petprotech.mascotas.views;

import static es.ifp.petprotech.app.model.FabricaViewModel.ANADIR_MASCOTA;
import static es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel.Accion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.function.Supplier;

import es.ifp.petprotech.R;
import es.ifp.petprotech.app.views.HomeActivity;
import es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel;
import es.ifp.petprotech.veterinarios.views.AnadirVeterinarioFragment;

public class AnadirMascotaActivity extends AppCompatActivity {

    @Override
    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_mascota);

        AnadirMascotaViewModel viewModel =
                new ViewModelProvider(this, ANADIR_MASCOTA.getFabrica()).get(AnadirMascotaViewModel.class);

        viewModel.getAccionEnProceso().observe(this, this::cambioDeAccion);
    }

    private void cambioDeAccion(Accion accion) {
        switch (accion) {
            case ANADIR_INFO -> cambiarFragmento(InfoMascotaFragment::new, accion.getTag());
            case ANADIR_FOTO -> cambiarFragmento(AnadirFotoMascotaFragment::new, accion.getTag());
            case ANADIR_VETERINARIO -> cambiarFragmento(AnadirVeterinarioFragment::new, accion.getTag());
            case TERMINAR -> irAHome();
        }
    }

    private void cambiarFragmento(@NonNull Supplier<Fragment> fragmento, String tag) {
        getSupportFragmentManager()
            .beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.fragment_container_view, fragmento.get(), tag)
            .commit();
    }

    private void irAHome() {
        Intent intent = new Intent(AnadirMascotaActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}