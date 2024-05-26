package es.ifp.petprotech.app.views;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import es.ifp.petprotech.R;

public abstract class EntidadActivity extends BaseActivity {

    protected abstract String mensajeAlertaEliminar();

    protected abstract void eliminarEntidad();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // infla el menu
        getMenuInflater().inflate(R.menu.entidad_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // gestiona la selección de los artículos del Menu
        if (item.getItemId() == R.id.borrar_entidad) {
            mostarAlerta();
            return true;
        }
        else return super.onOptionsItemSelected(item);
    }

    private void mostarAlerta() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getString(R.string.eliminar));
        alert.setMessage(mensajeAlertaEliminar());

        alert.setPositiveButton(android.R.string.yes, (dialog, which) -> {
            eliminarEntidad();
            finish();
        });
        alert.setNegativeButton(android.R.string.no, (dialog, which) -> dialog.cancel());

        alert.show();
    }

    protected long extraerId() {
        Intent intent = getIntent();
        long id = intent.getLongExtra("data", -1);

        if (id == -1)
            throw new IllegalStateException("Debes proporcionar una id de entidad en esta actividad");

        return id;
    }
}