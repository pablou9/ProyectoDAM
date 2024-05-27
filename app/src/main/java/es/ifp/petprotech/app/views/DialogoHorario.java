package es.ifp.petprotech.app.views;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.time.LocalTime;

import es.ifp.petprotech.app.util.FormatoFechaTiempo;

public class DialogoHorario extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private final EditText input;

    public DialogoHorario(EditText input) {
        this.input = input;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LocalTime time = LocalTime.now();

        assert getActivity() != null;

        return new TimePickerDialog(
                getActivity(),
                this,
                time.getHour(),
                time.getMinute(),
                true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        input.setText(FormatoFechaTiempo.formatoTiempo(LocalTime.of(hourOfDay, minute)));
    }

}