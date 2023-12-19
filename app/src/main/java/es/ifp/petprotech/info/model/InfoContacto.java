package es.ifp.petprotech.info.model;

public class InfoContacto {
    public static final String PAGINA_WEB = "pagina_web";
    public static final String EMAIL = "email";
    public static final String TELEFONO_FIJO = "telefono_fijo";
    public static final String TELEFONO_MOVIL = "telefono_movil";

    private final String direccionWeb;
    private final String email;
    private final String telefonoFijo;
    private final String telefonoMovil;

    public InfoContacto(String direccionWeb, String email, String telefonoFijo, String telefonoMovil) {
        this.direccionWeb = direccionWeb;
        this.email = email;
        this.telefonoFijo = telefonoFijo;
        this.telefonoMovil = telefonoMovil;
    }

    public String getDireccionWeb() {
        return direccionWeb;
    }
    public String getEmail() {
        return email;
    }
    public String getTelefonoFijo() {
        return telefonoFijo;
    }
    public String getTelefonoMovil() {
        return telefonoMovil;
    }
}
