package com.transmilenio.transmisurvey.activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.adapters.ServiceSelectionAdapter;
import com.transmilenio.transmisurvey.adapters.SurveySendAdapter;
import com.transmilenio.transmisurvey.models.db.AdPuntoEncuesta;
import com.transmilenio.transmisurvey.models.db.Cuadro;
import com.transmilenio.transmisurvey.models.db.EstacionServicio;
import com.transmilenio.transmisurvey.models.db.Serv;
import com.transmilenio.transmisurvey.models.db.ServTemp;
import com.transmilenio.transmisurvey.models.json.EncuestaTM;
import com.transmilenio.transmisurvey.models.util.ExtrasID;
import com.transmilenio.transmisurvey.models.util.Mensajes;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class SeleccionServicioActivity extends AppCompatActivity {


    private ListView listView;
    private Button buttonEnviar;
    private ServiceSelectionAdapter serviciosAdapter;
    private List<String> servicios;
    private SharedPreferences prefs;
    private Realm realm;
    private int  idEncuesta, idCuadro ;
    private String estacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_servicio);
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        realm = Realm.getDefaultInstance();
        getExtras();
        bindUI();
    }

    private void bindUI() {
        cargarServicios();
        listView = (ListView) findViewById(R.id.listView_adp_seleccion);
        buttonEnviar = (Button) findViewById(R.id.adp_contServ_button);
        serviciosAdapter = new ServiceSelectionAdapter(this,servicios,R.layout.list_view_select_service);
        listView.setAdapter(serviciosAdapter);

        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(serviciosAdapter.getSelectedItems().size()>0){
                    GuardarServiciosSeleccionados();
                    Intent intent = new Intent(SeleccionServicioActivity.this,ListaRegistrosADPActivity.class);
                    intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,  idEncuesta);
                    intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,  idCuadro);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(SeleccionServicioActivity.this, Mensajes.MSG_NO_HAY_ENCUESTAS,Toast.LENGTH_LONG).show();
                }
            }
        });


//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(AdapterView<?> adapter, View v, int position,
//                                    long arg3)
//            {
//                Cuadro value = (Cuadro)adapter.getItemAtPosition(position);
//                Toast.makeText(SeleccionServicioActivity.this,value.getNombreEncuesta(),Toast.LENGTH_LONG).show();
//            }
//        });

    }

    private void GuardarServiciosSeleccionados() {

        ArrayList<String> selectedItems = serviciosAdapter.getSelectedItems();
        //Borrar datos previos
        realm.beginTransaction();
        realm.delete(ServTemp.class);
        realm.commitTransaction();
        //guardar Nuevos datos
        realm.beginTransaction();
        for (String servicio:selectedItems){
            ServTemp servTemp = new ServTemp(servicio);
            realm.copyToRealmOrUpdate(servTemp);
        }
        realm.commitTransaction();
    }

    private void cargarServicios() {
        servicios = new ArrayList<>();
        if(estacion!=null){
            EstacionServicio estaciones = realm.where(EstacionServicio.class).equalTo("nombre", estacion).findFirst();
            if(estaciones!=null){
                RealmList<Serv> listServ = estaciones.getServicios();
                for(Serv serv:listServ){
                        servicios.add(serv.getNombre());

                }
            }
        }
    }


    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idEncuesta = (int) extras.get(ExtrasID.EXTRA_ID_ENCUESTA);
            idCuadro = (int) extras.get(ExtrasID.EXTRA_ID_CUADRO);
            estacion = (String) extras.get(ExtrasID.EXTRA_ID_ESTACION);
        }
    }


}
