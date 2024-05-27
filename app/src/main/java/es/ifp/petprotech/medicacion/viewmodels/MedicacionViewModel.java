package es.ifp.petprotech.medicacion.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import es.ifp.petprotech.bd.Entidad;
import es.ifp.petprotech.bd.Repositorio;
import es.ifp.petprotech.mascotas.model.Mascota;
import es.ifp.petprotech.medicacion.model.Medicacion;
import es.ifp.petprotech.medicacion.model.Medicamento;

public class MedicacionViewModel extends ViewModel {
    private static final String TAG = "MedicacionViewModel";

    private final Repositorio<Medicacion> repositorio;
    private final Repositorio<Mascota> repositorioMascotas;
    private final Repositorio<Medicamento> repositorioMedicamento;

    private final MutableLiveData<List<Medicacion>> listadoMedicacion = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Medicacion> medicacion = new MutableLiveData<>();

    private final ExecutorService background = Executors.newSingleThreadExecutor();

    public MedicacionViewModel(Repositorio<Medicacion> repositorio,
                               Repositorio<Mascota> repositorioMascotas,
                               Repositorio<Medicamento> repositorioMedicamento)
    {
        this.repositorio = repositorio;
        this.repositorioMascotas = repositorioMascotas;
        this.repositorioMedicamento = repositorioMedicamento;
    }

    public void actualizarLista(Mascota mascota) {
        queryMedicacion(mascota);
    }

    public LiveData<List<Medicacion>> getListadoMedicacion(Mascota mascota) {
        queryMedicacion(mascota);
        return listadoMedicacion;
    }

    private void queryMedicacion(Mascota mascota) {
        background.execute(() -> {

            Map<Long, List<Medicacion>> medicacionEnRepo =
                    repositorioMascotas.seleccionarUnoAMuchos(Medicacion.class, new long[]{mascota.getId()});

            Log.d(TAG, "getListadoMedicacion: MEDI: " + medicacionEnRepo);

            List<Medicacion> medicaciones = medicacionEnRepo.get(mascota.getId());

            if (medicaciones == null)
                return;

            long[] medicacionIds = medicaciones
                    .stream()
                    .mapToLong(Entidad::getId)
                    .toArray();


            Map<Long, List<Medicamento>> medicamentos =
                    repositorio.seleccionarMuchosAUno(Medicamento.class, medicacionIds);


            for (Medicacion medicacion : medicaciones) {
                medicacion.setMascota(mascota);

                List<Medicamento> medicamentoMedicacion = medicamentos.get(medicacion.getId());

                if (medicamentoMedicacion == null)
                    continue;

                for (Medicamento medicamento : medicamentoMedicacion) {
                    medicacion.setMedicamento(medicamento);
                }
            }

            listadoMedicacion.postValue(medicaciones);
        });
    }

    public Medicacion getMedicacion() {
        Medicacion medicacion = this.medicacion.getValue();

        if (medicacion == null)
            throw new IllegalStateException("No has seleccionado una medicacion todavía. Selecciona " +
                    "con getMedicacion(long)");

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
