package es.ifp.petprotech.app.datos;

import android.content.SharedPreferences;

public class SharedPreferencesRepositorio {

    public static final String NOMBRE_PREFERENCIAS = "es.ifp.petprotech.preferencias";
    private static SharedPreferencesRepositorio instancia;

    public static SharedPreferencesRepositorio instancia(SharedPreferences preferencias) {
        if (instancia == null)
            instancia = new SharedPreferencesRepositorio(preferencias);

        return instancia;
    }

    private final SharedPreferences preferencias;

    private SharedPreferencesRepositorio(SharedPreferences preferencias) {
        this.preferencias = preferencias;
    }

    public void guardar(String clave, String valor) {
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString(clave, valor);
        editor.apply();
    }

    public String recuperar(String clave) {
        return preferencias.getString(clave, "");
    }
}
