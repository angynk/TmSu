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
import com.transmilenio.transmisurvey.models.db.ServicioRutas;
import com.transmilenio.transmisurvey.models.db.TRecorridoEncuesta;
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

public class TiemposRecorridoActivity extends AppCompatActivity {

    private SearchableSpinner servicios;
    private TextView textFecha,textDiaSemana;
    private EditText editTextNumBuses, editTextRecorrido;
    private Button buttonContinuar;
    private Realm realm;
    private String nombreEncuesta,modo;
    private int idEncuesta, idCuadro;
    private boolean infoServicios;
    private SharedPreferences prefs;
    private boolean estacionBandera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiempos_recorrido);
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
            estacionBandera = false;
        }
    }

    private void bindEventos() {
        agregarFecha();
        agregarEventoBotonContinuar();
    }

    private void agregarEventoBotonContinuar() {
        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Validar Valores
                if( !camposCompletos() ){
                    if(!infoServicios){
                        Toast.makeText(TiemposRecorridoActivity.this, Mensajes.MSG_SINCRONIZE,Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(TiemposRecorridoActivity.this, Mensajes.MSG_COMPLETE_CAMPOS,Toast.LENGTH_LONG).show();
                    }

                }else{
                    if(!infoServicios){
                        Toast.makeText(TiemposRecorridoActivity.this, Mensajes.MSG_SINCRONIZE,Toast.LENGTH_LONG).show();
                    }else{
                        Intent intent = null;
                        EncuestaTM encuestaTM = new EncuestaTM();
                        idEncuesta = crearObjetoInfoBase(encuestaTM);
                        intent = new Intent(TiemposRecorridoActivity.this,ListaTiemposRecorridoActivity.class);
                        intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,  idEncuesta);
                        intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,  idCuadro);
                        intent.putExtra(ExtrasID.EXTRA_ID_SERVICIO,servicios.getSelectedItem().toString());
                        intent.putExtra(ExtrasID.EXTRA_MODO,modo);
                        intent.putExtra(ExtrasID.EXTRA_ESTACION_BANDERA,estacionBandera);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }



    private boolean camposCompletos(){
        if(editTextNumBuses.getText().toString().trim().equals("") || textFecha.toString().trim().equals("") ){
            return false;
        }
        return true;
    }

    private int crearObjetoInfoBase(final EncuestaTM encuestaTM){

        // Crear Encuesta general
        realm.beginTransaction();
        encuestaTM.setFecha_encuesta(textFecha.getText().toString());
        encuestaTM.setNombre_encuesta(nombreEncuesta);
        encuestaTM.setAforador(prefs.getString(ExtrasID.EXTRA_USER,ExtrasID.TIPO_USUARIO_INVITADO));
        encuestaTM.setTipo(TipoEncuesta.ENC_TI_RECORRIDOS);
        encuestaTM.setIdentificador("Fecha: "+textFecha.getText().toString() +" - "+servicios.getSelectedItem().toString());
        realm.copyToRealmOrUpdate(encuestaTM);
        realm.commitTransaction();

        // Incluir informacion especifica
        realm.beginTransaction();
        TRecorridoEncuesta tRecorridoEncuesta = new TRecorridoEncuesta();
        tRecorridoEncuesta.setServicio(servicios.getSelectedItem().toString());
        tRecorridoEncuesta.setDia_semana(textDiaSemana.getText().toString());
        tRecorridoEncuesta.setNum_bus(editTextNumBuses.getText().toString());
        tRecorridoEncuesta.setRecorrido(Integer.parseInt(editTextRecorrido.getText().toString()));
        realm.copyToRealmOrUpdate(tRecorridoEncuesta);
        encuestaTM.setT_recorridos(tRecorridoEncuesta);
        realm.copyToRealmOrUpdate(encuestaTM);
//        realm.copyToRealmOrUpdate(encuestaTM);
        realm.commitTransaction();
        idCuadro = tRecorridoEncuesta.getId();
        return encuestaTM.getId();
    }



    private void bindUI() {
        textFecha = (TextView) findViewById(R.id.tdr_fecha_text);
        textDiaSemana= (TextView) findViewById(R.id.tdr_dia_textView);
        editTextNumBuses = (EditText) findViewById(R.id.tdr_numBus_editText);
        editTextRecorrido = (EditText) findViewById(R.id.tdr_recorrido_editText);
        buttonContinuar = (Button) findViewById(R.id.tdr_continuar_button);
        agregarItemsListas();
    }

    private void agregarFecha() {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Date ahora = new Date();
        textFecha.setText(formato.format(ahora));
        textDiaSemana.setText(ProcessorUtil.obtenerDiaDeLaSemana(ahora));
    }

    public void agregarItemsListas() {

        servicios = (SearchableSpinner) findViewById(R.id.tdr_servicio_sepinner);
        List<String> listservicios = getServicios(modo);
        ArrayAdapter<String> dataAdapterservicios = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listservicios);
        dataAdapterservicios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servicios.setAdapter(dataAdapterservicios);
        servicios.setTitle(Mensajes.MSG_SELECCIONE);
        servicios.setPositiveButton(Mensajes.MSG_OK);

        if(listservicios.size()>0){
            infoServicios =true;
        }else{
            infoServicios = false;
        }

    }

    @NonNull
    private List<String> getServicios(String tipo) {
        List<String> list = new ArrayList<String>();
        RealmResults<ServicioRutas> servicios = realm.where(ServicioRutas.class).equalTo("tipo", tipo).findAll();
        for (ServicioRutas servicioRutas: servicios){
            list.add(servicioRutas.getNombre());
        }
        return list;
    }


}
