package es.ifp.petprotech.mascotas.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import es.ifp.petprotech.R;
import es.ifp.petprotech.mascotas.viewmodels.MascotasViewModel;
import es.ifp.petprotech.app.ViewModelsInitializers;

public class AnadirMascotaActivity extends AppCompatActivity {

    private ImageView perro;
    private ImageView gato;
    private ImageView roedor;
    private ImageView pajaro;
    private ImageView reptil;
    private ImageView otro;

    private MascotasViewModel viewModel;

    @Override
    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewModelsInitializers.init(this);
        viewModel = initViewModel();

        perro = findViewById(R.id.perro_icon);
        gato = findViewById(R.id.gato_icon);
        roedor = findViewById(R.id.hamster_icon);
        pajaro = findViewById(R.id.ave_icon);
        reptil = findViewById(R.id.reptil_icon);
        otro = findViewById(R.id.otro_icon);

        perro.setOnClickListener((view) -> {
            Log.d("Main", "onCreate: doggie touched!!");
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewModelsInitializers.cerrar();
    }

    private MascotasViewModel initViewModel() {
        return new ViewModelProvider(this,
            ViewModelProvider.Factory.from(
                ViewModelsInitializers
                    .inicializadorParaModelo(ViewModelsInitializers.Modelo.MASCOTA)))
            .get(MascotasViewModel.class);
    }
}