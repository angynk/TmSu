package com.transmilenio.transmisurvey.models.db;


import com.transmilenio.transmisurvey.app.MyApplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Estacion  extends RealmObject {

    @PrimaryKey
    private int id;

    private String nombre;

    public Estacion() {
        this.id = MyApplication.estacionID.incrementAndGet();
    }


    public Estacion(String nombre) {
        this.nombre = nombre;
        this.id = MyApplication.estacionID.incrementAndGet();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
