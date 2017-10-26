package com.transmilenio.transmisurvey.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.activites.ListaRegistrosActivity;
import com.transmilenio.transmisurvey.activites.MainActivity;
import com.transmilenio.transmisurvey.activites.RegistroActivity;
import com.transmilenio.transmisurvey.models.util.ExtrasID;
import com.transmilenio.transmisurvey.models.util.Mensajes;

/**
 * Created by nataly on 25/10/2017.
 */

public class AlertObservacion extends DialogFragment {

    private Button buttonFinalizar;
    private EditText observaciones;
    private int idEncuesta;
    private String servicio;


    public AlertObservacion() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_observacion, container,
                false);
        idEncuesta =  getArguments().getInt(ExtrasID.EXTRA_ID_ENCUESTA);
        servicio =  getArguments().getString(ExtrasID.EXTRA_ID_SERVICIO);
        return rootView;
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setTitle(Mensajes.MSG_OBSERVACIONES);

        buttonFinalizar = (Button) view.findViewById(R.id.d_finalizar_button);
        observaciones = (EditText) view.findViewById(R.id.d_observacion_text);

        buttonFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ListaRegistrosActivity.class);
                intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,idEncuesta);
                intent.putExtra(ExtrasID.EXTRA_ID_SERVICIO,servicio);
                intent.putExtra(ExtrasID.EXTRA_OBSER,observaciones.getText().toString());
                startActivity(intent);
            }
        });


    }
}
