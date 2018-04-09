package com.transmilenio.transmisurvey.activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.adapters.ModoSelectionAdapter;
import com.transmilenio.transmisurvey.adapters.ServiceSelectionAdapter;
import com.transmilenio.transmisurvey.models.db.Modo;
import com.transmilenio.transmisurvey.models.db.Opcion;
import com.transmilenio.transmisurvey.models.util.ExtrasID;
import com.transmilenio.transmisurvey.models.util.Mensajes;
import com.transmilenio.transmisurvey.util.ModosLlegada;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class SeleccionModoActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private Button troncalButton,alimentadorButton,zonalButton,troncalODButton;
    private String modo;
    private Realm realm;
    private ListView listView;
    private List<String> modos;
    private RealmResults<Modo> modosLista;
    private ModoSelectionAdapter modoSelectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_modo);
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        realm = Realm.getDefaultInstance();
        bindUI();
    }

    private void bindUI() {
        modo =  prefs.getString(ExtrasID.EXTRA_MODO,ExtrasID.TIPO_SERVICIO_TRONCAL);
        modos = getModosLista();

        listView = (ListView) findViewById(R.id.listView_sel_modo);
        modoSelectionAdapter = new ModoSelectionAdapter(this,modos,R.layout.list_view_surveys);
        listView.setAdapter(modoSelectionAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                String seleccion = (String) adapter.getItemAtPosition(position);
                String modoAbrv = encontrarModoAbrv(seleccion);
                if(modoAbrv!="nan"){
                    Intent intent = new Intent(SeleccionModoActivity.this,ListaSurveyActivity.class);
                    intent.putExtra(ExtrasID.EXTRA_MODO,modoAbrv);
                    startActivity(intent);
                }else{

                }


            }
        });


    }

    private String encontrarModoAbrv(String seleccion) {
        for(Modo modo:modosLista){
            if(modo.getNombre().equals(seleccion)){
                return modo.getAbreviatura();
            }
        }
        return "nan";
    }

    @NonNull
    private List<String> getModosLista() {
        List<String> modos = new ArrayList<>();
        if(modo!= "non"){
            modosLista = realm.where(Modo.class).equalTo("abreviatura",modo).findAll();
        }else{
            modosLista = realm.where(Modo.class).findAll();
        }

        for (Modo modo: modosLista){
            modos.add(modo.getNombre());
        }
        return modos;
    }
}
