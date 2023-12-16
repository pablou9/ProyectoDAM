package es.ifp.petprotech.mascotas.datos;

import android.provider.BaseColumns;

public class ContratoMascota {

    /* no instanciable */
    private ContratoMascota() {}

    public static final String NOMBRE_TABLA = "mascotas";

    /* Inner class that defines the table contents */
    public static class Columnas implements BaseColumns {
        public static final String NOMBRE = "nombre";
        public static final String FECHA_NACIMIENTO = "fecha_nacimiento";
        public static final String FAMILIA = "familia";
        public static final String ESPECIE = "especie";
        public static final String RAZA = "raza";
        public static final String CHIP = "chip";
    }

    public static final String CREAR_TABLA_MASCOTAS =
        "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA + " ( " +
            Columnas._ID + " INTEGER PRIMARY KEY NOT NULL, " +
            Columnas.NOMBRE + " TEXT NOT NULL " +
            Columnas.FECHA_NACIMIENTO + " INTEGER NOT NULL " +
            Columnas.FAMILIA + " TEXT NOT NULL " +
            Columnas.ESPECIE + " TEXT NOT NULL " +
            Columnas.RAZA + " TEXT " +
            Columnas.CHIP + " TEXT UNIQUE)";

    public static final String ELIMINAR_TABLA_MASCOTAS =
        "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
}
