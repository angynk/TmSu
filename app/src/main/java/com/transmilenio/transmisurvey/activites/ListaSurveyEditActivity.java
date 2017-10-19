package com.transmilenio.transmisurvey.activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.adapters.SurveyEditAdapter;
import com.transmilenio.transmisurvey.models.db.Cuadro;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ListaSurveyEditActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<Cuadro>> {

    private ListView listView;
    private SurveyEditAdapter surveyAdapter;
    private RealmResults<Cuadro> encuestas;
    private Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_survey_edit);

        realm = Realm.getDefaultInstance();
        encuestas = realm.where(Cuadro.class).findAll();
        encuestas.addChangeListener(this);
        bindUI();



    }


    private void bindUI() {
        listView = (ListView) findViewById(R.id.listView_edit_surveys);
        surveyAdapter = new SurveyEditAdapter(this,encuestas,R.layout.list_view_edi_survey);
        listView.setAdapter(surveyAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Cuadro value = (Cuadro)adapter.getItemAtPosition(position);
                Intent intent = new Intent(ListaSurveyEditActivity.this, SurveyActivity.class);
                intent.putExtra("id",value.getId());
                intent.putExtra("tipo","Edicion");
                startActivity(intent);
            }
        });


    }

    @Override
    public void onChange(RealmResults<Cuadro> registros) {
        surveyAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
