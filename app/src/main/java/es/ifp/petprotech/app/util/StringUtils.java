package es.ifp.petprotech.app.util;

public class StringUtils {

    public static String notNull(String s) {
        return s == null ? "" : s;
    }

    public static boolean notPresent(String s) {
        return s == null || s.isBlank();
    }

    public static boolean esNumero(String s) {
        return s.matches("[0-9]+");
    }
}
