package com.transmilenio.transmisurvey.activites;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.http.API;
import com.transmilenio.transmisurvey.http.SurveyService;
import com.transmilenio.transmisurvey.models.db.Estacion;
import com.transmilenio.transmisurvey.models.db.EstacionServicio;
import com.transmilenio.transmisurvey.models.db.Modo;
import com.transmilenio.transmisurvey.models.db.Serv;
import com.transmilenio.transmisurvey.models.db.ServicioRutas;
import com.transmilenio.transmisurvey.models.json.Config;
import com.transmilenio.transmisurvey.models.json.ConfigModo;
import com.transmilenio.transmisurvey.models.json.ModoJSON;
import com.transmilenio.transmisurvey.models.util.ExtrasID;
import com.transmilenio.transmisurvey.models.util.Mensajes;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeleccionarSincroActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private Button modoButton,infoBaseButton;
    private ProgressDialog progressDoalog;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_sincro);
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        realm = Realm.getDefaultInstance();
        bindUI();
    }

    private void bindUI() {
        modoButton = (Button) findViewById(R.id.ses_modos_button);
        infoBaseButton = (Button) findViewById(R.id.ses_info_base_button);

        modoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarModos();
            }
        });

        infoBaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(existenModos()){
                    Intent intent = new Intent(SeleccionarSincroActivity.this,ConfiguracionActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(SeleccionarSincroActivity.this,Mensajes.MSG_SINCRONIXE_MODOS,Toast.LENGTH_SHORT).show();
                }

            }


        });

    }

    private boolean existenModos() {
        RealmResults<Modo> modos = realm.where(Modo.class).findAll();
        if(modos.size()>0){
            return true;
        }
        return false;
    }

    public void cargarModos(){
        progressDoalog = new ProgressDialog(SeleccionarSincroActivity.this);
        progressDoalog.setMessage(Mensajes.MSG_SINCRONIZANDO);
        progressDoalog.setTitle(Mensajes.MSG_CONFIGURACION);
        progressDoalog.setCanceledOnTouchOutside(false);
        progressDoalog.show();

        SurveyService surveyService = API.getApi().create(SurveyService.class);
        Call<ConfigModo> call = surveyService.getModos();
        call.enqueue(new Callback<ConfigModo>() {
            @Override
            public void onResponse(Call<ConfigModo> call, Response<ConfigModo> response) {
                guardarModos(response.body().getModos());
                progressDoalog.dismiss();
                Toast.makeText(SeleccionarSincroActivity.this,Mensajes.MSG_SINCRONIZACION,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SeleccionarSincroActivity.this,MainActivity.class);
                startActivity(intent);

            }

            @Override
            public void onFailure(Call<ConfigModo> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(SeleccionarSincroActivity.this,Mensajes.MSG_SINCRONIZACION_FALLO,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarModos(List<ModoJSON> modos) {
        eliminarModos();
        eliminarInfoEstaciones();
        eliminarInfoServicios();
        for(ModoJSON modoJSON:modos){
            Modo modo = new Modo(modoJSON.getNombre(),modoJSON.getAbreviatura(),modoJSON.getDescripcion());
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(modo);
            realm.commitTransaction();
        }
    }

    private void eliminarModos() {
        realm.beginTransaction();
        realm.delete(Modo.class);
        realm.commitTransaction();

    }

    private void eliminarInfoEstaciones() {
        realm.beginTransaction();
        realm.delete(Serv.class);
        realm.delete(EstacionServicio.class);
        realm.commitTransaction();
    }

    private void eliminarInfoServicios() {
        realm.beginTransaction();
        realm.delete(Estacion.class);
        realm.delete(ServicioRutas.class);
        realm.commitTransaction();
    }


}
