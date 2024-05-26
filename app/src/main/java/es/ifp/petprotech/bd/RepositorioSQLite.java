package es.ifp.petprotech.bd;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class RepositorioSQLite<T extends Entidad> implements Repositorio<T> {

    private static final String TAG = "RepositorioSQLite";

    private final SQLiteDatabase baseDeDatos;
    private final String TABLA;

    private Map<Class<? extends Entidad>, Repositorio<? extends Entidad>> asociados;
    private Map<Class<? extends Entidad>, AsociaciacionUnoAMuchos> indiceAsociaciones;
    private Map<Class<? extends Entidad>, AsociaciacionMuchosAMuchos> indiceAsociacionesAMuchos;

    public RepositorioSQLite(BaseDeDatos<SQLiteDatabase> baseDeDatos, String tabla) {
        this.baseDeDatos = baseDeDatos.conectar();
        this.TABLA = tabla;
    }

    @Override
    public void setRepositoriosAsociados(Map<Class<? extends Entidad>, Repositorio<? extends Entidad>> contiene) {
        this.asociados = contiene;
    }

    public void anadirAsociaciones(Map<Class<? extends Entidad>, AsociaciacionUnoAMuchos> indiceAsociaciones) {
        this.indiceAsociaciones = indiceAsociaciones;
    }

    public void anadirAsociacionesAMuchos(Map<Class<? extends Entidad>, AsociaciacionMuchosAMuchos> indiceAsociaciones) {
        this.indiceAsociacionesAMuchos = indiceAsociaciones;
    }

    private T extraerEntidadYAnadirId(Cursor cursor) {
        T entidad = extraerEntidad(cursor);

        if (entidad.getId() == 0) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            entidad.setId(id);
        }

        return entidad;
    }

    protected abstract T extraerEntidad(Cursor cursor);
    protected abstract ContentValues extraerValores(T entidad);

    protected void despuesDeSeleccionar(List<T> entidades) {}
    protected void despuesDeCrear(T entidad) {}

    protected void antesDeEliminar(T entidad) {}

    protected String notNull(String s) {
        return s == null ? "" : s;
    }

    protected long[] extraerIds(List<? extends Entidad> entidades) {
        return entidades
                .stream()
                .mapToLong(Entidad::getId)
                .toArray();
    }

    protected <E> List<E> aplanarColeccion(Collection<List<E>> coleccion) {
        return coleccion
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    protected <E extends Entidad> Repositorio<E> getRepositorio(Class<E> clase) {
        if (asociados == null)
            throw new IllegalStateException("Debes anadir los repositorios contenidos");

        return (Repositorio<E>) asociados.get(clase);
    }

    private String queryParamsEn(long[] ids) {
        return IntStream
                .range(0, ids.length)
                .mapToObj(i -> "?")
                .collect(Collectors.joining(","));
    }

    @Override
    public boolean crear(T entidad) {
        try {
            validarEntidadParaCreacion(entidad);

            long id = baseDeDatos.insert(TABLA, null, extraerValores(entidad));

            if (id != -1) {
                entidad.setId(id);
                despuesDeCrear(entidad);
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
    public boolean asociarAEstaEntidad(T entidad, Entidad asociar) {
        try {
            if (asociar.getId() == 0)
                throw new IllegalArgumentException("La entidad a asociar debe haber sido " +
                        "creada por su respectivo repositorio antes de la asociación");

            AsociaciacionUnoAMuchos asociacion = extraerAsociacionUnoAMuchos(asociar.getClass());

            boolean insertarNueva = entidad.getId() < 1;

            if(insertarNueva) {
                boolean creada = crear(entidad);

                if (!creada)
                    return false;
            }

            ContentValues valorIdForanea = new ContentValues();
            valorIdForanea.put(asociacion.columnaClaveForanea, entidad.getId());

            int registrosActualizados = baseDeDatos.update(
                asociacion.tablaObjetivo,
                valorIdForanea,
                "_ID = ?",
                new String[]{String.valueOf(asociar.getId())});

            return registrosActualizados == 1;
        }
        catch (SQLiteConstraintException e) {
            Log.e(TAG, "asociarAEstaEntidad: violacion de restriccion" + e.getMessage());
            return false;
        }
    }

    private AsociaciacionUnoAMuchos extraerAsociacionUnoAMuchos(Class<?> claseAsociacion) {
        if (indiceAsociaciones == null)
            throw new IllegalStateException("El índice de asociaciones debe de estar " +
                    "inicializado antes de cualquier asociación");

        AsociaciacionUnoAMuchos asociacion = indiceAsociaciones.get(claseAsociacion);

        if (asociacion == null)
            throw new IllegalStateException("No se ha encontrado la entidad " +
                    claseAsociacion.getSimpleName() + " en el índice de asociaciones");

        return asociacion;
    }

    @Override
    public boolean asociarMuchos(T entidad, Entidad asociar) {
        try {
            AsociaciacionMuchosAMuchos asociacion = extraerAsociacionAMuchos(asociar);

            boolean insertarNueva = entidad.getId() < 1;

            if(insertarNueva) {
                ContentValues valores = extraerValores(entidad);

                long id = baseDeDatos.insert(TABLA, null, valores);

                if (id == -1)
                    return false;

                entidad.setId(id);
                despuesDeCrear(entidad);
            }

            ContentValues valoresIntermedios = new ContentValues();
            valoresIntermedios.put(asociacion.columnaIdEnIntermedia, entidad.getId());
            valoresIntermedios.put(asociacion.columnaAsociacionEnIntermedia, asociar.getId());

            long id = baseDeDatos.insert(asociacion.tablaIntermedia, null, valoresIntermedios);

            return id != -1;
        }
        catch (SQLiteConstraintException e) {
            Log.e(TAG, "asociarMuchos: violacion de restriccion" + e.getMessage());
            return false;
        }
    }

    private AsociaciacionMuchosAMuchos extraerAsociacionAMuchos(Entidad asociar) {
        if (asociar.getId() < 1)
            throw new IllegalArgumentException("La entidad que se va a asociar debe de haber " +
                    "sido insertada a la base de datos antes de la asociación");

        if (indiceAsociacionesAMuchos == null)
            throw new IllegalStateException("El índice de asociaciones debe de estar " +
                    "inicializado antes de cualquier asociación");

        AsociaciacionMuchosAMuchos asociacion = indiceAsociacionesAMuchos.get(asociar.getClass());

        if (asociacion == null)
            throw new IllegalStateException("No se ha encontrado la entidad " +
                    asociar.getClass().getSimpleName() + " en el índice de asociaciones a muchos");

        return asociacion;
    }

    @Override
    public T seleccionarPorId(long id) {
        if (id < 1)
            throw new IllegalArgumentException("No se puede seleccionar. Id inválida: " + id);

        try (Cursor cursor = baseDeDatos.rawQuery(
                "SELECT * FROM " + TABLA + "  WHERE " + BaseColumns._ID + " = ?", new String[]{String.valueOf(id)}))
        {
            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                T entidad = extraerEntidadYAnadirId(cursor);
                despuesDeSeleccionar(List.of(entidad));
                return entidad;
            }
            else return null;
        }
    }

    @Override
    public List<T> seleccionarTodo() {
        try (Cursor cursor = baseDeDatos.rawQuery("SELECT * FROM " + TABLA, null))
        {
            List<T> entidades = new ArrayList<>();
            while (cursor.moveToNext()) {
                T entidad = extraerEntidadYAnadirId(cursor);
                entidades.add(entidad);
            }

            despuesDeSeleccionar(entidades);

            return entidades;
        }
    }

    @Override
    public List<T> seleccionarEn(long[] ids) {
        String[] values = Arrays
                .stream(ids)
                .mapToObj(String::valueOf)
                .toArray(String[]::new);

        String queryParams = queryParamsEn(ids);

        try (Cursor cursor = baseDeDatos.rawQuery(
            "SELECT * FROM " + TABLA + " WHERE " + BaseColumns._ID + " IN ("+queryParams+")", values))
        {
            List<T> entidades = new ArrayList<>();
            while (cursor.moveToNext()) {
                T entidad = extraerEntidadYAnadirId(cursor);
                entidades.add(entidad);
            }

            despuesDeSeleccionar(entidades);

            return entidades;
        }
    }

    @Override
    public List<T> seleccionarPor(String atributo, String valor) {
        try (Cursor cursor = baseDeDatos.rawQuery(
                "SELECT * FROM " + TABLA +
                " WHERE " + atributo + " = ?", new String[]{valor}))
        {
            List<T> entidades = new ArrayList<>();
            while (cursor.moveToNext()) {
                T entidad = extraerEntidadYAnadirId(cursor);
                entidades.add(entidad);
            }
            despuesDeSeleccionar(entidades);

            return entidades;
        }
    }

    @Override
    public <E extends Entidad> Map<Long, List<E>> seleccionarUnoAMuchos(Class<E> claseAsociacion, long[] idsForaneas) {
        if (indiceAsociaciones == null)
            throw new IllegalStateException("El índice de asociación debe estar " +
                    "inicializado antes de cualquier selección por asociación");

        AsociaciacionUnoAMuchos asociacion = indiceAsociaciones.get(claseAsociacion);

        if (asociacion == null)
            throw new IllegalStateException("No se ha encontrado la entidad " +
                    claseAsociacion.getSimpleName() + "en el índice de asociaciones a muchos");

        String seleccion = idsForaneas == null
                ? ""
                : " WHERE " + TABLA+"."+BaseColumns._ID +
                (idsForaneas.length == 1 ? " = ?" : " IN ("+queryParamsEn(idsForaneas)+")");

        String[] argumentosSeleccion = idsForaneas == null
                ? null
                : Arrays.stream(idsForaneas)
                .mapToObj(String::valueOf)
                .toArray(String[]::new);

        Map<Long, List<E>> entidades = new HashMap<>();

        try (Cursor cursor = baseDeDatos.rawQuery(
            "SELECT "+asociacion.tablaObjetivo+".*, "+TABLA+"."+BaseColumns._ID + " AS id_where" +
                " FROM " + asociacion.tablaObjetivo +
                " INNER JOIN " + TABLA +
                " ON " + TABLA+"."+BaseColumns._ID +
                " = " + asociacion.tablaObjetivo+"."+asociacion.columnaClaveForanea +
                seleccion, argumentosSeleccion))
        {
            while (cursor.moveToNext()) {

                long id = cursor.getLong(cursor.getColumnIndexOrThrow("id_where"));

                List<E> listaEntidades = entidades.computeIfAbsent(id, k-> new ArrayList<>());
                RepositorioSQLite<E> repo = (RepositorioSQLite<E>) getRepositorio(claseAsociacion);
                listaEntidades.add(repo.extraerEntidadYAnadirId(cursor));
            }
        }

        return entidades;
    }

    @Override
    public <E extends Entidad> Map<Long, List<E>> seleccionarMuchosAUno(Class<E> claseAsociacion, long[] idsPropias) {
        if (indiceAsociaciones == null)
            throw new IllegalStateException("El índice de asociación debe estar " +
                    "inicializado antes de cualquier selección por asociación");

        AsociaciacionUnoAMuchos asociacion = indiceAsociaciones.get(claseAsociacion);

        if (asociacion == null)
            throw new IllegalStateException("No se ha encontrado la entidad " +
                claseAsociacion.getSimpleName() + "en el índice de asociaciones a muchos");

        String seleccion = idsPropias == null
                ? ""
                : " WHERE " + TABLA+"."+BaseColumns._ID +
                (idsPropias.length == 1 ? " = ?" : " IN ("+queryParamsEn(idsPropias)+")");

        String[] argumentosSeleccion = idsPropias == null
                ? null
                : Arrays.stream(idsPropias)
                .mapToObj(String::valueOf)
                .toArray(String[]::new);

        Map<Long, List<E>> entidades = new HashMap<>();

        try (Cursor cursor = baseDeDatos.rawQuery(
            "SELECT "+asociacion.tablaObjetivo+".*, "+TABLA+"."+BaseColumns._ID + " AS id_where" +
                " FROM " + asociacion.tablaObjetivo +
                " INNER JOIN " + TABLA +
                " ON " + TABLA +"."+asociacion.columnaClaveForanea +
                " = " + asociacion.tablaObjetivo+"."+BaseColumns._ID +
            seleccion,
            argumentosSeleccion))
        {
            while (cursor.moveToNext()) {

                long id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));

                List<E> listaEntidades = entidades.computeIfAbsent(id, k-> new ArrayList<>());
                RepositorioSQLite<E> repo = (RepositorioSQLite<E>) getRepositorio(claseAsociacion);
                listaEntidades.add(repo.extraerEntidadYAnadirId(cursor));
            }
        }

        return entidades;
    }

    @Override
    public <E extends Entidad> Map<Long, List<E>> seleccionarMuchosAMuchos(Class<E> claseAsociacion, long[] where) {
        if (indiceAsociacionesAMuchos == null)
            throw new IllegalStateException("El índice de asociación a muchos debe estar " +
                    "inicializado antes de cualquier selección por asociación a muchos");

        AsociaciacionMuchosAMuchos asociacion = indiceAsociacionesAMuchos.get(claseAsociacion);

        if (asociacion == null)
            throw new IllegalStateException("No se ha encontrado la entidad " +
                    claseAsociacion.getSimpleName() + "en el índice de asociaciones a muchos");

        Map<Long,List<E>> entidades = new HashMap<>();

        String seleccion = where == null
                ? ""
                : " WHERE " + TABLA+"."+BaseColumns._ID +
                (where.length == 1 ? " = ?" : " IN ("+queryParamsEn(where)+")");

        String[] argumentosSeleccion = where == null
                ? null
                : Arrays.stream(where)
                .mapToObj(String::valueOf)
                .toArray(String[]::new);

        Log.d(TAG, "seleccionarPorAsociacionAMuchos: ARGS" + Arrays.toString(argumentosSeleccion));

        try (Cursor cursor = baseDeDatos.rawQuery(
                // SELECT TABLA.*, TABLA_INTERMEDIA.COLUMNA_ID_EN_INTERMEDIA
                "SELECT " + asociacion.tablaObjetivo+".*, " + TABLA+"."+BaseColumns._ID +
                    " FROM " + asociacion.tablaObjetivo +
                    " INNER JOIN " + asociacion.tablaIntermedia +
                        " ON " + asociacion.tablaIntermedia+"."+asociacion.columnaAsociacionEnIntermedia +
                        " = " + asociacion.tablaObjetivo+"."+BaseColumns._ID +
                    " INNER JOIN " + TABLA +
                        " ON " + TABLA+"."+BaseColumns._ID +
                        " = " + asociacion.tablaIntermedia+"."+asociacion.columnaIdEnIntermedia +
                    seleccion,
                argumentosSeleccion))
        {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));

                List<E> listaEntidades = entidades.computeIfAbsent(id, k-> new ArrayList<>());
                RepositorioSQLite<E> repositorio = (RepositorioSQLite<E>) getRepositorio(claseAsociacion);
                listaEntidades.add(repositorio.extraerEntidadYAnadirId(cursor));
            }
        }

        return entidades;
    }

    @Override
    public boolean actualizar(T entidad) {
        try {

            int registrosActualizados = baseDeDatos.update(
                    TABLA,
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
    public boolean eliminar(T entidad) {
        antesDeEliminar(entidad);
        int registrosEliminados = baseDeDatos.delete(
                TABLA,
            "_ID = ?",
            new String[]{String.valueOf(entidad.getId())});

        return registrosEliminados == 1;
    }

    public static class AsociaciacionUnoAMuchos {
        private final String tablaObjetivo;
        private final String columnaClaveForanea;

        public AsociaciacionUnoAMuchos(String tablaObjetivo, String columnaClaveForanea) {
            this.tablaObjetivo = tablaObjetivo;
            this.columnaClaveForanea = columnaClaveForanea;
        }

        @Override
        @NonNull
        public String toString() {
            return "AsociaciacionUnoAMuchos{" +
                    "tablaClaveForanea='" + tablaObjetivo + '\'' +
                    ", columnaClaveForanea='" + columnaClaveForanea + '\'' +
                    '}';
        }
    }

    public static class AsociaciacionMuchosAMuchos {
        private final String tablaIntermedia;
        private final String tablaObjetivo;
        private final String columnaIdEnIntermedia;
        private final String columnaAsociacionEnIntermedia;

        public AsociaciacionMuchosAMuchos(String tablaObjetivo,
                                          String tablaIntermedia,
                                          String columnaIdEnIntermedia,
                                          String columnaAsociacionEnIntermedia)
        {
            this.tablaIntermedia = tablaIntermedia;
            this.tablaObjetivo = tablaObjetivo;
            this.columnaIdEnIntermedia = columnaIdEnIntermedia;
            this.columnaAsociacionEnIntermedia = columnaAsociacionEnIntermedia;
        }

        @Override
        @NonNull
        public String toString() {
            return "AsociaciacionAMuchos{" +
                    "tablaIntermedia='" + tablaIntermedia + '\'' +
                    ", columnaIdEntidad='" + tablaObjetivo + '\'' +
                    ", columnaIdEnIntermedia='" + columnaIdEnIntermedia + '\'' +
                    ", columnaAsociacionEnIntermedia='" + columnaAsociacionEnIntermedia + '\'' +
                    '}';
        }
    }
}
