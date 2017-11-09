package com.transmilenio.transmisurvey.models.db;

import com.transmilenio.transmisurvey.app.MyApplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class RegistroFrecOcupacion extends RealmObject {

    @PrimaryKey
    private int id;
    private String servicio;
    private String horaPaso;
    private Integer ocupacion;

    public RegistroFrecOcupacion() {
        this.id = MyApplication.registroFrecOcuID.incrementAndGet();
    }

    public RegistroFrecOcupacion(String servicio, String horaPaso, Integer ocupacion) {
        this.servicio = servicio;
        this.horaPaso = horaPaso;
        this.ocupacion = ocupacion;
        this.id = MyApplication.registroFrecOcuID.incrementAndGet();
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getHoraPaso() {
        return horaPaso;
    }

    public void setHoraPaso(String horaPaso) {
        this.horaPaso = horaPaso;
    }

    public Integer getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(Integer ocupacion) {
        this.ocupacion = ocupacion;
    }
}
