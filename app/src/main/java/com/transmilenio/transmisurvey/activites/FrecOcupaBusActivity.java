package com.transmilenio.transmisurvey.activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.db.Estacion;
import com.transmilenio.transmisurvey.models.db.FOcupacionBusBase;
import com.transmilenio.transmisurvey.models.db.FOcupacionEncuesta;
import com.transmilenio.transmisurvey.models.json.EncuestaTM;
import com.transmilenio.transmisurvey.models.json.TipoEncuesta;
import com.transmilenio.transmisurvey.models.util.ExtrasID;
import com.transmilenio.transmisurvey.models.util.Mensajes;
import com.transmilenio.transmisurvey.util.ProcessorUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class FrecOcupaBusActivity extends AppCompatActivity {

    private SearchableSpinner estaciones,sentidos;
    private TextView textFecha, textDiaSemana;
    private Button buttonContinuar;
    private Realm realm;
    private String nombreEncuesta,modo;
    private SharedPreferences prefs;
    private int idCuadro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frec_ocupa_bus);
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
            modo = (String) extras.get(ExtrasID.EXTRA_MODO);
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
                Intent intent = new Intent(FrecOcupaBusActivity.this,ListaRegistrosFOBusActivity.class);
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
        encuestaTM.setDia_semana(textDiaSemana.getText().toString());
        encuestaTM.setNombre_encuesta(nombreEncuesta);
        encuestaTM.setAforador(prefs.getString(ExtrasID.EXTRA_USER,ExtrasID.TIPO_USUARIO_INVITADO));
        encuestaTM.setTipo(TipoEncuesta.ENC_FR_OCUPACION);
        encuestaTM.setIdentificador("Fecha: "+textFecha.getText().toString() +" - "+estaciones.getSelectedItem().toString());
        realm.copyToRealmOrUpdate(encuestaTM);
        realm.commitTransaction();

        // Incluir informacion especifica
        realm.beginTransaction();
        FOcupacionBusBase fOcupacionEncuesta = new FOcupacionBusBase();
        fOcupacionEncuesta.setEstacion(estaciones.getSelectedItem().toString());
        fOcupacionEncuesta.setSentido(sentidos.getSelectedItem().toString());
        realm.copyToRealmOrUpdate(fOcupacionEncuesta);
        encuestaTM.setFo_bus(fOcupacionEncuesta);
        realm.copyToRealmOrUpdate(encuestaTM);
//        realm.copyToRealmOrUpdate(encuestaTM);
        realm.commitTransaction();
        idCuadro = fOcupacionEncuesta.getId();
        return encuestaTM.getId();
    }

    private void bindUI() {
        textFecha = (TextView) findViewById(R.id.frb_fecha_text);
        textDiaSemana = (TextView) findViewById(R.id.frb_diasSemana_textView);
        buttonContinuar = (Button) findViewById(R.id.frb_continuar_button);
        agregarItemsEstaciones();
        agregarItemsSentido();
    }

    private void agregarItemsSentido() {
        sentidos = (SearchableSpinner) findViewById(R.id.frb_sentido_sepinner);
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


    private void agregarItemsEstaciones() {
        estaciones = (SearchableSpinner) findViewById(R.id.frb_estacion_sepinner);
        List<String> listestaciones = getEstaciones(modo);
        ArrayAdapter<String> dataAdapterestaciones = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listestaciones);
        dataAdapterestaciones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estaciones.setAdapter(dataAdapterestaciones);
        estaciones.setTitle(Mensajes.MSG_SELECCIONE);
        estaciones.setPositiveButton(Mensajes.MSG_OK);
    }



    private void agregarFecha() {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Date ahora = new Date();
        textFecha.setText(formato.format(ahora));
        textDiaSemana.setText(ProcessorUtil.obtenerDiaDeLaSemana(ahora));
    }

    @NonNull
    private List<String> getEstaciones(String tipo) {
        List<String> list = new ArrayList<String>();
        RealmResults<Estacion> servicios = realm.where(Estacion.class).equalTo("tipo", tipo).findAll();
        for (Estacion estacion: servicios){
            list.add(estacion.getNombre());
        }
        return list;
    }


}
