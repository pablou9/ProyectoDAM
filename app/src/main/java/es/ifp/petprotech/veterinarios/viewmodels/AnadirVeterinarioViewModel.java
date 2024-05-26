package es.ifp.petprotech.veterinarios.viewmodels;

import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.APERTURA_CENTRO;
import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.CIERRE_CENTRO;
import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.DIRECCION_CENTRO;
import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.EMAIL_CENTRO;
import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.NOMBRE;
import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.NOMBRE_CENTRO;
import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.TELEFONO_CENTRO;
import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.WEB_CENTRO;

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
import es.ifp.petprotech.centros.model.CentroProfesional;
import es.ifp.petprotech.mascotas.model.Mascota;
import es.ifp.petprotech.veterinarios.model.Veterinario;

public class AnadirVeterinarioViewModel extends ViewModel implements ValidadorInput {

    public enum CamposVeterinario {
        NOMBRE,
        ESPECIALIDAD,
        NOMBRE_CENTRO,
        DIRECCION_CENTRO,
        WEB_CENTRO,
        EMAIL_CENTRO,
        TELEFONO_CENTRO,
        APERTURA_CENTRO,
        CIERRE_CENTRO;

        public String nombre() {
            return this.toString();
        }
    }

    private final Repositorio<CentroProfesional> repositorio;

    private final MutableLiveData<Map<String,Integer>> errores = new MutableLiveData<>();
    private final MutableLiveData<Boolean> veterinarioCreado = new MutableLiveData<>(false);

    private final ExecutorService background = Executors.newSingleThreadExecutor();

    public AnadirVeterinarioViewModel(Repositorio<CentroProfesional> repositorio) {
        this.repositorio = repositorio;
    }

    public void reset() {
        veterinarioCreado.setValue(false);
    }

    public MutableLiveData<Boolean> getVeterinarioCreado() {
        return veterinarioCreado;
    }

    // Testing
    List<CentroProfesional> todosLosCentrosVeterinarios() {
        return repositorio.seleccionarTodo();
    }

    public void crearCentroVeterinario(ValoresFormulario valores,
                                          Map<String, Boolean> diasDeApertura,
                                          Mascota mascota)
    {
        ResultadoValidacion resultado = validarInput(valores);

        if(resultado.contieneErrores()) {
            errores.setValue(resultado.getErrores());
            return;
        }

        CentroProfesional centroVeterinario = CentroProfesional.nuevoCentro()
                .nombre(valores.valor(NOMBRE_CENTRO.nombre()))
                .direccion(valores.valor(DIRECCION_CENTRO.nombre()))
                .paginaWeb(valores.valor(WEB_CENTRO.nombre()))
                .email(valores.valor(EMAIL_CENTRO.nombre()))
                .telefono(valores.valor(TELEFONO_CENTRO.nombre()))
                .apertura(valores.valor(APERTURA_CENTRO.nombre()))
                .cierre(valores.valor(CIERRE_CENTRO.nombre()))
                .dias(formatoDiasDeApertura(diasDeApertura))
                .build();

        Veterinario veterinario = Veterinario.nuevoVeterinario()
                .nombre(valores.valor(NOMBRE.nombre()))
                .especialidad(valores.valor(CamposVeterinario.ESPECIALIDAD.nombre()))
                .centro(centroVeterinario)
                .build();

        veterinario.anadirMascota(mascota);
        centroVeterinario.anadirVeterinario(veterinario);

        background.execute(() -> {
            boolean creado = repositorio.crear(centroVeterinario);

            if (creado)
                veterinarioCreado.postValue(true);
        });
    }

    private String formatoDiasDeApertura(Map<String,Boolean> diasDeApertura) {

        String inicioSegmento = null;
        String finalSegmento = null;

        String horario = "";

        for (String dia : diasDeApertura.keySet()) {
            boolean abierto = Boolean.TRUE.equals(diasDeApertura.get(dia));

            if (abierto) {
                if (inicioSegmento == null)
                    inicioSegmento = dia;
                else
                    finalSegmento = dia;
            }
            else {
                horario = inicioSegmento + " - " + finalSegmento;
                inicioSegmento = null;
                finalSegmento = null;
            }
        }

        return horario + inicioSegmento;
    }

    @Override
    public ResultadoValidacion validarInput(ValoresFormulario valores) {
        ResultadoValidacion resultado = new ResultadoValidacion();

        if (notPresent(valores.valor(NOMBRE.nombre()))) {
            resultado.anadirError(NOMBRE.nombre(), R.string.error_campo_obligatorio);
        }

        if (notPresent(valores.valor(NOMBRE_CENTRO.nombre()))) {
            resultado.anadirError(NOMBRE_CENTRO.nombre(), R.string.error_campo_obligatorio);
        }

        return resultado;
    }

    @Override
    public LiveData<Map<String, Integer>> getErrores() {
        return errores;
    }

    protected boolean notPresent(String s) {
        return s == null || s.isBlank();
    }
}
