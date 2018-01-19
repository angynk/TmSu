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
    private TextView textLlegada,textSalida,textServicio;
    private ToggleButton tieneObservaciones;
    private EditText seBajan,seSuben, seQuedan,numBus,observacion;
    private FloatingActionButton buttonNuevo,buttonCerrar;
    private String servicio, estacion,vagon;

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
            estacion = (String) extras.get(ExtrasID.EXTRA_ID_ESTACION);
            servicio = (String) extras.get(ExtrasID.EXTRA_ID_SERVICIO);
            vagon = (String) extras.get(ExtrasID.EXTRA_ID_VAGON);
        }
    }

    private void bindUI() {
        buttonLlegada = (ImageButton) findViewById(R.id.adp_llegada_button);
        buttonSalida = (ImageButton) findViewById(R.id.adp_salida_button);
        textLlegada = (TextView) findViewById(R.id.adp_llegada_textView);
        textSalida = (TextView) findViewById(R.id.adp_salida_textView);
        textServicio = (TextView) findViewById(R.id.adp_servicio_textView);
        textServicio.setText(servicio);
        seBajan = (EditText) findViewById(R.id.adp_bajan_editText);
        seQuedan = (EditText) findViewById(R.id.adp_quedan_editText);
        seQuedan.setEnabled(false);
        seBajan.setEnabled(false);
        seSuben = (EditText) findViewById(R.id.adp_suben_editText);
        numBus = (EditText) findViewById(R.id.adp_num_bus_editText);
        seSuben.setEnabled(false);
        buttonNuevo = (FloatingActionButton) findViewById(R.id.adp_nuevoRegistro_button);
        buttonCerrar = (FloatingActionButton) findViewById(R.id.adp_cancelarRegistro_button);
        buttonSalida.setEnabled(false);
        observacion = (EditText) findViewById(R.id.adp_observaciones_editText);
        tieneObservaciones = (ToggleButton) findViewById(R.id.adp_observaciones_toggleButton);
        tieneObservaciones.setEnabled(false);
        observacion.setEnabled(false);
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
        Intent intent = new Intent(AdpRegistroActivity.this,ListaRegistrosADPActivity.class);
        intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,idEncuesta);
        intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,idCuadroEncuesta);
        intent.putExtra(ExtrasID.EXTRA_ID_ESTACION,  estacion);
        intent.putExtra(ExtrasID.EXTRA_ID_VAGON,  vagon);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
                seQuedan.setEnabled(true);
                tieneObservaciones.setEnabled(true);
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
                    Intent intent = new Intent(AdpRegistroActivity.this,AdPuntoServiciosActivity.class);
                    intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,idEncuesta);
                    intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,idCuadroEncuesta);
                    intent.putExtra(ExtrasID.EXTRA_ID_ESTACION,  estacion);
                    intent.putExtra(ExtrasID.EXTRA_ID_VAGON,  vagon);
                    startActivity(intent);
                    finish();
                }

            }
        });

        buttonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdpRegistroActivity.this,AdPuntoServiciosActivity.class);
                intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,idEncuesta);
                intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,idCuadroEncuesta);
                intent.putExtra(ExtrasID.EXTRA_ID_ESTACION,  estacion);
                intent.putExtra(ExtrasID.EXTRA_ID_VAGON,  vagon);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        tieneObservaciones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    observacion.setEnabled(true);
                } else {
                    observacion.setEnabled(false);
                }
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
        registro.setPasQuedan(Integer.parseInt(seQuedan.getText().toString()));
        registro.setObservacion(observacion.getText().toString());
        registro.setNumBus(numBus.getText().toString());
        registro.setServicio(servicio);
        return registro;
    }

    private boolean informacionCompletada() {
        if( textLlegada.getText().toString().trim().equals("H.Llegada") ||
                textSalida.getText().toString().trim().equals("H.Salida")  ||
                seBajan.getText().toString().trim().equals("") ||
                seSuben.getText().toString().trim().equals("") ||
                seQuedan.getText().toString().trim().equals("")||
                numBus.getText().toString().trim().equals("") ){
            return false;
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        inluirDatos();
    }





}
