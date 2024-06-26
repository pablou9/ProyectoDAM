package es.ifp.petprotech.medicacion.views;

import static es.ifp.petprotech.app.model.FabricaViewModel.MASCOTA;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import es.ifp.petprotech.app.viewmodels.NavegacionAnadirEntidadesViewModel;
import es.ifp.petprotech.app.views.AdaptadorLista;
import es.ifp.petprotech.app.views.ListaActivity;
import es.ifp.petprotech.app.views.ViewHolderLista;
import es.ifp.petprotech.mascotas.model.Mascota;
import es.ifp.petprotech.mascotas.viewmodels.MascotasViewModel;

public class MedicacionMascotasActivity extends ListaActivity<Mascota> {

    @Override
    protected AdaptadorLista<Mascota> nuevoAdaptador() {
        return new MedicacionAdapter();
    }

    @Override
    protected Class<?> entidadActivity() {
        return MedicacionActivity.class;
    }

    @Override
    protected boolean mostrarActionButton() {
        return false;
    }

    @Override
    protected List<NavegacionAnadirEntidadesViewModel.Pantalla> pantallasNuevaEntidad() {
        return List.of();
    }

    @Override
    protected LiveData<List<Mascota>> getLista() {
        MascotasViewModel viewModel =
                new ViewModelProvider(this, MASCOTA.getFabrica()).get(MascotasViewModel.class);

        return viewModel.getMascotas();
    }

    /** El contenedor de cada item de mascota en la lista */
    public static class MascotaViewHolder extends ViewHolderLista {
        private TextView texto;
        private ImageView icono;

        public MascotaViewHolder(View view) {
            super(view);
            texto = view.findViewById(R.id.texto_item);
            icono = view.findViewById(R.id.icono);
        }

        @Override
        public void soltarVistasUnidas() {
            texto = null;
            icono = null;
        }

        public ImageView getIcono() {
            return icono;
        }
        public TextView getTexto() {
            return texto;
        }
    }

    /** El adaptador de la lista */
    public static class MedicacionAdapter extends AdaptadorLista<Mascota> {

        @NonNull
        @Override
        public MascotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Crea un nuevo contenedor item
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_lista, parent, false);

            return new MascotaViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderLista holder, int position) {
            super.onBindViewHolder(holder, position);

            MascotaViewHolder viewHolder = (MascotaViewHolder) holder;

            Mascota mascota = getEntidad(position);

            Uri fotoMascota = mascota.getFoto();

            if (fotoMascota != null) {
                viewHolder.getIcono().setImageURI(fotoMascota);
            }
            else {
                Resources resources = holder.getVista().getResources();
                Drawable imagen = ResourcesCompat.getDrawable(resources, R.drawable.baseline_pets_32, null);
                viewHolder.getIcono().setImageDrawable(imagen);
            }
            viewHolder.getTexto().setText(mascota.getNombre());
        }
    }
}