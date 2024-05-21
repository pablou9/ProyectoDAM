package es.ifp.petprotech.eventos.datos;

import android.provider.BaseColumns;

public class ContratoEventos {

    /* no instanciable */
    private ContratoEventos() {}

    public static final String NOMBRE_TABLA = "mascotas";

    /* Inner class that defines the table contents */
    public static class Columnas implements BaseColumns {
        public static final String NOMBRE = "nombre";
        public static final String MASCOTA_ID = "mascota_id";
        public static final String FECHA_INICIO = "fecha_inicio";
        public static final String FECHA_FINALIZACION = "fecha_finalizacion";
        public static final String PERIOCIDAD_DIAS = "periocidad_dias";
        public static final String CENTRO_ID = "centro_id";
    }

    public static final String CREAR_TABLA =
        "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA + " ( " +
            Columnas._ID + " INTEGER PRIMARY KEY NOT NULL, " +
            Columnas.NOMBRE + " TEXT NOT NULL, " +
            Columnas.FECHA_INICIO + " INTEGER NOT NULL, " +
            Columnas.FECHA_FINALIZACION + " INTEGER, " +
            Columnas.PERIOCIDAD_DIAS + " INTEGER DEFAULT 0, " +
            Columnas.MASCOTA_ID + " INTEGER NOT NULL, " +
            Columnas.CENTRO_ID + " INTEGER";

    public static final String ELIMINAR_TABLA =
        "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
}