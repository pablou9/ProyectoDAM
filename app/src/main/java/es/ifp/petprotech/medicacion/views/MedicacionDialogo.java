package es.ifp.petprotech.medicacion.views;

import static es.ifp.petprotech.medicacion.viewmodels.AnadirMedicacionViewModel.CamposMedicacion;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import es.ifp.petprotech.R;
import es.ifp.petprotech.app.datos.CampoFormulario;
import es.ifp.petprotech.app.datos.formulario.Formulario;
import es.ifp.petprotech.app.model.FabricaViewModel;
import es.ifp.petprotech.mascotas.viewmodels.MascotasViewModel;
import es.ifp.petprotech.medicacion.viewmodels.AnadirMedicacionViewModel;

public class MedicacionDialogo extends DialogFragment {
    private static final String TAG = "MedicacionDialogo";

    private Formulario formulario;
    private AnadirMedicacionViewModel viewModel;

    @Override
    public void onResume() {
        super.onResume();

        AlertDialog dialog = (AlertDialog)getDialog();

        if(dialog != null) {
            Button positiveButton = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                boolean anadida = anadirMedicacion(formulario);
                Log.d(TAG, "onResume: anadido: " + anadida);
                if (anadida)
                    dismiss();
            });
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this, FabricaViewModel.ANADIR_MEDICACION.getFabrica())
                            .get(AnadirMedicacionViewModel.class);

        viewModel.getErrores().observe(this, errores -> formulario.mostarErrores(context, errores));
    }

    @Override
    @NonNull
    @SuppressLint("InflateParams")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        // Inflador
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Pasa null como root porque la vista va dentro del layout del diálogo
         View contenidoDialogo = inflater.inflate(R.layout.dialog_anadir_medicacion, null);
         LinearLayout contenedorFormulario = contenidoDialogo.findViewById(R.id.formulario_medicacion);

        formulario = new Formulario();
        formulario.anadirCampos(contenedorFormulario,
            new CampoFormulario(CamposMedicacion.NOMBRE.nombre(), getString(R.string.nombre_medicamento)),
            new CampoFormulario(CamposMedicacion.TIPO.nombre(), getString(R.string.tipo_medicamento)),
            new CampoFormulario(CamposMedicacion.CANTIDAD.nombre(), getString(R.string.cantidad)),
            new CampoFormulario(CamposMedicacion.HORAS.nombre(), getString(R.string.horas)),
            new CampoFormulario(CamposMedicacion.DIAS.nombre(), getString(R.string.duracion_dias))
        );

        builder .setView(contenidoDialogo)
                .setMessage(R.string.anadir_mediacion)
                .setPositiveButton(R.string.anadir, null)
                .setNegativeButton(R.string.cancel, (dialog, id) -> dismiss());

        return builder.create();
    }

    private boolean anadirMedicacion(Formulario formulario) {
        MascotasViewModel mascotasViewModel =
            new ViewModelProvider(requireActivity(), FabricaViewModel.MASCOTA.getFabrica())
                    .get(MascotasViewModel.class);

        return viewModel.anadirMedicacionAMascota(
                formulario.getValores(), mascotasViewModel.getMascota());
    }
}
