package es.ifp.petprotech.app.views;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import es.ifp.petprotech.R;
import es.ifp.petprotech.app.viewmodels.NavegacionAnadirEntidadesViewModel;
import es.ifp.petprotech.bd.Entidad;
import es.ifp.petprotech.mascotas.views.AnadirEntidadesActivity;

public abstract class ListaActivity<T extends Entidad> extends BaseActivity {

    private AdaptadorLista<T> adaptadorLista;

    @Override
    protected final int getLayoutResource() {
        return R.layout.activity_lista;
    }

    @Override
    protected final boolean mostrarGoBack() {
        return true;
    }

    protected boolean mostrarActionButton() {
        return true;
    }
    protected abstract LiveData<List<T>> getLista();
    protected abstract AdaptadorLista<T> nuevoAdaptador();
    protected abstract Class<?> entidadActivity();

    protected abstract List<NavegacionAnadirEntidadesViewModel.Pantalla> pantallasNuevaEntidad();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FloatingActionButton fab = findViewById(R.id.boton_anadir);

        if (mostrarActionButton())
            fab.setOnClickListener(e -> lanzarActividadCrearEntidad());

        else fab.setVisibility(View.GONE);

        RecyclerView lista = findViewById(R.id.lista);

        lista.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lista.addItemDecoration(new DividerItemDecoration(
                lista.getContext(),
                LinearLayoutManager.VERTICAL));

        adaptadorLista = nuevoAdaptador();
        adaptadorLista.setGestorDeClicksEnItems(entidad ->
                lanzarActividad(entidadActivity(), entidad.getId()));

        lista.setAdapter(adaptadorLista);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLista().observe(this, adaptadorLista::setData);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    protected void lanzarActividadCrearEntidad() {
        Bundle bundle = new Bundle();
        List<NavegacionAnadirEntidadesViewModel.Pantalla> pantallas = pantallasNuevaEntidad();

        for (int i = 0; i < pantallas.size(); i++) {
            NavegacionAnadirEntidadesViewModel.Pantalla pantalla = pantallas.get(i);
            bundle.putSerializable(
                    NavegacionAnadirEntidadesViewModel.PANTALLAS+"_"+i, pantalla);
        }

        lanzarActividad(AnadirEntidadesActivity.class, bundle);
    }
}
