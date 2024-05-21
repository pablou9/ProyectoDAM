package es.ifp.petprotech.app.views;


import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;

import es.ifp.petprotech.bd.Entidad;

public abstract class AdaptadorLista<T extends Entidad>
        extends RecyclerView.Adapter<ViewHolderLista>
{

    private List<T> data;
    private Consumer<T> gestorClicks;

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    protected T getEntidad(int posicion) {
        return data.get(posicion);
    }

    protected void setGestorDeClicksEnItems(Consumer<T> verMascota) {
        this.gestorClicks = verMascota;
    }

    @Override
    public synchronized int getItemCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderLista holder, int position) {
        T entidad = getEntidad(position);
        holder.getVista().setOnClickListener(e -> gestorClicks.accept(entidad));
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolderLista holder) {
        super.onViewRecycled(holder);
        holder.soltarVistas();
    }
}
