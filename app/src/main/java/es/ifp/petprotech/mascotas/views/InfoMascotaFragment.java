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
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import es.ifp.petprotech.R;
import es.ifp.petprotech.app.datos.CampoFormulario;
import es.ifp.petprotech.app.views.DialogoCalendario;
import es.ifp.petprotech.app.datos.formulario.Formulario;
import es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel;

public class InfoMascotaFragment extends Fragment {

    private Button siguiente;

    private Formulario formulario;

    private final List<ImageView> tiposMascota = new ArrayList<>();

    private AnadirMascotaViewModel viewModel;

    public InfoMascotaFragment() {
        super(R.layout.fragment_info_mascota);
    }

    public Formulario getFormulario() {
        return formulario;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(requireActivity(), ANADIR_MASCOTA.getFabrica()).get(AnadirMascotaViewModel.class);
    }

    @Override
    public void onDetach() {
        viewModel = null;
        super.onDetach();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (viewModel == null)
            return;

        inicializarFormulario(view);
        inicializarTiposDeMascota(view);

        Button atras = view.findViewById(R.id.atras);
        atras.setVisibility(View.INVISIBLE);

        siguiente = view.findViewById(R.id.siguiente);
        siguiente.setText(R.string.siguiente);
        siguiente.setOnClickListener(e -> siguiente());

        viewModel.getErrores().observe(getViewLifecycleOwner(),
                errores -> formulario.mostarErrores(getContext(), errores));
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
        siguiente.setText(R.string.siguiente);
        formulario.cambiarHint(RAZA_ESPECIE.nombre(), hint);
    }

    private void siguiente() {
        viewModel.crearMascota(formulario.getValores());
    }

}
