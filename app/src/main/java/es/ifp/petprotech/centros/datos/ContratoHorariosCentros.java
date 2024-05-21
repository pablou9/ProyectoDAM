package es.ifp.petprotech.centros.datos;

import android.provider.BaseColumns;

public class ContratoHorariosCentros {

    /* no instanciable */
    private ContratoHorariosCentros() {}

    public static final String NOMBRE_TABLA = "horarios_centros_profesionales";

    public static class Columnas implements BaseColumns {
        public static final String APERTURA = "apertura";
        public static final String CIERRE = "cierre";
        public static final String ID_CENTRO = "id_centro";
    }

    public static final String CREAR_TABLA =
        "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA + " ( " +
            Columnas._ID + " INTEGER PRIMARY KEY NOT NULL, " +
            Columnas.APERTURA + " INTEGER NOT NULL, " +
            Columnas.CIERRE + " INTEGER NOT NULL, " +
            Columnas.ID_CENTRO + " INTEGER NOT NULL)";

    public static final String ELIMINAR_TABLA =
        "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
}
