package es.ifp.petprotech.eventos.datos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import es.ifp.petprotech.app.util.FormatoFechaTiempo;
import es.ifp.petprotech.bd.BaseDeDatos;
import es.ifp.petprotech.bd.Repositorio;
import es.ifp.petprotech.bd.RepositorioSQLite;
import es.ifp.petprotech.eventos.model.Evento;
import es.ifp.petprotech.mascotas.model.Mascota;
import es.ifp.petprotech.medicacion.model.Medicamento;
import es.ifp.petprotech.veterinarios.model.Veterinario;

public class EventosRepositorio extends RepositorioSQLite<Evento> {

    public EventosRepositorio(BaseDeDatos<SQLiteDatabase> baseDeDatos) {
        super(baseDeDatos, ContratoEventos.NOMBRE_TABLA);
    }

    @Override
    protected Evento extraerEntidad(Cursor cursor) {
        long inicia = cursor.getLong(cursor.getColumnIndexOrThrow(ContratoEventos.Columnas.INICIO));
        long finaliza = cursor.getLong(cursor.getColumnIndexOrThrow(ContratoEventos.Columnas.FINALIZACION));

        Evento evento = Evento.nuevoEvento()
                .nombre(notNull(cursor.getString(cursor.getColumnIndexOrThrow(ContratoEventos.Columnas.NOMBRE))))
                .inicia(FormatoFechaTiempo.convertirTiempo(inicia))
                .finaliza(FormatoFechaTiempo.convertirTiempo(finaliza))
                .periocidad( cursor.getLong(cursor.getColumnIndexOrThrow(ContratoEventos.Columnas.PERIOCIDAD)))
                .build();

        evento.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ContratoEventos.Columnas._ID)));
        return evento;
    }

    @Override
    protected ContentValues extraerValores(Evento evento) {
        LocalDateTime inicia = evento.getInicia();
        LocalDateTime finaliza = evento.getFinaliza();

        Medicamento medicamento = evento.getMedicamento();
        Veterinario veterinario = evento.getVeterinario();

        ContentValues valores = new ContentValues();
        valores.put(ContratoEventos.Columnas.NOMBRE, evento.getNombre());
        valores.put(ContratoEventos.Columnas.MASCOTA_ID, evento.getMascota().getId());
        valores.put(ContratoEventos.Columnas.INICIO, FormatoFechaTiempo.segundosEpoch(inicia));
        valores.put(ContratoEventos.Columnas.FINALIZACION, FormatoFechaTiempo.segundosEpoch(finaliza));
        valores.put(ContratoEventos.Columnas.PERIOCIDAD, evento.getPeriocidad());

        if (medicamento == null) {
            valores.putNull(ContratoEventos.Columnas.MEDICAMENTO_ID);
        }
        else valores.put(ContratoEventos.Columnas.MEDICAMENTO_ID, medicamento.getId());

        if (veterinario == null) {
            valores.putNull(ContratoEventos.Columnas.VETERINARIO_ID);
        }
        else valores.put(ContratoEventos.Columnas.VETERINARIO_ID, veterinario.getId());

        return valores;
    }

    @Override
    protected void despuesDeSeleccionar(List<Evento> eventos) {
//        anadirMascotas(eventos);
//        anadirMedicacion(eventos);
    }

    private void anadirMascotas(List<Evento> eventos) {
        Repositorio<Mascota> mascotaRepositorio = getRepositorio(Mascota.class);

        long[] idsEventos = extraerIds(eventos);

        Map<Long, List<Mascota>> asociadas =
            mascotaRepositorio.seleccionarMuchosAUno(Mascota.class, idsEventos);

        for (Evento evento : eventos) {
            List<Mascota> mascotas = asociadas.get(evento.getId());

            if (mascotas != null)
                evento.setMascota(mascotas.get(0));
        }
    }

    private void anadirMedicacion(List<Evento> eventos) {
        Repositorio<Medicamento> medicamentoRepositorio = getRepositorio(Medicamento.class);

        long[] idsEventos = extraerIds(eventos);

        Map<Long, List<Medicamento>> asociadas =
            medicamentoRepositorio.seleccionarMuchosAUno(Medicamento.class, idsEventos);

        for (Evento evento : eventos) {
            List<Medicamento> medicamentos = asociadas.get(evento.getId());

            if (medicamentos != null)
                evento.setMedicacion(medicamentos.get(0));
        }
    }
}
