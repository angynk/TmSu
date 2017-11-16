package com.transmilenio.transmisurvey.models.db;

import com.transmilenio.transmisurvey.app.MyApplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class RegistroFrecOcupacion extends RealmObject {

    @PrimaryKey
    private int id;
    private String servicio;
    private String hora_paso;
    private Integer ocupacion;

    public RegistroFrecOcupacion() {
        this.id = MyApplication.registroFrecOcuID.incrementAndGet();
    }

    public RegistroFrecOcupacion(String servicio, String hora_paso, Integer ocupacion) {
        this.servicio = servicio;
        this.hora_paso = hora_paso;
        this.ocupacion = ocupacion;
        this.id = MyApplication.registroFrecOcuID.incrementAndGet();
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getHora_paso() {
        return hora_paso;
    }

    public void setHora_paso(String hora_paso) {
        this.hora_paso = hora_paso;
    }

    public Integer getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(Integer ocupacion) {
        this.ocupacion = ocupacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
