package com.transmilenio.transmisurvey.models.db;

import com.transmilenio.transmisurvey.app.MyApplication;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class ServicioRutas extends RealmObject {

    @PrimaryKey
    private int id;

    private String nombre;
    private RealmList<Estacion> estaciones;

    public ServicioRutas() {
        this.id = MyApplication.servicioID.incrementAndGet();
        estaciones = new RealmList<Estacion>();
    }

    public ServicioRutas(String nombre) {
        this.nombre = nombre;
        this.id = MyApplication.servicioID.incrementAndGet();
        estaciones = new RealmList<Estacion>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public RealmList<Estacion> getEstaciones() {
        return estaciones;
    }

    public void setEstaciones(RealmList<Estacion> estaciones) {
        this.estaciones = estaciones;
    }
}