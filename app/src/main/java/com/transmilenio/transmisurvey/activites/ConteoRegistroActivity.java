package com.transmilenio.transmisurvey.activites;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.fragments.AlertObservacion;
import com.transmilenio.transmisurvey.models.db.ConteoDesEncuesta;
import com.transmilenio.transmisurvey.models.db.Estacion;
import com.transmilenio.transmisurvey.models.db.RegistroConteo;
import com.transmilenio.transmisurvey.models.db.ServicioRutas;
import com.transmilenio.transmisurvey.models.json.CuadroEncuesta;
import com.transmilenio.transmisurvey.models.json.RegistroEncuesta;
import com.transmilenio.transmisurvey.models.util.ExtrasID;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

public class ConteoRegistroActivity extends AppCompatActivity {

    private ImageButton buttonDespacho;
    private TextView textDespacho;
    private EditText numBus;
    private FloatingActionButton buttonNuevo,buttonCerrar;
    private Realm realm;

    private int idCuadroEncuesta,idEncuesta;
    private ConteoDesEncuesta encuesta;


    public ConteoRegistroActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conteo_registro);
        realm = Realm.getDefaultInstance();
        validarExtras();
        bindUI();
        setActionBarBotton();
    }

    private void validarExtras() {
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idEncuesta = (int) extras.get(ExtrasID.EXTRA_ID_ENCUESTA);
            idCuadroEncuesta = (int) extras.get(ExtrasID.EXTRA_ID_CUADRO);
            encuesta = realm.where(ConteoDesEncuesta.class).equalTo("id",idCuadroEncuesta).findFirst();
        }
    }

    private void bindUI() {
        buttonDespacho = (ImageButton) findViewById(R.id.cod_despacho_button);
        textDespacho = (TextView) findViewById(R.id.cod_despacho_textView);
        numBus = (EditText) findViewById(R.id.cod_num_bus_editText);
        buttonNuevo = (FloatingActionButton) findViewById(R.id.cod_nuevoRegistro_button);
        buttonCerrar = (FloatingActionButton) findViewById(R.id.cod_cancelarRegistro_button);
        agregarEventosBotones();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                inluirDatos();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        inluirDatos();
    }

    private void inluirDatos() {
        Intent intent = new Intent(this, ListaRegistrosConteoActivity.class);
        intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,idEncuesta);
        intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,idCuadroEncuesta);
        startActivity(intent);
    }

    private void setActionBarBotton() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void agregarEventosBotones() {
        buttonDespacho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
                Date ahora = new Date();
                textDespacho.setText(formato.format(ahora));
            }
        });

        buttonNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agregarRegistro()){
                    Intent intent = new Intent(ConteoRegistroActivity.this,ConteoRegistroActivity.class);
                    intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,idEncuesta);
                    intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,idCuadroEncuesta);
                    startActivity(intent);
                }

            }
        });

        buttonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConteoRegistroActivity.this,ListaRegistrosConteoActivity.class);
                intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,idEncuesta);
                intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,idCuadroEncuesta);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    public static AlertObservacion newInstance(int id, String servicio) {
        AlertObservacion f = new AlertObservacion();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt(ExtrasID.EXTRA_ID_ENCUESTA, id);
        args.putString(ExtrasID.EXTRA_ID_SERVICIO, servicio);
        f.setArguments(args);
        return f;
    }


    private boolean agregarRegistro(){
        if(informacionCompletada()){
            final RegistroConteo registro = crearObjetoRegistro();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealm(registro);
                    encuesta.getRegistros().add(registro);
                }
            });

        }else{
            Toast.makeText(ConteoRegistroActivity.this,"Complete todos los campos",Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }

    private RegistroConteo crearObjetoRegistro() {
        RegistroConteo registro = new RegistroConteo();
        registro.setNumBus(numBus.getText().toString());
        registro.setHoradespacho(textDespacho.getText().toString());
        return registro;
    }

    private boolean informacionCompletada() {
        if( textDespacho.getText().toString().trim().equals("H.Despacho") ||
                numBus.getText().toString().trim().equals("")){
            return false;
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
