package com.transmilenio.transmisurvey.activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.db.Aforador;
import com.transmilenio.transmisurvey.models.db.Cuadro;

import io.realm.Realm;
import io.realm.RealmResults;

public class CreacionUsuariosActivity extends AppCompatActivity {

    private Button buttonNuevo,buttonCrear,buttonEliminar;
    private EditText userEditText,passEditText;
    private LinearLayout nuevoUsuarioLayout;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creacion_usuarios);
        realm = Realm.getDefaultInstance();
        bindUI();
        eventosBotones();
    }

    private void eventosBotones() {
        buttonNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuevoUsuarioLayout.setVisibility(View.VISIBLE);
            }
        });

        buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarUsuarios();
            }


        });

        buttonCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarUsuario();
            }
        });
    }

    private void agregarUsuario() {
        String user = userEditText.getText().toString();
        String pass = passEditText.getText().toString();
        if(!TextUtils.isEmpty(user) &&
                !TextUtils.isEmpty(pass)){
            realm.beginTransaction();
            Aforador nuevoAforador = new Aforador(user,pass);
            realm.copyToRealm(nuevoAforador);
            realm.commitTransaction();
            setVisibilidadInicial();
            Toast.makeText(this,"Nuevo Usuario Creado",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Información incompleta, Intente de nuevo",Toast.LENGTH_LONG).show();
        }
    }

    private void eliminarUsuarios() {
        realm.beginTransaction();
        RealmResults<Aforador> rows = realm.where(Aforador.class).findAll();
        rows.deleteAllFromRealm();
        realm.commitTransaction();
        Toast.makeText(this,"Usuarios Eliminados",Toast.LENGTH_LONG).show();
    }

    private void bindUI() {
        userEditText = (EditText) findViewById(R.id.nu_user_editText);
        passEditText = (EditText) findViewById(R.id.nu_pass_editText);
        buttonNuevo = (Button) findViewById(R.id.nu_nuevo_button);
        buttonCrear = (Button) findViewById(R.id.nu_crear_button);
        buttonEliminar = (Button) findViewById(R.id.nu_eliminar_button);
        nuevoUsuarioLayout = (LinearLayout) findViewById(R.id.nu_usuario_linearLayout);
        setVisibilidadInicial();
    }

    private void setVisibilidadInicial() {
        nuevoUsuarioLayout.setVisibility(View.INVISIBLE);
    }
}
