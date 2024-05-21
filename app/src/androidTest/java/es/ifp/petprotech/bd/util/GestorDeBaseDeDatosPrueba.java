package es.ifp.petprotech.bd.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GestorDeBaseDeDatosPrueba extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public GestorDeBaseDeDatosPrueba(Context context, String nombreBaseDeDatos) {
        super(context, nombreBaseDeDatos, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ContratoDePrueba.CREAR_TABLA_PRUEBA);
        db.execSQL(ContratoDePrueba.CREAR_TABLA_INTERMEDIA);
        db.execSQL(ContratoDePrueba.CREAR_TABLA_ASOCIACION);
    }

    public void restart(SQLiteDatabase db) {
        db.execSQL(ContratoDePrueba.ELIMINAR_TABLA_PRUEBA);
        db.execSQL(ContratoDePrueba.ELIMINAR_TABLA_INTERMEDIA);
        db.execSQL(ContratoDePrueba.ELIMINAR_TABLA_ASOCIACION);
        onCreate(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new UnsupportedOperationException("La base de datos debe ser destruida y " +
            "reconstruida al lanzar las pruebas, no debes hacer upgrades");
    }

}
