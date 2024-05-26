package es.ifp.petprotech.bd;

import static es.ifp.petprotech.bd.util.ContratoDePrueba.ColumnasIntermedias;
import static es.ifp.petprotech.bd.util.ContratoDePrueba.ColumnasPrueba;
import static es.ifp.petprotech.bd.util.ContratoDePrueba.NOMBRE_TABLA_INTERMEDIA;
import static es.ifp.petprotech.bd.util.ContratoDePrueba.NOMBRE_TABLA_PRUEBA;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import es.ifp.petprotech.bd.util.ContratoDePrueba;
import es.ifp.petprotech.bd.util.EntidadAsociacionAMuchosPrueba;
import es.ifp.petprotech.bd.util.EntidadAsociacionPrueba;
import es.ifp.petprotech.bd.util.EntidadDePrueba;
import es.ifp.petprotech.bd.util.GestorDeBaseDeDatosPrueba;
import es.ifp.petprotech.bd.util.RepositorioAsociacionDePrueba;
import es.ifp.petprotech.bd.util.RepositorioDePrueba;
import es.ifp.petprotech.mascotas.model.Mascota;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder
public class RepositorioSQLiteTest {

    public static final String DATABASE_NAME = "TestDatabase.db";

    private static GestorDeBaseDeDatosPrueba gestorBaseDeDatos;
    private static SQLiteDatabase bd;
    private Repositorio<EntidadDePrueba> repositorio;

    @BeforeClass
    public static void recrearBaseDeDatos() {
        Context context = ApplicationProvider.getApplicationContext();

        context.deleteDatabase(DATABASE_NAME);

        gestorBaseDeDatos = new GestorDeBaseDeDatosPrueba(context, DATABASE_NAME);
        bd = gestorBaseDeDatos.getWritableDatabase();
    }

    @Before
    public void init() {
        gestorBaseDeDatos.restart(bd);
        BaseDeDatosDePrueba dbPrueba = new BaseDeDatosDePrueba(bd);

        RepositorioDePrueba repositorio = new RepositorioDePrueba(dbPrueba);
        repositorio.setRepositoriosAsociados(Map.of(
            EntidadAsociacionPrueba.class, new RepositorioAsociacionDePrueba(dbPrueba)
        ));

        this.repositorio = repositorio;
    }

    @AfterClass
    public static void close() {
        gestorBaseDeDatos.close();
    }

