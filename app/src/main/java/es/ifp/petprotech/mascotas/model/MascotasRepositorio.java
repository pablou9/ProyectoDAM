package es.ifp.petprotech.mascotas.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import es.ifp.petprotech.bd.BaseDeDatos;
import es.ifp.petprotech.bd.RepositorioSQLite;
import es.ifp.petprotech.mascotas.datos.ContratoMascotas;

import static es.ifp.petprotech.mascotas.datos.ContratoMascotas.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


public class MascotasRepositorio extends RepositorioSQLite<Mascota> {

    public MascotasRepositorio(BaseDeDatos<SQLiteDatabase> baseDeDatos) {
        super(baseDeDatos, ContratoMascotas.NOMBRE_TABLA);
    }

    @Override
    protected Mascota extraerEntidad(Cursor cursor) {
        long segundosEpochNacimiento =
            cursor.getLong(cursor.getColumnIndexOrThrow(Columnas.FECHA_NACIMIENTO));

        return Mascota.nuevaMascota()
            .nombre(cursor.getString(cursor.getColumnIndexOrThrow(Columnas.NOMBRE)))
            .fechaNacimiento(extraerFechaNacimiento(segundosEpochNacimiento))
            .familia(cursor.getString(cursor.getColumnIndexOrThrow(Columnas.FAMILIA)))
            .especie(cursor.getString(cursor.getColumnIndexOrThrow(Columnas.ESPECIE)))
            .raza(cursor.getString(cursor.getColumnIndexOrThrow(Columnas.RAZA)))
            .chip(cursor.getString(cursor.getColumnIndexOrThrow(Columnas.CHIP)))
            .build();
    }

    private LocalDateTime extraerFechaNacimiento(long segundos) {
        Instant momentoNacimiento = Instant.ofEpochSecond(segundos);
        return momentoNacimiento.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    @Override
    protected ContentValues extraerValores(Mascota mascota) {
        LocalDateTime fechaNacimiento = mascota.getFechaNacimiento();

        long segundosEpochNacimiento =
            fechaNacimiento.atZone(ZoneId.systemDefault()).toEpochSecond();

        ContentValues valores = new ContentValues();
        valores.put(Columnas.NOMBRE, mascota.getNombre());
        valores.put(Columnas.FECHA_NACIMIENTO, segundosEpochNacimiento);
        valores.put(Columnas.FAMILIA, mascota.getFamilia());
        valores.put(Columnas.ESPECIE, mascota.getEspecie());
        valores.put(Columnas.RAZA, mascota.getRaza());
        valores.put(Columnas.CHIP, mascota.getNumeroChip());

        return valores;
    }
}
