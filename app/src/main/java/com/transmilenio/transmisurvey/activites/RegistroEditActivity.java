package com.transmilenio.transmisurvey.activites;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.db.Cuadro;
import com.transmilenio.transmisurvey.models.db.Registro;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class RegistroEditActivity extends AppCompatActivity  {

    private SearchableSpinner estacion;
    private List<String> diasSemana;
    private TextView textLlegada,textSalida;
    private EditText seBajan,seSuben,seQuedan,llegada,salida;
    private FloatingActionButton buttonNuevo;

    private Realm realm;

    private int idRegistroEncuesta;
    private int idCuadroEncuesta;
    private String tipo;
    private Registro registro;
    private Cuadro encuesta;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_edit);

        realm = Realm.getDefaultInstance();
        bindUI();
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            tipo = (String) extras.get("tipo");
            idCuadroEncuesta = (int) extras.get("idEncuesta");
            encuesta = realm.where(Cuadro.class).equalTo("id",idCuadroEncuesta).findFirst();
            if( tipo.equals("Edicion")){
                idRegistroEncuesta = (int) extras.get("idRegistro");
                registro = realm.where(Registro.class).equalTo("id",idRegistroEncuesta).findFirst();
                agregarDatosRegistro();
            }
        }


    }

    private void agregarDatosRegistro() {
        llegada.setText(registro.getHoraLlegada());
        salida.setText(registro.getHoraSalida());
        seBajan.setText(Integer.toString(registro.getBajan()));
        seSuben.setText(Integer.toString(registro.getSuban()));
        seQuedan.setText(Integer.toString(registro.getQuedan()));
        estacion.setSelection(getIndex(estacion,registro.getEstacion()));
    }

    private void bindUI() {
        llegada = (EditText) findViewById(R.id.editText_llegada_edit);
        salida = (EditText) findViewById(R.id.editText_salida_edit);
        seBajan = (EditText) findViewById(R.id.editText_bajan_edit);
        seQuedan = (EditText) findViewById(R.id.editText_quedan_edit);
        seSuben = (EditText) findViewById(R.id.editText_suben_edit);
        buttonNuevo = (FloatingActionButton) findViewById(R.id.button_Registro_edit);
        agregarListaEstacion();
        agregarEventosBotones();
    }

    private void agregarEventosBotones() {
        buttonNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarRegistro();
                Intent intent = new Intent(RegistroEditActivity.this, ListaRegistrosEditActivity.class);
                intent.putExtra("idEncuesta",idCuadroEncuesta);
                intent.putExtra("tipo","Edicion");
                startActivity(intent);
            }
        });
    }




    private void agregarRegistro(){
        if(informacionCompletada()){
            if(tipo.equals("Nuevo")){
                registro = new Registro();
            }
            realm.beginTransaction();
            registro.setHoraSalida(salida.getText().toString());
            registro.setHoraLlegada(llegada.getText().toString());
            registro.setBajan(Integer.parseInt(seBajan.getText().toString()));
            registro.setSuban(Integer.parseInt(seSuben.getText().toString()));
            registro.setQuedan(Integer.parseInt(seQuedan.getText().toString()));
            registro.setEstacion(estacion.getSelectedItem().toString());
            realm.copyToRealmOrUpdate(registro);
            if(tipo.equals("Nuevo")){
                encuesta.getRegistros().add(registro);
            }
            //encuesta.getRegistros().add(registro);
            realm.commitTransaction();

        }else{
            Toast.makeText(RegistroEditActivity.this,"Complete todos los campos",Toast.LENGTH_LONG).show();
        }



    }



    private boolean informacionCompletada() {
        if( llegada.getText().toString().trim().equals("") ||
                salida.getText().toString().trim().equals("")  ||
                seBajan.getText().toString().trim().equals("") ||
                seSuben.getText().toString().trim().equals("") ||
                seQuedan.getText().toString().trim().equals("")){
            return false;
        }
        return true;
    }

    private void agregarListaEstacion() {
        estacion = (SearchableSpinner) findViewById(R.id.spinner_estacion_edit);
        List<String> list = getDiasEstaciones();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estacion.setAdapter(dataAdapter);
        estacion.setTitle("Seleccione Uno");
        estacion.setPositiveButton("OK");
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


    public List<String> getDiasEstaciones() {
        List<String> lista =  new ArrayList<>();
        lista.add("Comuneros");
        lista.add("Paloquemado");
        lista.add("Santa Isabel");
        lista.add("Ricaurte");
        lista.add("CAD");
        return lista;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }


}
