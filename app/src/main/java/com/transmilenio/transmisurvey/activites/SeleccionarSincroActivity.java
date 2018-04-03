package com.transmilenio.transmisurvey.activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.util.ExtrasID;

public class SeleccionarSincroActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private Button modoButton,infoBaseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_sincro);
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        bindUI();
    }

    private void bindUI() {
        modoButton = (Button) findViewById(R.id.ses_modos_button);
        infoBaseButton = (Button) findViewById(R.id.ses_info_base_button);

        modoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        infoBaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeleccionarSincroActivity.this,ConfiguracionActivity.class);
                startActivity(intent);
            }
        });

    }


}
