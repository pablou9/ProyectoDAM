package es.ifp.petprotech.profesionales.datos;

import android.provider.BaseColumns;

public class ContratoProfesiones {

    /* no instanciable */
    private ContratoProfesiones() {}

    public static final String NOMBRE_TABLA = "profesiones";

    public static class Columnas implements BaseColumns {
        public static final String NOMBRE = "nombre";
    }

    public static final String CREAR_TABLA =
        "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA + " ( " +
            Columnas.NOMBRE + " TEXT NOT NULL)";

    public static final String ELIMINAR_TABLA =
        "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
}
