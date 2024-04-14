package es.ifp.petprotech.profesionales.datos;

import android.provider.BaseColumns;

public class ContratoHorariosProfesionales {

    /* no instanciable */
    private ContratoHorariosProfesionales() {}

    public static final String NOMBRE_TABLA = "horarios_profesionales";

    public static class Columnas implements BaseColumns {
        public static final String ID_HORARIO = "id_horario";
        public static final String ID_PROFESIONAL = "id_profesional";
    }

    public static final String CREAR_TABLA =
        "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA + " ( " +
            Columnas._ID + " INTEGER PRIMARY KEY NOT NULL, " +
            Columnas.ID_HORARIO + " INTEGER NOT NULL, " +
            Columnas.ID_PROFESIONAL + " INTEGER NOT NULL)";

    public static final String ELIMINAR_TABLA =
        "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
}
