package es.ifp.petprotech.veterinarios.views;

import static es.ifp.petprotech.app.model.FabricaViewModel.ANADIR_MASCOTA;
import static es.ifp.petprotech.app.model.FabricaViewModel.ANADIR_VETERINARIO;
import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.APERTURA_CENTRO;
import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.CIERRE_CENTRO;
import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.DIRECCION_CENTRO;
import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.ESPECIALIDAD;
import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.NOMBRE;
import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.NOMBRE_CENTRO;
import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.TELEFONO_CENTRO;
import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.WEB_CENTRO;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import es.ifp.petprotech.R;
import es.ifp.petprotech.app.datos.CampoFormulario;
import es.ifp.petprotech.app.datos.formulario.Formulario;
import es.ifp.petprotech.app.datos.formulario.ValoresFormulario;
import es.ifp.petprotech.app.views.DialogoHorario;
import es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel;
import es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel;


public class AnadirVeterinarioFragment extends Fragment {

    private static final String TAG = "AnadirVeterinarioFragme";

    public AnadirVeterinarioFragment() {
        super(R.layout.fragment_anadir_veterinario);
    }

    private Formulario formularioVeterinario;
    private Formulario formularioCentro;

    private AnadirVeterinarioViewModel veterinarioViewModel;
    private AnadirMascotaViewModel mascotaViewModel;

    private List<ToggleButton> botonesSemana;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mascotaViewModel =
            new ViewModelProvider(requireActivity(), ANADIR_MASCOTA.getFabrica())
                    .get(AnadirMascotaViewModel.class);

        veterinarioViewModel =
                new ViewModelProvider(this, ANADIR_VETERINARIO.getFabrica())
                        .get(AnadirVeterinarioViewModel.class);
    }

    @Override
    public void onDetach() {
        mascotaViewModel = null;
        veterinarioViewModel = null;
        super.onDetach();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mascotaViewModel == null)
            return;

        inicializarFormularioVeterinario(view);
        inicializarFormularioCentro(view);

        Button siguiente = view.findViewById(R.id.siguiente);
        Button atras = view.findViewById(R.id.atras);

        siguiente.setOnClickListener(e -> siguiente());
        atras.setOnClickListener(e -> atras());

        botonesSemana = inicializarBotonesDiasDeSemana(view);

        veterinarioViewModel.getErrores().observe(getViewLifecycleOwner(),
            errores -> {
                formularioVeterinario.mostarErrores(getContext(), errores);
                formularioCentro.mostarErrores(getContext(), errores);
        });
    }

    private List<ToggleButton> inicializarBotonesDiasDeSemana(View view) {
        FragmentActivity activity = requireActivity();
        return IntStream.of(
            R.id.lunes, R.id.martes,
            R.id.miercoles, R.id.jueves,
            R.id.viernes, R.id.sabado, R.id.domingo)
        .mapToObj(view::findViewById)
        .map(v -> (ToggleButton) v)
        .peek(boton -> anadirListenerABoton(boton, activity))
        .collect(Collectors.toList());
    }

    private void anadirListenerABoton(ToggleButton boton, FragmentActivity activity) {
        new Random().nextInt(99);


        boton.setOnCheckedChangeListener((btn, checked) -> {
            if (checked) {
                ColorStateList tint = ContextCompat.getColorStateList(requireActivity(), R.color.primary);
                ViewCompat.setBackgroundTintList(boton, tint);
                boton.setTextColor(activity.getColor(R.color.white));
            }
            else {
                ViewCompat.setBackgroundTintList(boton, null);
                boton.setTextColor(activity.getColor(R.color.primary));
            }
        });
    }

    private void inicializarFormularioVeterinario(View view) {
        formularioVeterinario = new Formulario();

        formularioVeterinario.anadirCampos(view.findViewById(R.id.formulario_info_veterinario),
            new CampoFormulario(NOMBRE.nombre(), getString(R.string.nombre)+"*"),
            new CampoFormulario(ESPECIALIDAD.nombre(), getString(R.string.especializacion))
        );
    }

    private void inicializarFormularioCentro(View view) {
        formularioCentro = new Formulario();
        formularioCentro.anadirCampos(view.findViewById(R.id.formulario_info_centro),
            new CampoFormulario(NOMBRE_CENTRO.nombre(), getString(R.string.nombre)+"*"),
            new CampoFormulario(DIRECCION_CENTRO.nombre(), getString(R.string.direccion)),
            new CampoFormulario(WEB_CENTRO.name(), getString(R.string.pagina_web)),
            new CampoFormulario(TELEFONO_CENTRO.name(), getString(R.string.telefono)),
            new CampoFormulario(APERTURA_CENTRO.name(), getString(R.string.apertura), accionHorario()),
            new CampoFormulario(CIERRE_CENTRO.name(), getString(R.string.cierre), accionHorario())
        );
    }

    private CampoFormulario.AccionCampoFormulario accionHorario() {
        return new CampoFormulario.AccionCampoFormulario(
                editText ->
                        e -> {
                            DialogoHorario horario = new DialogoHorario(editText);
                            horario.show(getChildFragmentManager(), "horario");
                        },
                R.drawable.baseline_access_time_28);
    }

    private void siguiente() {
        ValoresFormulario valores = formularioVeterinario.unirValores(formularioCentro);

        boolean creado = veterinarioViewModel.crearCentroVeterinario(
                valores,
                extraerDiasDeApertura(),
                mascotaViewModel.getMascota());

        if (creado)
            mascotaViewModel.siguienteAccion();
    }

    private Map<String, Boolean> extraerDiasDeApertura() {
        return botonesSemana.stream()
                .collect(HashMap::new,
                        (mapa, boton) -> mapa.put((String) boton.getTag(),
                        boton.isChecked()), HashMap::putAll);
    }

    private void atras() {
        mascotaViewModel.accionAnterior();
    }
}