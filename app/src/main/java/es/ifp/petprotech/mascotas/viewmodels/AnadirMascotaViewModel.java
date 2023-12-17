package es.ifp.petprotech.mascotas.viewmodels;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.time.LocalDateTime;

import es.ifp.petprotech.mascotas.model.Mascota;
import es.ifp.petprotech.mascotas.model.MascotasRepositorio;
import es.ifp.petprotech.veterinarios.model.Veterinario;

public class AnadirMascotaViewModel extends ViewModel {

    private static final String TAG = "AnadirMascotaViewModel";

    private MascotasRepositorio repositorio;

    public AnadirMascotaViewModel(MascotasRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    private Mascota.Builder builder;
    private Veterinario veterinario;

    public void anadirMascota(String nombre,
                              LocalDateTime fechaNacimiento,
                              String familia,
                              String especie,
                              String raza,
                              String chip)
    {
        Mascota mascota = Mascota.nuevaMascota()
            .nombre(nombre)
            .fechaNacimiento(fechaNacimiento)
            .familia(familia)
            .especie(especie)
            .raza(raza)
            .chip(chip)
            .build();

        Log.d(TAG, "anadirMascota: anadiendo mascota: " + mascota);
    }
}
