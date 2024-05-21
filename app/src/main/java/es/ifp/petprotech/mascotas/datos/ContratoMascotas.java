package es.ifp.petprotech.mascotas.datos;

import android.provider.BaseColumns;

public class ContratoMascotas {

    /* no instanciable */
    private ContratoMascotas() {}

    public static final String NOMBRE_TABLA = "mascotas";

    /* Inner class that defines the table contents */
    public static class Columnas implements BaseColumns {
        public static final String NOMBRE = "nombre";
        public static final String FECHA_NACIMIENTO = "fecha_nacimiento";
        public static final String ESPECIE = "especie";
        public static final String RAZA = "raza";
        public static final String CHIP = "chip";
    }

    public static final String URI_FOTO = "uri_foto";

    public static final String CREAR_TABLA =
        "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA + " ( " +
            Columnas._ID + " INTEGER PRIMARY KEY NOT NULL, " +
            Columnas.NOMBRE + " TEXT NOT NULL, " +
            Columnas.FECHA_NACIMIENTO + " INTEGER, " +
            Columnas.ESPECIE + " TEXT NOT NULL, " +
            Columnas.RAZA + " TEXT, " +
            Columnas.CHIP + " TEXT UNIQUE CHECK (length("+Columnas.CHIP+") >= 15) )";

    public static final String ELIMINAR_TABLA =
        "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
}
