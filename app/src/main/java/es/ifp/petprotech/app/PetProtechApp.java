package es.ifp.petprotech.app;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import es.ifp.petprotech.app.model.FabricaViewModel;
import es.ifp.petprotech.app.model.Modelo;
import es.ifp.petprotech.bd.BaseDeDatos;
import es.ifp.petprotech.bd.BaseDeDatosSQLite;

public class PetProtechApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        BaseDeDatos<SQLiteDatabase> bd = new BaseDeDatosSQLite(this);

        // Inicializa repositorios
        for (Modelo modelo : Modelo.values())
            modelo.initRepositorio(bd);

        // Inicializa fábricas de ViewModels
        for (FabricaViewModel fabrica : FabricaViewModel.values())
            fabrica.initFabrica(this, fabrica.clase());
    }
}
