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
        // Esta base de datos es una base de datos de prueba, por lo que su política de
        // 'upgrade' es simplemente eliminar los datos y crear la base de datos de nuevo
        restart(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
