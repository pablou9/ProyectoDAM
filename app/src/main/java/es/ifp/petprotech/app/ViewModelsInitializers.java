package es.ifp.petprotech.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import es.ifp.petprotech.bd.BaseDeDatos;
import es.ifp.petprotech.bd.BaseDeDatosSQLite;
import es.ifp.petprotech.mascotas.model.MascotasRepositorio;
import es.ifp.petprotech.mascotas.viewmodels.MascotasViewModel;

public class ViewModelsInitializers {

    public enum Modelo {
        MASCOTA
    }

    private ViewModelsInitializers() {}

    private static BaseDeDatos<SQLiteDatabase> baseDeDatos;

    public static void init(Context context) {
        baseDeDatos = new BaseDeDatosSQLite(context);
    }

    public static void cerrar() {
        baseDeDatos.desconectar();
    }

    public static ViewModelInitializer<? extends ViewModel> inicializadorParaModelo(Modelo modelo) {
        return switch (modelo) {
            case MASCOTA -> mascotas;
        };
    }

    static final ViewModelInitializer<MascotasViewModel> mascotas = new ViewModelInitializer<>(
        MascotasViewModel.class,
        creationExtras -> {
            MascotasRepositorio repositorio = new MascotasRepositorio(baseDeDatos);
            return new MascotasViewModel(repositorio);
        }
    );
}
