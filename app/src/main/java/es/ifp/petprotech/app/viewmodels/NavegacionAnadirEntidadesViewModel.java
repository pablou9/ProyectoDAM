package es.ifp.petprotech.app.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NavegacionAnadirEntidadesViewModel extends ViewModel {
    private static final String TAG = "NavegacionAnadirEntidad";

    public static final String PANTALLAS = "pantallas";

    public enum Pantalla {

        ANADIR_MASCOTA("anadir_mascota", false),
        ANADIR_FOTO_MASCOTA("anadir_foto_mascota", true),
        ANADIR_VETERINARIO("anadir_veterinario", true);

        private final String tag;
        private final boolean sePuedeSaltar;

        Pantalla(String tag, boolean sePuedeSaltar) {
            this.tag = tag;
            this.sePuedeSaltar = sePuedeSaltar;
        }

        public static List<Pantalla> todas() {
            return Arrays.stream(values()).collect(Collectors.toList());
        }

        public boolean sePuedeSaltar() {
            return sePuedeSaltar;
        }

        public String getTag() {
            return tag;
        }
    }

    private List<Pantalla> pantallas = new ArrayList<>();

    private int pantallaActualInd;
    private final MutableLiveData<Pantalla> pantallaActual = new MutableLiveData<>();
    private final MutableLiveData<Boolean> finalizado = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> usuarioHaInteractuado = new MutableLiveData<>(false);

    public void anadirPantallas(List<Pantalla> pantallas) {
        Log.d(TAG, "anadirPantallas: PANTALLAS: " + pantallas);

        this.pantallas = pantallas;
        pantallaActual.setValue(pantallas.get(0));
    }

    public LiveData<Pantalla> getPantallaActual() {
        return pantallaActual;
    }
    public LiveData<Boolean> getUsuarioHaInteractuado() {
        return usuarioHaInteractuado;
    }

    public MutableLiveData<Boolean> getFinalizado() {
        return finalizado;
    }

    public boolean esPrimeraPantalla(Pantalla pantalla) {
        return pantallas.get(0) == pantalla;
    }

    public boolean esUltimaPantalla(Pantalla pantalla) {
        Log.d(TAG, "esUltimaPantalla: PANTALLA: " + pantalla + ". PANTALLAS: " + pantallas);
        return pantallas.get(pantallas.size() -1) == pantalla;
    }

    public void registrarInteraccionDeUsuario(boolean haInteractuado) {
        if (!Boolean.valueOf(haInteractuado)
                    .equals(usuarioHaInteractuado.getValue()))
            usuarioHaInteractuado.setValue(haInteractuado);
    }

    public void siguiente() {
        boolean enUltimaPantalla = pantallaActualInd == pantallas.size() -1;

        if (enUltimaPantalla) {
            finalizado.setValue(true);
            return;
        }

        pantallaActual.setValue(pantallas.get(++pantallaActualInd));
    }

    public void atras() {
        boolean enPrimeraPantalla = pantallaActualInd == 0;

        if(enPrimeraPantalla) {
            finalizado.setValue(true);
            return;
        }

        pantallaActual.setValue(pantallas.get(--pantallaActualInd));
    }
}
