package com.transmilenio.transmisurvey.models.db;

import com.transmilenio.transmisurvey.app.MyApplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;



public class RegistroAdPunto extends RealmObject {

    @PrimaryKey
    private int id;
    private String numBus;
    private String servicio;
    private String horaLlegada;
    private String horaSalida;
    private int pasBajan;
    private int pasSuben;
    private int pasQuedan;
    private String observacion;

    public RegistroAdPunto() {
        this.id = MyApplication.regAsDsPuntoID.incrementAndGet();
    }

    public RegistroAdPunto(String numBus, String horaLlegada, String horaSalida, int pasBajan, int pasSuben) {
        this.numBus = numBus;
        this.horaLlegada = horaLlegada;
        this.horaSalida = horaSalida;
        this.pasBajan = pasBajan;
        this.pasSuben = pasSuben;
        this.id = MyApplication.regAsDsPuntoID.incrementAndGet();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumBus() {
        return numBus;
    }

    public void setNumBus(String numBus) {
        this.numBus = numBus;
    }

    public String getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(String horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }

    public int getPasBajan() {
        return pasBajan;
    }

    public void setPasBajan(int pasBajan) {
        this.pasBajan = pasBajan;
    }

    public int getPasSuben() {
        return pasSuben;
    }

    public void setPasSuben(int pasSuben) {
        this.pasSuben = pasSuben;
    }

    public int getPasQuedan() {
        return pasQuedan;
    }

    public void setPasQuedan(int pasQuedan) {
        this.pasQuedan = pasQuedan;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }
}
