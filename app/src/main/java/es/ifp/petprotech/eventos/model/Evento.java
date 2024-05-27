package es.ifp.petprotech.eventos.model;

import static android.text.TextUtils.isEmpty;

import java.time.LocalDateTime;
import java.util.Objects;

import es.ifp.petprotech.bd.Entidad;
import es.ifp.petprotech.mascotas.model.Mascota;
import es.ifp.petprotech.medicacion.model.Medicamento;
import es.ifp.petprotech.veterinarios.model.Veterinario;

public class Evento extends Entidad {

    private final String nombre;
    private Mascota mascota;
    private Veterinario veterinario;
    private Medicamento medicamento;
    private final LocalDateTime inicia;
    private final LocalDateTime finaliza;
    private final long periocidad;

    public Evento(Builder builder) {
        validarEvento(builder);

        this.nombre = builder.nombre;
        this.mascota = builder.mascota;
        this.veterinario = builder.veterinario;
        this.inicia = builder.inicia;
        this.finaliza = builder.finaliza;
        this.periocidad = builder.periocidad;
    }

    private void validarEvento(Builder builder) {
        if (isEmpty(builder.nombre))
            throw new IllegalArgumentException("Debes introducir un nombre de evento");

        if (builder.inicia == null)
            throw new IllegalArgumentException("Debes introducir una fecha de inicio");
    }

    public String getNombre() {
        return nombre;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public Medicamento getMedicacion() {
        return medicamento;
    }

    public LocalDateTime getInicia() {
        return inicia;
    }

    public LocalDateTime getFinaliza() {
        return finaliza;
    }

    public long getPeriocidad() {
        return periocidad;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    public void setMedicacion(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evento evento = (Evento) o;
        return periocidad == evento.periocidad && Objects.equals(nombre, evento.nombre) && Objects.equals(mascota, evento.mascota) && Objects.equals(veterinario, evento.veterinario) && Objects.equals(medicamento, evento.medicamento) && Objects.equals(inicia, evento.inicia) && Objects.equals(finaliza, evento.finaliza);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, mascota, veterinario, medicamento, inicia, finaliza, periocidad);
    }

    public static Builder nuevaEvento() {
        return new Builder();
    }

    public static class Builder {
        private String nombre;
        private Mascota mascota;
        private Veterinario veterinario;
        private LocalDateTime inicia;
        private LocalDateTime finaliza;
        private long periocidad;

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }
        public Builder mascota(Mascota mascota) {
            this.mascota = mascota;
            return this;
        }
        public Builder veterinario(Veterinario veterinario) {
            this.veterinario = veterinario;
            return this;
        }
        public Builder inicia(LocalDateTime inicia) {
            this.inicia = inicia;
            return this;
        }
        public Builder finaliza(LocalDateTime finaliza) {
            this.finaliza = finaliza;
            return this;
        }
        public Builder periocidad(long periocidad) {
            this.periocidad = periocidad;
            return this;
        }

        public Evento build() {
            return new Evento(this);
        }
    }
}
