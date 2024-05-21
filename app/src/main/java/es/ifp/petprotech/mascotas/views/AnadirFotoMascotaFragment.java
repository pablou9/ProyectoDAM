package es.ifp.petprotech.mascotas.views;

import static es.ifp.petprotech.app.model.FabricaViewModel.ANADIR_MASCOTA;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.ifp.petprotech.R;
import es.ifp.petprotech.mascotas.viewmodels.AnadirMascotaViewModel;

public class AnadirFotoMascotaFragment extends Fragment {

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private ImageView fotoMascota;

    private AnadirMascotaViewModel viewModel;
    private Button siguiente;

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

    public AnadirFotoMascotaFragment() {
        super(R.layout.fragment_anadir_foto_mascota);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Registra una lanzador de actividad Photo Picker
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(),
            uri -> {
                // Invocado cuando el usuario slecciona una foto
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected URI: " + uri);
                    fotoMascota.setImageURI(uri);
                    viewModel.setFotoMascota(uri);
                    siguiente.setText(R.string.siguiente);
                }
                else {
                    Log.d("PhotoPicker", "No media selected");
                }
            }
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fotoMascota = view.findViewById(R.id.foto);

        siguiente = view.findViewById(R.id.siguiente);
        siguiente.setOnClickListener(e -> viewModel.siguienteAccion());

        Button atras = view.findViewById(R.id.atras);
        atras.setOnClickListener(e -> viewModel.accionAnterior());

        view.findViewById(R.id.boton_galeria).setOnClickListener(e -> mostrarGaleria());
    }

    private void mostrarGaleria() {
        // Lanza el Photo Picker y permite al usuario escoger sólo fotos
        siguiente.setText(R.string.siguiente);
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                            .build());
    }
}