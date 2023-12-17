package es.ifp.petprotech.viewmodels;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import es.ifp.petprotech.bd.BaseDeDatos;
import es.ifp.petprotech.bd.BaseDeDatosSQLite;
import es.ifp.petprotech.mascotas.model.MascotasRepositorio;
import es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel;

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

    static final ViewModelInitializer<AnadirMascotaViewModel> mascotas = new ViewModelInitializer<>(
        AnadirMascotaViewModel.class,
        creationExtras -> {
            MascotasRepositorio repositorio = new MascotasRepositorio(baseDeDatos);
            return new AnadirMascotaViewModel(repositorio);
        }
    );
}
