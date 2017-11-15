package com.transmilenio.transmisurvey.models.db;

import com.transmilenio.transmisurvey.app.MyApplication;
import com.transmilenio.transmisurvey.models.json.RegistroEncuesta;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;



public class FOcupacionEncuesta extends RealmObject {

    @PrimaryKey
    private int id;

    private String estacion;

    private String sentido;

    private RealmList<RegistroFrecOcupacion> registros;

    public FOcupacionEncuesta() {
        this.id = MyApplication.registroFrecOcuID.incrementAndGet();
        this.registros = new RealmList<RegistroFrecOcupacion>();
    }


    public FOcupacionEncuesta(String estacion, String sentido, RealmList<RegistroFrecOcupacion> registros) {
        this.estacion = estacion;
        this.sentido = sentido;
        this.registros = registros;
        this.id = MyApplication.registroFrecOcuID.incrementAndGet();
        this.registros = new RealmList<RegistroFrecOcupacion>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public String getSentido() {
        return sentido;
    }

    public void setSentido(String sentido) {
        this.sentido = sentido;
    }

    public RealmList<RegistroFrecOcupacion> getRegistros() {
        return registros;
    }

    public void setRegistros(RealmList<RegistroFrecOcupacion> registros) {
        this.registros = registros;
    }
}
