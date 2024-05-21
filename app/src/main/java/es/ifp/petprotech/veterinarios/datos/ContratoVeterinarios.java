package es.ifp.petprotech.veterinarios.datos;

import android.provider.BaseColumns;

public class ContratoVeterinarios {

    /* no instanciable */
    private ContratoVeterinarios() {}

    public static final String NOMBRE_TABLA = "veterinarios";

    public static class Columnas implements BaseColumns {
        public static final String ID_CENTRO = "id_centro";
        public static final String ESPECIALIDAD = "especialidad";
        public static final String NOMBRE = "nombre";
    }

    public static final String CREAR_TABLA =
        "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA + " ( " +
            Columnas._ID + " INTEGER PRIMARY KEY NOT NULL, " +
            Columnas.ID_CENTRO + " INTEGER NOT NULL, " +
            Columnas.NOMBRE + " TEXT NOT NULL, " +
            Columnas.ESPECIALIDAD + " TEXT)";

    public static final String ELIMINAR_TABLA =
        "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
}
