package es.ifp.petprotech.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDeDatosSQLite implements BaseDeDatos<SQLiteDatabase> {

    private static final String NOMBRE_BASE_DE_DATOS = "PetProtech.db";

    private final SQLiteOpenHelper gestorBaseDeDatos;
    private SQLiteDatabase baseDeDatos;

    public BaseDeDatosSQLite(Context context) {
        gestorBaseDeDatos = new GestorBaseDeDatos(context, NOMBRE_BASE_DE_DATOS);
    }

    @Override
    public SQLiteDatabase conectar() {
        if (baseDeDatos == null)
            baseDeDatos = gestorBaseDeDatos.getWritableDatabase();

        return baseDeDatos;
    }

    @Override
    public void desconectar() {
        baseDeDatos.close();
        gestorBaseDeDatos.close();
        baseDeDatos = null;
    }

    private static class GestorBaseDeDatos extends SQLiteOpenHelper {

        public static final int DATABASE_VERSION = 1;

        public GestorBaseDeDatos(Context context, String nombreBaseDeDatos) {
            super(context, nombreBaseDeDatos, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Create tables
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            super.onDowngrade(db, oldVersion, newVersion);
        }
    }
}
