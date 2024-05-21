package es.ifp.petprotech.centros.datos;

import android.provider.BaseColumns;

public class ContratoCentrosProfesionales {

    /* no instanciable */
    private ContratoCentrosProfesionales() {}

    public static final String NOMBRE_TABLA = "centros_profesionales";

    public static class Columnas implements BaseColumns {
        public static final String NOMBRE = "nombre";
        public static final String DIRECCION = "direccion";
        public static final String PAGINA_WEB = "pagina_web";
        public static final String EMAIL = "email";
        public static final String TELEFONO = "telefono";
        public static final String APERTURA = "apertura";
        public static final String CIERRE = "cierre";
        public static final String DIAS_TRABAJO = "dias_trabajo";
    }

    public static final String CREAR_TABLA =
        "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA + " ( " +
            Columnas._ID + " INTEGER PRIMARY KEY NOT NULL, " +
            Columnas.NOMBRE + " TEXT NOT NULL, " +
            Columnas.DIRECCION + " TEXT, " +
            Columnas.APERTURA + " TEXT, " +
            Columnas.CIERRE + " TEXT, " +
            Columnas.DIAS_TRABAJO + " TEXT, " +
            Columnas.PAGINA_WEB + " TEXT, " +
            Columnas.EMAIL + " TEXT, " +
            Columnas.TELEFONO + " TEXT)";

    public static final String ELIMINAR_TABLA = "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
}
