package es.ifp.petprotech.bd.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import es.ifp.petprotech.bd.BaseDeDatos;
import es.ifp.petprotech.bd.RepositorioSQLite;

public class RepositorioAsociacionDePrueba extends RepositorioSQLite<EntidadAsociacionPrueba> {

    public RepositorioAsociacionDePrueba(BaseDeDatos<SQLiteDatabase> baseDeDatos) {
        super(baseDeDatos, ContratoDePrueba.NOMBRE_TABLA_PRUEBA);
    }

    @Override
    protected EntidadAsociacionPrueba extraerEntidad(Cursor cursor) {
        long id = cursor.getInt(cursor.getColumnIndex(ContratoDePrueba.ColumnasAsociacion._ID));
        String nombre = cursor.getString(cursor.getColumnIndex(ContratoDePrueba.ColumnasAsociacion.NOMBRE));
        int random = cursor.getInt(cursor.getColumnIndex(ContratoDePrueba.ColumnasAsociacion.RANDOM));
        int idForanea = cursor.getInt(cursor.getColumnIndex(ContratoDePrueba.ColumnasAsociacion.CLAVE_FORANEA));

        return new EntidadAsociacionPrueba(id, nombre, random, idForanea);
    }

    @Override
    protected ContentValues extraerValores(EntidadAsociacionPrueba entidad) {
        ContentValues valores = new ContentValues();
        valores.put(ContratoDePrueba.ColumnasAsociacion.NOMBRE, entidad.getNombre());
        valores.put(ContratoDePrueba.ColumnasAsociacion.RANDOM, entidad.getRandom());
        valores.put(ContratoDePrueba.ColumnasAsociacion.CLAVE_FORANEA, entidad.getIdForanea());
        return valores;
    }
}
