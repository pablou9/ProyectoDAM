package es.ifp.petprotech.veterinarios.views;

import static es.ifp.petprotech.app.model.FabricaViewModel.VETERINARIO;

import android.os.Bundle;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import es.ifp.petprotech.R;
import es.ifp.petprotech.app.datos.FormatoFechaTiempo;
import es.ifp.petprotech.app.views.BaseActivity;
import es.ifp.petprotech.centros.model.CentroProfesional;
import es.ifp.petprotech.veterinarios.viewmodels.VeterinariosViewModel;

public class VeterinarioActivity extends BaseActivity {

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_veterinario;
    }

    @Override
    protected boolean mostrarGoBack() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        VeterinariosViewModel viewModel =
            new ViewModelProvider(this, VETERINARIO.getFabrica()).get(VeterinariosViewModel.class);

        long idVeterinario = extraerId();

        TextView nombre = findViewById(R.id.nombre_veterinario);
        TextView especializacion = findViewById(R.id.especializacion);
        TextView nombreCentro = findViewById(R.id.nombre_centro);
        TextView direccionCentro = findViewById(R.id.direccion_centro);
        TextView diasTrabajoCentro = findViewById(R.id.dias_trabajo_centro);
        TextView horarioCentro = findViewById(R.id.horario_centro);
        TextView telefonoCentro = findViewById(R.id.telefono_centro);
        TextView webCentro = findViewById(R.id.pagina_web_centro);

        viewModel.getVeterinario(idVeterinario).observe(this, veterinario -> {
            CentroProfesional centroVeterinario = veterinario.getCentro();

            String apertura = FormatoFechaTiempo.formatoTiempo(centroVeterinario.getApertura());
            String cierre = FormatoFechaTiempo.formatoTiempo(centroVeterinario.getCierre());

            String horario = String.format("%s - %s", apertura, cierre);

            nombre.setText(veterinario.getNombre());
            especializacion.setText(veterinario.getEspecializacion());
            nombreCentro.setText(centroVeterinario.getNombre());
            direccionCentro.setText(centroVeterinario.getDireccion());
            //diasTrabajoCentro.setText(centroVeterinario.get);
            horarioCentro.setText(horario);
            telefonoCentro.setText(centroVeterinario.getTelefono());
            webCentro.setText(centroVeterinario.getPaginaWeb());

        });
    }
}