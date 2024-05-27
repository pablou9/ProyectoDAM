package es.ifp.petprotech.app.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class FormatoFechaTiempo {

    private static final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter formatoTiempo = DateTimeFormatter.ofPattern("HH:mm");

    public static String formatoFecha(LocalDate fecha) {
        return fecha.format(formatoFecha);
    }

    public static LocalDate convertirFecha(String fecha) {
        return LocalDate.parse(fecha, formatoFecha);
    }

    public static LocalDate convertirFecha (long segundos) {
        Instant momentoNacimiento = Instant.ofEpochSecond(segundos);
        return momentoNacimiento.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static String formatoTiempo(LocalTime tiempo) {
        return tiempo != null ? tiempo.format(formatoTiempo) : "";
    }

    public static LocalTime convertirTiempo(String tiempo) {
        return LocalTime.parse(StringUtils.notNull(tiempo), formatoTiempo);
    }

    public static LocalDateTime convertirTiempo (long segundos) {
        Instant momentoNacimiento = Instant.ofEpochSecond(segundos);
        return momentoNacimiento.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static long segundosEpoch(LocalDate fecha) {
        return fecha != null
            ? segundosEpoch(LocalDateTime.of(fecha.getYear(),
                fecha.getMonth(),
                fecha.getDayOfMonth(),
                0,
                0))
            : 0;
    }

    public static long segundosEpoch(LocalDateTime fechaTiempo) {
        return fechaTiempo != null
            ? fechaTiempo.atZone(ZoneId.systemDefault()).toEpochSecond()
            : 0;
    }

}
