
package com.transmilenio.transmisurvey.activites;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.adapters.SurveyEditAdapter;
import com.transmilenio.transmisurvey.adapters.SurveySendAdapter;
import com.transmilenio.transmisurvey.http.SurveyService;
import com.transmilenio.transmisurvey.models.Cuadro;
import com.transmilenio.transmisurvey.models.Resultado;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListaSurveyEnvioActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<Cuadro>> {

    private ListView listView;
    private Button buttonEnviar;
    private SurveySendAdapter surveyAdapter;
    private RealmResults<Cuadro> encuestas;

    private ArrayList<Cuadro> modelArrayList;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_survey_envio);

        realm = Realm.getDefaultInstance();
        encuestas = realm.where(Cuadro.class).findAll();
        encuestas.addChangeListener(this);
        bindUI();
    }

    private void bindUI() {

        listView = (ListView) findViewById(R.id.listView_send_surveys);
        buttonEnviar = (Button) findViewById(R.id.button_enviar_encuestas);
        surveyAdapter = new SurveySendAdapter(this,encuestas,R.layout.list_view_send_survey);
        listView.setAdapter(surveyAdapter);

        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                surveyAdapter.getSelectedItems();
                enviarDatosEncuesta();
            }
        });

//        listView.setOnItemClickListener(onItemClickListener());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Cuadro value = (Cuadro)adapter.getItemAtPosition(position);
                Toast.makeText(ListaSurveyEnvioActivity.this,value.getNombreEncuesta(),Toast.LENGTH_LONG);
            }
        });

    }

    private void enviarDatosEncuesta() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/TmAPI/") // Localhost para android
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SurveyService surveyService = retrofit.create(SurveyService.class);
        Call<Resultado> call = surveyService.sendSurvey("ejemplo");
        call.enqueue(new Callback<Resultado>() {
            @Override
            public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                Resultado resultado = response.body();
            }

            @Override
            public void onFailure(Call<Resultado> call, Throwable t) {
                Toast.makeText(ListaSurveyEnvioActivity.this,"No funciono",Toast.LENGTH_LONG);
            }
        });

    }

    private ArrayList<Cuadro> getModel(boolean isSelect){
        ArrayList<Cuadro> list = new ArrayList<>();
        for(int i = 0; i < encuestas.size(); i++){
            Cuadro cuadro = encuestas.get(i);
            if(cuadro.isSelected()){
                list.add(cuadro);
            }
        }
        return list;
    }


    @Override
    public void onChange(RealmResults<Cuadro> cuadros) {
        surveyAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
