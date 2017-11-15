package com.transmilenio.transmisurvey.activites;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.util.ExtrasID;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

public class FrecRegistroActivity extends AppCompatActivity {


    private EditText horaPasoEditText, ocupacionEditText, codigoEditText;
    private FloatingActionButton buttonNuevo,buttonCerrar;

    private Realm realm;

    public FrecRegistroActivity() {
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frec_registro);
        bindUI();

    }

    private void bindUI() {
        horaPasoEditText = (EditText) findViewById(R.id.fre_hora_paso_editText);
        ocupacionEditText = (EditText) findViewById(R.id.fre_ocupacion_editText);
        codigoEditText = (EditText) findViewById(R.id.fre_codigo_editText);
        buttonNuevo = (FloatingActionButton) findViewById(R.id.fre_nuevoRegistro_button);
        buttonCerrar = (FloatingActionButton) findViewById(R.id.fre_cancelarRegistro_button);
        agregarEventosBotones();

      horaPasoEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getAction() != KeyEvent.ACTION_DOWN) {
                    return false;
                } else if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    String currentTime = sdf.format(new Date());
                    horaPasoEditText.setText(currentTime);
                    return false;
                }
                    return true;
            }
        });

    }

    private void agregarEventosBotones() {
        buttonNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(FrecRegistroActivity.this,MainActivity.class);
                    startActivity(intent);

            }
        });

        buttonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FrecRegistroActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

//        horaPasoEditText.set
    }

}
