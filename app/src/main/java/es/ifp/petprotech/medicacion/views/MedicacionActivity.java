package es.ifp.petprotech.medicacion.views;

import static es.ifp.petprotech.app.model.FabricaViewModel.MEDICACION;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import es.ifp.petprotech.R;
import es.ifp.petprotech.app.model.FabricaViewModel;
import es.ifp.petprotech.app.viewmodels.NavegacionAnadirEntidadesViewModel;
import es.ifp.petprotech.app.views.AdaptadorLista;
import es.ifp.petprotech.app.views.ListaActivity;
import es.ifp.petprotech.app.views.ViewHolderLista;
import es.ifp.petprotech.mascotas.model.Mascota;
import es.ifp.petprotech.mascotas.viewmodels.MascotasViewModel;
import es.ifp.petprotech.mascotas.views.MascotaActivity;
import es.ifp.petprotech.medicacion.model.Medicacion;
import es.ifp.petprotech.medicacion.viewmodels.MedicacionViewModel;

public class MedicacionActivity extends ListaActivity<Medicacion> {

    private Mascota mascota;
    private MedicacionViewModel viewModel;

    @Override
    protected AdaptadorLista<Medicacion> nuevoAdaptador() {
        return new MedicacionAdapter();
    }

    @Override
    protected Class<?> entidadActivity() {
        return MascotaActivity.class;
    }

    @Override
    protected List<NavegacionAnadirEntidadesViewModel.Pantalla> pantallasNuevaEntidad() {
        return List.of(NavegacionAnadirEntidadesViewModel.Pantalla.ANADIR_MASCOTA, NavegacionAnadirEntidadesViewModel.Pantalla.ANADIR_FOTO_MASCOTA, NavegacionAnadirEntidadesViewModel.Pantalla.ANADIR_VETERINARIO);
    }

    @Override
    protected LiveData<List<Medicacion>> getLista() {
        return viewModel.getListadoMedicacion(mascota);
    }

    @Override
    protected void lanzarActividadCrearEntidad() {
        new MedicacionDialogo(mascota, creada -> viewModel.actualizarLista(mascota))
            .show(getSupportFragmentManager(), "MEDICACION_DIALOGO");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this, MEDICACION.getFabrica()).get(MedicacionViewModel.class);

        Intent intent = getIntent();
        long id = intent.getLongExtra("data", -1);

        if (id == -1)
            throw new IllegalStateException("Debes proporcionar una id de entidad en esta actividad");

        MascotasViewModel mascotasViewModel =
                new ViewModelProvider(this, FabricaViewModel.MASCOTA.getFabrica())
                        .get(MascotasViewModel.class);

        mascota = mascotasViewModel.queryMascota(id);
    }

    private static final String TAG = "MedicacionActivity";

    /** El contenedor de cada item de medicacion en la lista */
    public static class MedicacionViewHolder extends ViewHolderLista {
        private TextView nombre;
        private TextView cantidad;
        private TextView tomas;
        private ImageView icono;

        public MedicacionViewHolder(View view) {
            super(view);
            nombre = view.findViewById(R.id.texto_item);
            cantidad = view.findViewById(R.id.cantidad);
            tomas = view.findViewById(R.id.tomas);
            icono = view.findViewById(R.id.icono);
        }

        @Override
        public void soltarVistasUnidas() {
            nombre = null;
            cantidad = null;
            tomas = null;
            icono = null;
        }

        public TextView getNombre() {
            return nombre;
        }
        public TextView getCantidad() {
            return cantidad;
        }
        public TextView getTomas() {
            return tomas;
        }
        public ImageView getIcono() {
            return icono;
        }
    }

    /** El adaptador de la lista */
    public static class MedicacionAdapter extends AdaptadorLista<Medicacion> {

        @NonNull
        @Override
        public MedicacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Crea un nuevo contenedor item
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.medicacion_item, parent, false);

            return new MedicacionViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderLista holder, int position) {
            super.onBindViewHolder(holder, position);

            MedicacionViewHolder viewHolder = (MedicacionViewHolder) holder;
            Resources resources = holder.getVista().getResources();

            Drawable imagen = ResourcesCompat.getDrawable(resources, R.drawable.baseline_medication_32, null);
            viewHolder.getIcono().setImageDrawable(imagen);

            Medicacion medicacion = getEntidad(position);
            String tomas = parseTomas(medicacion, resources);

            viewHolder.getNombre().setText(medicacion.getMedicamento().getNombre());
            viewHolder.getCantidad().setText(medicacion.getCantidad());
            viewHolder.getTomas().setText(tomas);
        }

        private String parseTomas(Medicacion medicacion, Resources resources) {
            return resources.getString(R.string.cada) + " " +
                    medicacion.getHoras() + " " +
                    resources.getString(R.string.horas).toLowerCase() +
                    " - " +
                    resources.getString(R.string.durante) + " " +
                    medicacion.getDias() + " " +
                    resources.getString(R.string.dias).toLowerCase();
        }
    }
}