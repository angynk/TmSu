package com.transmilenio.transmisurvey.activites;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.fragments.AlertGuardarDatos;
import com.transmilenio.transmisurvey.models.db.FOcupacionEncuesta;
import com.transmilenio.transmisurvey.models.json.EncuestaTM;
import com.transmilenio.transmisurvey.models.json.TipoEncuesta;
import com.transmilenio.transmisurvey.models.util.ExtrasID;
import com.transmilenio.transmisurvey.models.util.Mensajes;

import io.realm.Realm;

public class ContinuarEncuestaActivity extends AppCompatActivity {

    private Button buttonSi,buttonNo;
    private int idEncuesta,idCuadro;
    private String nombreEncuesta, modo;
    private Realm realm;
    FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continuar_encuesta);
        realm = Realm.getDefaultInstance();
        validarExtras();
        bindUI();
    }

    private void bindUI() {
        buttonSi = (Button) findViewById(R.id.coe_si_button);
        buttonNo = (Button) findViewById(R.id.coe_no_button);

        buttonSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuevaEncuesta();
                Intent intent = new Intent(ContinuarEncuestaActivity.this,ListaRegistrosFrecOcupacionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra(ExtrasID.EXTRA_ID_ENCUESTA,  idEncuesta);
                intent.putExtra(ExtrasID.EXTRA_ID_CUADRO,  idCuadro);
                intent.putExtra(ExtrasID.EXTRA_NOMBRE,  nombreEncuesta);
                intent.putExtra(ExtrasID.EXTRA_MODO,  modo);
                startActivity(intent);
            }
        });

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContinuarEncuestaActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void nuevaEncuesta() {
        EncuestaTM encuestaVieja = realm.where(EncuestaTM.class).equalTo("id", idEncuesta).findFirst();
        FOcupacionEncuesta fr_ocupacion = encuestaVieja.getFr_ocupacion();

        EncuestaTM encuestaTM = new EncuestaTM();

        // Crear Encuesta general
        realm.beginTransaction();
        encuestaTM.setFecha_encuesta(encuestaVieja.getFecha_encuesta());
        encuestaTM.setDia_semana(encuestaVieja.getDia_semana());
        encuestaTM.setNombre_encuesta(nombreEncuesta);
        encuestaTM.setAforador(encuestaVieja.getAforador());
        encuestaTM.setTipo(TipoEncuesta.ENC_FR_OCUPACION);
        encuestaTM.setIdentificador("Fecha: "+encuestaTM.getFecha_encuesta() +" - "+fr_ocupacion.getEstacion());
        realm.copyToRealmOrUpdate(encuestaTM);
        realm.commitTransaction();
        idEncuesta = encuestaTM.getId();

        // Incluir informacion especifica
        realm.beginTransaction();
        FOcupacionEncuesta fOcupacionEncuesta = new FOcupacionEncuesta();
        fOcupacionEncuesta.setEstacion(fr_ocupacion.getEstacion());
        fOcupacionEncuesta.setSentido(fr_ocupacion.getSentido());
        realm.copyToRealmOrUpdate(fOcupacionEncuesta);
        encuestaTM.setFr_ocupacion(fOcupacionEncuesta);
        realm.copyToRealmOrUpdate(encuestaTM);
//        realm.copyToRealmOrUpdate(encuestaTM);
        realm.commitTransaction();
        idCuadro = fOcupacionEncuesta.getId();

    }

    private void validarExtras() {
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idEncuesta = (int) extras.get(ExtrasID.EXTRA_ID_ENCUESTA);
            nombreEncuesta = (String) extras.get(ExtrasID.EXTRA_NOMBRE);
            modo = (String) extras.get(ExtrasID.EXTRA_MODO);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AlertGuardarDatos dFragment = newInstance(idEncuesta);
                dFragment.show(fm, Mensajes.MSG_SALIR_ENCUESTA);
                break;
        }
        return true;
    }

    public static AlertGuardarDatos newInstance(int id) {
        AlertGuardarDatos f = new AlertGuardarDatos();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt(ExtrasID.EXTRA_ID_ENCUESTA, id);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onBackPressed() {
        AlertGuardarDatos dFragment = newInstance(idEncuesta);
        dFragment.show(fm, Mensajes.MSG_SALIR_ENCUESTA);
    }
}
