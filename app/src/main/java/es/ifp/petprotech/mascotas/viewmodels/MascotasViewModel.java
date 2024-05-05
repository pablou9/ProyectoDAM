package es.ifp.petprotech.mascotas.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDateTime;

import es.ifp.petprotech.bd.Repositorio;
import es.ifp.petprotech.mascotas.model.Mascota;
import es.ifp.petprotech.mascotas.model.MascotasRepositorio;
import es.ifp.petprotech.veterinarios.model.Veterinario;

public class MascotasViewModel extends ViewModel {

    private static final String TAG = "MascotasViewModel";

    public enum TipoMascota {
        PERRO,
        GATO,
        REPTIL,
        ROEDOR,
        OTRO,
    }

    private Repositorio<Mascota> repositorio;

    public MascotasViewModel(MascotasRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    private Mascota.Builder builder;
    private Veterinario veterinario;
    private MutableLiveData<TipoMascota> tipoMascota = new MutableLiveData<>();

    public void tipoMascotaSeleccionado(TipoMascota tipo) {
        tipoMascota.postValue(tipo);
    }

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
