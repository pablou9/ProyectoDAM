
package es.ifp.petprotech.mascotas.views;

import static es.ifp.petprotech.app.model.FabricaViewModel.MASCOTA;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import es.ifp.petprotech.R;
import es.ifp.petprotech.app.views.BaseActivity;
import es.ifp.petprotech.centros.model.CentroProfesional;
import es.ifp.petprotech.mascotas.model.Mascota;
import es.ifp.petprotech.mascotas.viewmodels.MascotasViewModel;
import es.ifp.petprotech.veterinarios.model.Veterinario;

public class MascotaActivity extends BaseActivity {

    private ImageView foto;
    private TextView nombre;
    private TextView veterinario;
    private TextView centroVeterinario;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_mascota;
    }
    @Override
    protected boolean mostrarGoBack() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long idMascota = extraerIdMascota();

        foto = findViewById(R.id.foto);
        nombre = findViewById(R.id.nombre);
        veterinario = findViewById(R.id.veterinario);
        centroVeterinario = findViewById(R.id.centro_veterinario);

        MascotasViewModel viewModel  =
            new ViewModelProvider(this, MASCOTA.getFabrica()).get(MascotasViewModel.class);

        viewModel.getMascota(idMascota).observe(this, this::popularVista);
    }

    private long extraerIdMascota() {
        Intent intent = getIntent();
        long id = intent.getLongExtra("data", -1);

        if (id == -1)
            throw new IllegalStateException("Debes proporcionar una id de mascota en esta actividad");

        return id;
    }

    private void popularVista(Mascota mascota) {
        if (mascota == null)
            return;

        Veterinario veterinarioMascota = mascota.getVeterinario();
        Uri fotoMascota = mascota.getFoto();
        nombre.setText(mascota.getNombre());

        if (veterinarioMascota != null) {
            CentroProfesional clinica = veterinarioMascota.getCentro();
            veterinario.setText(veterinarioMascota.getNombre());
            centroVeterinario.setText(clinica != null ? clinica.getNombre() : "");
        }

        if (foto != null)
            foto.setImageURI(fotoMascota);
    }
}