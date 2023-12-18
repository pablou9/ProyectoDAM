package es.ifp.petprotech.profesionales.datos;

import android.provider.BaseColumns;

public class ContratoProfesionales {

    /* no instanciable */
    private ContratoProfesionales() {}

    public static final String NOMBRE_TABLA = "profesionales";

    public static class Columnas implements BaseColumns {
        public static final String NOMBRE = "nombre";
        public static final String APELLIDOS = "apellidos";
        public static final String ID_INFO_CONTACTO = "id_info_contacto";
    }

    public static final String CREAR_TABLA =
        "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA + " ( " +
            Columnas._ID + " INTEGER PRIMARY KEY NOT NULL, " +
            Columnas.NOMBRE + " TEXT NOT NULL " +
            Columnas.APELLIDOS + " TEXT NOT NULL " +
            Columnas.ID_INFO_CONTACTO + " INTEGER)";

    public static final String ELIMINAR_TABLA =
        "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
}
