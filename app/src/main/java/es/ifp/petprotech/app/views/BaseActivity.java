package es.ifp.petprotech.app.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import es.ifp.petprotech.R;

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int getLayoutResource();
    protected abstract boolean mostrarGoBack();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(getLayoutResource());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // configura la barra de acción
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (mostrarGoBack() && getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (mostrarGoBack()) {
            getOnBackPressedDispatcher().onBackPressed(); // navega a la actividad anterior
            return true;
        }
        else return super.onSupportNavigateUp();
    }

    protected void lanzarActividad(Class<?> clase) {
        Intent anadirMascota = new Intent(this, clase);
        startActivity(anadirMascota);
    }

    protected void lanzarActividad(Class<?> clase, long data) {
        Intent anadirMascota = new Intent(this, clase);
        anadirMascota.putExtra("data", data);

        startActivity(anadirMascota);
    }

    protected void lanzarActividad(Class<?> clase, Bundle bundle) {
        Intent anadirMascota = new Intent(this, clase);
        anadirMascota.putExtra("data", bundle);

        startActivity(anadirMascota);
    }

}