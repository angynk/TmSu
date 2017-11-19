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
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.db.AdPuntoEncuesta;
import com.transmilenio.transmisurvey.models.db.Estacion;
import com.transmilenio.transmisurvey.models.db.RegistroAdPunto;
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

public class BaseADPuntoFijoActivity extends AppCompatActivity {

    private SearchableSpinner servicios,estaciones;
    private TextView textFecha,textDiaSemana;
    private Button buttonContinuar;
    private Realm realm;
    private String nombreEncuesta,modo;
    private int idEncuesta, idCuadro;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_adpunto_fijo);
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        realm = Realm.getDefaultInstance();
        validarExtras();
        bindUI();
        bindEventos();
    }

    private void bindUI() {
        textFecha = (TextView) findViewById(R.id.adp_fecha_text);
        textDiaSemana= (TextView) findViewById(R.id.adp_dia_textView);
        buttonContinuar = (Button) findViewById(R.id.adp_continuar_button);
        agregarItemsListas();
    }

    private void agregarItemsListas() {

        servicios = (SearchableSpinner) findViewById(R.id.adp_servicio_sepinner);
        List<String> listservicios = getServicios(modo);
        ArrayAdapter<String> dataAdapterservicios = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listservicios);
        dataAdapterservicios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servicios.setAdapter(dataAdapterservicios);
        servicios.setTitle(Mensajes.MSG_SELECCIONE);
        servicios.setPositiveButton(Mensajes.MSG_OK);

        estaciones = (SearchableSpinner) findViewById(R.id.adp_estacion_sepinner);
        List<String> listEstaciones = getEstaciones(modo);
        ArrayAdapter<String> dataAdapterEstaciones = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listEstaciones);
        dataAdapterEstaciones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estaciones.setAdapter(dataAdapterEstaciones);
        estaciones.setTitle(Mensajes.MSG_SELECCIONE);
        estaciones.setPositiveButton(Mensajes.MSG_OK);


    }

    private List<String> getServicios(String modo) {
        List<String> list = new ArrayList<String>();
        RealmResults<ServicioRutas> servicios = realm.where(ServicioRutas.class).equalTo("tipo", modo).findAll();
        for (ServicioRutas servicioRutas: servicios){
            list.add(servicioRutas.getNombre());
        }
        return list;
    }

    private List<String> getEstaciones(String modo) {
        List<String> list = new ArrayList<String>();
        RealmResults<Estacion> estaciones = realm.where(Estacion.class).equalTo("tipo", modo).findAll();
        for (Estacion servicioRutas: estaciones){
            list.add(servicioRutas.getNombre());
        }
        return list;
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
            public void onClick(View v) {
                // Validar Valores
                if( !camposCompletos() ){
                        Toast.makeText(BaseADPuntoFijoActivity.this, Mensajes.MSG_COMPLETE_CAMPOS,Toast.LENGTH_LONG).show();


                }else{
                        Intent intent = null;
                        EncuestaTM encuestaTM = new EncuestaTM();
                        idEncuesta = crearObjetoInfoBase(encuestaTM);
                        intent = new Intent(BaseADPuntoFijoActivity.this,ListaRegistrosADPActivity.class);
                        intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,  idEncuesta);
                        intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,  idCuadro);
                        startActivity(intent);
                        finish();
                }
            }
        });
    }

    private int crearObjetoInfoBase(final EncuestaTM encuestaTM){

        // Crear Encuesta general
        realm.beginTransaction();
        encuestaTM.setFecha_encuesta(textFecha.getText().toString());
        encuestaTM.setNombre_encuesta(nombreEncuesta);
        encuestaTM.setAforador(prefs.getString(ExtrasID.EXTRA_USER,ExtrasID.TIPO_USUARIO_INVITADO));
        encuestaTM.setTipo(TipoEncuesta.ENC_AD_PUNTO);
        encuestaTM.setIdentificador("Fecha: "+textFecha.getText().toString() +" - "+estaciones.getSelectedItem().toString());
        realm.copyToRealmOrUpdate(encuestaTM);
        realm.commitTransaction();

        // Incluir informacion especifica
        realm.beginTransaction();
        AdPuntoEncuesta cuadroEncuesta = new AdPuntoEncuesta();
        cuadroEncuesta.setServicio(servicios.getSelectedItem().toString());
        cuadroEncuesta.setEstacion(estaciones.getSelectedItem().toString());
        cuadroEncuesta.setDiaSemana(textDiaSemana.getText().toString());
        cuadroEncuesta.setRegistros(new RealmList<RegistroAdPunto>());
        realm.copyToRealmOrUpdate(cuadroEncuesta);
        encuestaTM.setAd_punto(cuadroEncuesta);
        realm.copyToRealmOrUpdate(encuestaTM);
        realm.commitTransaction();
        idCuadro = cuadroEncuesta.getId();
        return encuestaTM.getId();
    }

    private boolean camposCompletos(){
        if(servicios.getSelectedItem() != null ||
                estaciones.getSelectedItem() != null ){
            return true;
        }
        return false;
    }

    private void agregarFecha() {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Date ahora = new Date();
        textFecha.setText(formato.format(ahora));
        textDiaSemana.setText(ProcessorUtil.obtenerDiaDeLaSemana(ahora));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
