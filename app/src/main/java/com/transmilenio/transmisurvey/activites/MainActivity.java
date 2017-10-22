package com.transmilenio.transmisurvey.activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.adapters.OpcionAdapter;
import com.transmilenio.transmisurvey.models.db.Cuadro;
import com.transmilenio.transmisurvey.models.db.Opcion;
import com.transmilenio.transmisurvey.models.db.Registro;
import com.transmilenio.transmisurvey.models.util.ExtrasID;
import com.transmilenio.transmisurvey.models.util.Mensajes;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

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
                    Intent intent = new Intent(MainActivity.this,ListaSurveyEditActivity.class);
                    startActivity(intent);
                }else if (position ==2){
                    Intent intent = new Intent(MainActivity.this,ListaSurveyEnvioActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    //Cargar opciones
    private void cargarOpciones() {
        int[] covers = new int[]{
                R.mipmap.ic_new,
                R.mipmap.ic_edit,
                R.mipmap.ic_send,
                R.mipmap.ic_settings};

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
