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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.db.EstacionServicio;
import com.transmilenio.transmisurvey.models.db.OrigenDestinoBase;
import com.transmilenio.transmisurvey.models.db.ServicioRutas;
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
import io.realm.RealmResults;

public class OrigenDestinoActivity extends AppCompatActivity {

    private SearchableSpinner estaciones;
    private TextView textFecha,textDiaSemana;
    private Button buttonContinuar;
    private Realm realm;
    private String nombreEncuesta,modo;
    private int idEncuesta, idCuadro;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_origen_destino);
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        realm = Realm.getDefaultInstance();
        validarExtras();
        bindUI();
        bindEventos();
    }

    private void validarExtras(){
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
            public void onClick(View v) {
                Intent intent = null;
                EncuestaTM encuestaTM = new EncuestaTM();
                        idEncuesta = crearObjetoInfoBase(encuestaTM);
                        intent = new Intent(OrigenDestinoActivity.this,ListaRegistrosODActivity.class);
                        intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,  idEncuesta);
                        intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,  idCuadro);
                        intent.putExtra(ExtrasID.EXTRA_ID_ESTACION,estaciones.getSelectedItem().toString());
                        startActivity(intent);
                        finish();
            }
        });
    }

    private int crearObjetoInfoBase(EncuestaTM encuestaTM) {
        realm.beginTransaction();
        encuestaTM.setFecha_encuesta(textFecha.getText().toString());
        encuestaTM.setNombre_encuesta(nombreEncuesta);
        encuestaTM.setAforador(prefs.getString(ExtrasID.EXTRA_USER,ExtrasID.TIPO_USUARIO_INVITADO));
        encuestaTM.setTipo(TipoEncuesta.ENC_ORI_DEST);
        encuestaTM.setIdentificador("Fecha: "+textFecha.getText().toString() +" - "+estaciones.getSelectedItem().toString());
        realm.copyToRealmOrUpdate(encuestaTM);
        realm.commitTransaction();

        // Incluir informacion especifica
        realm.beginTransaction();
        OrigenDestinoBase origenDestinoBase = new OrigenDestinoBase();
        origenDestinoBase.setEstacion(estaciones.getSelectedItem().toString());
        origenDestinoBase.setDia_semana(textDiaSemana.getText().toString());
//        cuadroEncuesta.setRegistros(new RealmList<RegistroEncuesta>());
        realm.copyToRealmOrUpdate(origenDestinoBase);
        encuestaTM.setOd_destino(origenDestinoBase);
        realm.copyToRealmOrUpdate(encuestaTM);
//        realm.copyToRealmOrUpdate(encuestaTM);
        realm.commitTransaction();
        idCuadro = origenDestinoBase.getId();
        return encuestaTM.getId();
    }

    private boolean camposCompletos(){
        if(estaciones.getSelectedItem().toString().trim().equals("")
                || textFecha.toString().trim().equals("") ){
            return false;
        }
        return true;
    }

    private void bindUI() {
        textFecha = (TextView) findViewById(R.id.ode_fecha_text);
        textDiaSemana= (TextView) findViewById(R.id.ode_dia_textView);
        buttonContinuar = (Button) findViewById(R.id.ode_continuar_button);
        agregarItemsListas();
    }

    private void agregarFecha() {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Date ahora = new Date();
        textFecha.setText(formato.format(ahora));
        textDiaSemana.setText(ProcessorUtil.obtenerDiaDeLaSemana(ahora));
    }

    public void agregarItemsListas() {

        estaciones = (SearchableSpinner) findViewById(R.id.ode_estacion_sepinner);
        List<String> listestaciones = getEstaciones(modo);
        ArrayAdapter<String> dataAdapterservicios = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listestaciones);
        dataAdapterservicios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estaciones.setAdapter(dataAdapterservicios);
        estaciones.setTitle(Mensajes.MSG_SELECCIONE);
        estaciones.setPositiveButton(Mensajes.MSG_OK);
    }

    @NonNull
    private List<String> getEstaciones(String tipo) {
        List<String> list = new ArrayList<String>();
        RealmResults<EstacionServicio> servicios = realm.where(EstacionServicio.class).equalTo("tipo", tipo).findAll();
        for (EstacionServicio servicioRutas: servicios){
            list.add(servicioRutas.getNombre());
        }
        return list;
    }
}
