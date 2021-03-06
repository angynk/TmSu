package com.transmilenio.transmisurvey.models.db;

import com.transmilenio.transmisurvey.app.MyApplication;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class OrigenDestinoBase extends RealmObject {

    @PrimaryKey
    private int id;
    private String estacion;
    private String tipo;
    private RealmList<RegistroOD> registros;

    public OrigenDestinoBase() {
        this.id = MyApplication.orDesBaseID.incrementAndGet();
    }

    public OrigenDestinoBase(  String estacion,String tipo) {
        this.id = MyApplication.orDesBaseID.incrementAndGet();
        this.estacion = estacion;
        this.tipo =tipo;
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

    public RealmList<RegistroOD> getRegistros() {
        return registros;
    }

    public void setRegistros(RealmList<RegistroOD> registros) {
        this.registros = registros;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
