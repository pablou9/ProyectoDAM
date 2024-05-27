package es.ifp.petprotech.medicacion.datos;

import android.provider.BaseColumns;

public class ContratoMedicacion {
    
    /* no instanciable */
    private ContratoMedicacion() {}

    public static final String NOMBRE_TABLA = "medicacion";

    /* Inner class that defines the table contents */
    public static class Columnas implements BaseColumns {
        public static final String MASCOTA_ID = "mascota_id";
        public static final String MEDICAMENTO_ID = "medicamento_id";
        public static final String CANTIDAD = "cantidad";
        public static final String HORAS = "horas";
        public static final String DIAS = "dias";
        
    }

    public static final String CREAR_TABLA =
            "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA + " ( " +
                    Columnas._ID + " INTEGER PRIMARY KEY NOT NULL, " +
                    Columnas.MASCOTA_ID + " INTEGER NOT NULL, " +
                    Columnas.MEDICAMENTO_ID + " TEXT NOT NULL, " +
                    Columnas.CANTIDAD + " TEXT NOT NULL, " +
                    Columnas.HORAS + " INTEGER NOT NULL, " +
                    Columnas.DIAS + " INTEGER)";

    public static final String ELIMINAR_TABLA = "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
}
