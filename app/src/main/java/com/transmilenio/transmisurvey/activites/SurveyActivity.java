package com.transmilenio.transmisurvey.activites;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.Cuadro;
import com.transmilenio.transmisurvey.models.Registro;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;


public class SurveyActivity extends AppCompatActivity {

   private SearchableSpinner diaSemana,servicios;
   private TextView textFecha;
   private EditText editTextNumBuses, editTextRecorrido;
   private Button buttonContinuar;
   private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        realm = Realm.getDefaultInstance();

//        realm.beginTransaction();
//        realm.deleteAll();
//        realm.commitTransaction();

        bindUI();
        bindEventos();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
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
                    int  idEncuesta = crearObjetoInfoBase();
                    Intent intent = new Intent(SurveyActivity.this,ListaRegistrosActivity.class);
                    intent.putExtra("idEncuesta",  idEncuesta);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }





    private int crearObjetoInfoBase(){
        Cuadro cuadro = new Cuadro();
        cuadro.setServicio(servicios.getSelectedItem().toString());
        cuadro.setDiaSemana(diaSemana.getSelectedItem().toString());
        cuadro.setFecha(textFecha.toString());
        cuadro.setNumBus(editTextNumBuses.getText().toString());
        cuadro.setRecorrido(Integer.parseInt(editTextRecorrido.getText().toString()));
        cuadro.setRegistros(new RealmList<Registro>());

        realm.beginTransaction();
        realm.copyToRealm(cuadro);
        realm.commitTransaction();

        return cuadro.getId();

//        return 0;
    }

    private void bindUI() {
        textFecha = (TextView) findViewById(R.id.text_fecha);
        editTextNumBuses = (EditText) findViewById(R.id.editText_numBus);
        editTextRecorrido = (EditText) findViewById(R.id.editText_recorrido);
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
}
