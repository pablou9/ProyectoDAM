package es.ifp.petprotech.veterinarios.views;

import static es.ifp.petprotech.app.model.FabricaViewModel.VETERINARIO;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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
import es.ifp.petprotech.app.views.AdaptadorLista;
import es.ifp.petprotech.app.views.ListaActivivity;
import es.ifp.petprotech.app.views.ViewHolderLista;
import es.ifp.petprotech.veterinarios.model.Veterinario;
import es.ifp.petprotech.veterinarios.viewmodels.VeterinariosViewModel;

public class VeterinariosActivity extends ListaActivivity<Veterinario> {

    @Override
    protected AdaptadorLista<Veterinario> adaptador() {
        return new VeterinariosActivity.VeterinariosAdapter();
    }

    @Override
    protected Class<?> anadirEntidadActivity() {
        return AnadirVeterinarioActivity.class;
    }

    @Override
    protected Class<?> entidadActivity() {
        return VeterinarioActivity.class;
    }

    @Override
    protected LiveData<List<Veterinario>> getLista() {
        VeterinariosViewModel viewModel =
                new ViewModelProvider(this, VETERINARIO.getFabrica()).get(VeterinariosViewModel.class);

        return viewModel.getVeterinarios();
    }

    /** El contenedor de cada item de mascota en la lista */
    public static class VeterinarioViewHolder extends ViewHolderLista {
        private TextView texto;
        private ImageView icono;

        public VeterinarioViewHolder(View view) {
            super(view);
            texto = view.findViewById(R.id.texto);
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
    public static class VeterinariosAdapter extends AdaptadorLista<Veterinario> {

        @NonNull
        @Override
        public VeterinariosActivity.VeterinarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Crea un nuevo contenedor item
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_lista, parent, false);

            return new VeterinariosActivity.VeterinarioViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderLista holder, int position) {
            super.onBindViewHolder(holder, position);

            VeterinariosActivity.VeterinarioViewHolder viewHolder = (VeterinariosActivity.VeterinarioViewHolder) holder;

            Veterinario mascota = getEntidad(position);

            Resources resources = holder.getVista().getResources();
            Drawable imagen = ResourcesCompat.getDrawable(resources, R.drawable.baseline_local_hospital_60, null);

            viewHolder.getIcono().setImageDrawable(imagen);
            viewHolder.getTexto().setText(mascota.getNombre());
        }
    }
}
