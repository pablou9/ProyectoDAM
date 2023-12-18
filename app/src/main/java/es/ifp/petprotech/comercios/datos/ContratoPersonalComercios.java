package es.ifp.petprotech.comercios.datos;

import android.provider.BaseColumns;

public class ContratoPersonalComercios {

    /* no instanciable */
    private ContratoPersonalComercios() {}

    public static final String NOMBRE_TABLA = "personal_comercios";

    public static class Columnas implements BaseColumns {
        public static final String ID_PROFESIONAL = "id_profesional";
        public static final String ID_COMERCIO = "id_comercio";
    }

    public static final String CREAR_TABLA =
        "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA + " ( " +
            Columnas._ID + " INTEGER PRIMARY KEY NOT NULL, " +
            Columnas.ID_PROFESIONAL + " INTEGER NOT NULL " +
            Columnas.ID_COMERCIO + " INTEGER NOT NULL)";

    public static final String ELIMINAR_TABLA =
        "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
}
