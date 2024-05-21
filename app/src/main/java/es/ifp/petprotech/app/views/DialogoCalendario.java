package es.ifp.petprotech.app.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;

import es.ifp.petprotech.app.datos.FormatoFechaTiempo;

public class DialogoCalendario extends DialogFragment implements DatePickerDialog.OnDateSetListener {

   private final EditText input;

    public DialogoCalendario(EditText input) {
        this.input = input;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();

        assert getActivity() != null;

        return new DatePickerDialog(
            getActivity(),
            this,
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        input.setText(FormatoFechaTiempo.formatoFecha(LocalDate.of(year, month + 1, dayOfMonth)));
    }
}
