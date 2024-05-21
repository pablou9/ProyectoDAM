package es.ifp.petprotech.bd.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import es.ifp.petprotech.bd.BaseDeDatos;
import es.ifp.petprotech.bd.RepositorioSQLite;

public class RepositorioDePrueba extends RepositorioSQLite<EntidadDePrueba> {

    public RepositorioDePrueba(BaseDeDatos<SQLiteDatabase> baseDeDatos) {
        this(baseDeDatos, false);
    }

    public RepositorioDePrueba(BaseDeDatos<SQLiteDatabase> baseDeDatos, boolean invalidarIndices) {
        super(baseDeDatos, ContratoDePrueba.NOMBRE_TABLA_PRUEBA);

        if (!invalidarIndices) {
            anadirAsociaciones(Map.of(EntidadAsociacionPrueba.class, ContratoDePrueba.ColumnasPrueba.CLAVE_FORANEA));
            anadirAsociacionesAMuchos(Map.of(
                    EntidadAsociacionAMuchosPrueba.class,
                    new AsociaciacionAMuchos(ContratoDePrueba.NOMBRE_TABLA_INTERMEDIA,
                            ContratoDePrueba.ColumnasPrueba._ID,
                            ContratoDePrueba.ColumnasIntermedias.PRUEBA_ID,
                            ContratoDePrueba.ColumnasIntermedias.ASOCIACION_ID)));
        }
    }

    @Override
    protected EntidadDePrueba extraerEntidad(Cursor cursor) {
        long id = cursor.getInt(cursor.getColumnIndex(ContratoDePrueba.ColumnasPrueba._ID));
        String nombre = cursor.getString(cursor.getColumnIndex(ContratoDePrueba.ColumnasPrueba.NOMBRE));
        int edad = cursor.getInt(cursor.getColumnIndex(ContratoDePrueba.ColumnasPrueba.EDAD));
        boolean prueba = cursor.getInt(cursor.getColumnIndex(ContratoDePrueba.ColumnasPrueba.PRUEBA)) == 1;

        return new EntidadDePrueba(id, nombre, edad, prueba);
    }

    @Override
    protected ContentValues extraerValores(EntidadDePrueba entidad) {
        ContentValues valores = new ContentValues();
        valores.put(ContratoDePrueba.ColumnasPrueba.NOMBRE, entidad.getNombre());
        valores.put(ContratoDePrueba.ColumnasPrueba.EDAD, entidad.getEdad());
        valores.put(ContratoDePrueba.ColumnasPrueba.PRUEBA, entidad.isPrueba());
        return valores;
    }
}
