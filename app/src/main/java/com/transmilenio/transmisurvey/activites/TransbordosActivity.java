package com.transmilenio.transmisurvey.activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.db.EstacionServicio;
import com.transmilenio.transmisurvey.models.db.OrigenDestinoBase;
import com.transmilenio.transmisurvey.models.db.RegistroOD;
import com.transmilenio.transmisurvey.models.db.Serv;
import com.transmilenio.transmisurvey.models.db.ServicioRutas;
import com.transmilenio.transmisurvey.models.db.TransbordoOD;
import com.transmilenio.transmisurvey.models.util.ExtrasID;
import com.transmilenio.transmisurvey.models.util.Mensajes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class TransbordosActivity extends AppCompatActivity {

    private int idEncuesta, idCuadro;
    private String estacion,modo;
    private FloatingActionButton buttonGuardar,buttonCancelar;
    private CheckBox transbordo1Check,transbordo2Check,transbordo3Check;
    private SearchableSpinner estacionesOrigen,estacionesDestino,estacionesTr1,estacionesTr2;
    private SearchableSpinner serviciosOrigen,serviciosTr1,serviciosTr2;
    private Realm realm;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transbordos);
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        realm = Realm.getDefaultInstance();
        validarExtras();
        bindUI();
    }

    private void validarExtras(){
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idEncuesta = (int) extras.get(ExtrasID.EXTRA_ID_ENCUESTA);
            idCuadro = (int) extras.get(ExtrasID.EXTRA_ID_CUADRO);
            estacion = (String) extras.get(ExtrasID.EXTRA_ID_ESTACION);
            modo = (String) extras.get(ExtrasID.EXTRA_MODO);

        }
    }

    private void bindUI() {
        agregarBotones();
        agregarCheckBox();
        agregarItemsListas();
    }

    private void agregarCheckBox() {
        transbordo1Check = (CheckBox) findViewById(R.id.trs_tr1_checkBox);
        transbordo2Check = (CheckBox) findViewById(R.id.trs_tr2_checkBox);
        transbordo3Check = (CheckBox) findViewById(R.id.trs_tr3_checkBox);

        transbordo1Check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                estacionesTr1.setEnabled(isChecked);
                serviciosTr1.setEnabled(isChecked);
                transbordo2Check.setEnabled(isChecked);
            }
        });

        transbordo2Check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                estacionesTr2.setEnabled(isChecked);
                serviciosTr2.setEnabled(isChecked);
                transbordo3Check.setEnabled(isChecked);
            }
        });
        transbordo2Check.setEnabled(false);

    }

    private void agregarBotones() {
        buttonGuardar = (FloatingActionButton) findViewById(R.id.trs_nuevoRegistro_button);
        buttonCancelar = (FloatingActionButton) findViewById(R.id.trs_cancelarRegistro_button);
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarDatosRegistro();
                Intent intent = new Intent(TransbordosActivity.this,ListaRegistrosODActivity.class);
                intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,  idEncuesta);
                intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,  idCuadro);
                intent.putExtra(ExtrasID.EXTRA_ID_ESTACION,estacion);
                intent.putExtra(ExtrasID.EXTRA_MODO,modo);
                startActivity(intent);
                finish();
            }
        });

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransbordosActivity.this,ListaRegistrosODActivity.class);
                intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,  idEncuesta);
                intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,  idCuadro);
                intent.putExtra(ExtrasID.EXTRA_ID_ESTACION,estacion);
                intent.putExtra(ExtrasID.EXTRA_MODO,modo);
                startActivity(intent);
                finish();
            }
        });
    }

    private void guardarDatosRegistro() {

        OrigenDestinoBase origenDestinoBase =  realm.where(OrigenDestinoBase.class).equalTo("id",idCuadro).findFirst();
        if(origenDestinoBase!=null){
            realm.beginTransaction();
            RealmList<TransbordoOD> transbordos = new RealmList<>();
            if(transbordo1Check.isChecked()){
                TransbordoOD transbordoOD1 = new TransbordoOD();
                transbordoOD1.setEstacion(estacionesTr1.getSelectedItem().toString());
                transbordoOD1.setServicio(serviciosTr1.getSelectedItem().toString());
                realm.copyToRealmOrUpdate(transbordoOD1);
                transbordos.add(transbordoOD1);
                if(transbordo1Check.isChecked()){
                    TransbordoOD transbordoOD2 = new TransbordoOD();
                    transbordoOD2.setEstacion(estacionesTr2.getSelectedItem().toString());
                    transbordoOD2.setServicio(serviciosTr2.getSelectedItem().toString());
                    realm.copyToRealmOrUpdate(transbordoOD2);
                    transbordos.add(transbordoOD2);
                }
            }
            realm.commitTransaction();

            realm.beginTransaction();
            RegistroOD registro = new RegistroOD();
            registro.setEstacionOrigen(estacionesOrigen.getSelectedItem().toString());
            registro.setEstacionDestino(estacionesDestino.getSelectedItem().toString());
            registro.setServicioOrigen(serviciosOrigen.getSelectedItem().toString());
            registro.setHora(obtenerHoraActual());
            registro.setTransbordos(transbordos);
            realm.copyToRealmOrUpdate(registro);
            realm.commitTransaction();

            realm.beginTransaction();
            origenDestinoBase.getRegistros().add(registro);
            realm.copyToRealmOrUpdate(origenDestinoBase);
            realm.commitTransaction();
        }
    }

    private String obtenerHoraActual() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE)+ ":" + now.get(Calendar.SECOND);
    }

    private void agregarItemsListas() {
            listasOrigen();
            listasDestino();
            listasTransbordo1();
            listasTransbordo2();
    }

    private void listasTransbordo2() {
        estacionesTr2 = (SearchableSpinner) findViewById(R.id.trs_es_tr2_sepinner);
        List<String> listestaciones = getEstaciones(modo);
        ArrayAdapter<String> dataAdapterestacionesOrigen = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listestaciones);
        dataAdapterestacionesOrigen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estacionesTr2.setAdapter(dataAdapterestacionesOrigen);
        estacionesTr2.setTitle(Mensajes.MSG_SELECCIONE);
        estacionesTr2.setPositiveButton(Mensajes.MSG_OK);
        estacionesTr2.setEnabled(false);

        addServiciosTransbordo2();

        estacionesTr2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addServiciosTransbordo2();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void addServiciosTransbordo2() {
        serviciosTr2 = (SearchableSpinner) findViewById(R.id.trs_ser_tr2_sepinner);
        List<String> listservicios = getServicios(estacionesTr2);
        ArrayAdapter<String> dataAdapterserviciosOrigen = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listservicios);
        dataAdapterserviciosOrigen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviciosTr2.setAdapter(dataAdapterserviciosOrigen);
        serviciosTr2.setTitle(Mensajes.MSG_SELECCIONE);
        serviciosTr2.setPositiveButton(Mensajes.MSG_OK);
        serviciosTr2.setEnabled(transbordo2Check.isChecked());
    }

    private void listasTransbordo1() {
        estacionesTr1 = (SearchableSpinner) findViewById(R.id.trs_es_tr1_sepinner);
        List<String> listestaciones = getEstaciones(modo);
        ArrayAdapter<String> dataAdapterestacionesOrigen = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listestaciones);
        dataAdapterestacionesOrigen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estacionesTr1.setAdapter(dataAdapterestacionesOrigen);
        estacionesTr1.setTitle(Mensajes.MSG_SELECCIONE);
        estacionesTr1.setPositiveButton(Mensajes.MSG_OK);
        estacionesTr1.setEnabled(false);

        addServiciosTransbordo1();
        estacionesTr1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addServiciosTransbordo1();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void addServiciosTransbordo1() {
        serviciosTr1 = (SearchableSpinner) findViewById(R.id.trs_ser_tr1_sepinner);
        List<String> listservicios = getServicios(estacionesTr1);
        ArrayAdapter<String> dataAdapterserviciosOrigen = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listservicios);
        dataAdapterserviciosOrigen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviciosTr1.setAdapter(dataAdapterserviciosOrigen);
        serviciosTr1.setTitle(Mensajes.MSG_SELECCIONE);
        serviciosTr1.setPositiveButton(Mensajes.MSG_OK);
        serviciosTr1.setEnabled(transbordo1Check.isChecked());
    }

    private void listasDestino() {
        estacionesDestino = (SearchableSpinner) findViewById(R.id.trs_es_des_sepinner);
        List<String> listestaciones = getEstaciones(modo);
        ArrayAdapter<String> dataAdapterestacionesOrigen = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listestaciones);
        dataAdapterestacionesOrigen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estacionesDestino.setAdapter(dataAdapterestacionesOrigen);
        estacionesDestino.setTitle(Mensajes.MSG_SELECCIONE);
        estacionesDestino.setPositiveButton(Mensajes.MSG_OK);
    }

    private void listasOrigen() {
        estacionesOrigen = (SearchableSpinner) findViewById(R.id.trs_es_ori_sepinner);
        List<String> listestaciones = getEstaciones(modo);
        ArrayAdapter<String> dataAdapterestacionesOrigen = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listestaciones);
        dataAdapterestacionesOrigen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estacionesOrigen.setAdapter(dataAdapterestacionesOrigen);
        estacionesOrigen.setTitle(Mensajes.MSG_SELECCIONE);
        estacionesOrigen.setPositiveButton(Mensajes.MSG_OK);
       addServiciosOrigen();
        estacionesOrigen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addServiciosOrigen();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void addServiciosOrigen() {
        serviciosOrigen = (SearchableSpinner) findViewById(R.id.trs_ser_ori_sepinner);
        List<String> listservicios = getServicios(estacionesOrigen);
        ArrayAdapter<String> dataAdapterserviciosOrigen = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listservicios);
        dataAdapterserviciosOrigen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviciosOrigen.setAdapter(dataAdapterserviciosOrigen);
        serviciosOrigen.setTitle(Mensajes.MSG_SELECCIONE);
        serviciosOrigen.setPositiveButton(Mensajes.MSG_OK);
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

    @NonNull
    private List<String> getServicios(SearchableSpinner estaciones) {
        List<String> list = new ArrayList<String>();
        EstacionServicio estacion = realm.where(EstacionServicio.class).equalTo("nombre", estaciones.getSelectedItem().toString()).findFirst();
        for (Serv servicioRutas: estacion.getServicios()){
            list.add(servicioRutas.getNombre());
        }
        return list;
    }


}
