package es.ifp.petprotech.app.views;

import android.os.Bundle;
import android.widget.ImageView;

import es.ifp.petprotech.R;
import es.ifp.petprotech.eventos.views.EventosActivity;
import es.ifp.petprotech.mascotas.views.MascotasActivity;
import es.ifp.petprotech.medicacion.views.MedicacionMascotasActivity;
import es.ifp.petprotech.veterinarios.views.VeterinariosActivity;

public class HomeActivity extends BaseActivity {

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home;
    }

    @Override
    protected boolean mostrarGoBack() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageView botonMascotas = findViewById(R.id.boton_mascotas);
        ImageView botonVeterinario = findViewById(R.id.boton_veterinarios);
        ImageView botonMedicacion = findViewById(R.id.boton_medicacion);
        ImageView botonEventos = findViewById(R.id.boton_eventos);

        botonMascotas.setOnClickListener(e -> lanzarActividad(MascotasActivity.class));
        botonVeterinario.setOnClickListener(e -> lanzarActividad(VeterinariosActivity.class));
        botonMedicacion.setOnClickListener(e -> lanzarActividad(MedicacionMascotasActivity.class));
        botonEventos.setOnClickListener(e -> lanzarActividad(EventosActivity.class));
    }
}