package es.ifp.petprotech.app.model;

import static android.content.Context.MODE_PRIVATE;
import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;
import static es.ifp.petprotech.app.datos.SharedPreferencesRepositorio.NOMBRE_PREFERENCIAS;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.function.Function;

import es.ifp.petprotech.app.PetProtechApp;
import es.ifp.petprotech.app.datos.SharedPreferencesRepositorio;
import es.ifp.petprotech.centros.model.CentroProfesional;
import es.ifp.petprotech.mascotas.model.Mascota;
import es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel;
import es.ifp.petprotech.mascotas.viewmodels.MascotasViewModel;
import es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel;

@SuppressWarnings("unchecked")
public enum FabricaViewModel {
    ANADIR_MASCOTA(AnadirMascotaViewModel.class,
        context ->
            new AnadirMascotaViewModel(
                    Modelo.getRepositorio(Modelo.MASCOTA, Mascota.class),
                    SharedPreferencesRepositorio.instancia(context.getSharedPreferences(NOMBRE_PREFERENCIAS, MODE_PRIVATE)))),
    VETERINARIO(AnadirVeterinarioViewModel.class,
        context ->
            new AnadirVeterinarioViewModel(
                    Modelo.getRepositorio(Modelo.CENTRO_PROFESIONAL, CentroProfesional.class))),
    MASCOTA(MascotasViewModel.class,
        context ->
            new MascotasViewModel(
                    Modelo.getRepositorio(Modelo.MASCOTA, Mascota.class),
                    SharedPreferencesRepositorio.instancia(context.getSharedPreferences(NOMBRE_PREFERENCIAS, MODE_PRIVATE))));


    private final Function<Context, ? extends ViewModel> constructorViewModel;
    private final Class<? extends ViewModel> clase;

    private ViewModelProvider.Factory fabricaViewModel;

    FabricaViewModel(Class<? extends ViewModel> clase,
                     Function<Context, ? extends ViewModel> constructorViewModel)
    {
        this.clase = clase;
        this.constructorViewModel = constructorViewModel;
    }

    public Class<? extends ViewModel> clase() {
        return clase;
    }

    /** @noinspection unchecked*/
    public <T extends ViewModel> void initFabrica(Context context, Class<T> clase) {

        ViewModelInitializer<T> inicializador = new ViewModelInitializer<>(
                clase,
                creationExtras -> {
                    PetProtechApp app = (PetProtechApp) creationExtras.get(APPLICATION_KEY);
                    assert app != null;

                    return (T) constructorViewModel.apply(context);
                });


        fabricaViewModel = ViewModelProvider.Factory.from(inicializador);
    }

    public ViewModelProvider.Factory getFabrica() {
        return fabricaViewModel;
    }
}
