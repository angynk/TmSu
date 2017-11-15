package com.transmilenio.transmisurvey.activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.db.FOcupacionEncuesta;
import com.transmilenio.transmisurvey.models.json.CuadroEncuesta;
import com.transmilenio.transmisurvey.models.json.EncuestaTM;
import com.transmilenio.transmisurvey.models.json.RegistroEncuesta;
import com.transmilenio.transmisurvey.models.json.TipoEncuesta;
import com.transmilenio.transmisurvey.models.util.ExtrasID;
import com.transmilenio.transmisurvey.models.util.Mensajes;
import com.transmilenio.transmisurvey.util.ProcessorUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

public class BaseFrecOcupacionActivity extends AppCompatActivity {

    private SearchableSpinner estaciones,sentidos;
    private TextView textFecha;
    private Button buttonContinuar;
    private Realm realm;
    private String nombreEncuesta;
    private SharedPreferences prefs;
    private int idCuadro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_frec_ocupacion);
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        realm = Realm.getDefaultInstance();
        validarExtras();
        bindUI();
        bindEventos();
    }

    private void validarExtras() {
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            nombreEncuesta = (String) extras.get(ExtrasID.EXTRA_NOMBRE);
        }
    }

    private void bindEventos() {
        agregarFecha();
        agregarEventoBotonContinuar();
    }

    private void agregarEventoBotonContinuar() {
        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idEncuesta = crearObjetoInfoBase();
                Intent intent = new Intent(BaseFrecOcupacionActivity.this,ListaRegistrosFrecOcupacionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,  idEncuesta);
                intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,  idCuadro);
                startActivity(intent);

            }
        });
    }

    private int crearObjetoInfoBase(){

        EncuestaTM encuestaTM = new EncuestaTM();

        // Crear Encuesta general
        realm.beginTransaction();
        encuestaTM.setFecha_encuesta(textFecha.getText().toString());
        encuestaTM.setNombre_encuesta(nombreEncuesta);
        encuestaTM.setAforador(prefs.getString(ExtrasID.EXTRA_USER,ExtrasID.TIPO_USUARIO_INVITADO));
        encuestaTM.setTipo(TipoEncuesta.ENC_FR_OCUPACION);
        encuestaTM.setIdentificador("Fecha: "+textFecha.getText().toString() +" - "+estaciones.getSelectedItem().toString());
        realm.copyToRealmOrUpdate(encuestaTM);
        realm.commitTransaction();

        // Incluir informacion especifica
        realm.beginTransaction();
        FOcupacionEncuesta fOcupacionEncuesta = new FOcupacionEncuesta();
        fOcupacionEncuesta.setEstacion(estaciones.getSelectedItem().toString());
        fOcupacionEncuesta.setSentido(sentidos.getSelectedItem().toString());
        realm.copyToRealmOrUpdate(fOcupacionEncuesta);
        encuestaTM.setFr_ocupacion(fOcupacionEncuesta);
        realm.copyToRealmOrUpdate(encuestaTM);
//        realm.copyToRealmOrUpdate(encuestaTM);
        realm.commitTransaction();
        idCuadro = fOcupacionEncuesta.getId();
        return encuestaTM.getId();
    }

    private void bindUI() {
        textFecha = (TextView) findViewById(R.id.fro_fecha_text);
        buttonContinuar = (Button) findViewById(R.id.fro_continuar_button);
        agregarItemsEstaciones();
        agregarItemsSentido();
    }

    private void agregarItemsSentido() {
        sentidos = (SearchableSpinner) findViewById(R.id.fro_sentido_sepinner);
        List<String> listsentidos = getSentidos();
        ArrayAdapter<String> dataAdaptersentido = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listsentidos);
        dataAdaptersentido.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sentidos.setAdapter(dataAdaptersentido);
        sentidos.setTitle(Mensajes.MSG_SELECCIONE);
        sentidos.setPositiveButton(Mensajes.MSG_OK);
    }

    private List<String> getSentidos() {
        List<String> sentidos = new ArrayList<>();
        sentidos.add("Norte-Sur");
        sentidos.add("Sur-Norte");
        sentidos.add("Oriente-Occidente");
        sentidos.add("Occidente-Oriente");
        return sentidos;
    }


    private List<String> getZonas() {
        List<String> zonas = new ArrayList<>();
        zonas.add("Caracas");
        zonas.add("AutoNorte");
        zonas.add("Suba");
        zonas.add("Calle 80");
        zonas.add("NQS Central");
        zonas.add("Américas");
        zonas.add("NQS Sur");
        zonas.add("Caracas Sur");
        zonas.add("Eje Ambiental");
        zonas.add("Calle 26");
        zonas.add("Carrera 10");
        zonas.add("Carrera 7");
        return zonas;
    }

    private void agregarItemsEstaciones() {
        estaciones = (SearchableSpinner) findViewById(R.id.fro_estacion_sepinner);
        List<String> listestaciones = getEstaciones();
        ArrayAdapter<String> dataAdapterestaciones = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listestaciones);
        dataAdapterestaciones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estaciones.setAdapter(dataAdapterestaciones);
        estaciones.setTitle(Mensajes.MSG_SELECCIONE);
        estaciones.setPositiveButton(Mensajes.MSG_OK);
    }

    private List<String> getEstaciones() {
        List<String> estaciones = new ArrayList<>();
        estaciones.add("Modelia");
        estaciones.add("Normandia");
        estaciones.add("Av. Rojas");
        return estaciones;
    }

    private void agregarFecha() {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Date ahora = new Date();
        textFecha.setText(formato.format(ahora));
    }



}
