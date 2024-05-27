package es.ifp.petprotech.eventos.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.ifp.petprotech.bd.Repositorio;
import es.ifp.petprotech.eventos.model.Evento;

public class EventosViewModel extends ViewModel {
    private static final String TAG = "EventoViewModel";

    private final Repositorio<Evento> repositorio;

    private final MutableLiveData<List<Evento>> eventos = new MutableLiveData<>(new ArrayList<>());

    private final ExecutorService background = Executors.newSingleThreadExecutor();


    public EventosViewModel(Repositorio<Evento> repositorio) {
        this.repositorio = repositorio;
    }

    public MutableLiveData<List<Evento>> getEventos() {
        background.execute(() -> {
            List<Evento> mascotasEnRepo = repositorio.seleccionarTodo();
            eventos.postValue(mascotasEnRepo);
        });

        return eventos;
    }
}
