package es.ifp.petprotech.medicacion.datos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import es.ifp.petprotech.bd.BaseDeDatos;
import es.ifp.petprotech.bd.RepositorioSQLite;
import es.ifp.petprotech.medicacion.model.Medicamento;

public class MedicamentosRepositorio extends RepositorioSQLite<Medicamento> {

    public MedicamentosRepositorio(BaseDeDatos<SQLiteDatabase> baseDeDatos) {
        super(baseDeDatos, ContratoMedicamentos.NOMBRE_TABLA);
    }

    @Override
    protected Medicamento extraerEntidad(Cursor cursor) {
        String nombre = cursor.getString(cursor.getColumnIndexOrThrow(ContratoMedicamentos.Columnas.NOMBRE));
        String uso = cursor.getString(cursor.getColumnIndexOrThrow(ContratoMedicamentos.Columnas.TIPO));

        Medicamento medicamento = new Medicamento(nombre, uso);
        medicamento.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ContratoMedicamentos.Columnas._ID)));

        return medicamento;
    }

    @Override
    protected ContentValues extraerValores(Medicamento medicamento) {

        ContentValues valores = new ContentValues();
        valores.put(ContratoMedicamentos.Columnas.NOMBRE, medicamento.getNombre());
        valores.put(ContratoMedicamentos.Columnas.TIPO, medicamento.getUso());

        return valores;
    }
}
