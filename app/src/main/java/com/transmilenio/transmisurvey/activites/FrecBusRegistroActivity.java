package com.transmilenio.transmisurvey.activites;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.db.FOcupacionBusBase;
import com.transmilenio.transmisurvey.models.db.FOcupacionEncuesta;
import com.transmilenio.transmisurvey.models.db.RegistroFrecOcupaBus;
import com.transmilenio.transmisurvey.models.db.RegistroFrecOcupacion;
import com.transmilenio.transmisurvey.models.util.ExtrasID;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

public class FrecBusRegistroActivity extends AppCompatActivity {

    private EditText ocupacionEditText, codigoEditText;
    private TextView horaPasoTextView;
    private ImageButton horaButton;
    private FloatingActionButton buttonNuevo,buttonCerrar;
    private int  idEncuesta, idCuadro ;
    private FOcupacionBusBase encuesta;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frec_bus_registro);
        realm = Realm.getDefaultInstance();
        bindUI();
        validarExtras();
        setActionBarBotton();
    }

    private void validarExtras() {

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idEncuesta = (int) extras.get(ExtrasID.EXTRA_ID_ENCUESTA);
            idCuadro = (int) extras.get(ExtrasID.EXTRA_ID_CUADRO);
            encuesta =  realm.where(FOcupacionBusBase.class).equalTo("id",idCuadro).findFirst();

        }
    }

    private void bindUI() {
//        horaPasoEditText = (EditText) findViewById(R.id.fre_hora_paso_editText);
        horaPasoTextView = (TextView) findViewById(R.id.frb_hora_paso_editText);
        ocupacionEditText = (EditText) findViewById(R.id.frb_ocupacion_editText);
        codigoEditText = (EditText) findViewById(R.id.frb_codigo_editText);
        buttonNuevo = (FloatingActionButton) findViewById(R.id.frb_nuevoRegistro_button);
        buttonCerrar = (FloatingActionButton) findViewById(R.id.frb_cancelarRegistro_button);
        horaButton = (ImageButton) findViewById(R.id.frb_getHora_button);
        codigoEditText.setVisibility(View.INVISIBLE);
        ocupacionEditText.setVisibility(View.INVISIBLE);
        agregarEventosBotones();



    }

    private void agregarEventosBotones() {
        buttonNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agregarRegistro()){
                    Intent intent = new Intent(FrecBusRegistroActivity.this,FrecBusRegistroActivity.class);
                    intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,  idEncuesta);
                    intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,  idCuadro);
                    startActivity(intent);
                }


            }
        });

        buttonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FrecBusRegistroActivity.this,ListaRegistrosFOBusActivity.class);
                intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,  idEncuesta);
                intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,  idCuadro);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        horaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String currentTime = sdf.format(new Date());
                horaPasoTextView.setText(currentTime);
                codigoEditText.setVisibility(View.VISIBLE);
                ocupacionEditText.setVisibility(View.VISIBLE);
                codigoEditText.requestFocus();
                codigoEditText.callOnClick();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(codigoEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        });

    }

    private boolean agregarRegistro(){
        if(informacionCompletada()){
            final RegistroFrecOcupaBus registro = new RegistroFrecOcupaBus();
            registro.setHoraPaso(horaPasoTextView.getText().toString());
            registro.setServicio(codigoEditText.getText().toString());
            registro.setMovBus(transformarValor(ocupacionEditText.getText().toString()));
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealm(registro);
                    encuesta.getRegistros().add(registro);
                }
            });

        }else{
            Toast.makeText(FrecBusRegistroActivity.this,"Complete todos los campos",Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }

    private Integer transformarValor(String s) {
        return Integer.parseInt(s);
    }

    private boolean informacionCompletada() {
        if( ocupacionEditText.getText().toString().trim().equals("") ||
                codigoEditText.getText().toString().trim().equals("")  ||
                horaPasoTextView.getText().toString().trim().equals("") ){
            return false;
        }
        return true;
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
        Intent intent = new Intent(FrecBusRegistroActivity.this,ListaRegistrosFOBusActivity.class);
        intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,  idEncuesta);
        intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,  idCuadro);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void setActionBarBotton() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
