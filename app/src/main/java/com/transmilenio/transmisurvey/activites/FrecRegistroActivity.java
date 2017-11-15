package com.transmilenio.transmisurvey.activites;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.db.FOcupacionEncuesta;
import com.transmilenio.transmisurvey.models.db.RegistroFrecOcupacion;
import com.transmilenio.transmisurvey.models.json.RegistroEncuesta;
import com.transmilenio.transmisurvey.models.util.ExtrasID;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

public class FrecRegistroActivity extends AppCompatActivity {


    private EditText horaPasoEditText, ocupacionEditText, codigoEditText;
    private FloatingActionButton buttonNuevo,buttonCerrar;
    private int  idEncuesta, idCuadro ;
    private FOcupacionEncuesta encuesta;

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

    }

    private void validarExtras() {

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idEncuesta = (int) extras.get(ExtrasID.EXTRA_ID_ENCUESTA);
            idCuadro = (int) extras.get(ExtrasID.EXTRA_ID_CUADRO);
//            servicio = (String) extras.get(ExtrasID.EXTRA_ID_SERVICIO);
            encuesta =  realm.where(FOcupacionEncuesta.class).equalTo("id",idCuadro).findFirst();

        }
    }

    private void bindUI() {
        horaPasoEditText = (EditText) findViewById(R.id.fre_hora_paso_editText);
        ocupacionEditText = (EditText) findViewById(R.id.fre_ocupacion_editText);
        codigoEditText = (EditText) findViewById(R.id.fre_codigo_editText);
        buttonNuevo = (FloatingActionButton) findViewById(R.id.fre_nuevoRegistro_button);
        buttonCerrar = (FloatingActionButton) findViewById(R.id.fre_cancelarRegistro_button);
        agregarEventosBotones();

      horaPasoEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getAction() != KeyEvent.ACTION_DOWN) {
                    return false;
                } else if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    String currentTime = sdf.format(new Date());
                    horaPasoEditText.setText(currentTime);
                    return false;
                }
                    return true;
            }
        });

    }

    private void agregarEventosBotones() {
        buttonNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agregarRegistro()){
                    Intent intent = new Intent(FrecRegistroActivity.this,FrecRegistroActivity.class);
                    intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,  idEncuesta);
                    intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,  idCuadro);
                    startActivity(intent);
                }


            }
        });

        buttonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FrecRegistroActivity.this,ListaRegistrosFrecOcupacionActivity.class);
                intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,  idEncuesta);
                intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,  idCuadro);
                startActivity(intent);
            }
        });

    }

    private boolean agregarRegistro(){
        if(informacionCompletada()){
            final RegistroFrecOcupacion registro = new RegistroFrecOcupacion();
            registro.setHoraPaso(horaPasoEditText.getText().toString());
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
                codigoEditText.getText().toString().trim().equals("")  ||
                horaPasoEditText.getText().toString().trim().equals("") ){
            return false;
        }
        return true;
    }

}
