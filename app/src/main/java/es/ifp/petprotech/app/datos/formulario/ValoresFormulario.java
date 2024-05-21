package es.ifp.petprotech.app.datos.formulario;

import java.util.Map;

public class ValoresFormulario {

    private final boolean usuarioHaInteractuado;
    private final Map<String,String> input;

    public ValoresFormulario(Map<String, String> input, boolean usuarioHaInteractuado) {
        this.usuarioHaInteractuado = usuarioHaInteractuado;
        this.input = input;
    }

    public ValoresFormulario(Map<String, String> input) {
        this(input, false);
    }

    public boolean usuarioHaInteractuado() {
        return usuarioHaInteractuado;
    }

    public String valor(String campo) {
        return input.get(campo);
    }

    Map<String, String> getInput() {
        return input;
    }
}
