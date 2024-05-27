package es.ifp.petprotech.medicacion.viewmodels;

import static es.ifp.petprotech.app.util.StringUtils.esNumero;
import static es.ifp.petprotech.app.util.StringUtils.notPresent;
import static es.ifp.petprotech.medicacion.viewmodels.AnadirMedicacionViewModel.CamposMedicacion.CANTIDAD;
import static es.ifp.petprotech.medicacion.viewmodels.AnadirMedicacionViewModel.CamposMedicacion.DIAS;
import static es.ifp.petprotech.medicacion.viewmodels.AnadirMedicacionViewModel.CamposMedicacion.HORAS;
import static es.ifp.petprotech.medicacion.viewmodels.AnadirMedicacionViewModel.CamposMedicacion.NOMBRE;
import static es.ifp.petprotech.medicacion.viewmodels.AnadirMedicacionViewModel.CamposMedicacion.TIPO;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.ifp.petprotech.R;
import es.ifp.petprotech.app.datos.ValidadorInput;
import es.ifp.petprotech.app.datos.formulario.ValoresFormulario;
import es.ifp.petprotech.bd.Repositorio;
import es.ifp.petprotech.mascotas.model.Mascota;
import es.ifp.petprotech.medicacion.model.Medicacion;
import es.ifp.petprotech.medicacion.model.Medicamento;

public class AnadirMedicacionViewModel extends ViewModel implements ValidadorInput {

    private final Repositorio<Medicacion> repositorioMedicacion;
    private final Repositorio<Medicamento> repositorioMedicamentos;

    private final MutableLiveData<Map<String, Integer>> errores = new MutableLiveData<>();

    public AnadirMedicacionViewModel(Repositorio<Medicacion> repositorioMedicacion,
                                     Repositorio<Medicamento> repositorioMedicamentos)
    {
        this.repositorioMedicacion = repositorioMedicacion;
        this.repositorioMedicamentos = repositorioMedicamentos;
    }

    public enum CamposMedicacion {
        NOMBRE,
        TIPO,
        CANTIDAD,
        HORAS,
        DIAS;

        public String nombre() {
            return this.toString();
        }
    }

    private final ExecutorService background = Executors.newSingleThreadExecutor();

    public boolean anadirMedicacionAMascota(ValoresFormulario valores, Mascota mascota) {
        ResultadoValidacion resultado = validarInput(valores);

        if (resultado.contieneErrores()) {
            errores.setValue(resultado.getErrores());
            return false;
        }

        String nombreMedicamento = valores.valor(NOMBRE.nombre());
        String tipoMedicamento = valores.valor(TIPO.nombre());

        background.execute(() -> {
            Medicamento medicamento = getMedicamento(nombreMedicamento, tipoMedicamento);
            Medicacion medicacion = Medicacion.nuevaMedicacion()
                    .cantidad(valores.valor(CANTIDAD.nombre()))
                    .horas(Integer.parseInt(valores.valor(HORAS.nombre(), "0")))
                    .dias(Integer.parseInt(valores.valor(DIAS.nombre(), "0")))
                    .build();

            medicacion.setMedicamento(medicamento);
            medicacion.setMascota(mascota);

            repositorioMedicacion.crear(medicacion);
        });

        return true;
    }

    private static final String TAG = "AnadirMedicacionViewMod";

    private Medicamento getMedicamento(String nombre, String tipo) {
        Log.d(TAG, "getMedicamento: nombre: " + nombre);

        List<Medicamento> medicamentos = repositorioMedicamentos.seleccionarPorNombre(nombre);

        Log.d(TAG, "getMedicamento: medicamentos: " + medicamentos);

        Medicamento medicamento;

        if (medicamentos == null || medicamentos.isEmpty()) {
            Log.d(TAG, "getMedicamento: epmty, crear");
            medicamento = new Medicamento(nombre, tipo);
            repositorioMedicamentos.crear(medicamento);
        }
        else medicamento = medicamentos.get(0);

        return medicamento;
    }

    @Override
    public ResultadoValidacion validarInput(ValoresFormulario valores) {
        ResultadoValidacion resultado = new ResultadoValidacion();

        if (notPresent(valores.valor(NOMBRE.nombre())))
            resultado.anadirError(NOMBRE.nombre(), R.string.error_campo_obligatorio);

        if (notPresent(valores.valor(CANTIDAD.nombre())))
            resultado.anadirError(CANTIDAD.nombre(), R.string.error_campo_obligatorio);

        String horas = valores.valor(HORAS.nombre());
        String dias = valores.valor(DIAS.nombre());
        
        if (notPresent(horas)) {
            resultado.anadirError(HORAS.nombre(), R.string.error_campo_obligatorio);
        }
        else if (!esNumero(horas)){
            resultado.anadirError(HORAS.nombre(), R.string.error_solo_numeros);
        }
        else if (Integer.parseInt(horas) < 1){
            resultado.anadirError(HORAS.nombre(), R.string.error_solo_numeros);
        }

        if (notPresent(dias)) {
            resultado.anadirError(DIAS.nombre(), R.string.error_campo_obligatorio);
        }
        else if (!esNumero(horas)){
            resultado.anadirError(DIAS.nombre(), R.string.error_solo_numeros);
        }
        else if (Integer.parseInt(horas) < 1){
            resultado.anadirError(DIAS.nombre(), R.string.error_solo_numeros);
        }

        return resultado;
    }

    @Override
    public LiveData<Map<String, Integer>> getErrores() {
        return errores;
    }
}
