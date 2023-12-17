package es.ifp.petprotech.bd;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public abstract class RepositorioSQLite<T extends Entidad> implements Repositorio<T> {

    private static final String TAG = "RepositorioSQLite";

    private final SQLiteDatabase baseDeDatos;
    private final String tabla;

    public RepositorioSQLite(BaseDeDatos<SQLiteDatabase> baseDeDatos, String tabla) {
        this.baseDeDatos = baseDeDatos.conectar();
        this.tabla = tabla;
    }

    protected abstract T extraerEntidad(Cursor cursor);
    protected abstract ContentValues extraerValores(T entidad);

    @Override
    public boolean crear(T entidad) {
        try {
            validarEntidadParaCreacion(entidad);

            long id = baseDeDatos.insert(tabla, null, extraerValores(entidad));

            if (id != -1) {
                entidad.setId(id);
                return true;
            }

            return false;
        }
        catch (SQLiteConstraintException e) {
            Log.e(TAG, "crear: violacion de restriccion" + e.getMessage());
            return false;
        }
    }

    private void validarEntidadParaCreacion(T entidad) {
        if (entidad.getId() > 0)
            throw new IllegalArgumentException(
                "Entidades a crear no deben tener un valor de id asignado. El valor de id será " +
                    "sobreescrito por esta clase, lo que puede llevar a comportamientos indeseados " +
                    "en el programa");
    }

    @Override
    public T seleccionarConId(long id) {
        try (Cursor cursor = baseDeDatos.rawQuery(
            "SELECT * FROM " + tabla + "  WHERE _ID = ?", new String[]{String.valueOf(id)}))
        {
            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                return extraerEntidad(cursor);
            }
            else return null;
        }
    }

    @Override
    public T seleccionar() {
        return null;
    }

    @Override
    public boolean actualizar(T entidad) {
        try {
            int registrosActualizados = baseDeDatos.update(
                tabla,
                extraerValores(entidad),
                "_ID = ?",
                new String[]{String.valueOf(entidad.getId())});

            return registrosActualizados == 1;
        }
        catch (Exception e) {
            Log.e(TAG, "actualizar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(T t) {
        int registrosEliminados = baseDeDatos.delete(
            tabla,
            "_ID = ?",
            new String[]{String.valueOf(t.getId())});

        return registrosEliminados == 1;
    }
}
