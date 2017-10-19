package com.transmilenio.transmisurvey.app;

import android.app.Application;

import com.transmilenio.transmisurvey.models.db.Cuadro;
import com.transmilenio.transmisurvey.models.db.Registro;

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

    @Override
    public void onCreate() {
        super.onCreate();

        setUpRealmConfig();
        Realm realm = Realm.getDefaultInstance();
        cuadroID = getIdByTable(realm, Cuadro.class);
        registroID = getIdByTable(realm, Registro.class);
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
