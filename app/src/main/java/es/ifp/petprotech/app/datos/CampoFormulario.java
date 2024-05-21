package es.ifp.petprotech.app.datos;


import android.view.View;
import android.widget.EditText;

import java.util.function.Function;

public class CampoFormulario {

    private final int id;
    private final String nombre;
    private final String hint;
    private final AccionCampoFormulario accion;

    public CampoFormulario(String nombre, String hint, AccionCampoFormulario accion) {
        this.id = View.generateViewId();
        this.nombre = nombre;
        this.hint = hint;
        this.accion = accion;
    }

    public CampoFormulario(String nombre, String hint) {
        this(nombre, hint, null);
    }

    public CampoFormulario(String nombre) {
        this(nombre, "", null);
    }

    public AccionCampoFormulario getAccion() {
        return accion;
    }

    public boolean tieneAccion() {
        return accion != null;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getHint() {
        return hint;
    }

    public static class AccionCampoFormulario {
        private final Function<EditText, View.OnClickListener> accion;
        private final int drawableId;

        public AccionCampoFormulario(Function<EditText, View.OnClickListener> accion, int drawableId) {
            this.accion = accion;
            this.drawableId = drawableId;
        }

        public Function<EditText, View.OnClickListener> getAccion() {
            return accion;
        }

        public int getDrawableId() {
            return drawableId;
        }
    }
}
