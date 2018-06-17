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
import com.transmilenio.transmisurvey.activites.MainActivity;
import com.transmilenio.transmisurvey.activites.TiemposRecorridoActivity;
import com.transmilenio.transmisurvey.models.db.TRecorridoEncuesta;
import com.transmilenio.transmisurvey.models.json.EncuestaTM;
import com.transmilenio.transmisurvey.models.util.ExtrasID;
import com.transmilenio.transmisurvey.models.util.Mensajes;

import io.realm.Realm;

/**
 * Created by nataly on 17/06/2018.
 */

public class AlertGuardarComentarios extends DialogFragment {

    private Button buttonGuardar,buttonCancelar;
    private EditText observacion;
    private int idEncuesta;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_guardar_comentarios, container,
                false);
        idEncuesta =  getArguments().getInt(ExtrasID.EXTRA_ID_ENCUESTA);
        return rootView;
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setTitle(Mensajes.MSG_SALIR_ENCUESTA);

        buttonGuardar = (Button) view.findViewById(R.id.button_guardar_alert);
        buttonCancelar = (Button) view.findViewById(R.id.button_cancelar_alert);
        observacion = (EditText) view.findViewById(R.id.tx_observaciones_edittext);

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
                guardarDatosComentarios();
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });



    }

    private void guardarDatosComentarios() {
        if(observacion.getText()!=null){
            if(!observacion.getText().toString().equals("")){
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                EncuestaTM encuestaTM = realm.where(EncuestaTM.class).equalTo("id", idEncuesta).findFirst();
                if(encuestaTM!=null){
                    TRecorridoEncuesta t_recorridos = encuestaTM.getT_recorridos();
                    t_recorridos.setObservacion(observacion.getText().toString());
                    realm.copyToRealmOrUpdate(t_recorridos);
                }
                realm.commitTransaction();
            }
        }

    }
}
