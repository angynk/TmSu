package com.transmilenio.transmisurvey.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.activites.MainActivity;
import com.transmilenio.transmisurvey.models.util.ExtrasID;
import com.transmilenio.transmisurvey.models.util.Mensajes;

import java.util.ArrayList;
import java.util.List;

import static com.transmilenio.transmisurvey.R.string.estacion;

/**
 * Created by nataly on 24/02/2018.
 */

public class AlertSincronizar extends DialogFragment {

    private Button buttonSincronizar;
    private SearchableSpinner modos;


    public AlertSincronizar() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_observacion, container,
                false);
        return rootView;
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setTitle(Mensajes.MSG_SINCRONIZANCION);

        buttonSincronizar = (Button) view.findViewById(R.id.di_sincronizar);
        modos = (SearchableSpinner) view.findViewById(R.id.di_modo);
        List<String> list = getModosLista();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modos.setAdapter(dataAdapter);
        modos.setTitle("Seleccione Uno");
        modos.setPositiveButton("OK");

        buttonSincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


    }

    private List<String> getModosLista() {
        List<String> modos = new ArrayList<>();
        modos.add("Troncal");
        modos.add("Alimentación");
        modos.add("Todos");
        return modos;
    }
}
