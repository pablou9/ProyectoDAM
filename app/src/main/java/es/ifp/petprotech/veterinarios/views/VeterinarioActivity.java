package es.ifp.petprotech.veterinarios.views;

import static es.ifp.petprotech.app.model.FabricaViewModel.VETERINARIO;

import android.os.Bundle;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import es.ifp.petprotech.R;
import es.ifp.petprotech.app.util.FormatoFechaTiempo;
import es.ifp.petprotech.app.views.EntidadActivity;
import es.ifp.petprotech.centros.model.CentroProfesional;
import es.ifp.petprotech.veterinarios.viewmodels.VeterinariosViewModel;

public class VeterinarioActivity extends EntidadActivity {

    private VeterinariosViewModel viewModel;

    private TextView nombre;
    private TextView especializacion;
    private TextView nombreCentro;
    private TextView direccionCentro;
    private TextView diasTrabajoCentro;
    private TextView horarioCentro;
    private TextView telefonoCentro;
    private TextView webCentro;
    
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_veterinario;
    }

    @Override
    protected boolean mostrarGoBack() {
        return true;
    }

    @Override
    protected String mensajeAlertaEliminar() {
        return getString(R.string.eliminar_veterinario);
    }

    @Override
    protected void eliminarEntidad() {
        viewModel.eliminarVeterinario();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this, VETERINARIO.getFabrica()).get(VeterinariosViewModel.class);

        nombre = findViewById(R.id.nombre_veterinario);
        especializacion = findViewById(R.id.especializacion);
        nombreCentro = findViewById(R.id.nombre_centro);
        direccionCentro = findViewById(R.id.direccion_centro);
        diasTrabajoCentro = findViewById(R.id.dias_trabajo_centro);
        horarioCentro = findViewById(R.id.horario_centro);
        telefonoCentro = findViewById(R.id.telefono_centro);
        webCentro = findViewById(R.id.pagina_web_centro);
    }

    @Override
    protected void onResume() {
        super.onResume();

        long idVeterinario = extraerId();

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