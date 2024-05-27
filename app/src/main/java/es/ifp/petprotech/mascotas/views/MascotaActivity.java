
package es.ifp.petprotech.mascotas.views;

import static es.ifp.petprotech.app.model.FabricaViewModel.MASCOTA;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import es.ifp.petprotech.R;
import es.ifp.petprotech.app.views.EntidadActivity;
import es.ifp.petprotech.centros.model.CentroProfesional;
import es.ifp.petprotech.mascotas.model.Mascota;
import es.ifp.petprotech.mascotas.viewmodels.MascotasViewModel;
import es.ifp.petprotech.medicacion.views.MedicacionDialogo;
import es.ifp.petprotech.veterinarios.model.Veterinario;

public class MascotaActivity extends EntidadActivity {

    private ImageView foto;
    private TextView nombre;
    private TextView veterinario;
    private TextView centroVeterinario;

    private MascotasViewModel viewModel;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_mascota;
    }

    @Override
    protected boolean mostrarGoBack() {
        return true;
    }

    @Override
    protected String mensajeAlertaEliminar() {
        return getString(R.string.eliminar_mascota);
    }

    @Override
    protected void eliminarEntidad() {
        viewModel.eliminarMascota();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long idMascota = extraerId();

        foto = findViewById(R.id.foto);
        nombre = findViewById(R.id.nombre);
        veterinario = findViewById(R.id.veterinario);
        centroVeterinario = findViewById(R.id.centro_veterinario);

        TextView anadirMedicamento = findViewById(R.id.anadir_medicamento);
        TextView anadirCita = findViewById(R.id.anadir_cita);

        anadirMedicamento.setOnClickListener(e -> mostrarDialogoMedicamento());
        anadirCita.setOnClickListener(e -> mostrarDialogoCita());

        viewModel = new ViewModelProvider(this, MASCOTA.getFabrica()).get(MascotasViewModel.class);
        viewModel.getMascota(idMascota).observe(this, this::popularVista);
    }

    private void mostrarDialogoMedicamento() {
        new MedicacionDialogo(viewModel.getMascota()).show(getSupportFragmentManager(), "MEDICACION_DIALOGO");
    }

    private void mostrarDialogoCita() {

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