package es.ifp.petprotech.centros.datos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import es.ifp.petprotech.app.util.FormatoFechaTiempo;
import es.ifp.petprotech.bd.BaseDeDatos;
import es.ifp.petprotech.bd.RepositorioSQLite;
import es.ifp.petprotech.centros.model.RangoHorario;

public class HorariosRepositorio extends RepositorioSQLite<RangoHorario> {

    public HorariosRepositorio(BaseDeDatos<SQLiteDatabase> baseDeDatos) {
        super(baseDeDatos, ContratoHorariosCentros.NOMBRE_TABLA);
    }

    @Override
    protected RangoHorario extraerEntidad(Cursor cursor) {
        String apertura = cursor.getString(cursor.getColumnIndexOrThrow(ContratoHorariosCentros.Columnas.APERTURA));
        String cierre = cursor.getString(cursor.getColumnIndexOrThrow(ContratoHorariosCentros.Columnas.CIERRE));

        return new RangoHorario(FormatoFechaTiempo.convertirTiempo(apertura), FormatoFechaTiempo.convertirTiempo(cierre));
    }

    @Override
    protected ContentValues extraerValores(RangoHorario entidad) {
        return null;
    }
}
