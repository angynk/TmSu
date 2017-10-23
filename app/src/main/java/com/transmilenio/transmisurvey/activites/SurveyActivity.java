package com.transmilenio.transmisurvey.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.db.Cuadro;
import com.transmilenio.transmisurvey.models.db.Registro;
import com.transmilenio.transmisurvey.models.db.ServicioRutas;
import com.transmilenio.transmisurvey.models.json.Servicio;
import com.transmilenio.transmisurvey.models.util.ExtrasID;
import com.transmilenio.transmisurvey.models.util.Mensajes;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class SurveyActivity extends AppCompatActivity {

   private SearchableSpinner diaSemana,servicios;
   private TextView textFecha;
   private EditText editTextNumBuses, editTextRecorrido, editTextNumPuerta;
   private Button buttonContinuar;
   private Realm realm;
   private String nombreEncuesta;
   private String tipoEncuesta;
   private int idEncuesta;
   private  Cuadro encuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        realm = Realm.getDefaultInstance();
        bindUI();
        bindEventos();
        validarExtras();
    }

    private void validarExtras(){
        tipoEncuesta = ExtrasID.VALOR_NUEVO;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            tipoEncuesta =(String) extras.get(ExtrasID.EXTRA_TIPO);
            if(tipoEncuesta!= null){
                if (tipoEncuesta.equals(ExtrasID.VALOR_NUEVO)){
                    nombreEncuesta = (String) extras.get(ExtrasID.EXTRA_NOMBRE);
                }else{
                    idEncuesta = (int) extras.get(ExtrasID.EXTRA_ID_ENCUESTA);
                    escribirInformacionActual();
                }
            }
        }
    }

    private void escribirInformacionActual() {
        encuesta = realm.where(Cuadro.class).equalTo("id",idEncuesta).findFirst();
        if(encuesta != null ){
            editTextNumBuses.setText(encuesta.getNumBus());
            editTextRecorrido.setText(Integer.toString(encuesta.getRecorrido()));
            editTextNumPuerta.setText(Integer.toString(encuesta.getRecorrido()));
            textFecha.setText(encuesta.getFecha());
            servicios.setSelection(getIndex(servicios, encuesta.getServicio()));
            diaSemana.setSelection(getIndex(diaSemana, encuesta.getDiaSemana()));
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
                        Toast.makeText(SurveyActivity.this, Mensajes.MSG_COMPLETE_CAMPOS,Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = null;
                    if(tipoEncuesta.equals(ExtrasID.VALOR_NUEVO)){
                        Cuadro cuadro = new Cuadro();
                        idEncuesta = crearObjetoInfoBase(cuadro);
                        intent = new Intent(SurveyActivity.this,ListaRegistrosActivity.class);
                    }else{
                        idEncuesta = crearObjetoInfoBase(encuesta);
                        intent = new Intent(SurveyActivity.this,ListaRegistrosEditActivity.class);
                    }
                    intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,  idEncuesta);
                    intent.putExtra(ExtrasID.EXTRA_TIPO,tipoEncuesta);
                    intent.putExtra(ExtrasID.EXTRA_ID_SERVICIO,servicios.getSelectedItem().toString());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }



    private boolean camposCompletos(){
        if(editTextNumBuses.getText().toString().trim().equals("") ||
                editTextRecorrido.getText().toString().trim().equals("")
                || textFecha.toString().trim().equals("")){
            return false;
        }
        return true;
    }

    private int crearObjetoInfoBase(final Cuadro cuadro){

        realm.beginTransaction();
        cuadro.setServicio(servicios.getSelectedItem().toString());
        cuadro.setDiaSemana(diaSemana.getSelectedItem().toString());
        cuadro.setFecha(textFecha.getText().toString());
        cuadro.setNumBus(editTextNumBuses.getText().toString());
        cuadro.setNumPuerta(Integer.parseInt(editTextNumPuerta.getText().toString()));
        cuadro.setRecorrido(Integer.parseInt(editTextRecorrido.getText().toString()));
        if(tipoEncuesta.equals(ExtrasID.VALOR_NUEVO)){
            cuadro.setRegistros(new RealmList<Registro>());
            cuadro.setNombreEncuesta(nombreEncuesta);
        }
        realm.copyToRealmOrUpdate(cuadro);
        realm.commitTransaction();
        return cuadro.getId();
    }

    private void bindUI() {
        textFecha = (TextView) findViewById(R.id.text_fecha);
        editTextNumBuses = (EditText) findViewById(R.id.editText_numBus);
        editTextRecorrido = (EditText) findViewById(R.id.editText_recorrido);
        editTextNumPuerta = (EditText) findViewById(R.id.editText_num_puerta);
        buttonContinuar = (Button) findViewById(R.id.button_continuar);
        agregarItemsListas();
    }

    private void agregarFecha() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date ahora = new Date();
        textFecha.setText(formato.format(ahora));
    }

    public void agregarItemsListas() {

        diaSemana = (SearchableSpinner) findViewById(R.id.spinner_diaSemana);
        List<String> list = getDiasSemana();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diaSemana.setAdapter(dataAdapter);
        diaSemana.setTitle(Mensajes.MSG_SELECCIONE);
        diaSemana.setPositiveButton(Mensajes.MSG_OK);

        servicios = (SearchableSpinner) findViewById(R.id.spinner_servicio);
        List<String> listservicios = getServicios();
        ArrayAdapter<String> dataAdapterservicios = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listservicios);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servicios.setAdapter(dataAdapterservicios);
        servicios.setTitle(Mensajes.MSG_SELECCIONE);
        servicios.setPositiveButton(Mensajes.MSG_OK);


    }


    // Change selection in Spinner by Value
    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }



    @NonNull
    private List<String> getDiasSemana() {
        List<String> list = new ArrayList<String>();
        list.add("Lunes");
        list.add("Martes");
        list.add("Miercoles");
        list.add("Jueves");
        list.add("Viernes");
        list.add("Sábado");
        list.add("Domingo");
        return list;
    }

    @NonNull
    private List<String> getServicios() {
        List<String> list = new ArrayList<String>();
        RealmResults<ServicioRutas> servicios = realm.where(ServicioRutas.class).findAll();
        for (ServicioRutas servicioRutas: servicios){
            list.add(servicioRutas.getNombre());
        }
        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
