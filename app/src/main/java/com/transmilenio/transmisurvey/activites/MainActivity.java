package com.transmilenio.transmisurvey.activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.adapters.OpcionAdapter;
import com.transmilenio.transmisurvey.http.API;
import com.transmilenio.transmisurvey.http.SurveyService;
import com.transmilenio.transmisurvey.models.db.Cuadro;
import com.transmilenio.transmisurvey.models.db.Estacion;
import com.transmilenio.transmisurvey.models.db.Opcion;
import com.transmilenio.transmisurvey.models.db.Registro;
import com.transmilenio.transmisurvey.models.db.Resultado;
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

    private RecyclerView recyclerView;
    private OpcionAdapter adapter;
    private List<Opcion> opcionesList;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();
        validarExtras();
        bindUI();
        cargarOpciones();
    }


    private void bindUI(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
        recyclerView.setHasFixedSize(true);
        opcionesList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new OpcionAdapter(this, opcionesList);
        recyclerView.setAdapter(adapter);
    }


    //Validacion de datos entre actividades
    private void validarExtras(){
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            eliminarEncuesta( (int) extras.get(ExtrasID.EXTRA_ID_ENCUESTA));
        }
    }

    private void eliminarEncuesta(final int idEncuesta) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //Eliminar Encuesta y Registros
                RealmResults<Cuadro> rows = realm.where(Cuadro.class).equalTo("id",idEncuesta).findAll();
                for(Cuadro cuadro: rows){
                    RealmList<Registro> registros = cuadro.getRegistros();
                    registros.deleteAllFromRealm();
                }
                rows.deleteAllFromRealm();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((OpcionAdapter) adapter).setOnItemClickListener(new OpcionAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if(position==0){
                    Intent intent = new Intent(MainActivity.this,ListaSurveyActivity.class);
                    startActivity(intent);
                }else if(position==1){
                }else if (position ==2){
                    Intent intent = new Intent(MainActivity.this,ListaSurveyEnvioActivity.class);
                    startActivity(intent);
                }else if (position == 3 ){
                    cargarServiciosTemporal();
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
            }

            @Override
            public void onFailure(Call<List<Servicio>> call, Throwable t) {
                System.out.println("Hola");
            }
        });
    }

    public void guardarServicios(List<Servicio> servicios){


        for (Servicio servicio:servicios){
            ServicioRutas servicioRutas = new ServicioRutas(servicio.getNombre());
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(servicioRutas);
            realm.commitTransaction();
            cargarEstaciones(servicioRutas.getNombre(),servicio.getEstaciones());

        }
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
                R.drawable.ic_edit_icon,
                R.drawable.ic_send_icon,
                R.drawable.ic_settings_icon};

        Opcion a = new Opcion(Mensajes.OPCION_NUEVA, covers[0]);
        opcionesList.add(a);

        a = new Opcion(Mensajes.OPCION_EDITAR, covers[1]);
        opcionesList.add(a);

        a = new Opcion(Mensajes.OPCION_ENVIAR, covers[2]);
        opcionesList.add(a);

        a = new Opcion(Mensajes.OPCION_CONFIG, covers[3]);
        opcionesList.add(a);

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
