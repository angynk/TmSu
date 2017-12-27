package com.transmilenio.transmisurvey.models.db;

import com.transmilenio.transmisurvey.app.MyApplication;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class OrigenDestinoBase extends RealmObject {

    @PrimaryKey
    private int id;
    private String dia_semana;
    private String estacion;
    private RealmList<RegistroOD> registros;

    public OrigenDestinoBase() {
        this.id = MyApplication.orDesBaseID.incrementAndGet();
    }

    public OrigenDestinoBase( String dia_semana, String estacion) {
        this.id = MyApplication.orDesBaseID.incrementAndGet();
        this.dia_semana = dia_semana;
        this.estacion = estacion;
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

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public RealmList<RegistroOD> getRegistros() {
        return registros;
    }

    public void setRegistros(RealmList<RegistroOD> registros) {
        this.registros = registros;
    }
}
