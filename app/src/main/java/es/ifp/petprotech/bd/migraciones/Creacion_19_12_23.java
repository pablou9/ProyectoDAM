package es.ifp.petprotech.bd.migraciones;

import android.database.sqlite.SQLiteDatabase;

import es.ifp.petprotech.comercios.datos.ContratoComercios;
import es.ifp.petprotech.comercios.datos.ContratoHorariosComercios;
import es.ifp.petprotech.comercios.datos.ContratoPersonalComercios;
import es.ifp.petprotech.info.datos.ContratoDirecciones;
import es.ifp.petprotech.info.datos.ContratoHorarios;
import es.ifp.petprotech.info.datos.ContratoInfoContacto;
import es.ifp.petprotech.mascotas.datos.ContratoMascotas;
import es.ifp.petprotech.profesionales.datos.ContratoHorariosProfesionales;
import es.ifp.petprotech.profesionales.datos.ContratoProfesionales;
import es.ifp.petprotech.profesionales.datos.ContratoProfesiones;
import es.ifp.petprotech.profesionales.datos.ContratoProfesionesProfesionales;
import es.ifp.petprotech.veterinarios.datos.ContratoEspecialidades;
import es.ifp.petprotech.veterinarios.datos.ContratoEspecialidadesVeterinarios;
import es.ifp.petprotech.veterinarios.datos.ContratoVeterinarios;

public class Creacion_19_12_23 {

    static public void ejecutar(SQLiteDatabase bd) {
        // Mascotas
        bd.execSQL(ContratoMascotas.CREAR_TABLA);
        // Veterinarios
        bd.execSQL(ContratoVeterinarios.CREAR_TABLA);
        bd.execSQL(ContratoEspecialidades.CREAR_TABLA);
        bd.execSQL(ContratoEspecialidadesVeterinarios.CREAR_TABLA);
        // Profesionales
        bd.execSQL(ContratoProfesionales.CREAR_TABLA);
        bd.execSQL(ContratoProfesiones.CREAR_TABLA);
        bd.execSQL(ContratoProfesionesProfesionales.CREAR_TABLA);
        bd.execSQL(ContratoHorariosProfesionales.CREAR_TABLA);
        // Comercios
        bd.execSQL(ContratoComercios.CREAR_TABLA);
        bd.execSQL(ContratoHorariosComercios.CREAR_TABLA);
        bd.execSQL(ContratoPersonalComercios.CREAR_TABLA);
        // Info
        bd.execSQL(ContratoDirecciones.CREAR_TABLA);
        bd.execSQL(ContratoHorarios.CREAR_TABLA);
        bd.execSQL(ContratoInfoContacto.CREAR_TABLA);
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
        bd.execSQL(ContratoHorariosProfesionales.ELIMINAR_TABLA);
        // Comercios
        bd.execSQL(ContratoComercios.ELIMINAR_TABLA);
        bd.execSQL(ContratoHorariosComercios.ELIMINAR_TABLA);
        bd.execSQL(ContratoPersonalComercios.ELIMINAR_TABLA);
        // Info
        bd.execSQL(ContratoDirecciones.ELIMINAR_TABLA);
        bd.execSQL(ContratoHorarios.ELIMINAR_TABLA);
        bd.execSQL(ContratoInfoContacto.ELIMINAR_TABLA);
    }
}
