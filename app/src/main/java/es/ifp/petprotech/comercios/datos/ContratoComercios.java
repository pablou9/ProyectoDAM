package es.ifp.petprotech.comercios.datos;

import android.provider.BaseColumns;

public class ContratoComercios {

    /* no instanciable */
    private ContratoComercios() {}

    public static final String NOMBRE_TABLA = "comercios";

    public static class Columnas implements BaseColumns {
        public static final String NOMBRE = "nombre";
        public static final String ID_DIRECCION = "id_direccion";
        public static final String ID_INFO_CONTACTO = "id_info_contacto";
    }

    public static final String CREAR_TABLA =
        "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA + " ( " +
            Columnas._ID + " INTEGER PRIMARY KEY NOT NULL, " +
            Columnas.NOMBRE + " TEXT NOT NULL, " +
            Columnas.ID_DIRECCION + " INTEGER, " +
            Columnas.ID_INFO_CONTACTO + " INTEGER NOT NULL)";

    public static final String ELIMINAR_TABLA =
        "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
}
