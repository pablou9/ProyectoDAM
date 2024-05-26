package es.ifp.petprotech.mascotas.viewmodels;

import static es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel.CamposInfoMascota.CHIP;
import static es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel.CamposInfoMascota.FECHA_NACIMIENTO;
import static es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel.CamposInfoMascota.NOMBRE;
import static es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel.CamposInfoMascota.RAZA_ESPECIE;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.ifp.petprotech.R;
import es.ifp.petprotech.app.datos.FormatoFechaTiempo;
import es.ifp.petprotech.app.datos.SharedPreferencesRepositorio;
import es.ifp.petprotech.app.datos.ValidadorInput;
import es.ifp.petprotech.app.datos.formulario.ValoresFormulario;
import es.ifp.petprotech.bd.Repositorio;
import es.ifp.petprotech.mascotas.datos.ContratoMascotas;
import es.ifp.petprotech.mascotas.model.Mascota;

public class AnadirMascotaViewModel extends ViewModel implements ValidadorInput {

    public enum TipoMascota {
        PERRO("Perro"),
        GATO("Gato"),
        REPTIL("Reptil"),
        ROEDOR("Roedor"),
        AVE("Ave"),
        OTRO("Sin Tipo");

        private final String nombre;

        TipoMascota(String nombre) {
            this.nombre = nombre;
        }

        public String nombre() {
            return nombre;
        }

        public boolean esPerroOGato() {
            return this == PERRO || this == GATO;
        }
    }

    public enum CamposInfoMascota {
        RAZA_ESPECIE,
        NOMBRE,
        FECHA_NACIMIENTO,
        CHIP;
        
        public String nombre() {
            return this.toString();
        }
    }

    private final MutableLiveData<Map<String,Integer>> errores = new MutableLiveData<>(new HashMap<>());
    private final MutableLiveData<Boolean> mascotaCreada = new MutableLiveData<>(false);

    private Mascota mascota;
    private TipoMascota tipoMascota;
    private final Repositorio<Mascota> mascotasRepositorio;
    private final SharedPreferencesRepositorio preferenciasRepositorio;

    private final ExecutorService background = Executors.newSingleThreadExecutor();

    public AnadirMascotaViewModel(Repositorio<Mascota> mascotasRepositorio,
                                  SharedPreferencesRepositorio preferenciasRepositorio)
    {
        this.mascotasRepositorio = mascotasRepositorio;
        this.preferenciasRepositorio = preferenciasRepositorio;
    }


    public void reset() {
        mascotaCreada.setValue(false);
    }

    public Mascota getMascota() {
        return this.mascota;
    }

    // Testing
    List<Mascota> todasLasMascotas() {
        return mascotasRepositorio.seleccionarTodo();
    }

    public LiveData<Boolean> getMascotaCreada() {
        return mascotaCreada;
    }

    public void setTipoMascota(TipoMascota tipoMascota) {
        this.tipoMascota = tipoMascota;
    }

    public LiveData<Map<String, Integer>> getErrores() {
        return errores;
    }

    public void setFotoMascota(Uri uri) {
        if (uri != null)
            preferenciasRepositorio.guardar(
                    ContratoMascotas.URI_FOTO+"_"+mascota.getNombre()+"_"+mascota.getEspecie(),
                    uri.toString());
    }

    public boolean crearMascota(ValoresFormulario valoresMascota) {
        ResultadoValidacion resultado = validarInput(valoresMascota);

        if (resultado.contieneErrores()) {
            errores.setValue(resultado.getErrores());
            return false;
        }

        String especie = tipoMascota.esPerroOGato()
                ? tipoMascota.nombre()
                : valoresMascota.valor(RAZA_ESPECIE.nombre());

        String raza = tipoMascota.esPerroOGato()
                ? valoresMascota.valor(RAZA_ESPECIE.nombre())
                : null;

        String fechaNacimiento = valoresMascota.valor(FECHA_NACIMIENTO.nombre());

        Mascota mascota = Mascota.nuevaMascota()
            .nombre(valoresMascota.valor(NOMBRE.nombre()))
            .fechaNacimiento(fechaNacimiento == null || fechaNacimiento.isBlank()
                ? null :
                FormatoFechaTiempo.convertirFecha(fechaNacimiento))
            .especie(especie)
            .raza(raza)
            .chip(valoresMascota.valor(CHIP.nombre()))
            .build();

        background.submit(() -> {
            boolean creada = mascotasRepositorio.crear(mascota);

            if (creada) {
                this.mascota = mascota;
                mascotaCreada.postValue(true);
            }
        });

        return true;
    }

    @Override
    public ResultadoValidacion validarInput(ValoresFormulario valores) {
        ResultadoValidacion resultado = new ResultadoValidacion();

        String fechaNacimiento = valores.valor(FECHA_NACIMIENTO.nombre());

        String chip = valores.valor(CHIP.nombre());

        if (notPresent(valores.valor(NOMBRE.nombre()))) {
            resultado.anadirError(NOMBRE.nombre(), R.string.error_campo_obligatorio);
        }

        if (notPresent(valores.valor(RAZA_ESPECIE.nombre()))) {
            resultado.anadirError(RAZA_ESPECIE.nombre(), R.string.error_campo_obligatorio);
        }

        if (fechaNacimiento != null &&
            !fechaNacimiento.isBlank() &&
            FormatoFechaTiempo.convertirFecha(fechaNacimiento).isAfter(LocalDate.now()))
        {
            resultado.anadirError(FECHA_NACIMIENTO.nombre(), R.string.error_nacimiento_futuro);
        }

        if (chip != null && !chip.isBlank() && chip.length() != Mascota.DIGITOS_NUMERO_CHIP) {
            resultado.anadirError(CHIP.nombre(), R.string.error_chip_invalido);
        }

        return resultado;
    }

    protected boolean notPresent(String s) {
        return s == null || s.isBlank();
    }
}
