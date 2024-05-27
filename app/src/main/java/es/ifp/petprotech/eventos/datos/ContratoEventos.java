package es.ifp.petprotech.eventos.datos;

import android.provider.BaseColumns;

public class ContratoEventos {

    /* no instanciable */
    private ContratoEventos() {}

    public static final String NOMBRE_TABLA = "eventos";

    /* Inner class that defines the table contents */
    public static class Columnas implements BaseColumns {
        public static final String NOMBRE = "nombre";
        public static final String MASCOTA_ID = "mascota_id";
        public static final String INICIO = "inicio";
        public static final String FINALIZACION = "finalizacion";
        public static final String PERIOCIDAD = "periocidad";
        public static final String VETERINARIO_ID = "veterinario_id";
        public static final String MEDICAMENTO_ID = "medicamento_id";
    }

    public static final String CREAR_TABLA =
        "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA + " ( " +
            Columnas._ID + " INTEGER PRIMARY KEY NOT NULL, " +
            Columnas.NOMBRE + " TEXT NOT NULL, " +
            Columnas.INICIO + " INTEGER NOT NULL, " +
            Columnas.FINALIZACION + " INTEGER DEFAULT 0, " +
            Columnas.PERIOCIDAD + " INTEGER DEFAULT 0, " +
            Columnas.MASCOTA_ID + " INTEGER NOT NULL, " +
            Columnas.MEDICAMENTO_ID + " INTEGER, " +
            Columnas.VETERINARIO_ID + " INTEGER)";

    public static final String ELIMINAR_TABLA = "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
}