package es.ifp.petprotech.mascotas.viewmodels;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import es.ifp.petprotech.app.datos.SharedPreferencesRepositorio;
import es.ifp.petprotech.bd.Repositorio;
import es.ifp.petprotech.mascotas.datos.ContratoMascotas;
import es.ifp.petprotech.mascotas.model.Mascota;

public class MascotasViewModel extends ViewModel {

    private final Repositorio<Mascota> repositorio;
    private final SharedPreferencesRepositorio preferenciasRepositorio;

    private final MutableLiveData<List<Mascota>> mascotas = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<Mascota> mascota;

    private final ExecutorService background = Executors.newSingleThreadExecutor();

    public MascotasViewModel(Repositorio<Mascota> repositorio, SharedPreferencesRepositorio preferenciasRepositorio) {
        this.repositorio = repositorio;
        this.preferenciasRepositorio = preferenciasRepositorio;
    }

    public MutableLiveData<List<Mascota>> getMascotas() {
        background.execute(() -> {
            List<Mascota> mascotasEnRepo = repositorio.seleccionarTodo();
            mascotas.postValue(mascotasEnRepo);
        });

        return mascotas;
    }

    private static final String TAG = "MascotasViewModel";

    public Mascota getMascota() {
        Mascota mascota = this.mascota.getValue();

        if (mascota == null)
            throw new IllegalStateException("No has seleccionado una mascota todavía. Selecciona " +
                    "con getMascota(long)");

        return mascota;
    }

    public MutableLiveData<Mascota> getMascota(long id) {
        if (mascota == null) {
            mascota = new MutableLiveData<>();
        }

        background.execute(() -> {
            Mascota mascotaEnRepo = repositorio.seleccionarPorId(id);

            if (mascotaEnRepo != null) {
                String uri = preferenciasRepositorio.recuperar(ContratoMascotas.URI_FOTO);

                if (!uri.isBlank())
                    mascotaEnRepo.setFoto(Uri.parse(uri));
            }
            mascota.postValue(mascotaEnRepo);
        });

        return mascota;
    }

    public void eliminarMascota() {
        background.execute(() -> {
            repositorio.eliminar(mascota.getValue());
            mascotas.postValue(Objects.requireNonNull(mascotas.getValue())
                    .stream()
                    .filter(enLista -> enLista.getId() != mascota.getValue().getId())
                    .collect(Collectors.toList()));
        });
    }
}
