package es.ifp.petprotech.centros.model;

import androidx.annotation.NonNull;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import es.ifp.petprotech.app.datos.FormatoFechaTiempo;
import es.ifp.petprotech.bd.Entidad;
import es.ifp.petprotech.veterinarios.model.Veterinario;

public class CentroProfesional extends Entidad {
    private final String nombre;
    private final String direccion;
    private final String ciudad;
    private final String paginaWeb;
    private final String email;
    private final String telefono;
    private final LocalTime apertura;
    private final LocalTime cierre;
    private final List<Veterinario> profesionales = new ArrayList<>();

    public CentroProfesional(Builder builder) {
        validarCentroVeterinario(builder);

        String aperturaInput = builder.apertura;
        String cierreInput = builder.cierre;

        nombre = builder.nombre;
        direccion = builder.direccion;
        ciudad = builder.ciudad;
        paginaWeb = builder.paginaWeb;
        email = builder.email;
        telefono = builder.telefono;
        apertura = isEmpty(aperturaInput) ? null : FormatoFechaTiempo.convertirTiempo(builder.apertura);
        cierre = isEmpty(cierreInput) ? null : FormatoFechaTiempo.convertirTiempo(builder.cierre);
    }

    private void validarCentroVeterinario(Builder builder) {
        if (isEmpty(builder.nombre))
            throw new IllegalArgumentException("Debes introducir un nombre");
    }

    private boolean isEmpty(String s) {
        return s == null || s.isBlank();
    }

    public static Builder nuevoCentro() {
        return new Builder();
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public LocalTime getApertura() {
        return apertura;
    }

    public LocalTime getCierre() {
        return cierre;
    }

    public void setVeterinarios(List<Veterinario> veterinarios) {
        profesionales.addAll(veterinarios);
    }

    public void anadirVeterinario(Veterinario veterinario) {
        profesionales.add(veterinario);
    }

    public List<Veterinario> getVeterinarios() {
        return profesionales;
    }

    public String getPaginaWeb() {
        return paginaWeb;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    @NonNull
    @Override
    public String toString() {
        return "CentroProfesional{" +
                "nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", paginaWeb='" + paginaWeb + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", apertura=" + apertura +
                ", cierre=" + cierre +
                ", profesionales=" + profesionales +
                '}';
    }

    public static class Builder  {
        private String nombre;
        private String direccion;
        private String ciudad;
        private String paginaWeb;
        private String email;
        private String telefono;
        private String apertura;
        private String cierre;

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }
        public Builder direccion(String direccion) {
            this.direccion = direccion;
            return this;
        }
        public Builder paginaWeb(String paginaWeb) {
            this.paginaWeb = paginaWeb;
            return this;
        }
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        public Builder telefono(String telefono) {
            this.telefono = telefono;
            return this;
        }
        public Builder apertura(String apertura) {
            this.apertura = apertura;
            return this;
        }
        public Builder cierre(String apertura) {
            this.cierre = cierre;
            return this;
        }

        public CentroProfesional build() {
            return new CentroProfesional(this);
        }
    }
}
