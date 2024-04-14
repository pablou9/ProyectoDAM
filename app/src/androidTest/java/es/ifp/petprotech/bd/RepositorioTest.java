package es.ifp.petprotech.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ThreadLocalRandom;

import es.ifp.petprotech.bd.util.ContratoDePrueba;
import es.ifp.petprotech.bd.util.EntidadDePrueba;
import es.ifp.petprotech.bd.util.GestorDeBaseDeDatosPrueba;
import es.ifp.petprotech.bd.util.RepositorioDePrueba;

@RunWith(AndroidJUnit4.class)
public class RepositorioTest {

    public static final String DATABASE_NAME = "TestDatabase.db";

    private GestorDeBaseDeDatosPrueba gestorBaseDeDatos;
    private SQLiteDatabase bd;
    private Repositorio<EntidadDePrueba> repositorio;

    @Before
    public void init() {
        Context context = ApplicationProvider.getApplicationContext();
        gestorBaseDeDatos = new GestorDeBaseDeDatosPrueba(context, DATABASE_NAME);
        bd = gestorBaseDeDatos.getWritableDatabase();
        repositorio = new RepositorioDePrueba(new BaseDeDatosDePrueba(bd));
    }

    @After
    public void close() {
        gestorBaseDeDatos.restart(bd);
        bd.close();
        gestorBaseDeDatos.close();
    }

    private long insertarPrueba() {
        ContentValues valores = new ContentValues();
        valores.put(ContratoDePrueba.Columnas.NOMBRE, "Entidad de prueba");
        valores.put(ContratoDePrueba.Columnas.EDAD, 29);
        valores.put(ContratoDePrueba.Columnas.PRUEBA, true);
        
        return bd.insert(ContratoDePrueba.NOMBRE_TABLA, null, valores);
    }

    private EntidadDePrueba entidadDePrueba() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int num = random.nextInt(1, 1000);
        return new EntidadDePrueba("nombre"+num, num, random.nextBoolean());
    }

    @Test
    public void seleccionarEntidadPorSuId_regresaLaEntidad() {
        EntidadDePrueba entidadDePrueba = new EntidadDePrueba("Prueba", 25, true);
        repositorio.crear(entidadDePrueba);

        long idGenerada = entidadDePrueba.getId();
        EntidadDePrueba entidad = repositorio.seleccionarConId(idGenerada);

        Assert.assertNotNull(entidad);
        Assert.assertEquals(idGenerada, entidad.getId());
    }

    @Test
    public void seleccionarEntidadInexistente_produceNull() {
        EntidadDePrueba entidad = repositorio.seleccionarConId(1);
        Assert.assertNull(entidad);
    }

    @Test
    public void insertarUnaEntidadExitosamente_produceTrue() {
        EntidadDePrueba entidad = entidadDePrueba();
        boolean exito = repositorio.crear(entidad);

        Assert.assertTrue(exito);
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
    public void insertarUnaEntidad_autogeneraUnaIdParaEsaEntidad() {
        EntidadDePrueba entidad = entidadDePrueba();
        repositorio.crear(entidad);
        Assert.assertEquals(1, entidad.getId());
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

        EntidadDePrueba enBaseDeDatos = repositorio.seleccionarConId(entidad.getId());

        Assert.assertNotNull(enBaseDeDatos);
        Assert.assertEquals(entidad, enBaseDeDatos);
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

        EntidadDePrueba actualizada = repositorio.seleccionarConId(entidad.getId());
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

        EntidadDePrueba enBaseDeDatos = repositorio.seleccionarConId(entidad.getId());
        Assert.assertNull(enBaseDeDatos);
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











