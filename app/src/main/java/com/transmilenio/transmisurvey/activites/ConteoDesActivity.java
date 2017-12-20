package com.transmilenio.transmisurvey.activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.db.ConteoDesEncuesta;
import com.transmilenio.transmisurvey.models.db.Estacion;
import com.transmilenio.transmisurvey.models.db.EstacionServicio;
import com.transmilenio.transmisurvey.models.db.FOcupacionEncuesta;
import com.transmilenio.transmisurvey.models.db.Serv;
import com.transmilenio.transmisurvey.models.db.ServicioRutas;
import com.transmilenio.transmisurvey.models.json.EncuestaTM;
import com.transmilenio.transmisurvey.models.json.Servicio;
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

public class ConteoDesActivity extends AppCompatActivity {

    private SearchableSpinner estaciones,servicios;
    private TextView textFecha, textDiaSemana;
    private Button buttonContinuar;
    private Realm realm;
    private String nombreEncuesta,modo;
    private SharedPreferences prefs;
    private int idCuadro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conteo_des);
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
                if (!servicioValido()){
                    Toast.makeText(ConteoDesActivity.this, Mensajes.MSG_SERVICIO_NO_ESTACION,Toast.LENGTH_LONG).show();
                }else{
                int idEncuesta = crearObjetoInfoBase();
                Intent intent = new Intent(ConteoDesActivity.this,ListaRegistrosConteoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,  idEncuesta);
                intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,  idCuadro);
                startActivity(intent);
                }


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
        encuestaTM.setTipo(TipoEncuesta.ENC_CONT_DESPACHOS);
        encuestaTM.setIdentificador("Fecha: "+textFecha.getText().toString() +" - "+estaciones.getSelectedItem().toString());
        realm.copyToRealmOrUpdate(encuestaTM);
        realm.commitTransaction();

        // Incluir informacion especifica
        realm.beginTransaction();
        ConteoDesEncuesta conteoDesEncuesta = new ConteoDesEncuesta();
        conteoDesEncuesta.setEstacion(estaciones.getSelectedItem().toString());
        conteoDesEncuesta.setServicio(servicios.getSelectedItem().toString());
        realm.copyToRealmOrUpdate(conteoDesEncuesta);
        encuestaTM.setCo_despachos(conteoDesEncuesta);
        realm.copyToRealmOrUpdate(encuestaTM);
//        realm.copyToRealmOrUpdate(encuestaTM);
        realm.commitTransaction();
        idCuadro = conteoDesEncuesta.getId();




        return encuestaTM.getId();
    }

    private void bindUI() {
        textFecha = (TextView) findViewById(R.id.cod_fecha_text);
        textDiaSemana = (TextView) findViewById(R.id.cod_diasSemana_textView);
        buttonContinuar = (Button) findViewById(R.id.cod_continuar_button);
        agregarItemsEstaciones();
        agregarItemsSentido();
    }

    private void agregarItemsSentido() {
        servicios = (SearchableSpinner) findViewById(R.id.cod_servicio_sepinner);
        List<String> listsentidos = getServicios(modo);
        ArrayAdapter<String> dataAdaptersentido = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listsentidos);
        dataAdaptersentido.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servicios.setAdapter(dataAdaptersentido);
        servicios.setTitle(Mensajes.MSG_SELECCIONE);
        servicios.setPositiveButton(Mensajes.MSG_OK);
    }



    private void agregarItemsEstaciones() {
        estaciones = (SearchableSpinner) findViewById(R.id.cod_estacion_sepinner);
        List<String> listestaciones = getEstaciones(modo);
        ArrayAdapter<String> dataAdapterestaciones = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listestaciones);
        dataAdapterestaciones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estaciones.setAdapter(dataAdapterestaciones);
        estaciones.setTitle(Mensajes.MSG_SELECCIONE);
        estaciones.setPositiveButton(Mensajes.MSG_OK);
        estaciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                agregarItemsSentido();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        RealmResults<EstacionServicio> servicios = realm.where(EstacionServicio.class).equalTo("tipo", tipo).findAll();
        for (EstacionServicio estacion: servicios){
            list.add(estacion.getNombre());
        }
        return list;
    }

    @NonNull
    private List<String> getServicios(String tipo) {
        List<String> list = new ArrayList<String>();
        EstacionServicio estacion = realm.where(EstacionServicio.class).equalTo("nombre", estaciones.getSelectedItem().toString()).findFirst();
        for (Serv servicioRutas: estacion.getServicios()){
            list.add(servicioRutas.getNombre());
        }
        return list;
    }

    private boolean servicioValido() {
        String servicio = servicios.getSelectedItem().toString();
        String estacion = estaciones.getSelectedItem().toString();
        ServicioRutas servicioRutas = realm.where(ServicioRutas.class).equalTo("nombre", servicio).findFirst();
        if(servicioRutas!=null){
            RealmList<Estacion> estaciones = servicioRutas.getEstaciones();
            for(Estacion esProv:estaciones){
                if(esProv.getNombre().equals(estacion)){
                    return true;
                }
            }
        }
        return false;
    }


}
