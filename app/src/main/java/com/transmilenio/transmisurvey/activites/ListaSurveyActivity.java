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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_survey);
        realm = Realm.getDefaultInstance();
        bindUI();
        definirEventos();
    }




    private void bindUI() {
        encuestas = new ArrayList<>();
        encuestas.add(new Encuesta("Ascensos y Descensos Troncal"));
        encuestas.add(new Encuesta("Eliminar Todo"));
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
                if(value.getNombre().equals("Ascensos y Descensos Troncal")){
                    Intent intent = new Intent(ListaSurveyActivity.this, SurveyActivity.class);
                    intent.putExtra(ExtrasID.EXTRA_NOMBRE,"Ascensos y Descensos Troncal");
                    intent.putExtra(ExtrasID.EXTRA_TIPO,ExtrasID.VALOR_NUEVO);
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
