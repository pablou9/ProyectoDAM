package es.ifp.petprotech.info.datos;

import android.provider.BaseColumns;

public class ContratoInfoContacto {

    /* no instanciable */
    private ContratoInfoContacto() {}

    public static final String NOMBRE_TABLA = "info_contacto";

    public static class Columnas implements BaseColumns {
        public static final String PAGINA_WEB = "pagina_web";
        public static final String EMAIL = "email";
        public static final String TELEFONO_FIJO = "telefono_fijo";
        public static final String TELEFONO_MOVIL = "telefono_movil";
    }

    public static final String CREAR_TABLA =
        "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA + " ( " +
            Columnas._ID + " INTEGER PRIMARY KEY NOT NULL, " +
            Columnas.PAGINA_WEB + " TEXT, " +
            Columnas.EMAIL + " TEXT, " +
            Columnas.TELEFONO_FIJO + " TEXT, " +
            Columnas.TELEFONO_MOVIL + " TEXT)";

    public static final String ELIMINAR_TABLA =
        "DROP TABLE IF EXISTS " + NOMBRE_TABLA;

}
