package com.transmilenio.transmisurvey.activites;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.adapters.OptionAdapter;
import com.transmilenio.transmisurvey.fragments.AlertGuardarDatos;
import com.transmilenio.transmisurvey.fragments.AlertObservacion;
import com.transmilenio.transmisurvey.fragments.AlertSincronizar;
import com.transmilenio.transmisurvey.http.API;
import com.transmilenio.transmisurvey.http.SurveyService;
import com.transmilenio.transmisurvey.models.db.Cuadro;
import com.transmilenio.transmisurvey.models.db.Estacion;
import com.transmilenio.transmisurvey.models.db.EstacionServicio;
import com.transmilenio.transmisurvey.models.db.Opcion;
import com.transmilenio.transmisurvey.models.db.Registro;
import com.transmilenio.transmisurvey.models.db.RegistroAdPunto;
import com.transmilenio.transmisurvey.models.db.Serv;
import com.transmilenio.transmisurvey.models.db.ServicioRutas;
import com.transmilenio.transmisurvey.models.json.Config;
import com.transmilenio.transmisurvey.models.json.CuadroEncuesta;
import com.transmilenio.transmisurvey.models.json.EncuestaTM;
import com.transmilenio.transmisurvey.models.json.EstacionTs;
import com.transmilenio.transmisurvey.models.json.RegistroEncuesta;
import com.transmilenio.transmisurvey.models.json.Servicio;
import com.transmilenio.transmisurvey.models.json.TipoEncuesta;
import com.transmilenio.transmisurvey.models.util.ExtrasID;
import com.transmilenio.transmisurvey.models.util.Mensajes;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity  {

    private ListView listView;
    private OptionAdapter adapter;
    private List<Opcion> opcionesList;

    private TextView userNameTextView,encuePendientes,encuesEnviadas;

    private Realm realm;
    private SharedPreferences prefs;
    private String tipoUsuario;

    FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        validarExtras();
        bindUI();
        cargarDatosUsuario();
    }

    private void cargarDatosUsuario() {
        String nombreUsuario = prefs.getString(ExtrasID.EXTRA_USER,ExtrasID.TIPO_USUARIO_INVITADO);
        userNameTextView.setText(nombreUsuario);
        String encPendientes = "0";
        String encEnviadas = "0";
        encuePendientes.setText(encPendientes);
        encuesEnviadas.setText(encEnviadas);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.lg_menu_salir:
                removeSharedPreferences();
                logOut();
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    public static AlertSincronizar newInstance() {
        AlertSincronizar f = new AlertSincronizar();
        return f;
    }



    private void logOut(){
        Intent intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void removeSharedPreferences(){
        prefs.edit().clear().apply();
    }

    private void bindUI(){
        listView = (ListView) findViewById(R.id.ini_opciones_listView);
        userNameTextView = (TextView) findViewById(R.id.bn_user_textView);
        encuesEnviadas = (TextView) findViewById(R.id.ini_encuestasC_textView);
        encuePendientes = (TextView) findViewById(R.id.ini_encuestasP_textView);
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
                    Intent intent = new Intent(MainActivity.this,SeleccionModoActivity.class);
                    startActivity(intent);
                }else if( value.getName().equals(Mensajes.OPCION_ENVIAR)){
                    Intent intent = new Intent(MainActivity.this,ListaSurveyEnvioActivity.class);
                    startActivity(intent);
                }else if( value.getName().equals(Mensajes.OPCION_CONFIG)){
                    Intent intent = new Intent(MainActivity.this,SeleccionarSincroActivity.class);
                    startActivity(intent);
                }else if(value.getName().equals(Mensajes.OPCION_USUARIO)){
                    Intent intent = new Intent(MainActivity.this,CreacionUsuariosActivity.class);
                    startActivity(intent);
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
        tipoUsuario = prefs.getString(ExtrasID.EXTRA_TIPO_USUARIO,ExtrasID.TIPO_USUARIO_INVITADO);
    }

    private void eliminarEncuesta(final int idEncuesta) {
        //Eliminar Encuesta y Registros
        EncuestaTM encuestaTM = realm.where(EncuestaTM.class).equalTo("id", idEncuesta).findFirst();
        if(encuestaTM!=null){
            int tipo = encuestaTM.getTipo();
            if( tipo == TipoEncuesta.ENC_AD_ABORDO){
                CuadroEncuesta ad_abordo = encuestaTM.getAd_abordo();
                realm.beginTransaction();
                ad_abordo.deleteFromRealm();
                realm.commitTransaction();
                realm.beginTransaction();
                encuestaTM.deleteFromRealm();
                realm.commitTransaction();
            }

        }
    }













    //Cargar opciones
    private void cargarOpciones() {
        int[] covers = new int[]{
                R.drawable.ic_new_icon,
                R.drawable.ic_send_icon,
                R.drawable.ic_settings_icon,
                R.drawable.ic_user_icon};

        Opcion a = new Opcion(Mensajes.OPCION_NUEVA, covers[0]);
        opcionesList.add(a);

        a = new Opcion(Mensajes.OPCION_ENVIAR, covers[1]);
        opcionesList.add(a);

        a = new Opcion(Mensajes.OPCION_CONFIG, covers[2]);
        opcionesList.add(a);

        if(tipoUsuario.equals(ExtrasID.TIPO_USUARIO_ADMIN)){
            a = new Opcion(Mensajes.OPCION_USUARIO, covers[3]);
            opcionesList.add(a);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
