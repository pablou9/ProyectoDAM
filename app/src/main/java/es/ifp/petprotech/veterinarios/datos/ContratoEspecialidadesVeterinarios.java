package es.ifp.petprotech.veterinarios.datos;

import android.provider.BaseColumns;

public class ContratoEspecialidadesVeterinarios {
    
    /* no instanciable */
    private ContratoEspecialidadesVeterinarios() {}

    public static final String NOMBRE_TABLA = "especialidades_veterinarios";

    public static class Columnas implements BaseColumns {
        public static final String ID_VETERINARIO = "id_veterinario";
        public static final String ID_ESPECIALIDAD = "id_especialidad";
    }

    public static final String CREAR_TABLA =
        "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA + " ( " +
            Columnas._ID + " INTEGER PRIMARY KEY NOT NULL, " +
            Columnas.ID_VETERINARIO + " INTEGER NOT NULL, " +
            Columnas.ID_ESPECIALIDAD + " INTEGER NOT NULL)";

    public static final String ELIMINAR_TABLA =
        "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
}
