package com.transmilenio.transmisurvey.models.db;

import com.transmilenio.transmisurvey.app.MyApplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Modo extends RealmObject {


    @PrimaryKey
    private int id;
    private String nombre;
    private String abreviatura;
    private String descripcion;

    public Modo(String nombre, String abreviatura, String descripcion) {
        this.nombre = nombre;
        this.abreviatura = abreviatura;
        this.descripcion = descripcion;
        this.id = MyApplication.modoID.incrementAndGet();
    }

    public Modo() {
        this.id = MyApplication.modoID.incrementAndGet();
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

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
