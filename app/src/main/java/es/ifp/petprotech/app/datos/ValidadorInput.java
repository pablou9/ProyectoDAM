package es.ifp.petprotech.app.datos;

import androidx.lifecycle.LiveData;

import java.util.HashMap;
import java.util.Map;

import es.ifp.petprotech.app.datos.formulario.ValoresFormulario;

public interface ValidadorInput {

    ResultadoValidacion validarInput(ValoresFormulario valores);
    LiveData<Map<String,Integer>> getErrores();

    class ResultadoValidacion {
        private final Map<String,Integer> errores = new HashMap<>();

        public void anadirError(String campo, Integer errorId) {
            errores.put(campo, errorId);
        }

        public Map<String, Integer> getErrores() {
            return errores;
        }

        public boolean contieneErrores() {
            return !errores.isEmpty();
        }

        @Override
        public String toString() {
            return "ResultadoValidacion{" +
                    "errores=" + errores +
                    '}';
        }
    }


}
