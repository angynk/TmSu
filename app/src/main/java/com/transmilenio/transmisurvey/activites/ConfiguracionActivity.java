package com.transmilenio.transmisurvey.activites;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.http.API;
import com.transmilenio.transmisurvey.http.SurveyService;
import com.transmilenio.transmisurvey.models.db.Estacion;
import com.transmilenio.transmisurvey.models.db.EstacionServicio;
import com.transmilenio.transmisurvey.models.db.Serv;
import com.transmilenio.transmisurvey.models.db.ServicioRutas;
import com.transmilenio.transmisurvey.models.json.Config;
import com.transmilenio.transmisurvey.models.json.EstacionTs;
import com.transmilenio.transmisurvey.models.json.Servicio;
import com.transmilenio.transmisurvey.models.util.Mensajes;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfiguracionActivity extends AppCompatActivity {

    private Button buttonSincronizar;
    private SearchableSpinner modos;
    private ProgressDialog progressDoalog;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        realm = Realm.getDefaultInstance();
        bindUI();
    }

    private void bindUI() {
        buttonSincronizar = (Button) findViewById(R.id.di_sincronizar);
        modos = (SearchableSpinner) findViewById(R.id.di_modo);
        List<String> list = getModosLista();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modos.setAdapter(dataAdapter);
        modos.setTitle("Seleccione Uno");
        modos.setPositiveButton("OK");

        buttonSincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modos.getSelectedItem().toString()!=null){
                    String modo = obtenerModo(modos.getSelectedItem().toString());
                    cargarServiciosTemporal(modo);
                }
            }
        });
    }

    private String obtenerModo(String valor) {

        if(valor.equals("Troncal")){
            return "tro";
        }else if (valor.equals("Alimentación")){
            return "ali";
        }

        return "tod";
    }

    private List<String> getModosLista() {
        List<String> modos = new ArrayList<>();
        modos.add("Troncal");
        modos.add("Alimentación");
        modos.add("Todos");
        return modos;
    }

    private void cargarServiciosTemporal(String modo){
        progressDoalog = new ProgressDialog(ConfiguracionActivity.this);
        progressDoalog.setMessage(Mensajes.MSG_SINCRONIZANDO);
        progressDoalog.setTitle(Mensajes.MSG_CONFIGURACION);
        progressDoalog.setCanceledOnTouchOutside(false);
        progressDoalog.show();

        SurveyService surveyService = API.getApi().create(SurveyService.class);
        Call<Config> call = surveyService.getServicios(modo);
        call.enqueue(new Callback<Config>() {
            @Override
            public void onResponse(Call<Config> call, Response<Config> response) {
                guardarServicios(response.body().getServicios());
                guardarEstaciones(response.body().getEstacionTs());
                progressDoalog.dismiss();
                Toast.makeText(ConfiguracionActivity.this,Mensajes.MSG_SINCRONIZACION,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Config> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(ConfiguracionActivity.this,Mensajes.MSG_SINCRONIZACION_FALLO,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarEstaciones(List<EstacionTs> estaciones) {
        eliminarInfoEstaciones();
        for (EstacionTs estacion:estaciones){
            EstacionServicio estacionServicio = new EstacionServicio(estacion.getNombre(),estacion.getTipo());
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(estacionServicio);
            realm.commitTransaction();
            cargarServicios(estacion.getNombre(),estacion.getServicios());

        }
    }

    private void eliminarInfoEstaciones() {
        realm.beginTransaction();
        realm.delete(Serv.class);
        realm.delete(EstacionServicio.class);
        realm.commitTransaction();
    }


    public void guardarServicios(List<Servicio> servicios){

        eliminarInfoServicios();

        for (Servicio servicio:servicios){
            ServicioRutas servicioRutas = new ServicioRutas(servicio.getNombre(),servicio.getTipo());
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(servicioRutas);
            realm.commitTransaction();
            cargarEstaciones(servicioRutas.getNombre(),servicio.getEstaciones(),servicio.getTipo());

        }
    }

    private void cargarServicios(String nombre, List<String> servicios) {

        realm.beginTransaction();
        EstacionServicio estacionServicio = realm.where(EstacionServicio.class).equalTo("nombre", nombre).findFirst();
        for(String servNombre: servicios){
            Serv serv = realm.where(Serv.class).equalTo("nombre", servNombre).findFirst();
            if(serv==null){
                serv = new Serv(servNombre);
                realm.copyToRealmOrUpdate(serv);
            }
            estacionServicio.getServicios().add(serv);
        }
        realm.commitTransaction();
    }

    private void eliminarInfoServicios() {
        realm.beginTransaction();
        realm.delete(Estacion.class);
        realm.delete(ServicioRutas.class);
        realm.commitTransaction();
    }

    private void cargarEstaciones(String nombre, List<String> estaciones, String tipo) {
        realm.beginTransaction();
        ServicioRutas servicioRutas = realm.where(ServicioRutas.class).equalTo("nombre", nombre).findFirst();
        for(String estacionNombre: estaciones){
            Estacion estacion = realm.where(Estacion.class).equalTo("nombre", estacionNombre).findFirst();
            if(estacion==null){
                estacion = new Estacion(estacionNombre,tipo);
                realm.copyToRealmOrUpdate(estacion);
            }
            servicioRutas.getEstaciones().add(estacion);
        }
        realm.commitTransaction();
    }
}
