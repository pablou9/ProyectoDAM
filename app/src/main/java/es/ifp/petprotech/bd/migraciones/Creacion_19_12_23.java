package es.ifp.petprotech.bd.migraciones;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import es.ifp.petprotech.centros.datos.ContratoCentrosProfesionales;
import es.ifp.petprotech.centros.datos.ContratoHorariosCentros;
import es.ifp.petprotech.centros.datos.ContratoPersonalCentros;
import es.ifp.petprotech.mascotas.datos.ContratoMascotas;
import es.ifp.petprotech.mascotas.datos.ContratoMascotasVeterinarios;
import es.ifp.petprotech.profesionales.datos.ContratoProfesionales;
import es.ifp.petprotech.profesionales.datos.ContratoProfesiones;
import es.ifp.petprotech.profesionales.datos.ContratoProfesionesProfesionales;
import es.ifp.petprotech.veterinarios.datos.ContratoEspecialidades;
import es.ifp.petprotech.veterinarios.datos.ContratoEspecialidadesVeterinarios;
import es.ifp.petprotech.veterinarios.datos.ContratoVeterinarios;

public class Creacion_19_12_23 {

    private static final String TAG = "Creacion_19_12_23";

    static public void ejecutar(SQLiteDatabase bd) {
        Log.d(TAG, "ejecutar: creando tablas..");
        // Mascotas
        bd.execSQL(ContratoMascotas.CREAR_TABLA);
        bd.execSQL(ContratoMascotasVeterinarios.CREAR_TABLA);
        // Veterinarios
        bd.execSQL(ContratoVeterinarios.CREAR_TABLA);
        bd.execSQL(ContratoEspecialidades.CREAR_TABLA);
        bd.execSQL(ContratoEspecialidadesVeterinarios.CREAR_TABLA);
        // Profesionales
        bd.execSQL(ContratoProfesionales.CREAR_TABLA);
        bd.execSQL(ContratoProfesiones.CREAR_TABLA);
        bd.execSQL(ContratoProfesionesProfesionales.CREAR_TABLA);
        // Centros
        bd.execSQL(ContratoCentrosProfesionales.CREAR_TABLA);
        bd.execSQL(ContratoHorariosCentros.CREAR_TABLA);
        bd.execSQL(ContratoPersonalCentros.CREAR_TABLA);
        bd.execSQL(ContratoHorariosCentros.CREAR_TABLA);

        Log.d(TAG, "ejecutar: tablas creadas");
    }

    public void deshacer(SQLiteDatabase bd) {
        // Mascotas
        bd.execSQL(ContratoMascotas.ELIMINAR_TABLA);
        // Veterinarios
        bd.execSQL(ContratoVeterinarios.ELIMINAR_TABLA);
        bd.execSQL(ContratoEspecialidades.ELIMINAR_TABLA);
        bd.execSQL(ContratoEspecialidadesVeterinarios.ELIMINAR_TABLA);
        // Profesionales
        bd.execSQL(ContratoProfesionales.ELIMINAR_TABLA);
        bd.execSQL(ContratoProfesiones.ELIMINAR_TABLA);
        bd.execSQL(ContratoProfesionesProfesionales.ELIMINAR_TABLA);
        // Comercios
        bd.execSQL(ContratoCentrosProfesionales.ELIMINAR_TABLA);
        bd.execSQL(ContratoHorariosCentros.ELIMINAR_TABLA);
        bd.execSQL(ContratoPersonalCentros.ELIMINAR_TABLA);
        bd.execSQL(ContratoHorariosCentros.ELIMINAR_TABLA);
    }
}
