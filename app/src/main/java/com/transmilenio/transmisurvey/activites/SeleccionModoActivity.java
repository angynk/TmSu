package com.transmilenio.transmisurvey.activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.util.ExtrasID;
import com.transmilenio.transmisurvey.util.ModosLlegada;

public class SeleccionModoActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private Button troncalButton,alimentadorButton,zonalButton;
    private String modo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_modo);
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        bindUI();
    }

    private void bindUI() {
        modo =  prefs.getString(ExtrasID.EXTRA_MODO,ExtrasID.TIPO_SERVICIO_TRONCAL);
        troncalButton = (Button) findViewById(R.id.sel_troncal_button);
        alimentadorButton = (Button) findViewById(R.id.sel_alimentador_button);
        zonalButton = (Button) findViewById(R.id.sel_zonal_button);

        troncalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeleccionModoActivity.this,ListaSurveyActivity.class);
                intent.putExtra(ExtrasID.EXTRA_MODO,ExtrasID.TIPO_SERVICIO_TRONCAL);
                startActivity(intent);
            }
        });

        alimentadorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeleccionModoActivity.this,ListaSurveyActivity.class);
                intent.putExtra(ExtrasID.EXTRA_MODO,ExtrasID.TIPO_SERVICIO_ALIMENTADOR);
                startActivity(intent);
            }
        });

        zonalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeleccionModoActivity.this,ListaSurveyActivity.class);
                intent.putExtra(ExtrasID.EXTRA_MODO,ExtrasID.TIPO_SERVICIO_ZONAL);
                startActivity(intent);
            }
        });

        troncalButton.setVisibility(View.INVISIBLE);
        alimentadorButton.setVisibility(View.INVISIBLE);
        zonalButton.setVisibility(View.INVISIBLE);

        if(modo.equals("tro")|| modo.equals("tro-od")){
            troncalButton.setVisibility(View.VISIBLE);
        }else if(modo.equals("ali")){
            alimentadorButton.setVisibility(View.VISIBLE);
        }else if(modo.equals("zon")){
            zonalButton.setVisibility(View.VISIBLE);
        }else{
            troncalButton.setVisibility(View.VISIBLE);
            alimentadorButton.setVisibility(View.VISIBLE);
            zonalButton.setVisibility(View.VISIBLE);
        }
    }
}
