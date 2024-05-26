package es.ifp.petprotech.app.views;

import androidx.fragment.app.Fragment;

public abstract class AnadirEntidadFragment extends Fragment {

    public AnadirEntidadFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

    public abstract void crearEntidad();
}
