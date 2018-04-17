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
import com.transmilenio.transmisurvey.models.db.Estacion;
import com.transmilenio.transmisurvey.models.db.RegistroTiempoRecorrido;
import com.transmilenio.transmisurvey.models.db.ServicioRutas;
import com.transmilenio.transmisurvey.models.db.TRecorridoEncuesta;
import com.transmilenio.transmisurvey.models.json.CuadroEncuesta;
import com.transmilenio.transmisurvey.models.json.RegistroEncuesta;
import com.transmilenio.transmisurvey.models.util.ExtrasID;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

public class TRecorridoRegistroActivity extends AppCompatActivity {

    private SearchableSpinner estacion;
    private List<String> diasSemana;
    private ToggleButton tieneObservaciones,esPrimeraEnZonaDestino;
    private ImageButton buttonLlegada, buttonSalida;
    private TextView textLlegada,textSalida;
    private EditText observacion;
    private FloatingActionButton buttonNuevo,buttonCerrar;
    private boolean estacionBandera;

    private Realm realm;

    private int idCuadroEncuesta,idEncuesta;
    private TRecorridoEncuesta encuesta;
    private String servicio;
    private String modo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trecorrido_registro);
        servicio = "";
        realm = Realm.getDefaultInstance();
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idEncuesta = (int) extras.get(ExtrasID.EXTRA_ID_ENCUESTA);
            idCuadroEncuesta = (int) extras.get(ExtrasID.EXTRA_ID_CUADRO);
            encuesta = realm.where(TRecorridoEncuesta.class).equalTo("id",idCuadroEncuesta).findFirst();
            servicio = (String) extras.get(ExtrasID.EXTRA_ID_SERVICIO);
            modo = (String) extras.get(ExtrasID.EXTRA_MODO);
            estacionBandera = (boolean) extras.get(ExtrasID.EXTRA_ESTACION_BANDERA);
        }
        bindUI();
        setActionBarBotton();
        Toast.makeText(TRecorridoRegistroActivity.this,"Nueva Entrada, para iniciar Registre la hora de llegada",Toast.LENGTH_LONG).show();
    }

    private void bindUI() {
        buttonLlegada = (ImageButton) findViewById(R.id.tdr_llegada_button);
        buttonSalida = (ImageButton) findViewById(R.id.tdr_salida_button);
        textLlegada = (TextView) findViewById(R.id.tdr_llegada_textView);
        textSalida = (TextView) findViewById(R.id.tdr_salida_texView);
        buttonNuevo = (FloatingActionButton) findViewById(R.id.tdr_nuevoRegistro_button);
        buttonCerrar = (FloatingActionButton) findViewById(R.id.tdr_cancelarRegistro_button);
        buttonSalida.setEnabled(false);
        observacion = (EditText) findViewById(R.id.tdr_observaciones_editText);
        tieneObservaciones = (ToggleButton) findViewById(R.id.tdr_observaciones_toggleButton);
        esPrimeraEnZonaDestino = (ToggleButton) findViewById(R.id.tdr_estacion_zona_toggleButton);
        if(estacionBandera) esPrimeraEnZonaDestino.setEnabled(false);
        tieneObservaciones.setEnabled(false);
        observacion.setEnabled(false);
        agregarListaEstacion();
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
        Intent intent = new Intent(this, ListaTiemposRecorridoActivity.class);
        intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,idEncuesta);
        intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,idCuadroEncuesta);
        intent.putExtra(ExtrasID.EXTRA_ID_SERVICIO,servicio);
        intent.putExtra(ExtrasID.EXTRA_MODO,modo);
        intent.putExtra(ExtrasID.EXTRA_ESTACION_BANDERA,esPrimeraEnZonaDestino.isChecked());
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
                buttonSalida.setEnabled(true);
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
                    Intent intent = new Intent(TRecorridoRegistroActivity.this,TRecorridoRegistroActivity.class);
                    intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,idEncuesta);
                    intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,idCuadroEncuesta);
                    intent.putExtra(ExtrasID.EXTRA_ID_SERVICIO,servicio);
                    intent.putExtra(ExtrasID.EXTRA_MODO,modo);
                    intent.putExtra(ExtrasID.EXTRA_ESTACION_BANDERA,esPrimeraEnZonaDestino.isChecked());
                    startActivity(intent);
                }

            }
        });


        buttonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TRecorridoRegistroActivity.this,ListaTiemposRecorridoActivity.class);
                intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,idEncuesta);
                intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,idCuadroEncuesta);
                intent.putExtra(ExtrasID.EXTRA_ID_SERVICIO,servicio);
                intent.putExtra(ExtrasID.EXTRA_MODO,modo);
                intent.putExtra(ExtrasID.EXTRA_ESTACION_BANDERA,esPrimeraEnZonaDestino.isChecked());
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
        args.putString(ExtrasID.EXTRA_ID_SERVICIO, servicio);
        f.setArguments(args);
        return f;
    }


    private boolean agregarRegistro(){
        if(informacionCompletada()){
            if(estacionNoHaSidoCompletada()){
                final RegistroTiempoRecorrido registro = crearObjetoRegistro();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealm(registro);
                        encuesta.getRegistros().add(registro);
                    }
                });
            }else{
                Toast.makeText(TRecorridoRegistroActivity.this,"Ya hay datos para la estación seleccionada",Toast.LENGTH_LONG).show();
                return false;
            }
        }else{
            Toast.makeText(TRecorridoRegistroActivity.this,"Complete todos los campos",Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }

    private boolean estacionNoHaSidoCompletada() {
        for(RegistroTiempoRecorrido reg:encuesta.getRegistros()){
            if(reg.getEstacion().equals(estacion.getSelectedItem().toString())){
                return false;
            }
        }
        return true;
    }

    private RegistroTiempoRecorrido crearObjetoRegistro() {
        RegistroTiempoRecorrido registro = new RegistroTiempoRecorrido();
        registro.setHora_salida(textSalida.getText().toString());
        registro.setHora_llegada(textLlegada.getText().toString());
        registro.setEstacion(obtenerEstacion());
        registro.setObservacion(observacion.getText().toString());
        registro.setPrimera_zon_destino(esPrimeraEnZonaDestino.isChecked());
        return registro;
    }

    private String obtenerEstacion() {
        String estacionValor = estacion.getSelectedItem().toString();
        if(modo.equals(ExtrasID.TIPO_SERVICIO_ALIMENTADOR) || modo.equals(ExtrasID.TIPO_SERVICIO_ZONAL)){
            return estacionValor.split("-")[1];
        }

        return estacionValor;
    }

    private boolean informacionCompletada() {
        if( textLlegada.getText().toString().trim().equals("H.Llegada") ||
                textSalida.getText().toString().trim().equals("H.Salida") ){
            return false;
        }
        return true;
    }

    private void agregarListaEstacion() {
        estacion = (SearchableSpinner) findViewById(R.id.tdr_estacion_spinner);
        List<String> list = getDiasEstaciones();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estacion.setAdapter(dataAdapter);
        estacion.setTitle("Seleccione Uno");
        estacion.setPositiveButton("OK");
    }


    public List<String> getDiasEstaciones() {
        ServicioRutas servicioRutas = realm.where(ServicioRutas.class).equalTo("nombre", servicio).findFirst();
        RealmList<Estacion> estaciones = servicioRutas.getEstaciones();
        List<String> lista =  new ArrayList<>();
        if(modo.equals(ExtrasID.TIPO_SERVICIO_ALIMENTADOR) || modo.equals(ExtrasID.TIPO_SERVICIO_ZONAL) ){
            for(int x=0;x<estaciones.size();x++){
                lista.add(x+"-"+estaciones.get(x).getNombre());
            }
        }else{
            for(Estacion estacion:estaciones){
                lista.add(estacion.getNombre());
            }
        }

        lista.add("Otro");
        return lista;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
