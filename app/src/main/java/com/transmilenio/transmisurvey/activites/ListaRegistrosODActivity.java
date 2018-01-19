package com.transmilenio.transmisurvey.activites;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.adapters.RegistroAdapter;
import com.transmilenio.transmisurvey.adapters.RegistroOdAdapter;
import com.transmilenio.transmisurvey.fragments.AlertGuardarDatos;
import com.transmilenio.transmisurvey.models.db.OrigenDestinoBase;
import com.transmilenio.transmisurvey.models.db.RegistroOD;
import com.transmilenio.transmisurvey.models.json.CuadroEncuesta;
import com.transmilenio.transmisurvey.models.json.RegistroEncuesta;
import com.transmilenio.transmisurvey.models.util.ExtrasID;
import com.transmilenio.transmisurvey.models.util.Mensajes;
import com.transmilenio.transmisurvey.util.TipoODencuesta;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;

public class ListaRegistrosODActivity extends AppCompatActivity implements RealmChangeListener<RealmList<RegistroOD>> {

    private FloatingActionButton buttonAdd;
    private Button buttonGuardar;
    private ListView listView;
    private RegistroOdAdapter registroAdapter;
    private RealmList<RegistroOD> registros;
    private int  idEncuesta,idCuadro ;
    private String estacion,modo,tipo;

    //For dialog fragment
    FragmentManager fm = getSupportFragmentManager();

    private Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_registros_od);
        realm = Realm.getDefaultInstance();
        validarExtras();
        bindUI();
        setActionBarBotton();
    }

    private void validarExtras(){
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idEncuesta = (int) extras.get(ExtrasID.EXTRA_ID_ENCUESTA);
            idCuadro = (int) extras.get(ExtrasID.EXTRA_ID_CUADRO);
            estacion = (String) extras.get(ExtrasID.EXTRA_ID_ESTACION);
            modo = (String) extras.get(ExtrasID.EXTRA_MODO);
            tipo = (String) extras.get(ExtrasID.EXTRA_ID_ORIGEN);
            OrigenDestinoBase origenDestinoBase =  realm.where(OrigenDestinoBase.class).equalTo("id",idCuadro).findFirst();
            if(origenDestinoBase!=null){
                registros = origenDestinoBase.getRegistros();
                registros.addChangeListener(this);
            }
        }

    }

    private void setActionBarBotton() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AlertGuardarDatos dFragment = newInstance(idEncuesta);
                dFragment.show(fm, Mensajes.MSG_SALIR_ENCUESTA);
                break;
        }
        return true;
    }

    private void bindUI() {
        buttonAdd = (FloatingActionButton) findViewById(R.id.od_nuevo_button);
        buttonGuardar = (Button) findViewById(R.id.od_guardar_button);

        listView = (ListView) findViewById(R.id.od_registros_listView);
        registroAdapter = new RegistroOdAdapter(this,registros,R.layout.list_view_od_registro_item);
        listView.setAdapter(registroAdapter);


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaRegistrosODActivity.this, TransbordosActivity.class);
                if(tipo.equals(TipoODencuesta.TIPO_DESTINO)){
                    intent = new Intent(ListaRegistrosODActivity.this, OdDestinoActivity.class);
                }else if (tipo.equals(TipoODencuesta.TIPO_TRANSBORDO)){
                    intent = new Intent(ListaRegistrosODActivity.this, OdTransbordoActivity.class);
                }
                intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,  idEncuesta);
                intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,  idCuadro);
                intent.putExtra(ExtrasID.EXTRA_ID_ESTACION,  estacion);
                intent.putExtra(ExtrasID.EXTRA_ID_ORIGEN,  tipo);
                intent.putExtra(ExtrasID.EXTRA_MODO,  modo);
                startActivity(intent);
            }
        });

        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertGuardarDatos dFragment = newInstance(idEncuesta);
                dFragment.show(fm, Mensajes.MSG_SALIR_ENCUESTA);
            }
        });
    }

    public static AlertGuardarDatos newInstance(int id) {
        AlertGuardarDatos f = new AlertGuardarDatos();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt(ExtrasID.EXTRA_ID_ENCUESTA, id);
        f.setArguments(args);

        return f;
    }



    @Override
    public void onChange(RealmList<RegistroOD> registroODs) {
        registroAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        AlertGuardarDatos dFragment = newInstance(idEncuesta);
        dFragment.show(fm, Mensajes.MSG_SALIR_ENCUESTA);
    }
}
