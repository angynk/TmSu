package com.transmilenio.transmisurvey.activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.adapters.SurveyAdapter;
import com.transmilenio.transmisurvey.models.db.Encuesta;
import com.transmilenio.transmisurvey.models.util.ExtrasID;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class ListaSurveyActivity extends AppCompatActivity {

    private ListView listView;
    private SurveyAdapter surveyAdapter;
    private List<Encuesta> encuestas;
    private Realm realm;
    private String modo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_survey);
        realm = Realm.getDefaultInstance();
        extraerExtras();
        bindUI();
        definirEventos();
    }

    private void extraerExtras() {
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            modo = (String) extras.get(ExtrasID.EXTRA_MODO);
        }
    }


    private void bindUI() {
        encuestas = new ArrayList<>();
        encuestas.add(new Encuesta(ExtrasID.NOMBRE_ENCUESTA_ASCDES_TRONCAL));
        encuestas.add(new Encuesta(ExtrasID.NOMBRE_ENCUESTA_FRECUENCIA_OCUPACION));
        encuestas.add(new Encuesta(ExtrasID.NOMBRE_ENCUESTA_ASCDES_PUNTO));
        encuestas.add(new Encuesta(ExtrasID.NOMBRE_ENCUESTA_CONTEO_DESPACHOS));
        encuestas.add(new Encuesta(ExtrasID.NOMBRE_ENCUESTA_ORIGEN_DESTINO));
        encuestas.add(new Encuesta(ExtrasID.NOMBRE_ENCUESTA_FRECUENCIA_OCUPACION_BUS));
        encuestas.add(new Encuesta(ExtrasID.NOMBRE_ENCUESTA_TIEMPOS_RECORRIDO));
//        encuestas.add(new Encuesta("Eliminar Todo"));
        listView = (ListView) findViewById(R.id.listView_surveys);
        surveyAdapter = new SurveyAdapter(this,encuestas,R.layout.list_view_surveys);
        listView.setAdapter(surveyAdapter);
    }

    private void definirEventos(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Encuesta value = (Encuesta)adapter.getItemAtPosition(position);
                if(value.getNombre().equals(ExtrasID.NOMBRE_ENCUESTA_ASCDES_TRONCAL)){
                    Intent intent = new Intent(ListaSurveyActivity.this, SurveyActivity.class);
                    intent.putExtra(ExtrasID.EXTRA_NOMBRE,value.getNombre());
                    intent.putExtra(ExtrasID.EXTRA_MODO,modo);
                    startActivity(intent);
                }else if (value.getNombre().equals(ExtrasID.NOMBRE_ENCUESTA_FRECUENCIA_OCUPACION)) {
                    Intent intent = new Intent(ListaSurveyActivity.this, BaseFrecOcupacionActivity.class);
                    intent.putExtra(ExtrasID.EXTRA_NOMBRE,value.getNombre());
                    intent.putExtra(ExtrasID.EXTRA_MODO,modo);
                    startActivity(intent);
                }else if (value.getNombre().equals(ExtrasID.NOMBRE_ENCUESTA_ASCDES_PUNTO)) {
                    Intent intent = new Intent(ListaSurveyActivity.this, BaseADPuntoFijoActivity.class);
                    intent.putExtra(ExtrasID.EXTRA_NOMBRE,value.getNombre());
                    intent.putExtra(ExtrasID.EXTRA_MODO,modo);
                    startActivity(intent);
                }else if (value.getNombre().equals(ExtrasID.NOMBRE_ENCUESTA_CONTEO_DESPACHOS)) {
                    Intent intent = new Intent(ListaSurveyActivity.this, ConteoDesActivity.class);
                    intent.putExtra(ExtrasID.EXTRA_NOMBRE,value.getNombre());
                    intent.putExtra(ExtrasID.EXTRA_MODO,modo);
                    startActivity(intent);
                }else if (value.getNombre().equals(ExtrasID.NOMBRE_ENCUESTA_ORIGEN_DESTINO)) {
                    Intent intent = new Intent(ListaSurveyActivity.this, OrigenDestinoActivity.class);
                    intent.putExtra(ExtrasID.EXTRA_NOMBRE,value.getNombre());
                    intent.putExtra(ExtrasID.EXTRA_MODO,modo);
                    startActivity(intent);
                }else if (value.getNombre().equals(ExtrasID.NOMBRE_ENCUESTA_FRECUENCIA_OCUPACION_BUS)) {
                    Intent intent = new Intent(ListaSurveyActivity.this, FrecOcupaBusActivity.class);
                    intent.putExtra(ExtrasID.EXTRA_NOMBRE,value.getNombre());
                    intent.putExtra(ExtrasID.EXTRA_MODO,modo);
                    startActivity(intent);
                }else if (value.getNombre().equals(ExtrasID.NOMBRE_ENCUESTA_TIEMPOS_RECORRIDO)) {
                    Intent intent = new Intent(ListaSurveyActivity.this, TiemposRecorridoActivity.class);
                    intent.putExtra(ExtrasID.EXTRA_NOMBRE,value.getNombre());
                    intent.putExtra(ExtrasID.EXTRA_MODO,modo);
                    startActivity(intent);
                }else{
                    realm.beginTransaction();
                    realm.deleteAll();
                    realm.commitTransaction();
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
