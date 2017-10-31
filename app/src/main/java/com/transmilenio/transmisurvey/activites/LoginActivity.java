package com.transmilenio.transmisurvey.activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.util.ExtrasID;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    private EditText editTextUsuario,editTextContrasena;
    private Switch switchRecordar;
    private Button buttonIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        bindUI();
        createEvents();
        setcredentialsIfExist();
    }

    private void setcredentialsIfExist() {
        String user = prefs.getString(ExtrasID.EXTRA_USER,"");
        String pass = prefs.getString(ExtrasID.EXTRA_PASS,"");
        if(!TextUtils.isEmpty(user) && !TextUtils.isEmpty(pass)){
            editTextUsuario.setText(user);
            editTextContrasena.setText(pass);
        }
    }

    private void createEvents() {
        buttonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user =editTextUsuario.getText().toString();
                String pass = editTextContrasena.getText().toString();
               if( inicioSession(user,pass)){
                   goToMain();
                   saveOnPreferences(user,pass);
               }
            }
        });
    }

    private void bindUI(){
        editTextUsuario = (EditText) findViewById(R.id.lg_user_editText);
        editTextContrasena = (EditText) findViewById(R.id.lg_pass_editText);
        switchRecordar = (Switch) findViewById(R.id.lg_recordar_switch);
        buttonIngresar = (Button) findViewById(R.id.lg_iniciar_button);
    }

    private boolean inicioSession(String user,String password){
        if(!TextUtils.isEmpty(user) && !TextUtils.isEmpty(password)){
            return true;
        }else{
            Toast.makeText(this,"Información incompleta, Intente de nuevo",Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private void goToMain(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void saveOnPreferences(String user,String password){
        if(switchRecordar.isChecked()){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(ExtrasID.EXTRA_USER,user);
            editor.putString(ExtrasID.EXTRA_PASS,password);
            editor.apply();
        }
    }
}
