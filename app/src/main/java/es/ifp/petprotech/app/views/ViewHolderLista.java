package es.ifp.petprotech.app.views;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public abstract class ViewHolderLista extends RecyclerView.ViewHolder {

    private View vista;

    public ViewHolderLista(View view) {
        super(view);
        vista = view;
    }

    public abstract void soltarVistasUnidas();

    protected void soltarVistas() {
        vista = null;
        soltarVistasUnidas();
    }

    public View getVista() {
        return vista;
    }
}
