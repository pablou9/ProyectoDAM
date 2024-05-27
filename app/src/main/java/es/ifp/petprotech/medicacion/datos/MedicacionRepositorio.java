package es.ifp.petprotech.medicacion.datos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.Map;

import es.ifp.petprotech.bd.BaseDeDatos;
import es.ifp.petprotech.bd.RepositorioSQLite;
import es.ifp.petprotech.mascotas.model.Mascota;
import es.ifp.petprotech.medicacion.model.Medicacion;
import es.ifp.petprotech.medicacion.model.Medicamento;

public class MedicacionRepositorio extends RepositorioSQLite<Medicacion> {

    public MedicacionRepositorio(BaseDeDatos<SQLiteDatabase> baseDeDatos) {
        super(baseDeDatos, ContratoMedicacion.NOMBRE_TABLA);
    }

    @Override
    protected Medicacion extraerEntidad(Cursor cursor) {
        String cantidad = cursor.getString(cursor.getColumnIndexOrThrow(ContratoMedicacion.Columnas.CANTIDAD));
        int dias = cursor.getInt(cursor.getColumnIndexOrThrow(ContratoMedicacion.Columnas.DIAS));
        int horas = cursor.getInt(cursor.getColumnIndexOrThrow(ContratoMedicacion.Columnas.HORAS));

        Medicacion medicacion = Medicacion.nuevaMedicacion()
                                            .cantidad(cantidad)
                                            .dias(dias)
                                            .horas(horas)
                                            .build();

        medicacion.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ContratoMedicacion.Columnas._ID)));

        return medicacion;
    }

    @Override
    protected ContentValues extraerValores(Medicacion medicacion) {
        ContentValues valores = new ContentValues();

        valores.put(ContratoMedicacion.Columnas.MASCOTA_ID, medicacion.getMascota().getId());
        valores.put(ContratoMedicacion.Columnas.MEDICAMENTO_ID, medicacion.getMedicamento().getId());
        valores.put(ContratoMedicacion.Columnas.CANTIDAD, medicacion.getCantidad());
        valores.put(ContratoMedicacion.Columnas.DIAS, medicacion.getDias());
        valores.put(ContratoMedicacion.Columnas.HORAS, medicacion.getHoras());

        return valores;
    }

    @Override
    protected void despuesDeSeleccionar(List<Medicacion> listaMedicacion) {

        long[] idsMedicacion = extraerIds(listaMedicacion);

        Map<Long, List<Mascota>> mascotas = seleccionarMuchosAUno(Mascota.class, idsMedicacion);
        Map<Long, List<Medicamento>> medicamento = seleccionarMuchosAUno(Medicamento.class, idsMedicacion);

        for (Medicacion medicacion : listaMedicacion) {
            List<Mascota> mascotaMedicacion = mascotas.get(medicacion.getId());
            List<Medicamento> medicamentoMedicacion = medicamento.get(medicacion.getId());

            if (mascotaMedicacion != null)
                medicacion.setMascota(mascotaMedicacion.get(0));

            if (medicamentoMedicacion != null) {
                medicacion.setMedicamento(medicamentoMedicacion.get(0));
            }
        }
    }
}