    private EntidadDePrueba entidadDePrueba() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int num = random.nextInt(1, 1000);
        return new EntidadDePrueba("nombre"+num, num, random.nextBoolean());
    }

    @Test
    public void insertarUnaEntidadExitosamente_produceTrue() {
        EntidadDePrueba entidad = entidadDePrueba();
        boolean exito = repositorio.crear(entidad);

        Assert.assertTrue(exito);
    }

    @Test
    public void insertarUnaEntidad_autogeneraUnaIdParaEsaEntidad() {
        EntidadDePrueba entidad = entidadDePrueba();
        repositorio.crear(entidad);
        Assert.assertEquals(1, entidad.getId());
    }

    @Test
    public void falloAlInsertarUnaEntidad_produceFalse() {
        EntidadDePrueba entidad = entidadDePrueba();
        repositorio.crear(entidad);

        // entidad invalida. Viola la restriccion UNIQUE de el atributo 'nombre'
        EntidadDePrueba invalida = new EntidadDePrueba(entidad.getNombre(), 1, false);

        boolean exito = repositorio.crear(invalida);
        Assert.assertFalse(exito);
    }

    @Test
    public void insertarUnaEntidadConId_lanzaExcepcion() {
        EntidadDePrueba entidad = entidadDePrueba();
        entidad.setId(1);
        Assert.assertThrows(IllegalArgumentException.class, () -> repositorio.crear(entidad));
    }

    @Test
    public void insertarUnaEntidad_ySeleccionarPorLaIdGenerada_regresaLaEntidad() {
        EntidadDePrueba entidad = entidadDePrueba();
        repositorio.crear(entidad);

        assertEntidadCreada(entidad);
    }

    @Test
    public void asociarUnaEntidadExitosamente_produceTrue() {
        EntidadAsociacionPrueba entidadAsociacion = insertarEntidadAsociacion();
        EntidadDePrueba entidadDePrueba = entidadDePrueba();

        boolean asociada = repositorio.asociarAEstaEntidad(entidadDePrueba, entidadAsociacion);

        Assert.assertTrue(asociada);
    }

    @Test
    public void falloAlAsociarUnaEntidad_produceFalse() {
        EntidadDePrueba entidad = entidadDePrueba();
        repositorio.crear(entidad);

        // entidad invalida. Viola la restriccion UNIQUE de el atributo 'nombre'
        EntidadDePrueba invalida = new EntidadDePrueba(entidad.getNombre(), 1, false);

        boolean asociada = repositorio.asociarAEstaEntidad(invalida, insertarEntidadAsociacion());

        Assert.assertFalse(asociada);
    }

    @Test
    public void asociarUnaEntidadSinId_creaLaEntidadYLuegoLaAsocia() {
        EntidadAsociacionPrueba entidadAsociacion = insertarEntidadAsociacion();
        EntidadDePrueba entidadDePrueba = entidadDePrueba();

        repositorio.asociarAEstaEntidad(entidadDePrueba, entidadAsociacion);

        try (Cursor cursor = bd.rawQuery("SELECT * FROM " + ContratoDePrueba.NOMBRE_TABLA_ASOCIACION, null)) {

            cursor.moveToNext();

            long idAsociada = cursor.getInt(cursor.getColumnIndexOrThrow(ContratoDePrueba.ColumnasAsociacion.CLAVE_FORANEA));
            Assert.assertEquals(entidadDePrueba.getId(), idAsociada);
        }


        Assert.assertEquals(1, entidadDePrueba.getId());
    }

    @Test
    public void asociarUnaEntidadAUnaEntidadSinId_lanzaExcepcion() {
        EntidadDePrueba entidad = entidadDePrueba();
        EntidadAsociacionPrueba asociar =
                new EntidadAsociacionPrueba("Asociacion", ThreadLocalRandom.current().nextInt());

        Assert.assertThrows(IllegalArgumentException.class,
            () -> repositorio.asociarAEstaEntidad(entidad, asociar));
    }

    @Test
    public void asociarUnaEntidadSinInicializarIndiceDeAsociaciones_lanzaExcepcion() {
        RepositorioDePrueba repo = new RepositorioDePrueba(new BaseDeDatosDePrueba(bd), true);

        EntidadDePrueba entidad = entidadDePrueba();
        EntidadAsociacionPrueba asociar = insertarEntidadAsociacion();

        Assert.assertThrows(IllegalStateException.class,
                () -> repo.asociarAEstaEntidad(entidad, asociar));
    }

    @Test
    public void asociarUnaEntidadAOtraSinIncluirEnElIndiceDeAsociaciones_lanzaExcepcion() {
        RepositorioDePrueba repo = new RepositorioDePrueba(new BaseDeDatosDePrueba(bd));
        repo.anadirAsociaciones(Map.of(
                Mascota.class, new RepositorioSQLite.AsociaciacionUnoAMuchos("a", "a")));

        EntidadDePrueba entidad = entidadDePrueba();
        EntidadAsociacionPrueba asociar = insertarEntidadAsociacion();

        Assert.assertThrows(IllegalStateException.class,
                () -> repo.asociarAEstaEntidad(entidad, asociar));
    }

    @Test
    public void asociarUnaEntidadAMuchasExitosamente_produceTrue() {
        EntidadAsociacionAMuchosPrueba entidadAsociacion = insertarEntidadAsociacionAMuchos();
        EntidadDePrueba entidadDePrueba = entidadDePrueba();

        boolean asociada = repositorio.asociarMuchos(entidadDePrueba, entidadAsociacion);

        Assert.assertTrue(asociada);
    }

    @Test
    public void asociarUnaEntidadSinIdAMuchas_creaLaEntidadYLuegoLaAsocia() {
        EntidadAsociacionAMuchosPrueba entidadAsociacion = insertarEntidadAsociacionAMuchos();
        EntidadDePrueba entidadDePrueba = entidadDePrueba();

        repositorio.asociarMuchos(entidadDePrueba, entidadAsociacion);

        assertEntidadCreada(entidadDePrueba);
    }

    @Test
    public void asociarUnaEntidadAMuchas_ySeleccionarPorLaIdEnTableIntermedia_regresaLaEntidad() {
        EntidadAsociacionAMuchosPrueba entidadAsociacion = insertarEntidadAsociacionAMuchos();
        EntidadDePrueba entidad = entidadDePrueba();

        repositorio.asociarMuchos(entidad, entidadAsociacion);

        try (Cursor cursor = bd.rawQuery(
            "SELECT * FROM " + NOMBRE_TABLA_PRUEBA +
            " INNER JOIN " + NOMBRE_TABLA_INTERMEDIA +
                " ON " + NOMBRE_TABLA_INTERMEDIA+"."+ColumnasIntermedias.PRUEBA_ID +
                " = " + NOMBRE_TABLA_PRUEBA+"."+ColumnasPrueba._ID +
            " WHERE " + ColumnasIntermedias.ASOCIACION_ID + " = ?",
            new String[]{String.valueOf(entidadAsociacion.getId())}))
        {
            Assert.assertEquals(1, cursor.getCount());
            Assert.assertTrue(cursor.moveToNext());
            Assert.assertEquals(entidad.getNombre(), cursor.getString(cursor.getColumnIndexOrThrow(ContratoDePrueba.ColumnasPrueba.NOMBRE)));
            Assert.assertEquals(entidad.getEdad(), cursor.getInt(cursor.getColumnIndexOrThrow(ContratoDePrueba.ColumnasPrueba.EDAD)));
            Assert.assertEquals(1, cursor.getInt(cursor.getColumnIndexOrThrow(ContratoDePrueba.ColumnasPrueba.PRUEBA)));
        }
    }

    @Test
    public void asociarUnaEntidadAMuchasSinId_lanzaExcepcion() {
        EntidadDePrueba entidad = entidadDePrueba();
        EntidadAsociacionPrueba asociar =
                new EntidadAsociacionPrueba("Asociacion", ThreadLocalRandom.current().nextInt());

        Assert.assertThrows(IllegalArgumentException.class,
                () -> repositorio.asociarMuchos(entidad, asociar));
    }

    @Test
    public void asociarUnaEntidadAMuchasSinInicializarIndiceDeMuchasAsociaciones_lanzaExcepcion() {
        RepositorioDePrueba repo = new RepositorioDePrueba(new BaseDeDatosDePrueba(bd), true);

        EntidadDePrueba entidad = entidadDePrueba();
        EntidadAsociacionPrueba asociar = insertarEntidadAsociacion();

        Assert.assertThrows(IllegalStateException.class,
                () -> repo.asociarMuchos(entidad, asociar));
    }

    @Test
    public void asociarUnaEntidadAMuchasSinIncluirEnElIndiceDeAsociaciones_lanzaExcepcion() {
        RepositorioDePrueba repo = new RepositorioDePrueba(new BaseDeDatosDePrueba(bd));
        repo.anadirAsociacionesAMuchos(Map.of(
                Mascota.class, new RepositorioSQLite.AsociaciacionMuchosAMuchos("", "", "", "")));

        EntidadDePrueba entidad = entidadDePrueba();
        EntidadAsociacionPrueba asociar = insertarEntidadAsociacion();

        Assert.assertThrows(IllegalStateException.class,
                () -> repo.asociarMuchos(entidad, asociar));
    }

    @Test
    public void seleccionarEntidadPorSuId_regresaLaEntidad() {
        EntidadDePrueba entidadDePrueba = entidadDePrueba();
        repositorio.crear(entidadDePrueba);

        long idGenerada = entidadDePrueba.getId();
        EntidadDePrueba entidad = repositorio.seleccionarPorId(idGenerada);

        Assert.assertNotNull(entidad);
        Assert.assertEquals(idGenerada, entidad.getId());
    }

    @Test
    public void seleccionarEntidadPorUnaIdInvalida_lanzaExcepcion() {
        Assert.assertThrows(IllegalArgumentException.class, () -> repositorio.seleccionarPorId(0));
        Assert.assertThrows(IllegalArgumentException.class, () -> repositorio.seleccionarPorId(-1));
    }

    @Test
    public void seleccionarEntidadInexistente_produceNull() {
        EntidadDePrueba entidad = repositorio.seleccionarPorId(1);
        Assert.assertNull(entidad);
    }

    @Test
    public void seleccionaTodasLasEntidades() {
        EntidadDePrueba entidad1 = new EntidadDePrueba("Prueba 1", 25, true);
        EntidadDePrueba entidad2 = new EntidadDePrueba("Prueba 2", 35, true);
        EntidadDePrueba entidad3 = new EntidadDePrueba("Prueba 3", 45, true);

        repositorio.crear(entidad1);
        repositorio.crear(entidad2);
        repositorio.crear(entidad3);

        List<EntidadDePrueba> todas = repositorio.seleccionarTodo();

        Assert.assertEquals(3, todas.size());
        Assert.assertTrue(todas.contains(entidad1));
        Assert.assertTrue(todas.contains(entidad2));
        Assert.assertTrue(todas.contains(entidad3));
    }

    @Test
    public void seleccionaPorAttributoConValor() {
        int edad = 25;
        EntidadDePrueba entidad1 = new EntidadDePrueba("entidad1", edad, true);
        EntidadDePrueba entidad2 = new EntidadDePrueba("entidad2", edad, true);
        EntidadDePrueba entidad3 = new EntidadDePrueba("entidad3", edad, true);
        
        repositorio.crear(entidad1);
        repositorio.crear(entidad2);
        repositorio.crear(entidad3);

        List<EntidadDePrueba> resultado =
            repositorio.seleccionarPor(ColumnasPrueba.EDAD, String.valueOf(edad));

        Assert.assertEquals(3, resultado.size());
        Assert.assertEquals(entidad1, resultado.get(0));
        Assert.assertEquals(entidad2, resultado.get(1));
        Assert.assertEquals(entidad3, resultado.get(2));
    }

    @Test
    public void seleccionaPorSetDeIds() {
        EntidadDePrueba entidad1 = entidadDePrueba();
        EntidadDePrueba entidad2 = entidadDePrueba();
        EntidadDePrueba entidad3 = entidadDePrueba();

        repositorio.crear(entidad1);
        repositorio.crear(entidad2);
        repositorio.crear(entidad3);

        List<EntidadDePrueba> resultado = repositorio.seleccionarEn(
                new long[]{entidad1.getId(), entidad2.getId(), entidad3.getId()});

        Assert.assertEquals(3, resultado.size());
        Assert.assertEquals(entidad1, resultado.get(0));
        Assert.assertEquals(entidad2, resultado.get(1));
        Assert.assertEquals(entidad3, resultado.get(2));
    }

    @Test
    public void seleccionaEntidadesPorAsociacion() {
        EntidadAsociacionPrueba entidadAsociacion1 = insertarEntidadAsociacion();
        EntidadAsociacionPrueba entidadAsociacion2 = insertarEntidadAsociacion();
        EntidadAsociacionPrueba entidadAsociacion3 = insertarEntidadAsociacion();

        EntidadDePrueba entidad = entidadDePrueba();

        repositorio.asociarAEstaEntidad(entidad, entidadAsociacion1);
        repositorio.asociarAEstaEntidad(entidad, entidadAsociacion2);
        repositorio.asociarAEstaEntidad(entidad, entidadAsociacion3);

        Map<Long, List<EntidadAsociacionPrueba>> asociaciones =
                repositorio.seleccionarMuchosAUno(EntidadAsociacionPrueba.class, null);

        List<EntidadAsociacionPrueba> entidadesAsociadas = asociaciones.get(entidad.getId());
        assert entidadesAsociadas != null;

        Log.d(TAG, "seleccionaEntidadesPorAsociacion: RESULT " + entidadesAsociadas);

        Assert.assertEquals(3, entidadesAsociadas.size());
        Assert.assertTrue(entidadesAsociadas.containsAll(List.of(
            entidadAsociacion1, entidadAsociacion2, entidadAsociacion3
        )));
    }

    private static final String TAG = "RepositorioSQLiteTest";

    @Test
    public void seleccionaEntidadesPorAsociacionAMuchos() {
        EntidadAsociacionAMuchosPrueba entidadAsociacion1 = insertarEntidadAsociacionAMuchos();
        EntidadAsociacionAMuchosPrueba entidadAsociacion2 = insertarEntidadAsociacionAMuchos();
        EntidadAsociacionAMuchosPrueba entidadAsociacion3 = insertarEntidadAsociacionAMuchos();

        EntidadDePrueba entidad1 = entidadDePrueba();
        EntidadDePrueba entidad2 = entidadDePrueba();
        EntidadDePrueba entidad3 = entidadDePrueba();

        repositorio.asociarMuchos(entidad1, entidadAsociacion1);
        repositorio.asociarMuchos(entidad1, entidadAsociacion2);
        repositorio.asociarMuchos(entidad2, entidadAsociacion3);
        repositorio.asociarMuchos(entidad3, entidadAsociacion2);

        Map<Long,List<EntidadAsociacionAMuchosPrueba>> asociaciones =
            repositorio.seleccionarMuchosAMuchos(
                    EntidadAsociacionAMuchosPrueba.class,
                    new long[]{entidadAsociacion2.getId(), entidadAsociacion3.getId()});

        List<EntidadAsociacionAMuchosPrueba> asociacionesAEntidadAsociacion2 = asociaciones.get(entidadAsociacion2.getId());
        List<EntidadAsociacionAMuchosPrueba> asociacionesAEntidadAsociacion3 = asociaciones.get(entidadAsociacion3.getId());

        assert asociacionesAEntidadAsociacion2 != null;
        assert asociacionesAEntidadAsociacion3 != null;

        Assert.assertEquals(2, asociaciones.size());
        Assert.assertTrue(asociacionesAEntidadAsociacion2.containsAll(List.of(entidad1, entidad3)));
        Assert.assertEquals(entidad2, asociacionesAEntidadAsociacion3.get(0));
    }

    @Test
    public void seleccionaEntidadesPorAsociacionSinInicializarIndicesDeAsociacion_lanzaExcepcion() {
        RepositorioDePrueba repositorio = new RepositorioDePrueba(new BaseDeDatosDePrueba(bd), true);

        Assert.assertThrows(IllegalStateException.class,
                () -> repositorio.seleccionarMuchosAUno(EntidadAsociacionAMuchosPrueba.class, null));
    }

    @Test
    public void seleccionarEntidadesPorAsociacionDeEntidadesNoPresentesEnNingunIndiceDeAsociacion_lanzaExcepcion() {
        RepositorioDePrueba repositorio = new RepositorioDePrueba(new BaseDeDatosDePrueba(bd));

        repositorio.anadirAsociaciones(Map.of(
                Mascota.class, new RepositorioSQLite.AsociaciacionUnoAMuchos("", "")));

        repositorio.anadirAsociacionesAMuchos(Map.of(
                Mascota.class, new RepositorioSQLite.AsociaciacionMuchosAMuchos("", "", "", "")));

        Assert.assertThrows(IllegalStateException.class,
                () -> repositorio.seleccionarMuchosAUno(EntidadAsociacionAMuchosPrueba.class, null));
    }

    @Test
    public void actualizarUnaEntidadExitosamente_produceTrue() {
        EntidadDePrueba entidad = entidadDePrueba();
        repositorio.crear(entidad);

        entidad.setNombre("otro nombre");
        boolean exito = repositorio.actualizar(entidad);

        Assert.assertTrue(exito);
    }

    @Test
    public void falloAlActualizarUnaEntidad_produceFalse() {
        EntidadDePrueba entidad1 = entidadDePrueba();
        EntidadDePrueba entidad2 = entidadDePrueba();
        EntidadDePrueba entidad3 = entidadDePrueba();

        repositorio.crear(entidad1);
        repositorio.crear(entidad2);

        entidad2.setNombre(entidad1.getNombre());

        boolean resultado1 = repositorio.actualizar(entidad2);  // Violacion de restriccion UNIQUE
        boolean resultado2 = repositorio.actualizar(entidad3);  // entidad no creada

        Assert.assertFalse(resultado1);
        Assert.assertFalse(resultado2);
    }

    @Test
    public void actualizarUnaEntidad_ySeleccionarLaEntidad_regresaLaEntidadConSusNuevosValores() {
        EntidadDePrueba entidad = entidadDePrueba();
        repositorio.crear(entidad);

        entidad.setNombre("otro nombre");
        repositorio.actualizar(entidad);

        EntidadDePrueba actualizada = repositorio.seleccionarPorId(entidad.getId());
        Assert.assertEquals(entidad, actualizada);
    }

    @Test
    public void eliminarUnaEntidadExitosamente_produceTrue() {
        EntidadDePrueba entidad = entidadDePrueba();
        repositorio.crear(entidad);

        boolean exito = repositorio.eliminar(entidad);
        Assert.assertTrue(exito);
    }

    @Test
    public void falloAlEliminarUnaEntidad_produceFalse() {
        EntidadDePrueba entidad = entidadDePrueba();

        boolean exito = repositorio.eliminar(entidad);
        Assert.assertFalse(exito);
    }

    @Test
    public void eliminarUnaEntidad_ySeleccionarLaEntidad_regresaNull() {
        EntidadDePrueba entidad = entidadDePrueba();

        repositorio.crear(entidad);
        repositorio.eliminar(entidad);

        EntidadDePrueba enBaseDeDatos = repositorio.seleccionarPorId(entidad.getId());
        Assert.assertNull(enBaseDeDatos);
    }

    private Cursor assertEntidadCreada(EntidadDePrueba entidad) {
        Assert.assertEquals(1, entidad.getId());

        Cursor cursor = bd.rawQuery(
                "SELECT * FROM " + NOMBRE_TABLA_PRUEBA +
                        " WHERE " + ContratoDePrueba.ColumnasPrueba._ID + " = ?",
                new String[]{String.valueOf(entidad.getId())});

        Assert.assertTrue(cursor.moveToNext());
        Assert.assertEquals(entidad.getNombre(), cursor.getString(cursor.getColumnIndexOrThrow(ContratoDePrueba.ColumnasPrueba.NOMBRE)));
        Assert.assertEquals(entidad.getEdad(), cursor.getInt(cursor.getColumnIndexOrThrow(ContratoDePrueba.ColumnasPrueba.EDAD)));

        return cursor;
    }

    private EntidadAsociacionPrueba insertarEntidadAsociacion() {
        int random = ThreadLocalRandom.current().nextInt();
        EntidadAsociacionPrueba entidad =
            new EntidadAsociacionPrueba("Asociacion"+random, random);

        ContentValues valores = new ContentValues();
        valores.put(ContratoDePrueba.ColumnasAsociacion.NOMBRE, entidad.getNombre());
        valores.put(ContratoDePrueba.ColumnasAsociacion.RANDOM, entidad.getRandom());

        long idGenerada = bd.insert(ContratoDePrueba.NOMBRE_TABLA_ASOCIACION, null, valores);

        entidad.setId(idGenerada);
        return entidad;
    }

    private EntidadAsociacionAMuchosPrueba insertarEntidadAsociacionAMuchos() {
        int random = ThreadLocalRandom.current().nextInt();
        EntidadAsociacionAMuchosPrueba entidad =
            new EntidadAsociacionAMuchosPrueba("Asociacion"+random, random);

        ContentValues valores = new ContentValues();
        valores.put(ContratoDePrueba.ColumnasAsociacion.NOMBRE, entidad.getNombre());
        valores.put(ContratoDePrueba.ColumnasAsociacion.RANDOM, entidad.getRandom());

        long idGenerada = bd.insert(ContratoDePrueba.NOMBRE_TABLA_ASOCIACION, null, valores);

        entidad.setId(idGenerada);
        return entidad;
    }

    private static final class BaseDeDatosDePrueba implements BaseDeDatos<SQLiteDatabase> {

        private final SQLiteDatabase baseDeDatos;

        public BaseDeDatosDePrueba(SQLiteDatabase baseDeDatos) {
            this.baseDeDatos = baseDeDatos;
        }

        @Override
        public SQLiteDatabase conectar() {
            return baseDeDatos;
        }

        @Override
        public void desconectar() {

        }
    }
}











