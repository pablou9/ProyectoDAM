package es.ifp.petprotech.info.datos;

import android.provider.BaseColumns;

public class ContratoDirecciones {

    /* no instanciable */
    private ContratoDirecciones() {}

    public static final String NOMBRE_TABLA = "direcciones";

    public static class Columnas implements BaseColumns {
        public static final String CALLE = "calle";
        public static final String NUMERO = "numero";
        public static final String ESCALERA = "escalera";
        public static final String PLANTA = "planta";
        public static final String PUERTA = "puerta";
        public static final String CODIGO_POSTAL = "codigo_postal";
        public static final String CIUDAD = "ciudad";
        public static final String PROVINCIA = "provincia";
    }

    public static final String CREAR_TABLA =
        "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA + " ( " +
            Columnas._ID + " INTEGER PRIMARY KEY NOT NULL, " +
            Columnas.CALLE + " TEXT NOT NULL, " +
            Columnas.NUMERO + " INTEGER NOT NULL, " +
            Columnas.ESCALERA + " TEXT, " +
            Columnas.PLANTA + " TEXT, " +
            Columnas.PUERTA + " TEXT, " +
            Columnas.CODIGO_POSTAL + " TEXT NOT NULL, " +
            Columnas.CIUDAD + " TEXT NOT NULL, " +
            Columnas.PROVINCIA + " TEXT NOT NULL)";

    public static final String ELIMINAR_TABLA =
        "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
}
