package com.transmilenio.transmisurvey.activites;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.fragments.AlertGuardarDatos;
import com.transmilenio.transmisurvey.fragments.AlertObservacion;
import com.transmilenio.transmisurvey.models.db.Cuadro;
import com.transmilenio.transmisurvey.models.db.Estacion;
import com.transmilenio.transmisurvey.models.db.Registro;
import com.transmilenio.transmisurvey.models.db.ServicioRutas;
import com.transmilenio.transmisurvey.models.json.CuadroEncuesta;
import com.transmilenio.transmisurvey.models.json.RegistroEncuesta;
import com.transmilenio.transmisurvey.models.util.ExtrasID;
import com.transmilenio.transmisurvey.models.util.Mensajes;
import com.transmilenio.transmisurvey.util.ModosLlegada;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;


public class RegistroActivity extends AppCompatActivity  {

    private SearchableSpinner estacion;
    private List<String> diasSemana;
    private ImageButton buttonLlegada, buttonSalida;
    private ToggleButton tieneObservaciones;
    private TextView textLlegada,textSalida;
    private EditText seBajan,seSuben,seQuedan,observaciones;
    private FloatingActionButton buttonNuevo,buttonCerrar;

    private Realm realm;

    private int idCuadroEncuesta,idEncuesta;
    private CuadroEncuesta encuesta;
    private String servicio;
    private String modo;




    public RegistroActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adt_registro);
        servicio = "";
        realm = Realm.getDefaultInstance();
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idEncuesta = (int) extras.get(ExtrasID.EXTRA_ID_ENCUESTA);
            idCuadroEncuesta = (int) extras.get(ExtrasID.EXTRA_ID_CUADRO);
            encuesta = realm.where(CuadroEncuesta.class).equalTo("id",idCuadroEncuesta).findFirst();
            servicio = (String) extras.get(ExtrasID.EXTRA_ID_SERVICIO);
            modo = (String) extras.get(ExtrasID.EXTRA_MODO);
        }
        bindUI();
        setActionBarBotton();
        Toast.makeText(RegistroActivity.this,"Nueva Entrada, para iniciar Registre la hora de llegada",Toast.LENGTH_LONG).show();
    }

    private void bindUI() {
        buttonLlegada = (ImageButton) findViewById(R.id.adt_llegada_button);
        buttonSalida = (ImageButton) findViewById(R.id.adt_salida_button);
        textLlegada = (TextView) findViewById(R.id.adt_llegada_textView);
        textSalida = (TextView) findViewById(R.id.textView_salida);
        seBajan = (EditText) findViewById(R.id.adt_bajan_editText);
        seBajan.setEnabled(false);
        seQuedan = (EditText) findViewById(R.id.adt_quedan_editText);
        seQuedan.setEnabled(false);
        seSuben = (EditText) findViewById(R.id.adt_suben_editText);
        seSuben.setEnabled(false);
        buttonNuevo = (FloatingActionButton) findViewById(R.id.adt_nuevoRegistro_button);
        buttonCerrar = (FloatingActionButton) findViewById(R.id.adt_cancelarRegistro_button);
        buttonSalida.setEnabled(false);
        observaciones = (EditText) findViewById(R.id.adt_observaciones_editText);
        tieneObservaciones = (ToggleButton) findViewById(R.id.adt_observaciones_toggleButton);
        tieneObservaciones.setEnabled(false);
        observaciones.setEnabled(false);
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
        Intent intent = new Intent(this, ListaRegistrosActivity.class);
        intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,idEncuesta);
        intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,idCuadroEncuesta);
        intent.putExtra(ExtrasID.EXTRA_ID_SERVICIO,servicio);
        intent.putExtra(ExtrasID.EXTRA_MODO,modo);
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
                seQuedan.setEnabled(true);
                estacion.setEnabled(true);
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
                    Intent intent = new Intent(RegistroActivity.this,RegistroActivity.class);
                    intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,idEncuesta);
                    intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,idCuadroEncuesta);
                    intent.putExtra(ExtrasID.EXTRA_ID_SERVICIO,servicio);
                    intent.putExtra(ExtrasID.EXTRA_MODO,modo);
                    startActivity(intent);
                }

            }
        });

        buttonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistroActivity.this,ListaRegistrosActivity.class);
                intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,idEncuesta);
                intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,idCuadroEncuesta);
                intent.putExtra(ExtrasID.EXTRA_ID_SERVICIO,servicio);
                intent.putExtra(ExtrasID.EXTRA_MODO,modo);
                startActivity(intent);
            }
        });

        tieneObservaciones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                   observaciones.setEnabled(true);
                } else {
                    observaciones.setEnabled(false);
                }
            }
        });
    }

    public static AlertObservacion newInstance(int id,String servicio) {
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
               final RegistroEncuesta registro = crearObjetoRegistro();
               realm.executeTransaction(new Realm.Transaction() {
                   @Override
                   public void execute(Realm realm) {
                       realm.copyToRealm(registro);
                       encuesta.getRegistros().add(registro);
                   }
               });
           }else{
               Toast.makeText(RegistroActivity.this,"Ya hay datos para la estación seleccionada",Toast.LENGTH_LONG).show();
               return false;
           }
       }else{
           Toast.makeText(RegistroActivity.this,"Complete todos los campos",Toast.LENGTH_LONG).show();
           return false;
       }

        return true;

    }

    private boolean estacionNoHaSidoCompletada() {
        for(RegistroEncuesta reg:encuesta.getRegistros()){
            if(!estacion.getSelectedItem().toString().equals("Otro")){
                if(reg.getEstacion().equals(estacion.getSelectedItem().toString())){
                    return false;
                }
            }

        }
        return true;
    }

    private RegistroEncuesta crearObjetoRegistro() {
        RegistroEncuesta registro = new RegistroEncuesta();
        registro.setHora_salida(textSalida.getText().toString());
        registro.setHora_llegada(textLlegada.getText().toString());
        registro.setBajan(Integer.parseInt(seBajan.getText().toString()));
        registro.setSuban(Integer.parseInt(seSuben.getText().toString()));
        registro.setQuedan(Integer.parseInt(seQuedan.getText().toString()));
        registro.setEstacion(obtenerEstacion());
        registro.setObservacion(observaciones.getText().toString());
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
                textSalida.getText().toString().trim().equals("H.Salida")  ||
                seBajan.getText().toString().trim().equals("") ||
                seSuben.getText().toString().trim().equals("") ||
                seQuedan.getText().toString().trim().equals("")){
            return false;
        }
        return true;
    }

    private void agregarListaEstacion() {
        estacion = (SearchableSpinner) findViewById(R.id.adt_estacion_spinner);
        List<String> list = getDiasEstaciones();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estacion.setAdapter(dataAdapter);
        estacion.setTitle("Seleccione Uno");
        estacion.setPositiveButton("OK");
        estacion.setBackgroundColor(Color.rgb(230,230,230));
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
