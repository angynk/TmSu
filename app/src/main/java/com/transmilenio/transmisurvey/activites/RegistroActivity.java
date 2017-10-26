package com.transmilenio.transmisurvey.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.fragments.AlertGuardarDatos;
import com.transmilenio.transmisurvey.fragments.AlertObservacion;
import com.transmilenio.transmisurvey.models.db.Cuadro;
import com.transmilenio.transmisurvey.models.db.Estacion;
import com.transmilenio.transmisurvey.models.db.Registro;
import com.transmilenio.transmisurvey.models.db.ServicioRutas;
import com.transmilenio.transmisurvey.models.util.ExtrasID;
import com.transmilenio.transmisurvey.models.util.Mensajes;

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
    private TextView textLlegada,textSalida;
    private EditText seBajan,seSuben,seQuedan;
    private FloatingActionButton buttonNuevo;

    private Realm realm;

    private int idCuadroEncuesta;
    private Cuadro encuesta;
    private String servicio;

    FragmentManager fm = getSupportFragmentManager();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adt_registro);
        servicio = "";
        realm = Realm.getDefaultInstance();
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idCuadroEncuesta = (int) extras.get(ExtrasID.EXTRA_ID_ENCUESTA);
            encuesta = realm.where(Cuadro.class).equalTo("id",idCuadroEncuesta).findFirst();
            servicio = (String) extras.get(ExtrasID.EXTRA_ID_SERVICIO);
        }
        bindUI();
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
        agregarListaEstacion();
        agregarEventosBotones();
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
                    AlertObservacion dFragment = newInstance(idCuadroEncuesta,servicio);
                    dFragment.show(fm, Mensajes.MSG_OBSERVACIONES);
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
           final Registro registro = crearObjetoRegistro();
           realm.executeTransaction(new Realm.Transaction() {
               @Override
               public void execute(Realm realm) {
                   realm.copyToRealm(registro);
                   encuesta.getRegistros().add(registro);
               }
           });

       }else{
           Toast.makeText(RegistroActivity.this,"Complete todos los campos",Toast.LENGTH_LONG).show();
           return false;
       }

        return true;

    }

    private Registro crearObjetoRegistro() {
        Registro registro = new Registro();
        registro.setHoraSalida(textSalida.getText().toString());
        registro.setHoraLlegada(textLlegada.getText().toString());
        registro.setBajan(Integer.parseInt(seBajan.getText().toString()));
        registro.setSuban(Integer.parseInt(seSuben.getText().toString()));
        registro.setQuedan(Integer.parseInt(seQuedan.getText().toString()));
        registro.setEstacion(estacion.getSelectedItem().toString());
        return registro;
    }

    private boolean informacionCompletada() {
        if( textLlegada.getText().toString().trim().equals("Hora Llegada") ||
                textSalida.getText().toString().trim().equals("Hora Salida")  ||
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
    }


    public List<String> getDiasEstaciones() {
        ServicioRutas servicioRutas = realm.where(ServicioRutas.class).equalTo("nombre", servicio).findFirst();
        RealmList<Estacion> estaciones = servicioRutas.getEstaciones();
        List<String> lista =  new ArrayList<>();
        for(Estacion estacion:estaciones){
            lista.add(estacion.getNombre());
        }
        return lista;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }


}
