package es.ifp.petprotech.centros.datos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import es.ifp.petprotech.bd.BaseDeDatos;
import es.ifp.petprotech.bd.Repositorio;
import es.ifp.petprotech.bd.RepositorioSQLite;
import es.ifp.petprotech.centros.model.CentroProfesional;
import es.ifp.petprotech.mascotas.model.Mascota;
import es.ifp.petprotech.veterinarios.model.Veterinario;

public class CentrosProfesionalesRepositorio extends RepositorioSQLite<CentroProfesional> {

    public CentrosProfesionalesRepositorio(BaseDeDatos<SQLiteDatabase> baseDeDatos) {
        super(baseDeDatos, ContratoCentrosProfesionales.NOMBRE_TABLA);
    }

    @Override
    protected CentroProfesional extraerEntidad(Cursor cursor) {
        return CentroProfesional.nuevoCentro()
            .nombre(cursor.getString(cursor.getColumnIndexOrThrow(ContratoCentrosProfesionales.Columnas.NOMBRE)))
            .direccion(cursor.getString(cursor.getColumnIndexOrThrow(ContratoCentrosProfesionales.Columnas.DIRECCION)))
            .paginaWeb(cursor.getString(cursor.getColumnIndexOrThrow(ContratoCentrosProfesionales.Columnas.PAGINA_WEB)))
            .email(cursor.getString(cursor.getColumnIndexOrThrow(ContratoCentrosProfesionales.Columnas.EMAIL)))
            .telefono(cursor.getString(cursor.getColumnIndexOrThrow(ContratoCentrosProfesionales.Columnas.TELEFONO)))
            .build();
    }

    @Override
    protected ContentValues extraerValores(CentroProfesional centroProfesional) {
        ContentValues valores = new ContentValues();
        valores.put(ContratoCentrosProfesionales.Columnas.NOMBRE, centroProfesional.getNombre());
        valores.put(ContratoCentrosProfesionales.Columnas.DIRECCION, centroProfesional.getDireccion());
        valores.put(ContratoCentrosProfesionales.Columnas.PAGINA_WEB, centroProfesional.getPaginaWeb());
        valores.put(ContratoCentrosProfesionales.Columnas.EMAIL, centroProfesional.getEmail());
        valores.put(ContratoCentrosProfesionales.Columnas.TELEFONO, centroProfesional.getTelefono());
        valores.put(ContratoCentrosProfesionales.Columnas.APERTURA, centroProfesional.getTelefono());
        valores.put(ContratoCentrosProfesionales.Columnas.CIERRE, centroProfesional.getTelefono());
        valores.put(ContratoCentrosProfesionales.Columnas.DIAS_TRABAJO, centroProfesional.getTelefono());

        return valores;
    }

    @Override
    protected void despuesDeSeleccionar(List<CentroProfesional> centros) {
        List<Veterinario> veterinarios = anadirVeterinariosACentro(centros);
        anadirMascotasAVeterinarios(veterinarios);
    }

    private List<Veterinario> anadirVeterinariosACentro(List<CentroProfesional> centros) {
        Log.d(TAG, "anadirVeterinariosACentro: CENTROS: " + centros);
        long[] idsCentros = extraerIds(centros);
        Log.d(TAG, "anadirVeterinariosACentro: IDS: " + Arrays.toString(idsCentros));

        Map<Long, List<Veterinario>> veterinariosPorId = seleccionarMuchosAUno(Veterinario.class, idsCentros);

        Log.d(TAG, "anadirVeterinariosACentro: VETES: " + veterinariosPorId);

        List<Veterinario> veterinarios = new ArrayList<>();

        for (CentroProfesional centroProfesional : centros) {
            List<Veterinario> veterinariosCentro = veterinariosPorId.get(centroProfesional.getId());
            centroProfesional.setVeterinarios(veterinariosCentro);
            assert veterinariosCentro != null;
            veterinarios.addAll(veterinariosCentro);
        }

        return veterinarios;
    }

    private void anadirMascotasAVeterinarios(List<Veterinario> veterinarios) {
        Repositorio<Veterinario> veterinarioRepositorio = getRepositorio(Veterinario.class);

        long[] idsVeterinarios = extraerIds(veterinarios);

        Map<Long, List<Mascota>> mascotas =
            veterinarioRepositorio.seleccionarMuchosAMuchos(Mascota.class, idsVeterinarios);

        for (Veterinario veterinario : veterinarios)
            veterinario.setMascotas(mascotas.get(veterinario.getId()));
    }

    private static final String TAG = "CentrosProfesionalesRep";

    @Override
    protected void despuesDeCrear(CentroProfesional centroProfesional) {
        Log.d(TAG, "despuesDeCrear: creado: " + centroProfesional);

        for (Veterinario veterinario : centroProfesional.getVeterinarios()) {
            if (veterinario.getId() == 0)
                getRepositorio(Veterinario.class).crear(veterinario);

            asociarAEstaEntidad(centroProfesional, veterinario);
        }
    }

    @Override
    protected void antesDeEliminar(CentroProfesional entidad) {
        super.antesDeEliminar(entidad);
    }
}
