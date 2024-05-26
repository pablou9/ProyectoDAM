package es.ifp.petprotech.mascotas.views;

import static es.ifp.petprotech.app.model.FabricaViewModel.ANADIR_MASCOTA;
import static es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel.CamposInfoMascota.CHIP;
import static es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel.CamposInfoMascota.FECHA_NACIMIENTO;
import static es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel.CamposInfoMascota.NOMBRE;
import static es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel.CamposInfoMascota.RAZA_ESPECIE;
import static es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel.TipoMascota;
import static es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel.TipoMascota.GATO;
import static es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel.TipoMascota.PERRO;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import es.ifp.petprotech.R;
import es.ifp.petprotech.app.datos.CampoFormulario;
import es.ifp.petprotech.app.datos.formulario.Formulario;
import es.ifp.petprotech.app.viewmodels.NavegacionAnadirEntidadesViewModel;
import es.ifp.petprotech.app.views.AnadirEntidadFragment;
import es.ifp.petprotech.app.views.DialogoCalendario;
import es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel;

public class AnadirMascotaFragment extends AnadirEntidadFragment {

    private Formulario formulario;

    private final List<ImageView> tiposMascota = new ArrayList<>();

    private AnadirMascotaViewModel viewModel;
    private NavegacionAnadirEntidadesViewModel navegacionViewModel;

    public AnadirMascotaFragment() {
        super(R.layout.fragment_anadir_mascota);
    }

    public Formulario getFormulario() {
        return formulario;
    }

    @Override
    public void crearEntidad() {
        viewModel.crearMascota(formulario.getValores());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        navegacionViewModel = new ViewModelProvider(
                requireActivity()).get(NavegacionAnadirEntidadesViewModel.class);

        viewModel = new ViewModelProvider(
                requireActivity(), ANADIR_MASCOTA.getFabrica()).get(AnadirMascotaViewModel.class);
    }

    @Override
    public void onDetach() {
        viewModel.reset();
        viewModel = null;
        navegacionViewModel = null;
        super.onDetach();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (viewModel == null || navegacionViewModel == null)
            return;

        inicializarFormulario(view);
        inicializarTiposDeMascota(view);

        viewModel.getErrores().observe(requireActivity(),
                errores -> formulario.mostarErrores(getContext(), errores));

        viewModel.getMascotaCreada().observe(requireActivity(), creada -> {
            if (creada)
                navegacionViewModel.siguiente();
        });
    }

    private CampoFormulario.AccionCampoFormulario accionCalendario() {
        return new CampoFormulario.AccionCampoFormulario(
            editText ->
                e -> {
                    DialogoCalendario calendario = new DialogoCalendario(editText);
                    calendario.show(getChildFragmentManager(), "calendario_fecha_nacimiento_mascota");
                },
            R.drawable.baseline_calendar_month_28);
    }

    private void inicializarFormulario(View view) {
        formulario = new Formulario();

        formulario.anadirCampos(view.findViewById(R.id.formulario_info_mascota),
                new CampoFormulario(RAZA_ESPECIE.nombre()),
                new CampoFormulario(NOMBRE.nombre(), getString(R.string.nombre)),
                new CampoFormulario(FECHA_NACIMIENTO.nombre(), getString(R.string.fecha_nacimiento), accionCalendario()),
                new CampoFormulario(CHIP.name(), getString(R.string.numero_chip))
        );

        formulario.setOnUsuarioInteraccionListener(navegacionViewModel::registrarInteraccionDeUsuario);

        formulario.setHabilitado(false);
    }

    private void inicializarTiposDeMascota(View view) {
        tiposMascota.add(view.findViewById(R.id.perro_icon));
        tiposMascota.add(view.findViewById(R.id.gato_icon));
        tiposMascota.add(view.findViewById(R.id.roedor_icon));
        tiposMascota.add(view.findViewById(R.id.reptil_icon));
        tiposMascota.add(view.findViewById(R.id.ave_icon));
        tiposMascota.add(view.findViewById(R.id.otro_icon));

        for (ImageView tipo : tiposMascota) {
            tipo.setOnClickListener(e ->
                cambiarTipoDeMascota(TipoMascota.valueOf(tipo.getTag().toString().toUpperCase())));
        }
    }

    private void cambiarTipoDeMascota(TipoMascota tipoMascota) {
        viewModel.setTipoMascota(tipoMascota);

        String hint = getString(tipoMascota == PERRO || tipoMascota == GATO
                ? R.string.raza
                : R.string.especie);

        formulario.setHabilitado(true);
        formulario.cambiarHint(RAZA_ESPECIE.nombre(), hint);
    }
}
