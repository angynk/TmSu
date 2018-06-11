package com.transmilenio.transmisurvey.activites;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.db.FOcupacionEncuesta;
import com.transmilenio.transmisurvey.models.db.RegistroFrecOcupacion;
import com.transmilenio.transmisurvey.models.util.ExtrasID;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

public class FrecRegistroActivity extends AppCompatActivity {


    private EditText  ocupacionEditText, codigoEditText;
    private FloatingActionButton buttonCerrar;
    private int  idEncuesta, idCuadro, numRegistros ;
    private FOcupacionEncuesta encuesta;
    private String nombreEncuesta,modo;

    private Realm realm;

    public FrecRegistroActivity() {
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frec_registro);
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
            numRegistros = (int) extras.get(ExtrasID.EXTRA_ID_REGISTROS);
            encuesta =  realm.where(FOcupacionEncuesta.class).equalTo("id",idCuadro).findFirst();
            nombreEncuesta = (String) extras.get(ExtrasID.EXTRA_NOMBRE);
            modo = (String) extras.get(ExtrasID.EXTRA_MODO);

        }
    }

    private void bindUI() {
        ocupacionEditText = (EditText) findViewById(R.id.fre_ocupacion_editText);
        codigoEditText = (EditText) findViewById(R.id.fre_codigo_editText);
        buttonCerrar = (FloatingActionButton) findViewById(R.id.fre_cancelarRegistro_button);
        agregarEventosBotones();

        ocupacionEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(agregarRegistro()){
                        numRegistros++;
                        if(numRegistros>200){
                            Toast.makeText(FrecRegistroActivity.this,"Continue la encuesta en la siguiente pantalla",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(FrecRegistroActivity.this,BaseFrecOcupacionActivity.class);
                            intent.putExtra(ExtrasID.EXTRA_NOMBRE,  nombreEncuesta);
                            intent.putExtra(ExtrasID.EXTRA_MODO,  modo);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(FrecRegistroActivity.this,FrecRegistroActivity.class);
                            intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,  idEncuesta);
                            intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,  idCuadro);
                            intent.putExtra(ExtrasID.EXTRA_ID_REGISTROS,  numRegistros);
                            intent.putExtra(ExtrasID.EXTRA_NOMBRE,  nombreEncuesta);
                            intent.putExtra(ExtrasID.EXTRA_MODO,  modo);
                            startActivity(intent);
                        }

                    }
                }
                return false;
            }
        });

        codigoEditText.setVisibility(View.VISIBLE);
        ocupacionEditText.setVisibility(View.VISIBLE);
        codigoEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(codigoEditText, InputMethodManager.SHOW_IMPLICIT);

    }







    private void agregarEventosBotones() {

        buttonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FrecRegistroActivity.this,ListaRegistrosFrecOcupacionActivity.class);
                intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,  idEncuesta);
                intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,  idCuadro);
                intent.putExtra(ExtrasID.EXTRA_NOMBRE,  nombreEncuesta);
                intent.putExtra(ExtrasID.EXTRA_MODO,  modo);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    private boolean agregarRegistro(){
        if(informacionCompletada()){
            final RegistroFrecOcupacion registro = new RegistroFrecOcupacion();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            registro.setHora_paso(sdf.format(new Date()));
            registro.setServicio(codigoEditText.getText().toString());
            registro.setOcupacion(transformarValor(ocupacionEditText.getText().toString()));
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealm(registro);
                    encuesta.getRegistros().add(registro);
                }
            });

        }else{
            Toast.makeText(FrecRegistroActivity.this,"Complete todos los campos",Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }

    private Integer transformarValor(String s) {
        return Integer.parseInt(s);
    }

    private boolean informacionCompletada() {
        if( ocupacionEditText.getText().toString().trim().equals("") ||
                codigoEditText.getText().toString().trim().equals("") ){
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
        Intent intent = new Intent(FrecRegistroActivity.this,ListaRegistrosFrecOcupacionActivity.class);
        intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,  idEncuesta);
        intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,  idCuadro);
        startActivity(intent);
    }

    private void setActionBarBotton() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

}
