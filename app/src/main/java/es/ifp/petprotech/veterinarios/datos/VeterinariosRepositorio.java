package es.ifp.petprotech.veterinarios.datos;

import static es.ifp.petprotech.veterinarios.datos.ContratoVeterinarios.Columnas.ESPECIALIDAD;
import static es.ifp.petprotech.veterinarios.datos.ContratoVeterinarios.Columnas.ID_CENTRO;
import static es.ifp.petprotech.veterinarios.datos.ContratoVeterinarios.Columnas.NOMBRE;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.Map;

import es.ifp.petprotech.bd.BaseDeDatos;
import es.ifp.petprotech.bd.Entidad;
import es.ifp.petprotech.bd.Repositorio;
import es.ifp.petprotech.bd.RepositorioSQLite;
import es.ifp.petprotech.centros.datos.CentrosProfesionalesRepositorio;
import es.ifp.petprotech.centros.model.CentroProfesional;
import es.ifp.petprotech.mascotas.model.Mascota;
import es.ifp.petprotech.veterinarios.model.Veterinario;

public class VeterinariosRepositorio extends RepositorioSQLite<Veterinario> {

    public VeterinariosRepositorio(BaseDeDatos<SQLiteDatabase> baseDeDatos) {
        super(baseDeDatos, ContratoVeterinarios.NOMBRE_TABLA);
    }

    @Override
    protected Veterinario extraerEntidad(Cursor cursor) {
        Repositorio<CentroProfesional> repositorioCentros =
                (CentrosProfesionalesRepositorio) getRepositorio(CentroProfesional.class);

        CentroProfesional centro = repositorioCentros
                .seleccionarPorId(cursor.getLong(cursor.getColumnIndexOrThrow(ID_CENTRO)));

        return Veterinario.nuevoVeterinario()
                .centro(centro)
                .nombre(cursor.getString(cursor.getColumnIndexOrThrow(NOMBRE)))
                .especialidad(cursor.getString(cursor.getColumnIndexOrThrow(ESPECIALIDAD)))
                .build();
    }

    @Override
    protected ContentValues extraerValores(Veterinario veterinario) {
        ContentValues values = new ContentValues();
        values.put(NOMBRE, veterinario.getNombre());
        values.put(ESPECIALIDAD, veterinario.getEspecialidad());
        values.put(ID_CENTRO, veterinario.getCentro().getId());

        return values;
    }

    @Override
    protected void despuesDeCrear(Veterinario veterinario) {
        for (Mascota mascota : veterinario.getMascotas()) {
            asociarMuchos(veterinario, mascota);
        }
    }

    @Override
    protected void despuesDeSeleccionar(List<Veterinario> veterinarios) {
        long[] idsVeterinarios = extraerIdsVeterianarios(veterinarios);

        Map<Long,List<Mascota>> mascotas =
                seleccionarPorAsociacion(
                        Mascota.class, idsVeterinarios);

        for (Veterinario veterinario : veterinarios)
            veterinario.setMascotas(mascotas.get(veterinario.getId()));
    }

    private long[] extraerIdsVeterianarios(List<Veterinario> veterinarios) {
        return veterinarios
                .stream()
                .mapToLong(Entidad::getId)
                .toArray();
    }
}
