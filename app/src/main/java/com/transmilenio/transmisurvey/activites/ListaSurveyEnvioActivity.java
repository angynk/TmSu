
package com.transmilenio.transmisurvey.activites;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.adapters.SurveySendAdapter;
import com.transmilenio.transmisurvey.http.API;
import com.transmilenio.transmisurvey.http.SurveyService;
import com.transmilenio.transmisurvey.models.db.Cuadro;
import com.transmilenio.transmisurvey.models.db.Registro;
import com.transmilenio.transmisurvey.models.db.Resultado;
import com.transmilenio.transmisurvey.models.json.CuadroEncuesta;
import com.transmilenio.transmisurvey.models.json.EncuestasTerminadas;
import com.transmilenio.transmisurvey.models.json.RegistroEncuesta;
import com.transmilenio.transmisurvey.models.util.Mensajes;

import java.util.ArrayList;;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaSurveyEnvioActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<Cuadro>> {

    private ListView listView;
    private Button buttonEnviar;
    private SurveySendAdapter surveyAdapter;
    private RealmResults<Cuadro> encuestas;

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
                if(surveyAdapter.getSelectedItems().size()>0){
                    enviarDatosEncuesta();
                }else{
                    Toast.makeText(ListaSurveyEnvioActivity.this,Mensajes.MSG_NO_HAY_ENCUESTAS,Toast.LENGTH_LONG).show();
                }

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Cuadro value = (Cuadro)adapter.getItemAtPosition(position);
                Toast.makeText(ListaSurveyEnvioActivity.this,value.getNombreEncuesta(),Toast.LENGTH_LONG).show();
            }
        });

    }

    private void enviarDatosEncuesta() {
        ArrayList<Cuadro> selectedItems = surveyAdapter.getSelectedItems();
        progressDoalog = new ProgressDialog(ListaSurveyEnvioActivity.this);
        progressDoalog.setMessage(Mensajes.MSG_ENVIANDO);
        progressDoalog.setTitle(Mensajes.MSG_ENCUESTA);
        progressDoalog.setCanceledOnTouchOutside(false);
        progressDoalog.show();

        EncuestasTerminadas encuestas = new EncuestasTerminadas();
        ArrayList<CuadroEncuesta> valores = new ArrayList<>();
        for(Cuadro cuadro:selectedItems){
            valores.add(fromCuadroToJson(cuadro));
        }
        encuestas.setEncuestas(valores);
        enviarEncuesta(encuestas);

    }

    ProgressDialog progressDoalog;


    private void enviarEncuesta(final EncuestasTerminadas encuestas) {
        SurveyService surveyService = API.getApi().create(SurveyService.class);
        Call<List<Resultado>> call = surveyService.sendSurvey(encuestas);
               call.enqueue(new Callback<List<Resultado>>() {
            @Override
            public void onResponse(Call<List<Resultado>> call, Response<List<Resultado>> response) {
                List<Resultado> resultado = response.body();
                eliminarResultados(resultado);
                progressDoalog.dismiss();
                showAlertDialog(Mensajes.MSG_ENCUESTAS_ENVIADAS);
            }

            @Override
            public void onFailure(Call<List<Resultado>> call, Throwable t) {
                progressDoalog.dismiss();
                showAlertDialog(Mensajes.MSG_ENCUESTAS_NO_ENVIADAS);
            }
        });

    }


    private void showAlertDialog(String mensaje){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(Mensajes.MSG_RESULTADO_ENVIO);
        alertDialog.setMessage(mensaje);
        alertDialog.setButton(Dialog.BUTTON_POSITIVE,Mensajes.MSG_ACEPTAR, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                finish();
            }
        });
        alertDialog.show();
    }

    private void eliminarResultados(List<Resultado> resultado) {
        for(Resultado resul:resultado){
            if(resul.getId()!=-1){
                Cuadro cuadro = realm.where(Cuadro.class).equalTo("id", resul.getId()).findFirst();
                if(cuadro!=null){
                    realm.beginTransaction();
                    cuadro.deleteFromRealm();
                    realm.commitTransaction();
                }

            }
        }
        surveyAdapter.setSelectedItems(new ArrayList<Cuadro>());
    }

    private CuadroEncuesta fromCuadroToJson(Cuadro cuadro) {
        CuadroEncuesta request = new CuadroEncuesta();
        request.setNombre_encuesta(cuadro.getNombreEncuesta());
        request.setDia_semana(cuadro.getDiaSemana());
        request.setNum_bus(cuadro.getNumBus());
        request.setNum_puerta(cuadro.getNumPuerta());
        request.setFecha_encuesta(cuadro.getFecha());
        request.setRecorrido(cuadro.getRecorrido());
        request.setServicio(cuadro.getServicio());
        request.setId_realm(cuadro.getId());

        List<RegistroEncuesta> listaRegistros = new ArrayList<>();
        for(Registro registro: cuadro.getRegistros()){
            RegistroEncuesta reg = new RegistroEncuesta();
            reg.setBajan(registro.getBajan());
            reg.setEstacion(registro.getEstacion());
            reg.setHora_llegada(registro.getHoraLlegada());
            reg.setHora_salida(registro.getHoraSalida());
            reg.setEstacion(registro.getEstacion());
            reg.setQuedan(registro.getQuedan());
            reg.setSuban(registro.getSuban());
            listaRegistros.add(reg);
        }

        request.setRegistros(listaRegistros);
        return request;
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
