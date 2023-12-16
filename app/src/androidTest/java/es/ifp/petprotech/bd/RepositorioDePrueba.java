package es.ifp.petprotech.bd;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RepositorioDePrueba extends RepositorioSQLite<EntidadDePrueba> {

    public RepositorioDePrueba(SQLiteDatabase baseDeDatos) {
        super(baseDeDatos, ContratoDePrueba.NOMBRE_TABLA);
    }

    @Override
    protected EntidadDePrueba extraerEntidad(Cursor cursor) {
        long id = cursor.getInt(cursor.getColumnIndex(ContratoDePrueba.Columnas._ID));
        String nombre = cursor.getString(cursor.getColumnIndex(ContratoDePrueba.Columnas.NOMBRE));
        int edad = cursor.getInt(cursor.getColumnIndex(ContratoDePrueba.Columnas.EDAD));
        boolean prueba = cursor.getInt(cursor.getColumnIndex(ContratoDePrueba.Columnas.PRUEBA)) == 1;

        return new EntidadDePrueba(id, nombre, edad, prueba);
    }

    @Override
    protected ContentValues extraerValores(EntidadDePrueba entidad) {
        ContentValues valores = new ContentValues();
        valores.put(ContratoDePrueba.Columnas.NOMBRE, entidad.getNombre());
        valores.put(ContratoDePrueba.Columnas.EDAD, entidad.getEdad());
        valores.put(ContratoDePrueba.Columnas.PRUEBA, entidad.isPrueba());
        return valores;
    }
}
