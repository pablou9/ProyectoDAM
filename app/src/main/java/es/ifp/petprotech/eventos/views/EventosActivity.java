package es.ifp.petprotech.eventos.views;

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
import es.ifp.petprotech.app.model.FabricaViewModel;
import es.ifp.petprotech.app.viewmodels.NavegacionAnadirEntidadesViewModel;
import es.ifp.petprotech.app.views.AdaptadorLista;
import es.ifp.petprotech.app.views.ListaActivity;
import es.ifp.petprotech.app.views.ViewHolderLista;
import es.ifp.petprotech.eventos.model.Evento;
import es.ifp.petprotech.eventos.viewmodels.EventosViewModel;

public class EventosActivity extends ListaActivity<Evento> {

    @Override
    protected LiveData<List<Evento>> getLista() {
        EventosViewModel viewModel = new ViewModelProvider(this, FabricaViewModel.EVENTO.getFabrica())
                .get(EventosViewModel.class);

        return viewModel.getEventos();
    }

    @Override
    protected AdaptadorLista<Evento> nuevoAdaptador() {
        return new EventosAdapter();
    }

    @Override
    protected Class<?> entidadActivity() {
        return null;
    }

    @Override
    protected boolean mostrarActionButton() {
        return false;
    }

    @Override
    protected List<NavegacionAnadirEntidadesViewModel.Pantalla> pantallasNuevaEntidad() {
        return null;
    }

    /** El contenedor de cada item de evento en la lista */
    public static class EventoViewHolder extends ViewHolderLista {
        private TextView texto;
        private ImageView icono;

        public EventoViewHolder(View view) {
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
    public static class EventosAdapter extends AdaptadorLista<Evento> {

        @NonNull
        @Override
        public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Crea un nuevo contenedor item
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_lista, parent, false);

            return new EventoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderLista holder, int position) {
            super.onBindViewHolder(holder, position);

            EventoViewHolder viewHolder = (EventoViewHolder) holder;

            Evento evento = getEntidad(position);
            viewHolder.getTexto().setText(evento.getNombre());

            Resources resources = holder.getVista().getResources();
            Drawable imagen = ResourcesCompat.getDrawable(resources, R.drawable.baseline_access_time_28, null);
            viewHolder.getIcono().setImageDrawable(imagen);
        }
    }
}