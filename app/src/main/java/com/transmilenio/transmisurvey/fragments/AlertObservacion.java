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
import com.transmilenio.transmisurvey.models.db.Cuadro;
import com.transmilenio.transmisurvey.models.db.Registro;
import com.transmilenio.transmisurvey.models.db.Resultado;
import com.transmilenio.transmisurvey.models.util.ExtrasID;
import com.transmilenio.transmisurvey.models.util.Mensajes;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;


public class AlertObservacion extends DialogFragment {

    private Button buttonFinalizar;
    private EditText observaciones;
    private int idEncuesta;
    private List<Integer> eliminar;


    public AlertObservacion() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_observacion, container,
                false);
//        idEncuesta =  getArguments().getInt(ExtrasID.EXTRA_ID_ENCUESTA);
        eliminar =  getArguments().getIntegerArrayList("lista");
        return rootView;
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setTitle(Mensajes.MSG_OBSERVACIONES);

        buttonFinalizar = (Button) view.findViewById(R.id.d_finalizar_button);
//        observaciones = (EditText) view.findViewById(R.id.d_observacion_text);

        buttonFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarResultados(eliminar);
                Intent intent = new Intent(view.getContext(), MainActivity.class);
//                intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,idEncuesta);
//                intent.putExtra(ExtrasID.EXTRA_ID_SERVICIO,servicio);
//                intent.putExtra(ExtrasID.EXTRA_OBSER,observaciones.getText().toString());
                startActivity(intent);
            }
        });


    }

    private void eliminarResultados(List<Integer> resultado) {
        Realm realm = Realm.getDefaultInstance();

        for(Integer resul:resultado){
            if(resul!=-1){
                final Cuadro cuadro = realm.where(Cuadro.class).equalTo("id", resul).findFirst();
                if(cuadro!=null){
                    if(cuadro.isValid()){
                        RealmList<Registro> registros = cuadro.getRegistros();
                        List<Integer> regIn= new ArrayList<>();
                        for(Registro re:registros){
                            regIn.add(re.getId());
                        }
                        for(Integer value:regIn){
                            Registro registro = realm.where(Registro.class).equalTo("id", value).findFirst();
                            if(registro!=null){
                                if(registro.isValid()){
                                    realm.beginTransaction();
                                    registro.deleteFromRealm();
                                    realm.commitTransaction();
                                }
                            }

                        }
                        realm.beginTransaction();
                        cuadro.deleteFromRealm();
                        realm.commitTransaction();
                    }

                }

            }
        }


        realm.close();
    }
}
