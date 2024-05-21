package es.ifp.petprotech.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.ifp.petprotech.bd.migraciones.Creacion_19_12_23;

@RunWith(AndroidJUnit4.class)
public class SchemaTest {

    private static final String NOMBRE = "bd_prueba.db";

    private Context context;

    @Before
    public void init() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @After
    public void cleanUp() {
        context.deleteDatabase(NOMBRE);
    }

    @Test
    public void creaElSchemaDeLaBaseDeDatos() {
        try (BaseDeDatosTest bd = new BaseDeDatosTest(context, NOMBRE)) {
            bd.getWritableDatabase();
            Assert.assertTrue(bd.recreada);
        }
    }

    private static class BaseDeDatosTest extends SQLiteOpenHelper {

        private boolean recreada;

        public BaseDeDatosTest(Context context, String nombreBaseDeDatos) {
            super(context, nombreBaseDeDatos, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Creacion_19_12_23.ejecutar(db);
            recreada = true;
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            super.onDowngrade(db, oldVersion, newVersion);
        }
    }
}
