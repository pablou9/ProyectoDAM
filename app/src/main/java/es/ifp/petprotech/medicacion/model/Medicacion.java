package es.ifp.petprotech.medicacion.model;

import static android.text.TextUtils.isEmpty;

import java.util.Objects;

import es.ifp.petprotech.bd.Entidad;
import es.ifp.petprotech.mascotas.model.Mascota;

public class Medicacion extends Entidad {

    private Mascota mascota;
    private Medicamento medicamento;
    private final String cantidad;
    private final int horas;
    private final int dias;

    private Medicacion(Builder builder) {
        validarMedicacion(builder);

        cantidad = builder.cantidad;
        horas = builder.horas;
        dias = builder.dias;
    }

    private void validarMedicacion(Builder builder) {
        if (isEmpty(builder.cantidad))
            throw new IllegalArgumentException("Debes proporcionar una cantidad");

        if (builder.horas == 0)
            throw new IllegalArgumentException("Debes proporcionar cada cuantas horas hay tomas");
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public String getCantidad() {
        return cantidad;
    }

    public int getHoras() {
        return horas;
    }

    public int getDias() {
        return dias;
    }

    @Override
    public String toString() {
        return "Medicacion{" +
                "mascota=" + mascota +
                ", medicamento=" + medicamento +
                ", cantidad='" + cantidad + '\'' +
                ", horas=" + horas +
                ", dias=" + dias +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medicacion that = (Medicacion) o;
        return horas == that.horas && dias == that.dias && Objects.equals(medicamento, that.medicamento) && Objects.equals(cantidad, that.cantidad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicamento, cantidad, horas, dias);
    }

    public static Builder nuevaMedicacion() {
        return new Builder();
    }

    public static class Builder {
        private String cantidad;
        private int horas;
        private int dias;

        public Builder cantidad(String cantidad) {
            this.cantidad = cantidad;
            return this;
        }
        public Builder horas(int horas) {
            this.horas = horas;
            return this;
        }
        public Builder dias(int dias) {
            this.dias = dias;
            return this;
        }

        public Medicacion build() {
            return new Medicacion(this);
        }
    }
}
