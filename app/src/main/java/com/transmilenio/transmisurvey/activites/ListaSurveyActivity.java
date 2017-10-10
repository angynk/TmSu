package com.transmilenio.transmisurvey.activites;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.adapters.RegistroAdapter;
import com.transmilenio.transmisurvey.adapters.SurveyAdapter;
import com.transmilenio.transmisurvey.models.Encuesta;

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
        bindUI();
        realm = Realm.getDefaultInstance();


    }




    private void bindUI() {
        encuestas = new ArrayList<>();
        encuestas.add(new Encuesta("Ascensos y Descensos Troncal"));
        encuestas.add(new Encuesta("Eliminar Todo"));
        listView = (ListView) findViewById(R.id.listView_surveys);
        surveyAdapter = new SurveyAdapter(this,encuestas,R.layout.list_view_surveys);
        listView.setAdapter(surveyAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Encuesta value = (Encuesta)adapter.getItemAtPosition(position);
                if(value.getNombre().equals("Ascensos y Descensos Troncal")){
                    Intent intent = new Intent(ListaSurveyActivity.this, SurveyActivity.class);
                    startActivity(intent);
                }else{
                            realm.beginTransaction();
                            realm.deleteAll();
                            realm.commitTransaction();
                }

            }
        });


    }
}
