package es.ifp.petprotech.mascotas.viewmodels;

import static es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel.CamposInfoMascota.CHIP;
import static es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel.CamposInfoMascota.FECHA_NACIMIENTO;
import static es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel.CamposInfoMascota.NOMBRE;
import static es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel.CamposInfoMascota.RAZA_ESPECIE;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.util.Arrays;
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

    public enum Accion {
        ANADIR_INFO(0, "anadir_info"),
        ANADIR_FOTO(1, "anadir_foto"),
        ANADIR_VETERINARIO(2, "anadir_veterinario"),
        TERMINAR(3, "terminar");

        private final String tag;
        private final int orden;

        Accion(int orden, String tag) {
            this.tag = tag;
            this.orden = orden;
        }

        public String getTag() {
            return tag;
        }

        public int getOrden() {
            return orden;
        }

        public static Accion getAccion(int posicion) {
            return Arrays.stream(Accion.values())
                .filter(accion -> accion.orden == posicion)
                .findFirst()
                .orElseThrow(() ->
                    new IllegalArgumentException("No existe la posición " + posicion + " para acciones"));
        }
    }

    private final MutableLiveData<Accion> accionEnProceso = new MutableLiveData<>(Accion.ANADIR_VETERINARIO);
    private final MutableLiveData<Map<String,Integer>> errores = new MutableLiveData<>(new HashMap<>());

    private Mascota mascota;
    private TipoMascota tipoMascota;
    private final Repositorio<Mascota> mascotasRepositorio;
    private final SharedPreferencesRepositorio preferenciasRepositorio;

    private final ExecutorService background = Executors.newSingleThreadExecutor();

    public AnadirMascotaViewModel(Repositorio<Mascota> mascotasRepositorio,
                                  SharedPreferencesRepositorio preferenciasRepositorio,
                                  Accion accion)
    {
        this.mascotasRepositorio = mascotasRepositorio;
        this.preferenciasRepositorio = preferenciasRepositorio;
        this.accionEnProceso.setValue(accion);
    }

    public AnadirMascotaViewModel(Repositorio<Mascota> mascotasRepositorio,
                                  SharedPreferencesRepositorio preferenciasRepositorio)
    {
        this(mascotasRepositorio, preferenciasRepositorio, Accion.ANADIR_INFO);
    }

    public Mascota getMascota() {
        return this.mascota;
    }

    // Testing
    List<Mascota> todasLasMascotas() {
        return mascotasRepositorio.seleccionarTodo();
    }

    public MutableLiveData<Accion> getAccionEnProceso() {
        return accionEnProceso;
    }

    public void setTipoMascota(TipoMascota tipoMascota) {
        this.tipoMascota = tipoMascota;
    }

    public MutableLiveData<Map<String, Integer>> getErrores() {
        return errores;
    }

    public void setFotoMascota(Uri uri) {
        preferenciasRepositorio.guardar(ContratoMascotas.URI_FOTO, uri.toString());
    }

    public void crearMascota(ValoresFormulario valoresMascota) {
        ResultadoValidacion resultado = validarInput(valoresMascota);

        if (resultado.contieneErrores()) {
            errores.setValue(resultado.getErrores());
            return;
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
                siguienteAccion();
            }
        });
    }

    public void siguienteAccion() {
        Accion accion = accionEnProceso.getValue();

        if (accion == null || accion.orden == Accion.TERMINAR.orden)
            throw new IllegalStateException("No se ha podido ir a la siguiente acción: " +
                (accion == null ? "no hay accion" : accion.toString()));

        accionEnProceso.postValue(Accion.getAccion(accion.getOrden() + 1));
    }

    public void accionAnterior() {
        Accion accion = accionEnProceso.getValue();

        if (accion == null || accion.orden == 0)
            throw new IllegalStateException("No se ha podido ir a la acción anterior");

        accionEnProceso.setValue(Accion.getAccion(accion.getOrden() - 1));
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
