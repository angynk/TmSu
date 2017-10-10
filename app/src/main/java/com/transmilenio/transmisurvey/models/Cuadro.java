package com.transmilenio.transmisurvey.models;

import com.transmilenio.transmisurvey.app.MyApplication;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


public class Cuadro extends RealmObject {

    @PrimaryKey
    private int id;
    private String fecha;
    private String diaSemana;
    private String servicio;
    private String numBus;
    private int recorrido;

    private RealmList<Registro> registros;

    public Cuadro() {
    }

    public Cuadro(String fecha, String diaSemana, String servicio, String numBus, int recorrido) {
        this.fecha = fecha;
        this.diaSemana = diaSemana;
        this.servicio = servicio;
        this.numBus = numBus;
        this.recorrido = recorrido;
        this.id = MyApplication.cuadroID.incrementAndGet();
        this.registros = new RealmList<Registro>();
    }

    public int getId() {
        return id;
    }


    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getNumBus() {
        return numBus;
    }

    public void setNumBus(String numBus) {
        this.numBus = numBus;
    }

    public int getRecorrido() {
        return recorrido;
    }

    public void setRecorrido(int recorrido) {
        this.recorrido = recorrido;
    }


    public RealmList<Registro> getRegistros() {
        return registros;
    }

    public void setRegistros(RealmList<Registro> registros) {
        this.registros = registros;
    }
}
