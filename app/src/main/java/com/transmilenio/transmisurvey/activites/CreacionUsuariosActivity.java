package com.transmilenio.transmisurvey.activites;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.adapters.UsuariosAdapter;
import com.transmilenio.transmisurvey.fragments.AlertNuevoUsuario;
import com.transmilenio.transmisurvey.models.db.Aforador;
import com.transmilenio.transmisurvey.models.db.Cuadro;
import com.transmilenio.transmisurvey.models.db.Registro;
import com.transmilenio.transmisurvey.models.util.Mensajes;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class CreacionUsuariosActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<Aforador>> {

    private Button buttonCrear,buttonEliminar;
    private ListView usuariosListView;
    private RealmResults<Aforador> usuariosLista;
    private UsuariosAdapter usuariosAdapter;

    private Realm realm;
    FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creacion_usuarios);
        realm = Realm.getDefaultInstance();
        cargarUsuarios();
        bindUI();
        eventosBotones();
    }

    private void cargarUsuarios() {
        usuariosLista = realm.where(Aforador.class).findAll();
    }

    private void eventosBotones() {

        buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarUsuarios();
            }


        });

        buttonCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertNuevoUsuario alertNuevoUsuario = new AlertNuevoUsuario();
                alertNuevoUsuario.show(fm, Mensajes.MSG_CREAR_USUARIO);
            }
        });
    }



    private void eliminarUsuarios() {
        realm.beginTransaction();
        RealmResults<Aforador> rows = realm.where(Aforador.class).findAll();
        rows.deleteAllFromRealm();
        realm.commitTransaction();
        Toast.makeText(this,"Usuarios Eliminados",Toast.LENGTH_LONG).show();
        usuariosAdapter.notifyDataSetChanged();
    }

    private void bindUI() {
        buttonCrear = (Button) findViewById(R.id.cu_crear_button);
        buttonEliminar = (Button) findViewById(R.id.cu_eliminar_button);
        usuariosListView = (ListView) findViewById(R.id.cu_usuarios_listView);
        usuariosAdapter = new UsuariosAdapter(this,usuariosLista,R.layout.list_view_usuarios);
        usuariosListView.setAdapter(usuariosAdapter);
    }

    @Override
    public void onChange(RealmResults<Aforador> aforadors) {
        usuariosAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
