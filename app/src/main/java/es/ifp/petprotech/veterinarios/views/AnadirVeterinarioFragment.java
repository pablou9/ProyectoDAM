package es.ifp.petprotech.veterinarios.views;

import static es.ifp.petprotech.app.model.FabricaViewModel.ANADIR_MASCOTA;
import static es.ifp.petprotech.app.model.FabricaViewModel.VETERINARIO;
import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.APERTURA_CENTRO;
import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.CIERRE_CENTRO;
import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.DIRECCION_CENTRO;
import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.ESPECIALIDAD;
import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.NOMBRE;
import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.NOMBRE_CENTRO;
import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.TELEFONO_CENTRO;
import static es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel.CamposVeterinario.WEB_CENTRO;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.ifp.petprotech.R;
import es.ifp.petprotech.app.datos.CampoFormulario;
import es.ifp.petprotech.app.datos.formulario.Formulario;
import es.ifp.petprotech.app.datos.formulario.ValoresFormulario;
import es.ifp.petprotech.app.views.DialogoHorario;
import es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel;
import es.ifp.petprotech.veterinarios.viewmodels.AnadirVeterinarioViewModel;


public class AnadirVeterinarioFragment extends Fragment {

    public AnadirVeterinarioFragment() {
        super(R.layout.fragment_anadir_veterinario);
    }

    private Button siguiente;
    private Button atras;

    private Formulario formularioVeterinario;
    private Formulario formularioCentro;

    private AnadirVeterinarioViewModel veterinarioViewModel;
    private AnadirMascotaViewModel viewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel =
            new ViewModelProvider(requireActivity(), ANADIR_MASCOTA.getFabrica())
                    .get(AnadirMascotaViewModel.class);

        veterinarioViewModel =
                new ViewModelProvider(this, VETERINARIO.getFabrica())
                        .get(AnadirVeterinarioViewModel.class);
    }

    @Override
    public void onDetach() {
        viewModel = null;
        veterinarioViewModel = null;
        super.onDetach();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (viewModel == null)
            return;

        inicializarFormularioVeterinario(view);
        inicializarFormularioCentro(view);

        siguiente = view.findViewById(R.id.siguiente);
        siguiente.setOnClickListener(e -> siguiente());

        atras = view.findViewById(R.id.atras);
        atras.setOnClickListener(e -> atras());

        veterinarioViewModel.getErrores().observe(getViewLifecycleOwner(),
                errores -> {
                    formularioVeterinario.mostarErrores(getContext(), errores);
                    formularioCentro.mostarErrores(getContext(), errores);
        });
    }

    private void inicializarFormularioVeterinario(View view) {
        formularioVeterinario = new Formulario();

        formularioVeterinario.anadirCampos(view.findViewById(R.id.formulario_info_veterinario),
            new CampoFormulario(NOMBRE.nombre(), getString(R.string.nombre)+"*"),
            new CampoFormulario(ESPECIALIDAD.nombre(), getString(R.string.especialidad))
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

    private static final String TAG = "AnadirVeterinarioFragme";

    private void siguiente() {
        ValoresFormulario valores = formularioVeterinario.unirValores(formularioCentro);

        Log.d(TAG, "siguiente: interaccion: " + valores.usuarioHaInteractuado());

        if (valores.usuarioHaInteractuado()) {
            boolean creado = veterinarioViewModel.crearCentroVeterinario(valores, viewModel.getMascota());

            if (creado)
                viewModel.siguienteAccion();
        }
        else {
            viewModel.siguienteAccion();
        }
    }

    private void atras() {
        viewModel.accionAnterior();
    }
}