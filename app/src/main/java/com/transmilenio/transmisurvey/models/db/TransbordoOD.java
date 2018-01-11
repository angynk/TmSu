package com.transmilenio.transmisurvey.models.db;

import com.transmilenio.transmisurvey.app.MyApplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;



public class TransbordoOD extends RealmObject {

    @PrimaryKey
    private int id;
    private String servicio;
    private String estacion;

    public TransbordoOD() {
        this.id = MyApplication.odTranRegID.incrementAndGet();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }
}
