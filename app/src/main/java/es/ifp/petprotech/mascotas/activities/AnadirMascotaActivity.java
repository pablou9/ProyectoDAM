package es.ifp.petprotech.mascotas.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import es.ifp.petprotech.R;

public class AnadirMascotaActivity extends AppCompatActivity {

    private ImageView perro;
    private ImageView gato;
    private ImageView roedor;
    private ImageView pajaro;
    private ImageView reptil;
    private ImageView otro;

    @Override
    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        perro = findViewById(R.id.perro_icon);
        gato = findViewById(R.id.gato_icon);
        roedor = findViewById(R.id.hamster_icon);
        pajaro = findViewById(R.id.ave_icon);
        reptil = findViewById(R.id.reptil_icon);
        otro = findViewById(R.id.otro_icon);

        Log.d("Main", "onCreate: here");

        perro.setOnClickListener((view) -> {
            Log.d("Main", "onCreate: doggie touched!!");
        });
    }
}