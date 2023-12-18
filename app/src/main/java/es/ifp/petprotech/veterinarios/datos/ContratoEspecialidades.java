package es.ifp.petprotech.veterinarios.datos;

import android.provider.BaseColumns;

public class ContratoEspecialidades {

    /* no instanciable */
    private ContratoEspecialidades() {}

    public static final String NOMBRE_TABLA = "especialidades";

    public static class Columnas implements BaseColumns {
        public static final String NOMBRE = "nombre";
        public static final String SUBESPECIALIDAD = "subespecialidad";
    }

    public static final String CREAR_TABLA =
        "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA + " ( " +
            Columnas._ID + " INTEGER PRIMARY KEY NOT NULL, " +
            Columnas.NOMBRE + " TEXT NOT NULL " +
            Columnas.SUBESPECIALIDAD + " INTEGER)";

    public static final String ELIMINAR_TABLA =
        "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
}
