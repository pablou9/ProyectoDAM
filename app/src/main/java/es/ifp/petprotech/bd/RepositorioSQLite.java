package es.ifp.petprotech.bd;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

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

    private Map<Class<? extends Entidad>, Repositorio<? extends Entidad>> contiene;
    private Map<Class<? extends Entidad>, String> indiceAsociaciones;
    private Map<Class<? extends Entidad>, AsociaciacionAMuchos> indiceAsociacionesAMuchos;

    public RepositorioSQLite(BaseDeDatos<SQLiteDatabase> baseDeDatos, String tabla) {
        this.baseDeDatos = baseDeDatos.conectar();
        this.TABLA = tabla;
    }

    public void setRepositoriosContenidos(Map<Class<? extends Entidad>, Repositorio<? extends Entidad>> contiene) {
        this.contiene = contiene;
    }

    public void anadirAsociaciones(Map<Class<? extends Entidad>, String> indiceAsociaciones) {
        this.indiceAsociaciones = indiceAsociaciones;
    }

    public void anadirAsociacionesAMuchos(Map<Class<? extends Entidad>, AsociaciacionAMuchos> indiceAsociaciones) {
        this.indiceAsociacionesAMuchos = indiceAsociaciones;
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
        if (contiene == null)
            throw new IllegalStateException("Debes sobreescribir el método contiene() desde la " +
                    "subclase desde la que se invoca este método");

        return (Repositorio<E>) contiene.get(clase);
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
    public boolean asociar(T entidad, Entidad asociar) {
        try {
            String columnaClaveForanea = extraerColumnaClaveForanea(asociar);

            boolean insertarNueva = entidad.getId() < 1;

            if(insertarNueva) {
                ContentValues valores = extraerValores(entidad);
                valores.put(columnaClaveForanea, asociar.getId());

                long id = baseDeDatos.insert(TABLA, null, valores);

                if (id != -1) {
                    entidad.setId(id);
                    despuesDeCrear(entidad);
                    return true;
                }

                return false;
            }
            else {
                // La entidad ya existe, actualizar la clave foránea de la entidad
                ContentValues valorIdForanea = new ContentValues();
                valorIdForanea.put(columnaClaveForanea, asociar.getId());

                int registrosActualizados = baseDeDatos.update(
                        TABLA,
                    valorIdForanea,
                    "_ID = ?",
                    new String[]{String.valueOf(entidad.getId())});

                return registrosActualizados == 1;
            }
        }
        catch (SQLiteConstraintException e) {
            Log.e(TAG, "crear: violacion de restriccion" + e.getMessage());
            return false;
        }
    }

    private String extraerColumnaClaveForanea(Entidad asociar) {
        if (asociar.getId() < 1)
            throw new IllegalArgumentException("La entidad que se va a asociar debe de haber " +
                    "sido insertada a la base de datos antes de la asociación");

        if (indiceAsociaciones == null)
            throw new IllegalStateException("El índice de asociaciones debe de estar " +
                    "inicializado antes de cualquier asociación");

        String columnaForanea = indiceAsociaciones.get(asociar.getClass());

        if (columnaForanea == null)
            throw new IllegalStateException("No se ha encontrado la entidad " +
                    asociar.getClass().getSimpleName() + " en el índice de asociaciones");

        return columnaForanea;
    }

    @Override
    public boolean asociarMuchos(T entidad, Entidad asociar) {
        try {
            AsociaciacionAMuchos asociacion = extraerAsociacionAMuchos(asociar);

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
            Log.e(TAG, "crear: violacion de restriccion" + e.getMessage());
            return false;
        }
    }

    private AsociaciacionAMuchos extraerAsociacionAMuchos(Entidad asociar) {
        if (asociar.getId() < 1)
            throw new IllegalArgumentException("La entidad que se va a asociar debe de haber " +
                    "sido insertada a la base de datos antes de la asociación");

        if (indiceAsociaciones == null)
            throw new IllegalStateException("El índice de asociaciones debe de estar " +
                    "inicializado antes de cualquier asociación");

        AsociaciacionAMuchos asociacion = indiceAsociacionesAMuchos.get(asociar.getClass());

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
                T entidad = extraerEntidad(cursor);
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
                T entidad = extraerEntidad(cursor);
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
                T entidad = extraerEntidad(cursor);
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
                T entidad = extraerEntidad(cursor);
                entidades.add(entidad);
            }
            despuesDeSeleccionar(entidades);

            return entidades;
        }
    }

    @Override
    public Map<Long, List<T>> seleccionarPorAsociacion(Class<?> claseAsociacion, long[] where) {
        if (indiceAsociaciones == null)
            throw new IllegalStateException("El índice de asociación debe estar " +
                    "inicializado antes de cualquier selección por asociación");

        String columnaClaveForanea = indiceAsociaciones.get(claseAsociacion);

        if (columnaClaveForanea == null)
            throw new IllegalStateException("No se ha encontrado la entidad " +
                claseAsociacion.getSimpleName() + "en el índice de asociaciones a muchos");

        String seleccion = where == null
                ? ""
                : " WHERE " + columnaClaveForanea +
                (where.length == 1 ? " = ?" : " IN ("+queryParamsEn(where)+")");

        String[] argumentosSeleccion = where == null
                ? null
                : Arrays.stream(where)
                .mapToObj(String::valueOf)
                .toArray(String[]::new);

        Map<Long, List<T>> entidades = new HashMap<>();

        try (Cursor cursor = baseDeDatos.rawQuery("SELECT * FROM " + TABLA + seleccion, argumentosSeleccion))
        {
            while (cursor.moveToNext()) {

                long id = cursor.getLong(cursor.getColumnIndexOrThrow(columnaClaveForanea));

                List<T> listaEntidades = entidades.computeIfAbsent(id, k-> new ArrayList<>());
                listaEntidades.add(extraerEntidad(cursor));
            }
        }

        return entidades;
    }

    @Override
    public Map<Long, List<T>> seleccionarPorAsociacionAMuchos(Class<?> claseAsociacion, long[] where) {
        if (indiceAsociacionesAMuchos == null)
            throw new IllegalStateException("El índice de asociación a muchos debe estar " +
                    "inicializado antes de cualquier selección por asociación a muchos");

        AsociaciacionAMuchos asociacion = indiceAsociacionesAMuchos.get(claseAsociacion);

        if (asociacion == null)
            throw new IllegalStateException("No se ha encontrado la entidad " +
                    claseAsociacion.getSimpleName() + "en el índice de asociaciones a muchos");

        Map<Long,List<T>> entidades = new HashMap<>();

        String seleccion = where == null
                ? ""
                : " WHERE " + asociacion.tablaIntermedia+"."+asociacion.columnaAsociacionEnIntermedia +
                (where.length == 1 ? " = ?" : " IN ("+queryParamsEn(where)+")");

        String[] argumentosSeleccion = where == null
                ? null
                : Arrays.stream(where)
                .mapToObj(String::valueOf)
                .toArray(String[]::new);

        try (Cursor cursor = baseDeDatos.rawQuery(
                // SELECT TABLA.*, TABLA_INTERMEDIA.COLUMNA_ID_EN_INTERMEDIA
                "SELECT " + TABLA+".*, " + asociacion.tablaIntermedia+"."+asociacion.columnaAsociacionEnIntermedia +
                    " FROM " + TABLA +
                        " INNER JOIN " + asociacion.tablaIntermedia +
                        " ON " + asociacion.tablaIntermedia+"."+asociacion.columnaIdEnIntermedia +
                        " = " + TABLA +"."+asociacion.columnaIdEntidad +
                    seleccion,
                argumentosSeleccion))
        {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(
                    cursor.getColumnIndexOrThrow(asociacion.columnaAsociacionEnIntermedia));

                List<T> listaEntidades = entidades.computeIfAbsent(id, k-> new ArrayList<>());
                listaEntidades.add(extraerEntidad(cursor));
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

    public static class AsociaciacionAMuchos {
        private final String tablaIntermedia;
        private final String columnaIdEntidad;
        private final String columnaIdEnIntermedia;
        private final String columnaAsociacionEnIntermedia;

        public AsociaciacionAMuchos(String tablaIntermedia,
                                    String columnaIdEntidad,
                                    String columnaIdEnIntermedia,
                                    String columnaAsociacionEnIntermedia)
        {
            this.tablaIntermedia = tablaIntermedia;
            this.columnaIdEntidad = columnaIdEntidad;
            this.columnaIdEnIntermedia = columnaIdEnIntermedia;
            this.columnaAsociacionEnIntermedia = columnaAsociacionEnIntermedia;
        }

        @Override
        public String toString() {
            return "AsociaciacionAMuchos{" +
                    "tablaIntermedia='" + tablaIntermedia + '\'' +
                    ", columnaIdEntidad='" + columnaIdEntidad + '\'' +
                    ", columnaIdEnIntermedia='" + columnaIdEnIntermedia + '\'' +
                    ", columnaAsociacionEnIntermedia='" + columnaAsociacionEnIntermedia + '\'' +
                    '}';
        }
    }
}
