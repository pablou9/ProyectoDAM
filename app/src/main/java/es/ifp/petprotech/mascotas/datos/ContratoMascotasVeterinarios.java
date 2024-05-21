package es.ifp.petprotech.mascotas.datos;

import android.provider.BaseColumns;

public class ContratoMascotasVeterinarios {

    /* no instanciable */
    private ContratoMascotasVeterinarios() {}

    public static final String NOMBRE_TABLA = "mascotas_veterinarios";

    /* Inner class that defines the table contents */
    public static class Columnas implements BaseColumns {
        public static final String ID_MASCOTA = "id_mascota";
        public static final String ID_VETERINARIO = "id_veterinario";
    }

    public static final String CREAR_TABLA =
            "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA + " ( " +
                Columnas._ID + " INTEGER PRIMARY KEY NOT NULL, " +
                Columnas.ID_MASCOTA + " INTEGER NOT NULL, " +
                Columnas.ID_VETERINARIO + " INTEGER NOT NULL)";

    public static final String ELIMINAR_TABLA =
            "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
}
