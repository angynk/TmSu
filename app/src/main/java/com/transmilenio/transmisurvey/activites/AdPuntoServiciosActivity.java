package com.transmilenio.transmisurvey.activites;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.adapters.GridServiciosAdapter;
import com.transmilenio.transmisurvey.models.db.EstacionServicio;
import com.transmilenio.transmisurvey.models.db.Serv;
import com.transmilenio.transmisurvey.models.db.ServTemp;
import com.transmilenio.transmisurvey.models.util.ExtrasID;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class AdPuntoServiciosActivity extends AppCompatActivity {


    private int idCuadro;
    private int idEncuesta;
    private String estacion;
    private GridView gridView;
    private List<String> servicios;
    private Realm realm;
    private FloatingActionButton buttonCerrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_punto_servicios);
        realm = Realm.getDefaultInstance();
        getExtras();
        bindUi();
    }

    public void getExtras(){
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idCuadro = (int) extras.get(ExtrasID.EXTRA_ID_CUADRO);
            idEncuesta = (int) extras.get(ExtrasID.EXTRA_ID_ENCUESTA);
        }
    }

    public void bindUi(){
        servicios = new ArrayList<>();
        encontrarServicios();
        gridView = (GridView) findViewById(R.id.adp_gridView);
        buttonCerrar = (FloatingActionButton) findViewById(R.id.adp_cancelarRegistro_button);
        GridServiciosAdapter adapter = new GridServiciosAdapter(this,servicios,R.layout.service_layout);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Intent intent = new Intent(AdPuntoServiciosActivity.this,AdpRegistroActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,  idEncuesta);
                intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,  idCuadro);
                intent.putExtra(ExtrasID.EXTRA_ID_SERVICIO,  servicios.get(position));
                startActivity(intent);
            }});

        buttonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdPuntoServiciosActivity.this,ListaRegistrosADPActivity.class);
                intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,  idEncuesta);
                intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,  idCuadro);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void encontrarServicios() {
            RealmResults<ServTemp> servTemp = realm.where(ServTemp.class).findAll();
                for(ServTemp serv:servTemp){
                    if(serv.getServicio().length()> 13){
                        servicios.add(serv.getServicio().substring(0,13));
                    }else{
                        servicios.add(serv.getServicio());
                    }
                }

    }
}
