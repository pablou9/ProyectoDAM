package es.ifp.petprotech.veterinarios.views;

import android.os.Bundle;

import es.ifp.petprotech.R;
import es.ifp.petprotech.app.views.BaseActivity;

public class AnadirVeterinarioActivity extends BaseActivity {

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_anadir_veterinario;
    }

    @Override
    protected boolean mostrarGoBack() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}