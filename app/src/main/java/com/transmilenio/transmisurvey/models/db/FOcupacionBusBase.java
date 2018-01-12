package com.transmilenio.transmisurvey.models.db;

import com.transmilenio.transmisurvey.app.MyApplication;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nataly on 11/01/2018.
 */

public class FOcupacionBusBase extends RealmObject {

    @PrimaryKey
    private int id;

    private String estacion;

    private String sentido;

    private RealmList<RegistroFrecOcupaBus> registros;

    public FOcupacionBusBase(String estacion, String sentido, RealmList<RegistroFrecOcupaBus> registros) {
        this.estacion = estacion;
        this.sentido = sentido;
        this.registros = registros;
        this.id = MyApplication.freOcupaBusID.incrementAndGet();
        this.registros = new RealmList<RegistroFrecOcupaBus>();
    }

    public FOcupacionBusBase() {
        this.id = MyApplication.freOcupaBusID.incrementAndGet();
        this.registros = new RealmList<RegistroFrecOcupaBus>();
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

    public RealmList<RegistroFrecOcupaBus> getRegistros() {
        return registros;
    }

    public void setRegistros(RealmList<RegistroFrecOcupaBus> registros) {
        this.registros = registros;
    }
}
