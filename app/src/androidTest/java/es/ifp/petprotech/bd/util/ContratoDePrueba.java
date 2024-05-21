package es.ifp.petprotech.bd.util;

import android.provider.BaseColumns;

public class ContratoDePrueba {

    public static final String NOMBRE_TABLA_PRUEBA = "entidad_prueba";
    public static final String NOMBRE_TABLA_INTERMEDIA = "tabla_intermedia";
    public static final String NOMBRE_TABLA_ASOCIACION = "entidad_asociacion";

    public static final class ColumnasPrueba implements BaseColumns {
        public static final String NOMBRE = "nombre";
        public static final String EDAD = "edad";
        public static final String PRUEBA = "prueba";
    }

    public static final class ColumnasIntermedias implements BaseColumns {
        public static final String PRUEBA_ID = "prueba_id";
        public static final String ASOCIACION_ID = "asociacion_id";
    }

    public static final class ColumnasAsociacion implements BaseColumns {
        public static final String NOMBRE = "nombre";
        public static final String RANDOM = "random";
        public static final String CLAVE_FORANEA = "clave_foranea";
    }

    public static final String CREAR_TABLA_PRUEBA =
        "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA_PRUEBA + " ( " +
            ColumnasPrueba._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ColumnasPrueba.NOMBRE + " TEXT NOT NULL UNIQUE, " +
            ColumnasPrueba.EDAD + " INTEGER NOT NULL, " +
            ColumnasPrueba.PRUEBA + " BOOLEAN DEFAULT TRUE)";

    public static final String CREAR_TABLA_INTERMEDIA =
        "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA_INTERMEDIA + " ( " +
            ColumnasIntermedias._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ColumnasIntermedias.PRUEBA_ID + " INTEGER NOT NULL, " +
            ColumnasIntermedias.ASOCIACION_ID + " INTEGER NOT NULL)";

    public static final String CREAR_TABLA_ASOCIACION =
        "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA_ASOCIACION + " ( " +
            ColumnasAsociacion._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ColumnasAsociacion.NOMBRE + " TEXT NOT NULL UNIQUE, " +
            ColumnasAsociacion.RANDOM + " INTEGER NOT NULL, " +
            ColumnasAsociacion.CLAVE_FORANEA + " INTEGER)";

    public static final String ELIMINAR_TABLA_PRUEBA =
        "DROP TABLE " + NOMBRE_TABLA_PRUEBA;

    public static final String ELIMINAR_TABLA_INTERMEDIA =
        "DROP TABLE " + NOMBRE_TABLA_INTERMEDIA;

    public static final String ELIMINAR_TABLA_ASOCIACION =
        "DROP TABLE " + NOMBRE_TABLA_ASOCIACION;
}
