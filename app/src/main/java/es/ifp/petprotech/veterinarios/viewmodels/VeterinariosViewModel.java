package es.ifp.petprotech.veterinarios.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import es.ifp.petprotech.bd.Repositorio;
import es.ifp.petprotech.veterinarios.model.Veterinario;

public class VeterinariosViewModel extends ViewModel {
    
    private final Repositorio<Veterinario> repositorio;

    private final MutableLiveData<List<Veterinario>> veterinarios = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<Veterinario> veterinario;

    private final ExecutorService background = Executors.newSingleThreadExecutor();

    public VeterinariosViewModel(Repositorio<Veterinario> repositorio) {
        this.repositorio = repositorio;
    }

    public MutableLiveData<List<Veterinario>> getVeterinarios() {
        background.execute(() -> {
            List<Veterinario> veterinariosEnRepo = repositorio.seleccionarTodo();
            veterinarios.postValue(veterinariosEnRepo);
        });

        return veterinarios;
    }

    private static final String TAG = "VeterinariosViewModel";

    public MutableLiveData<Veterinario> getVeterinario(long id) {
        if (veterinario == null) {
            veterinario = new MutableLiveData<>();
        }

        background.execute(() -> {
            Log.d(TAG, "getVeterinario: con id:" + id);
            Veterinario veterinarioEnRepo = repositorio.seleccionarPorId(id);

            Log.d(TAG, "getVeterinario: en repo:" + veterinarioEnRepo);
            veterinario.postValue(veterinarioEnRepo);
        });

        return veterinario;
    }

    public void eliminarVeterinario() {
        background.execute(() -> {
            repositorio.eliminar(veterinario.getValue());

            veterinarios.postValue(Objects.requireNonNull(veterinarios.getValue())
                    .stream()
                    .filter(enLista -> enLista.getId() != veterinario.getValue().getId())
                    .collect(Collectors.toList()));
        });
    }
}
