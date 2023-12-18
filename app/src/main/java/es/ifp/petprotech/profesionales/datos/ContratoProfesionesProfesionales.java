package es.ifp.petprotech.profesionales.datos;

import android.provider.BaseColumns;

public class ContratoProfesionesProfesionales {
    
    /* no instanciable */
    private ContratoProfesionesProfesionales() {}

    public static final String NOMBRE_TABLA = "profesiones_profesionales";

    public static class Columnas implements BaseColumns {
        public static final String ID_PROFESION = "id_profesion";
        public static final String ID_PROFESIONAL = "id_profesional";
    }

    public static final String CREAR_TABLA =
        "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA + " ( " +
            Columnas._ID + " INTEGER PRIMARY KEY NOT NULL, " +
            Columnas.ID_PROFESION + " INTEGER NOT NULL " +
            Columnas.ID_PROFESIONAL + " INTEGER NOT NULL)";

    public static final String ELIMINAR_TABLA =
        "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
}
