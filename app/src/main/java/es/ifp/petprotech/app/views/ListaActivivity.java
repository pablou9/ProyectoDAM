package es.ifp.petprotech.app.views;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import es.ifp.petprotech.R;
import es.ifp.petprotech.bd.Entidad;

public abstract class ListaActivivity<T extends Entidad> extends BaseActivity {

    @Override
    protected final int getLayoutResource() {
        return R.layout.activity_lista;
    }

    @Override
    protected final boolean mostrarGoBack() {
        return true;
    }

    protected abstract LiveData<List<T>> getLista();
    protected abstract AdaptadorLista<T> adaptador();
    protected abstract Class<?> anadirEntidadActivity();
    protected abstract Class<?> entidadActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FloatingActionButton fab = findViewById(R.id.boton_anadir);
        fab.setOnClickListener(e -> lanzarActividad(anadirEntidadActivity()));

        RecyclerView lista = findViewById(R.id.lista);

        lista.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lista.addItemDecoration(new DividerItemDecoration(
                lista.getContext(),
                LinearLayoutManager.VERTICAL));

        AdaptadorLista<T> adaptadorLista = adaptador();
        adaptadorLista.setGestorDeClicksEnItems(entidad ->
                lanzarActividad(entidadActivity(), entidad.getId()));

        lista.setAdapter(adaptadorLista);

        getLista().observe(this, adaptadorLista::setData);
    }
}
