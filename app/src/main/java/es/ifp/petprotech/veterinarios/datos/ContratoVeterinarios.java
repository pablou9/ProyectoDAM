package es.ifp.petprotech.veterinarios.datos;

import android.provider.BaseColumns;

public class ContratoVeterinarios {

    /* no instanciable */
    private ContratoVeterinarios() {}

    public static final String NOMBRE_TABLA = "veterinarios";

    public static class Columnas implements BaseColumns {
        public static final String ID_PROFESIONAL = "id_profesional";
        public static final String NUMERO_COLEGIADO = "numero_colegiado";
    }

    public static final String CREAR_TABLA =
        "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA + " ( " +
            Columnas._ID + " INTEGER PRIMARY KEY NOT NULL, " +
            Columnas.ID_PROFESIONAL + " INTEGER NOT NULL " +
            Columnas.NUMERO_COLEGIADO + " INTEGER UNIQUE NOT NULL)";

    public static final String ELIMINAR_TABLA =
        "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
}
