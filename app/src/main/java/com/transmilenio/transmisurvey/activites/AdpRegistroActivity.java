package com.transmilenio.transmisurvey.activites;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.fragments.AlertObservacion;
import com.transmilenio.transmisurvey.models.db.AdPuntoEncuesta;
import com.transmilenio.transmisurvey.models.db.RegistroAdPunto;
import com.transmilenio.transmisurvey.models.json.CuadroEncuesta;
import com.transmilenio.transmisurvey.models.json.RegistroEncuesta;
import com.transmilenio.transmisurvey.models.util.ExtrasID;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

public class AdpRegistroActivity extends AppCompatActivity {

    private ImageButton buttonLlegada, buttonSalida;
    private TextView textLlegada,textSalida;
    private EditText seBajan,seSuben,numBus;
    private FloatingActionButton buttonNuevo,buttonCerrar;

    private Realm realm;

    private int idCuadroEncuesta,idEncuesta;
    private AdPuntoEncuesta encuesta;

    public AdpRegistroActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adp_registro);
        realm = Realm.getDefaultInstance();
        validarExtras();
        bindUI();
        setActionBarBotton();
        Toast.makeText(AdpRegistroActivity.this,"Nueva Entrada, para iniciar Registre la hora de llegada",Toast.LENGTH_LONG).show();
    }

    private void validarExtras() {
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idEncuesta = (int) extras.get(ExtrasID.EXTRA_ID_ENCUESTA);
            idCuadroEncuesta = (int) extras.get(ExtrasID.EXTRA_ID_CUADRO);
            encuesta = realm.where(AdPuntoEncuesta.class).equalTo("id",idCuadroEncuesta).findFirst();
        }
    }

    private void bindUI() {
        buttonLlegada = (ImageButton) findViewById(R.id.adp_llegada_button);
        buttonSalida = (ImageButton) findViewById(R.id.adp_salida_button);
        textLlegada = (TextView) findViewById(R.id.adp_llegada_textView);
        textSalida = (TextView) findViewById(R.id.adp_salida_textView);
        seBajan = (EditText) findViewById(R.id.adp_bajan_editText);
        seBajan.setEnabled(false);
        seSuben = (EditText) findViewById(R.id.adp_suben_editText);
        numBus = (EditText) findViewById(R.id.adp_num_bus_editText);
        numBus.setEnabled(false);
        seSuben.setEnabled(false);
        buttonNuevo = (FloatingActionButton) findViewById(R.id.adp_nuevoRegistro_button);
        buttonCerrar = (FloatingActionButton) findViewById(R.id.adp_cancelarRegistro_button);
        buttonSalida.setEnabled(false);
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

    private void inluirDatos() {
        Intent intent = new Intent(this, ListaRegistrosADPActivity.class);
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
        buttonLlegada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
                Date ahora = new Date();
                textLlegada.setText(formato.format(ahora));
                seSuben.setEnabled(true);
                seBajan.setEnabled(true);
                buttonSalida.setEnabled(true);
                numBus.setEnabled(true);
            }
        });

        buttonSalida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
                Date ahora = new Date();
                textSalida.setText(formato.format(ahora));
            }
        });
        buttonNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agregarRegistro()){
                    Intent intent = new Intent(AdpRegistroActivity.this,AdpRegistroActivity.class);
                    intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,idEncuesta);
                    intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,idCuadroEncuesta);
                    startActivity(intent);
                }

            }
        });

        buttonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdpRegistroActivity.this,ListaRegistrosADPActivity.class);
                intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,idEncuesta);
                intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,idCuadroEncuesta);
                startActivity(intent);
            }
        });
    }

    public static AlertObservacion newInstance(int id, String servicio) {
        AlertObservacion f = new AlertObservacion();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt(ExtrasID.EXTRA_ID_ENCUESTA, id);
        f.setArguments(args);
        return f;
    }

    private boolean agregarRegistro(){
        if(informacionCompletada()){
            final RegistroAdPunto registro = crearObjetoRegistro();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealm(registro);
                    encuesta.getRegistros().add(registro);
                }
            });

        }else{
            Toast.makeText(AdpRegistroActivity.this,"Complete todos los campos",Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }

    private RegistroAdPunto crearObjetoRegistro() {
        RegistroAdPunto registro = new RegistroAdPunto();
        registro.setHoraSalida(textSalida.getText().toString());
        registro.setHoraLlegada(textLlegada.getText().toString());
        registro.setPasBajan(Integer.parseInt(seBajan.getText().toString()));
        registro.setPasSuben(Integer.parseInt(seSuben.getText().toString()));
        registro.setNumBus(numBus.getText().toString());
        return registro;
    }

    private boolean informacionCompletada() {
        if( textLlegada.getText().toString().trim().equals("H.Llegada") ||
                textSalida.getText().toString().trim().equals("H.Salida")  ||
                seBajan.getText().toString().trim().equals("") ||
                seSuben.getText().toString().trim().equals("") ){
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
