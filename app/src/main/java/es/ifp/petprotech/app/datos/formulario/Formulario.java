package es.ifp.petprotech.app.datos.formulario;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import es.ifp.petprotech.app.datos.CampoFormulario;

public class Formulario {

    private final List<View> campos = new ArrayList<>();
    private boolean habilitado;
    private boolean usuarioHaInteractuado;
    private Consumer<Boolean> onUsuarioInteraccionListener;

    private final Set<String> camposConInput = new HashSet<>();

    /**
     * Añade campos a este formulario. Cada campo está respresentado por un input en el layout objetivo
     * @param layout El layout objetivo, al cual se añadiran los inputs
     * @param campos Los campos a añadir al formulario
     */
    public void anadirCampos(LinearLayout layout, CampoFormulario... campos) {
        Context context = layout.getContext();

        int nombresDuplicados = contarDuplicados(campos);

        if (nombresDuplicados > 0)
            throw new IllegalArgumentException("El nombre de cada campo debe ser único");

        for (CampoFormulario campo : campos) {

            View input = campo.tieneAccion()
                    ? crearInputConAccion(context, crearTextInputLayout(campo, context), campo.getAccion())
                    : crearTextInputLayout(campo, context);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 16, 0, 16);

            layout.addView(input, params);

            this.campos.add(input);
        }
    }

    private int contarDuplicados(CampoFormulario[] campos) {
        return campos.length - Arrays.stream(campos)
                .map(CampoFormulario::getNombre)
                .collect(Collectors.toSet())
                .size();
    }

    @NonNull
    private TextInputLayout crearTextInputLayout(CampoFormulario campo, Context context) {
        TextInputLayout input = new TextInputLayout(context);
        input.setId(campo.getId());
        input.setTag(campo.getNombre());

        TextInputEditText editText = new TextInputEditText(input.getContext());
        editText.setTag("input_"+campo.getNombre()); // para testing

        asignarUsuarioHaInteractuadoListener(editText, campo);

        LinearLayout.LayoutParams editTextLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        input.addView(editText, editTextLayoutParams);
        input.setHint(campo.getHint());
        return input;
    }

    private void asignarUsuarioHaInteractuadoListener(TextInputEditText editText, CampoFormulario campo) {
        editText.setOnFocusChangeListener((v, enfocado) -> {
            if (!enfocado) {
                EditText et = (EditText) (v);
                String contenido = et.getText().toString();

                if (contenido.isBlank())
                    camposConInput.remove(campo.getNombre());
                else
                    camposConInput.add(campo.getNombre());

                usuarioHaInteractuado = alMenosUnCampoTieneInput();
                onUsuarioInteraccionListener.accept(usuarioHaInteractuado);
            }
        });
    }

    private boolean alMenosUnCampoTieneInput() {
        return !camposConInput.isEmpty();
    }

    /**
     * Inidica si el usuario ha interactuado con este formulario. Por interacción se entiene que el
     * usuario ha introducido text en al menos uno de los campos de este formulario
     * @return true si se ha registrado al menos una interacción, de otro modo false
     */
    public boolean usuarioHaInteractuado() {
        return usuarioHaInteractuado;
    }

    /**
     * Regresa los valores de cada input
     * @return Un Map con claves String que corresponden al nombre del campo al que se refiere
     * el input y valores String que corresponden al valor actual del input
     */
    public ValoresFormulario getValores() {
        Map<String,String> valores = new HashMap<>();

        for (View view : campos) {
            TextInputLayout input = getInput(view);

            String campo = (String) input.getTag();
            String valor = getValor(input);

            valores.put(campo, valor);
        }

        return new ValoresFormulario(valores, usuarioHaInteractuado);
    }

    /**
     * Produce una instancia de ValoresFormulario con los valores de este formulario unidos a los
     * valores del formulario dado
     * @param otro El formulario cuyos valores se unirán a los valores de este formulario
     * @return ValoresFormulario con los valores combinados de ambos formularios. ValoresFormulario indicará
     * interacción del usuario si al menos uno de los dos formulario ha registrado interaccióon
     */
    public ValoresFormulario unirValores(Formulario otro) {
        Map<String,String> valores = Stream.concat(
                        getValores().getInput().entrySet().stream(),
                        otro.getValores().getInput().entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return new ValoresFormulario(valores, usuarioHaInteractuado || otro.usuarioHaInteractuado);
    }

    private TextInputLayout getInput(View view) {
        return view instanceof TextInputLayout
                ? (TextInputLayout) view
                : (TextInputLayout) view.findViewById((int)view.getTag());

    }

    private String getValor(TextInputLayout input) {
        EditText editText = input.getEditText();
        return editText != null ? editText.getText().toString().trim() : "";
    }

    private View crearInputConAccion(Context context, TextInputLayout input, CampoFormulario.AccionCampoFormulario accion) {
        ConstraintLayout layout = new ConstraintLayout(context);
        layout.setTag(input.getId());

        ImageView accionButton = new ImageView(context);
        accionButton.setId(View.generateViewId());
        accionButton.setTag("accion");
        accionButton.setImageResource(accion.getDrawableId());
        accionButton.setClickable(true);
        accionButton.setOnClickListener(accion.getAccion().apply(input.getEditText()));

        layout.addView(input, new ViewGroup.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.addView(accionButton, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        ConstraintSet constraints = new ConstraintSet();
        constraints.clone(layout);

        constraints.connect(input.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraints.connect(input.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraints.connect(input.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraints.connect(input.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);

        constraints.connect(accionButton.getId(), ConstraintSet.TOP, input.getId(), ConstraintSet.TOP);
        constraints.connect(accionButton.getId(), ConstraintSet.BOTTOM, input.getId(), ConstraintSet.BOTTOM);
        constraints.connect(accionButton.getId(), ConstraintSet.END, input.getId(), ConstraintSet.END);
        constraints.setMargin(accionButton.getId(), ConstraintSet.END, 32);
        constraints.setMargin(accionButton.getId(), ConstraintSet.TOP, 4);

        constraints.applyTo(layout);

        return layout;
    }

    public boolean estaHabilitado() {
        return habilitado;
    }

    /**
     * Cambia el estado habilitado para todos los inputs y botones de accion de este formulario
     * @param habilitado El nuevo estado de habilitado
     */
    public void setHabilitado(boolean habilitado) {
        for (View view : campos) {
            view.setEnabled(habilitado);

            if (view instanceof ConstraintLayout)
                setHabilitadoHijos((ViewGroup) view, habilitado);
        }

        this.habilitado = habilitado;
    }

    public void setOnUsuarioInteraccionListener(Consumer<Boolean> onUsuarioInteraccionListener) {
        this.onUsuarioInteraccionListener = onUsuarioInteraccionListener;
    }

    private void setHabilitadoHijos(ViewGroup grupo, boolean habilitado) {
        for (View view : getHijos(grupo)) {

            if (view instanceof ConstraintLayout) {
                setHabilitadoHijos(grupo, habilitado);
            }
            else if (!(view instanceof TextInputLayout)) {
                view.findViewWithTag("accion").setClickable(habilitado);
            }
            else {
                view.setEnabled(habilitado);
            }
        }
    }

    private List<View> getHijos(ViewGroup group) {
        List<View> hijos = new ArrayList<>();

        for (int i = 0; i < group.getChildCount(); i++) {
            hijos.add(group.getChildAt(i));
        }
        return hijos;
    }

    /**
     * Cambia los inputs que corresponden a los compos dados a estado de error y muestra el error correspondiente
     * @param context El contexto en el que se invoca este método
     * @param errores Un Map con claves String que corresponden al nombre del campo al que se asignará
     *  el error y valores String que corresponden al mensaje del error
     */
    public void mostarErrores(Context context, Map<String,Integer> errores) {
        for (String campo : errores.keySet()) {
            iterarInputs(input -> {
                int errorStringId = Objects.requireNonNull(errores.get(campo));
                String error = context.getString(errorStringId);

                if (input.getTag().equals(campo))
                    input.setError(error);
            });
        }
    }

    /**
     * Cambia el hint del input correspondiente al campo proporcionado
     * @param campo El nombre del campo al que se cambiará el hint
     * @param hint El nuevo hint que se le asignará al input correspondiente al nombre de campo proporcionado
     */
    public void cambiarHint(String campo, String hint) {
        iterarInputs(input -> {
            if (input.getTag().equals(campo))
                input.setHint(hint);
        });
    }

    private void iterarInputs(Consumer<TextInputLayout> accion) {

        for (View view : campos) {
            if (view instanceof TextInputLayout) {
                accion.accept((TextInputLayout) view);
            }
            else {
                iterarInputs((ViewGroup) view, accion);
            }
        }
    }

    private void iterarInputs(ViewGroup layout, Consumer<TextInputLayout> accion) {

        for (int i = 0; i < layout.getChildCount(); i++) {
            View view = layout.getChildAt(i);

            if (view instanceof TextInputLayout) {
                accion.accept((TextInputLayout) view);
            }
        }
    }
}
