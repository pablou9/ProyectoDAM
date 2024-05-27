package es.ifp.petprotech.medicacion.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import es.ifp.petprotech.bd.Repositorio;
import es.ifp.petprotech.medicacion.model.Medicacion;

public class MedicacionViewModel extends ViewModel {
    private static final String TAG = "MedicacionViewModel";

    private final Repositorio<Medicacion> repositorio;

    private final MutableLiveData<List<Medicacion>> listadoMedicacion = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Medicacion> medicacion = new MutableLiveData<>();

    private final ExecutorService background = Executors.newSingleThreadExecutor();

    public MedicacionViewModel(Repositorio<Medicacion> repositorio) {
        this.repositorio = repositorio;
    }

    public MutableLiveData<List<Medicacion>> getListadoMedicacion() {
        background.execute(() -> {
            List<Medicacion> medicacionEnRepo = repositorio.seleccionarTodo();
            listadoMedicacion.postValue(medicacionEnRepo);
        });

        return listadoMedicacion;
    }

    public Medicacion getMedicacion() {
        Medicacion medicacion = this.medicacion.getValue();

        if (medicacion == null)
            throw new IllegalStateException("No has seleccionado una medicacion todavía. Selecciona " +
                    "con getMedicacion(long)");

        return medicacion;
    }

    public MutableLiveData<Medicacion> getMedicacion(long id) {
        background.execute(() -> {
            Medicacion medicacionEnRepo = repositorio.seleccionarPorId(id);
            medicacion.postValue(medicacionEnRepo);
        });

        return medicacion;
    }

    public void eliminarMedicacion() {
        background.execute(() -> {
            repositorio.eliminar(medicacion.getValue());
            listadoMedicacion.postValue(Objects.requireNonNull(listadoMedicacion.getValue())
                    .stream()
                    .filter(enLista -> enLista.getId() != medicacion.getValue().getId())
                    .collect(Collectors.toList()));
        });
    }
}
