package com.transmilenio.transmisurvey.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.activites.ListaRegistrosActivity;
import com.transmilenio.transmisurvey.activites.MainActivity;
import com.transmilenio.transmisurvey.activites.RegistroActivity;
import com.transmilenio.transmisurvey.models.util.ExtrasID;
import com.transmilenio.transmisurvey.models.util.Mensajes;

/**
 * Created by nataly on 10/10/2017.
 */

public class AlertGuardarDatos extends DialogFragment {

    private Button buttonGuardar,buttonCancelar,buttonIgnorar;
    private int idEncuesta;
    private String tipoEncuesta;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialogguardar, container,
                false);
        idEncuesta =  getArguments().getInt(ExtrasID.EXTRA_ID_ENCUESTA);
        tipoEncuesta = getArguments().getString(ExtrasID.EXTRA_TIPO);
        return rootView;
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setTitle(Mensajes.MSG_SALIR_ENCUESTA);

        buttonGuardar = (Button) view.findViewById(R.id.button_guardar_alert);
        buttonCancelar = (Button) view.findViewById(R.id.button_cancelar_alert);
        buttonIgnorar = (Button) view.findViewById(R.id.button_noguardar_alert);

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // Cerrar Dialog
            }
        });

        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Guardar cambios
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        if(!tipoEncuesta.equals(ExtrasID.VALOR_NUEVO)){
            buttonIgnorar.setVisibility(View.GONE);
        }

        buttonIgnorar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,  idEncuesta);
                startActivity(intent);
            }
        });
    }

}
