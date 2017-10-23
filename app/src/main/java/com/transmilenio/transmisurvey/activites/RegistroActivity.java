package com.transmilenio.transmisurvey.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.db.Cuadro;
import com.transmilenio.transmisurvey.models.db.Estacion;
import com.transmilenio.transmisurvey.models.db.Registro;
import com.transmilenio.transmisurvey.models.db.ServicioRutas;
import com.transmilenio.transmisurvey.models.json.Servicio;
import com.transmilenio.transmisurvey.models.util.ExtrasID;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
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
        buttonLlegada = (ImageButton) findViewById(R.id.button_llegada);
        buttonSalida = (ImageButton) findViewById(R.id.button_salida);
        textLlegada = (TextView) findViewById(R.id.textView_llegada);
        textSalida = (TextView) findViewById(R.id.textView_salida);
        seBajan = (EditText) findViewById(R.id.editText_bajan);
        seQuedan = (EditText) findViewById(R.id.editText_quedan);
        seSuben = (EditText) findViewById(R.id.editText_suben);
        buttonNuevo = (FloatingActionButton) findViewById(R.id.button_nuevoRegistro);
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
                agregarRegistro();
                Intent intent = new Intent(RegistroActivity.this, ListaRegistrosActivity.class);
                intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,idCuadroEncuesta);
                intent.putExtra(ExtrasID.EXTRA_TIPO,ExtrasID.VALOR_NUEVO);
                startActivity(intent);
            }
        });
    }




    private void agregarRegistro(){
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
       }



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
        if( textLlegada.getText().toString().trim().equals("") ||
                textSalida.getText().toString().trim().equals("")  ||
                seBajan.getText().toString().trim().equals("") ||
                seSuben.getText().toString().trim().equals("") ||
                seQuedan.getText().toString().trim().equals("")){
            return false;
        }
        return true;
    }

    private void agregarListaEstacion() {
        estacion = (SearchableSpinner) findViewById(R.id.spinner_estacion);
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
