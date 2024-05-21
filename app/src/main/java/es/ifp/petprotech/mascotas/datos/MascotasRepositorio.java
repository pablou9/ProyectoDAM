package es.ifp.petprotech.mascotas.datos;

import static es.ifp.petprotech.mascotas.datos.ContratoMascotas.Columnas;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import es.ifp.petprotech.bd.BaseDeDatos;
import es.ifp.petprotech.bd.RepositorioSQLite;
import es.ifp.petprotech.mascotas.model.Mascota;


public class MascotasRepositorio extends RepositorioSQLite<Mascota> {

    public MascotasRepositorio(BaseDeDatos<SQLiteDatabase> baseDeDatos) {
        super(baseDeDatos, ContratoMascotas.NOMBRE_TABLA);
    }

    private LocalDate extraerFechaNacimiento(long segundos) {
        Instant momentoNacimiento = Instant.ofEpochSecond(segundos);
        return momentoNacimiento.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @Override
    protected Mascota extraerEntidad(Cursor cursor) {
        long segundosEpochNacimiento =
            cursor.getLong(cursor.getColumnIndexOrThrow(Columnas.FECHA_NACIMIENTO));

        Mascota mascota = Mascota.nuevaMascota()
            .nombre(notNull(cursor.getString(cursor.getColumnIndexOrThrow(Columnas.NOMBRE))))
            .fechaNacimiento(extraerFechaNacimiento(segundosEpochNacimiento))
            .especie(notNull(cursor.getString(cursor.getColumnIndexOrThrow(Columnas.ESPECIE))))
            .raza(notNull(cursor.getString(cursor.getColumnIndexOrThrow(Columnas.RAZA))))
            .chip(notNull(cursor.getString(cursor.getColumnIndexOrThrow(Columnas.CHIP))))
            .build();

        mascota.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Columnas._ID)));
        return mascota;
    }

    @Override
    protected ContentValues extraerValores(Mascota mascota) {
        LocalDate nacimiento = mascota.getFechaNacimiento();
        String chip = mascota.getNumeroChip();

        ContentValues valores = new ContentValues();
        valores.put(Columnas.NOMBRE, mascota.getNombre());
        valores.put(Columnas.ESPECIE, mascota.getEspecie());
        valores.put(Columnas.RAZA, mascota.getRaza());

        if (chip == null || chip.isBlank())
            valores.putNull(Columnas.CHIP);

        else valores.put(Columnas.CHIP, mascota.getNumeroChip());

        if (nacimiento == null)
            valores.putNull(Columnas.FECHA_NACIMIENTO);

        else valores.put(Columnas.FECHA_NACIMIENTO, segundosEpochNacimiento(nacimiento));

        return valores;
    }

    private long segundosEpochNacimiento(LocalDate nacimiento) {
        return LocalDateTime.of(nacimiento.getYear(),
                        nacimiento.getMonth(),
                        nacimiento.getDayOfMonth(),
                        0,
                        0)
                .atZone(ZoneId.systemDefault())
                .toEpochSecond();
    }
}
