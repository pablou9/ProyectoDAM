package es.ifp.petprotech.info.datos;

import android.provider.BaseColumns;

public class ContratoHorarios {

    /* no instanciable */
    private ContratoHorarios() {}

    public static final String NOMBRE_TABLA = "horarios";

    public static class Columnas implements BaseColumns {
        public static final String DIA_SEMANA = "dia_semana";
        public static final String LABORAL = "laboral";
        public static final String FESTIVO = "festivo";
        public static final String APERTURA = "apertura";
        public static final String CIERRE = "cierre";
        public static final String DESCANSO = "descanso";
    }

    public static final String CREAR_TABLA =
        "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA + " ( " +
            Columnas._ID + " INTEGER PRIMARY KEY NOT NULL, " +
            Columnas.DIA_SEMANA + " INTEGER CHECK (" + Columnas.DIA_SEMANA + " IN (1,2,3,4,5,6,7)), " +
            Columnas.LABORAL + " BOOLEAN DEFAULT TRUE, " +
            Columnas.FESTIVO + " BOOLEAN DEFAULT FALSE, " +
            Columnas.APERTURA + " INTEGER NOT NULL, " +
            Columnas.CIERRE + " INTEGER NOT NULL, " +
            Columnas.DESCANSO + " INTEGER NOT NULL)";

    public static final String ELIMINAR_TABLA =
        "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
}
