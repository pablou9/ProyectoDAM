package es.ifp.petprotech.bd.util;

import android.provider.BaseColumns;

public class ContratoDePrueba {

    public static final String NOMBRE_TABLA = "entidad_prueba";

    public static final class Columnas implements BaseColumns {
        public static final String NOMBRE = "nombre";
        public static final String EDAD = "edad";
        public static final String PRUEBA = "prueba";
    }

    public static final String CREAR_TABLA =
        "CREATE TABLE " + NOMBRE_TABLA + " ( " +
            Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Columnas.NOMBRE + " TEXT NOT NULL, UNIQUE, " +
            Columnas.EDAD + " INTEGER NOT NULL, " +
            Columnas.PRUEBA + " BOOLEAN DEFAULT TRUE)";

    public static final String ELIMINAR_TABLA =
        "DROP TABLE " + NOMBRE_TABLA;
}
