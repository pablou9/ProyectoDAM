package es.ifp.petprotech.app.datos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import es.ifp.petprotech.app.util.StringUtils;

public class FormatoFechaTiempo {

    private static final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter formatoTiempo = DateTimeFormatter.ofPattern("HH:mm");

    public static String formatoFecha(LocalDate fecha) {
        return fecha.format(formatoFecha);
    }

    public static LocalDate convertirFecha(String fecha) {
        return LocalDate.parse(fecha, formatoFecha);
    }

    public static String formatoTiempo(LocalTime tiempo) {
        return tiempo != null ? tiempo.format(formatoTiempo) : "";
    }

    public static LocalTime convertirTiempo(String tiempo) {
        return LocalTime.parse(StringUtils.notNull(tiempo), formatoTiempo);
    }

}
