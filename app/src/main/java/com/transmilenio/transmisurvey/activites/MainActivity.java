package com.transmilenio.transmisurvey.activites;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.adapters.OptionAdapter;
import com.transmilenio.transmisurvey.fragments.AlertGuardarDatos;
import com.transmilenio.transmisurvey.http.API;
import com.transmilenio.transmisurvey.http.SurveyService;
import com.transmilenio.transmisurvey.models.db.Cuadro;
import com.transmilenio.transmisurvey.models.db.Estacion;
import com.transmilenio.transmisurvey.models.db.Opcion;
import com.transmilenio.transmisurvey.models.db.Registro;
import com.transmilenio.transmisurvey.models.db.ServicioRutas;
import com.transmilenio.transmisurvey.models.json.Servicio;
import com.transmilenio.transmisurvey.models.util.ExtrasID;
import com.transmilenio.transmisurvey.models.util.Mensajes;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private OptionAdapter adapter;
    private List<Opcion> opcionesList;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();
        validarExtras();
        bindUI();
    }


    private void bindUI(){
        listView = (ListView) findViewById(R.id.ini_opciones_listView);
        opcionesList = new ArrayList<>();
        cargarOpciones();
        adapter = new OptionAdapter(this, opcionesList,R.layout.list_view_options);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Opcion value = (Opcion) adapter.getItemAtPosition(position);
                if(value.getName().equals(Mensajes.OPCION_NUEVA)){
                    Intent intent = new Intent(MainActivity.this,ListaSurveyActivity.class);
                    startActivity(intent);
                }else if( value.getName().equals(Mensajes.OPCION_ENVIAR)){
                    Intent intent = new Intent(MainActivity.this,ListaSurveyEnvioActivity.class);
                    startActivity(intent);
                }else if( value.getName().equals(Mensajes.OPCION_CONFIG)){
                    cargarServiciosTemporal();
            }

            }
        });
    }


    //Validacion de datos entre actividades
    private void validarExtras(){
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            Object idEliminar =  extras.get(ExtrasID.EXTRA_ID_ENCUESTA);
            if(idEliminar!=null){
                eliminarEncuesta((int)idEliminar);
            }
        }
    }

    private void eliminarEncuesta(final int idEncuesta) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //Eliminar Encuesta y Registros
                RealmResults<Cuadro> rows = realm.where(Cuadro.class).equalTo("id", idEncuesta).findAll();
                for (Cuadro cuadro : rows) {
                    RealmList<Registro> registros = cuadro.getRegistros();
                    registros.deleteAllFromRealm();
                }
                if (rows.size() > 0) {
                    rows.deleteAllFromRealm();
                }

            }
        });
    }


    private void cargarServiciosTemporal(){
        SurveyService surveyService = API.getApi().create(SurveyService.class);
        Call<List<Servicio>> call = surveyService.getServicios();
        call.enqueue(new Callback<List<Servicio>>() {
            @Override
            public void onResponse(Call<List<Servicio>> call, Response<List<Servicio>> response) {
                    guardarServicios(response.body());
                    Toast.makeText(MainActivity.this,Mensajes.MSG_SINCRONIZACION,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<List<Servicio>> call, Throwable t) {
                Toast.makeText(MainActivity.this,Mensajes.MSG_SINCRONIZACION_FALLO,Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    public void guardarServicios(List<Servicio> servicios){

        eliminarInfoServicios();

        for (Servicio servicio:servicios){
            ServicioRutas servicioRutas = new ServicioRutas(servicio.getNombre());
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(servicioRutas);
            realm.commitTransaction();
            cargarEstaciones(servicioRutas.getNombre(),servicio.getEstaciones());

        }
    }

    private void eliminarInfoServicios() {
        realm.beginTransaction();
        realm.delete(Estacion.class);
        realm.delete(ServicioRutas.class);
        realm.commitTransaction();
    }

    private void cargarEstaciones(String nombre, List<String> estaciones) {
        realm.beginTransaction();
        ServicioRutas servicioRutas = realm.where(ServicioRutas.class).equalTo("nombre", nombre).findFirst();
        for(String estacionNombre: estaciones){
            Estacion estacion = new Estacion(estacionNombre);
//                realm.beginTransaction();
            realm.copyToRealmOrUpdate(estacion);
            servicioRutas.getEstaciones().add(estacion);

        }
        realm.commitTransaction();
        System.out.println("");
    }


    //Cargar opciones
    private void cargarOpciones() {
        int[] covers = new int[]{
                R.drawable.ic_new_icon,
                R.drawable.ic_send_icon,
                R.drawable.ic_settings_icon};

        Opcion a = new Opcion(Mensajes.OPCION_NUEVA, covers[0]);
        opcionesList.add(a);

        a = new Opcion(Mensajes.OPCION_ENVIAR, covers[1]);
        opcionesList.add(a);

        a = new Opcion(Mensajes.OPCION_CONFIG, covers[2]);
        opcionesList.add(a);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
