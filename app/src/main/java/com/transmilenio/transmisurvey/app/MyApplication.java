package com.transmilenio.transmisurvey.app;

import android.app.Application;

import com.transmilenio.transmisurvey.models.db.AdPuntoEncuesta;
import com.transmilenio.transmisurvey.models.db.Aforador;
import com.transmilenio.transmisurvey.models.db.Cuadro;
import com.transmilenio.transmisurvey.models.db.Estacion;
import com.transmilenio.transmisurvey.models.db.Registro;
import com.transmilenio.transmisurvey.models.db.RegistroAdPunto;
import com.transmilenio.transmisurvey.models.db.RegistroFrecOcupacion;
import com.transmilenio.transmisurvey.models.db.ServicioRutas;
import com.transmilenio.transmisurvey.models.json.CuadroEncuesta;
import com.transmilenio.transmisurvey.models.json.EncuestaTM;
import com.transmilenio.transmisurvey.models.json.RegistroEncuesta;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by nataly on 04/10/2017.
 */

public class MyApplication extends Application {

    public static AtomicInteger cuadroID = new AtomicInteger();
    public static AtomicInteger registroID = new AtomicInteger();
    public static AtomicInteger registroFrecOcuID = new AtomicInteger();
    public static AtomicInteger servicioID = new AtomicInteger();
    public static AtomicInteger estacionID = new AtomicInteger();
    public static AtomicInteger aforadorID = new AtomicInteger();
    public static AtomicInteger encuestasTmID = new AtomicInteger();
    public static AtomicInteger encAsDsAbordoID = new AtomicInteger();
    public static AtomicInteger regAsDsAbordoID = new AtomicInteger();
    public static AtomicInteger encAsDsPuntoID = new AtomicInteger();
    public static AtomicInteger regAsDsPuntoID = new AtomicInteger();

    @Override
    public void onCreate() {
        super.onCreate();

        setUpRealmConfig();
        Realm realm = Realm.getDefaultInstance();
        cuadroID = getIdByTable(realm, Cuadro.class);
        registroID = getIdByTable(realm, Registro.class);
        servicioID = getIdByTable(realm, ServicioRutas.class);
        estacionID = getIdByTable(realm, Estacion.class);
        aforadorID = getIdByTable(realm, Aforador.class);
        registroFrecOcuID = getIdByTable(realm, RegistroFrecOcupacion.class);
        encuestasTmID = getIdByTable(realm, EncuestaTM.class);
        encAsDsAbordoID = getIdByTable(realm, CuadroEncuesta.class);
        regAsDsAbordoID = getIdByTable(realm, RegistroEncuesta.class);
        encAsDsPuntoID = getIdByTable(realm, AdPuntoEncuesta.class);
        regAsDsPuntoID = getIdByTable(realm, RegistroAdPunto.class);
        realm.close();
    }

    private void setUpRealmConfig(){
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("myrealm.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    private <T extends RealmObject> AtomicInteger getIdByTable(Realm realm,Class<T> anyClass){
        RealmResults<T> result = realm.where(anyClass).findAll();
        return (result.size()>0) ? new AtomicInteger(result.max("id").intValue()): new AtomicInteger()  ;
    }
}
