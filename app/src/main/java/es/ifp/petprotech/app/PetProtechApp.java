package es.ifp.petprotech.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.function.Function;

import es.ifp.petprotech.bd.BaseDeDatos;
import es.ifp.petprotech.bd.BaseDeDatosSQLite;
import es.ifp.petprotech.bd.Repositorio;
import es.ifp.petprotech.mascotas.model.MascotasRepositorio;
import es.ifp.petprotech.mascotas.viewmodels.MascotasViewModel;

public class PetProtechApp extends Application {

    public enum Modelo {
        MASCOTA(MascotasViewModel.class, MascotasRepositorio::new, repo -> new MascotasViewModel((MascotasRepositorio) repo));

        private final Function<BaseDeDatos<SQLiteDatabase>,Repositorio<?>> inicializadorRepositorio;
        private final Function<Repositorio<?>, ? extends ViewModel> constructorViewModel;
        private final Class<? extends ViewModel> clase;

        private ViewModelProvider.Factory fabricaViewModel;

        Modelo(Class<? extends ViewModel> clase,
               Function<BaseDeDatos<SQLiteDatabase>, Repositorio<?>> inicializadorRepositorio,
               Function<Repositorio<?>, ? extends ViewModel> constructorViewModel)
        {
            this.clase = clase;
            this.inicializadorRepositorio = inicializadorRepositorio;
            this.constructorViewModel = constructorViewModel;
        }

        Class<? extends ViewModel> clase() {
            return clase;
        }

        /** @noinspection unchecked*/
        <T extends ViewModel> void initFabrica(BaseDeDatos<SQLiteDatabase> bd, Class<T> clase) {

            Repositorio<?> repositorio = inicializadorRepositorio.apply(bd);

            ViewModelInitializer<T> inicializador = new ViewModelInitializer<>(
                clase,
                creationExtras -> {
                    PetProtechApp app = (PetProtechApp) creationExtras.get(APPLICATION_KEY);
                    assert app != null;

                    return (T) constructorViewModel.apply(repositorio);
                });


            fabricaViewModel = ViewModelProvider.Factory.from(inicializador);
        }

        public ViewModelProvider.Factory getFabricaViewModel() {
            return fabricaViewModel;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        BaseDeDatos<SQLiteDatabase> bd = new BaseDeDatosSQLite(this);

        for (Modelo modelo : Modelo.values())
            modelo.initFabrica(bd, modelo.clase());
    }


}
