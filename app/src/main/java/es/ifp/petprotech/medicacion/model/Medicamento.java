package es.ifp.petprotech.medicacion.model;

import static android.text.TextUtils.isEmpty;

import java.util.Objects;

import es.ifp.petprotech.bd.Entidad;

public class Medicamento extends Entidad {
    
    private final String nombre;
    private final String uso;

    public Medicamento(String nombre, String uso) {
        if (isEmpty(nombre))
            throw new IllegalArgumentException("Debes introducir un nombre de medicamento");

        this.nombre = nombre;
        this.uso = uso;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUso() {
        return uso;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medicamento that = (Medicamento) o;
        return Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}
