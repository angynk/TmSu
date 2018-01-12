package com.transmilenio.transmisurvey.models.db;

import com.transmilenio.transmisurvey.app.MyApplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;



public class RegistroFrecOcupaBus extends RealmObject {

    @PrimaryKey
    private int id;
    private String servicio;
    private String horaPaso;
    private Integer movBus;

    public RegistroFrecOcupaBus() {
        this.id = MyApplication.regfreOcupaBusID.incrementAndGet();
    }

    public RegistroFrecOcupaBus(String servicio, String horaPaso, Integer movBus) {
        this.id = MyApplication.regfreOcupaBusID.incrementAndGet();
        this.servicio = servicio;
        this.horaPaso = horaPaso;
        this.movBus = movBus;
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

    public String getHoraPaso() {
        return horaPaso;
    }

    public void setHoraPaso(String horaPaso) {
        this.horaPaso = horaPaso;
    }

    public Integer getMovBus() {
        return movBus;
    }

    public void setMovBus(Integer movBus) {
        this.movBus = movBus;
    }
}
