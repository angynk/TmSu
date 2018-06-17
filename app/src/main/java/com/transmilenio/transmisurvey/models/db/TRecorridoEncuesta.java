package com.transmilenio.transmisurvey.models.db;


import com.transmilenio.transmisurvey.app.MyApplication;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TRecorridoEncuesta extends RealmObject {

    @PrimaryKey
    private int id;

    private String dia_semana;
    private String servicio;
    private String num_bus;
    private String observacion;
    private int recorrido;

    private RealmList<RegistroTiempoRecorrido> registros;


    public TRecorridoEncuesta() {
        this.id = MyApplication.encTRecorridoID.incrementAndGet();
        registros = new RealmList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDia_semana() {
        return dia_semana;
    }

    public void setDia_semana(String dia_semana) {
        this.dia_semana = dia_semana;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getNum_bus() {
        return num_bus;
    }

    public void setNum_bus(String num_bus) {
        this.num_bus = num_bus;
    }

    public RealmList<RegistroTiempoRecorrido> getRegistros() {
        return registros;
    }

    public void setRegistros(RealmList<RegistroTiempoRecorrido> registros) {
        this.registros = registros;
    }

    public int getRecorrido() {
        return recorrido;
    }

    public void setRecorrido(int recorrido) {
        this.recorrido = recorrido;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
