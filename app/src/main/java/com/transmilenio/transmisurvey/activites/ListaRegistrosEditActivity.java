package com.transmilenio.transmisurvey.activites;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.adapters.RegistroAdapter;
import com.transmilenio.transmisurvey.fragments.AlertGuardarDatos;
import com.transmilenio.transmisurvey.models.db.Cuadro;
import com.transmilenio.transmisurvey.models.db.Registro;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;

public class ListaRegistrosEditActivity extends AppCompatActivity implements RealmChangeListener<RealmList<Registro>> {

    private FloatingActionButton buttonAdd;
    private Button buttonGuardar;
    private ListView listView;
    private RegistroAdapter registroAdapter;
    private RealmList<Registro> registros;
    private int  idEncuesta ;
    private String tipoEncuesta;

    //For dialog fragment
    FragmentManager fm = getSupportFragmentManager();

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_registros_edit);
        Bundle extras = getIntent().getExtras();

        //DB
        realm = Realm.getDefaultInstance();

        if(extras != null){
            idEncuesta = (int) extras.get("idEncuesta");
            tipoEncuesta = (String) extras.get("tipo");
            Cuadro cuadro =  realm.where(Cuadro.class).equalTo("id",idEncuesta).findFirst();
            registros = cuadro.getRegistros();
        }

        registros.addChangeListener(this);

        bindUI();
        setActionBarBotton();



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
                AlertGuardarDatos dFragment = newInstance(idEncuesta,tipoEncuesta);
                dFragment.show(fm, "Salir de Encuesta");
                break;
        }
        return true;
    }


    private void bindUI() {


        buttonAdd = (FloatingActionButton) findViewById(R.id.button_nuevo_edit);
        buttonGuardar = (Button) findViewById(R.id.button_guardar_edit);

        listView = (ListView) findViewById(R.id.listView_registros_edit);
        registroAdapter = new RegistroAdapter(this,registros,R.layout.list_view_registro_item);
        listView.setAdapter(registroAdapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaRegistrosEditActivity.this, RegistroEditActivity.class);
                intent.putExtra("idEncuesta",  idEncuesta);
                intent.putExtra("tipo","Nuevo");
                startActivity(intent);
            }
        });

        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertGuardarDatos dFragment = newInstance(idEncuesta,tipoEncuesta);
                dFragment.show(fm, "Salir de Encuesta");
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Registro value = (Registro) adapter.getItemAtPosition(position);
                Intent intent = new Intent(ListaRegistrosEditActivity.this, RegistroEditActivity.class);
                intent.putExtra("idRegistro",value.getId());
                intent.putExtra("idEncuesta",idEncuesta);
                intent.putExtra("tipo","Edicion");
                startActivity(intent);
            }
        });
    }

    public static AlertGuardarDatos newInstance(int id,String tipoEncuesta) {
        AlertGuardarDatos f = new AlertGuardarDatos();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("idEncuesta", id);
        args.putString("tipo", tipoEncuesta);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onChange(RealmList<Registro> registros) {
        registroAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
