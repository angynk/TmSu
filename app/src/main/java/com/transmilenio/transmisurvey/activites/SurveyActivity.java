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


public class SurveyActivity extends AppCompatActivity {

   private SearchableSpinner servicios;
   private TextView textFecha,textDiaSemana;
   private EditText editTextNumBuses, editTextRecorrido, editTextNumPuerta;
   private Button buttonContinuar;
   private Realm realm;
   private String nombreEncuesta;
   private int idEncuesta;
   private  Cuadro encuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adt_survey);
        realm = Realm.getDefaultInstance();
        bindUI();
        bindEventos();
        validarExtras();
    }

    private void validarExtras(){
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
            public void onClick(View v) {
                // Validar Valores
                if( !camposCompletos() ){
                        Toast.makeText(SurveyActivity.this, Mensajes.MSG_COMPLETE_CAMPOS,Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = null;
                    Cuadro cuadro = new Cuadro();
                    idEncuesta = crearObjetoInfoBase(cuadro);
                    intent = new Intent(SurveyActivity.this,ListaRegistrosActivity.class);
                    intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,  idEncuesta);
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
                || textFecha.toString().trim().equals("") ||
                editTextNumPuerta.getText().toString().trim().equals("")){
            return false;
        }
        return true;
    }

    private int crearObjetoInfoBase(final Cuadro cuadro){

        realm.beginTransaction();
        cuadro.setServicio(servicios.getSelectedItem().toString());
        cuadro.setDiaSemana(textDiaSemana.getText().toString());
        cuadro.setFecha(textFecha.getText().toString());
        cuadro.setNumBus(editTextNumBuses.getText().toString());
        cuadro.setNumPuerta(Integer.parseInt(editTextNumPuerta.getText().toString()));
        cuadro.setRecorrido(Integer.parseInt(editTextRecorrido.getText().toString()));
        cuadro.setRegistros(new RealmList<Registro>());
        cuadro.setNombreEncuesta(nombreEncuesta);
        realm.copyToRealmOrUpdate(cuadro);
        realm.commitTransaction();
        return cuadro.getId();
    }

    private void bindUI() {
        textFecha = (TextView) findViewById(R.id.adt_fecha_text);
        textDiaSemana= (TextView) findViewById(R.id.adt_dia_textView);
        editTextNumBuses = (EditText) findViewById(R.id.adt_numBus_editText);
        editTextRecorrido = (EditText) findViewById(R.id.adt_recorrido_editText);
        editTextNumPuerta = (EditText) findViewById(R.id.adt_numPuerta_editText);
        buttonContinuar = (Button) findViewById(R.id.adt_continuar_button);
        agregarItemsListas();
    }

    private void agregarFecha() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date ahora = new Date();
        textFecha.setText(formato.format(ahora));
        textDiaSemana.setText(ProcessorUtil.obtenerDiaDeLaSemana(ahora));
    }

    public void agregarItemsListas() {

        servicios = (SearchableSpinner) findViewById(R.id.adt_servicio_sepinner);
        List<String> listservicios = getServicios();
        ArrayAdapter<String> dataAdapterservicios = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listservicios);
        dataAdapterservicios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
