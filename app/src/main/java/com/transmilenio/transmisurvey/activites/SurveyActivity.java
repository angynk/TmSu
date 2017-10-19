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


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;


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

        tipoEncuesta = "Nuevo";
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            tipoEncuesta =(String) extras.get("tipo");
            if (tipoEncuesta.equals("Nuevo")){
                nombreEncuesta = (String) extras.get("nombre");
            }else{
                idEncuesta = (int) extras.get("id");
                escribirInformacionActual();
            }
        }




    }

    private void escribirInformacionActual() {
        encuesta = realm.where(Cuadro.class).equalTo("id",idEncuesta).findFirst();
        editTextNumBuses.setText(encuesta.getNumBus());
        editTextRecorrido.setText(Integer.toString(encuesta.getRecorrido()));
        editTextNumPuerta.setText(Integer.toString(encuesta.getRecorrido()));
        textFecha.setText(encuesta.getFecha());
        servicios.setSelection(getIndex(servicios, encuesta.getServicio()));
        diaSemana.setSelection(getIndex(diaSemana, encuesta.getDiaSemana()));
    }


    private void bindEventos() {
        agregarFecha();
        agregarEventoBotonContinuar();
    }

    private void agregarEventoBotonContinuar() {
        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Validar Valores
                if( editTextNumBuses.getText().toString().trim().equals("") ||
                        editTextRecorrido.getText().toString().trim().equals("")
                        || textFecha.toString().trim().equals("")){
                        Toast.makeText(SurveyActivity.this,"Complete todos los campos",Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = null;
                    if(tipoEncuesta.equals("Nuevo")){
                        Cuadro cuadro = new Cuadro();
                        idEncuesta = crearObjetoInfoBase(cuadro);
                        intent = new Intent(SurveyActivity.this,ListaRegistrosActivity.class);
                    }else{
                        idEncuesta = crearObjetoInfoBase(encuesta);
                        intent = new Intent(SurveyActivity.this,ListaRegistrosEditActivity.class);
                    }


                    intent.putExtra("idEncuesta",  idEncuesta);
                    intent.putExtra("tipo",tipoEncuesta);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }





    private int crearObjetoInfoBase(final Cuadro cuadro){

        realm.beginTransaction();
        cuadro.setServicio(servicios.getSelectedItem().toString());
        cuadro.setDiaSemana(diaSemana.getSelectedItem().toString());
        cuadro.setFecha(textFecha.getText().toString());
        cuadro.setNumBus(editTextNumBuses.getText().toString());
        cuadro.setNumPuerta(Integer.parseInt(editTextNumPuerta.getText().toString()));
        cuadro.setRecorrido(Integer.parseInt(editTextRecorrido.getText().toString()));
        if(tipoEncuesta.endsWith("Nuevo")){
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
        diaSemana.setTitle("Seleccione Uno");
        diaSemana.setPositiveButton("OK");

        servicios = (SearchableSpinner) findViewById(R.id.spinner_servicio);
        List<String> listservicios = getServicios();
        ArrayAdapter<String> dataAdapterservicios = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listservicios);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servicios.setAdapter(dataAdapterservicios);
        servicios.setTitle("Seleccione Uno");
        servicios.setPositiveButton("OK");


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
        list.add("B11");
        list.add("B12");
        list.add("G11");
        list.add("G12");
        list.add("C17");
        list.add("H17");
        list.add("L80");
        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
