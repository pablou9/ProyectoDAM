package es.ifp.petprotech.medicacion.datos;

import android.provider.BaseColumns;

public class ContratoMedicamentos {

    /* no instanciable */
    private ContratoMedicamentos() {}

    public static final String NOMBRE_TABLA = "medicamentos";

    /* Inner class that defines the table contents */
    public static class Columnas implements BaseColumns {
        public static final String NOMBRE = "nombre";
        public static final String TIPO = "tipo";
    }

    public static final String CREAR_TABLA =
        "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA + " ( " +
            Columnas._ID + " INTEGER PRIMARY KEY NOT NULL, " +
            Columnas.NOMBRE + " TEXT UNIQUE NOT NULL, " +
            Columnas.TIPO + " TEXT)";

    public static final String ELIMINAR_TABLA = "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
}