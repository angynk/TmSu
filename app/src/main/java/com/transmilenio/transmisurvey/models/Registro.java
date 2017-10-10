package com.transmilenio.transmisurvey.models;

import com.transmilenio.transmisurvey.app.MyApplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


public class Registro extends RealmObject{

    @PrimaryKey
    private int id;
    private String estacion;
    private String horaLlegada;
    private int bajan;
    private int suban;
    private int quedan;
    private String horaSalida;

    public Registro() {
        this.id = MyApplication.registroID.incrementAndGet();
    }

    public Registro(String estacion, String horaLlegada, int bajan, int suban, int quedan, String horaSalida) {
        this.id = MyApplication.registroID.incrementAndGet();
        this.estacion = estacion;
        this.horaLlegada = horaLlegada;
        this.bajan = bajan;
        this.suban = suban;
        this.quedan = quedan;
        this.horaSalida = horaSalida;
    }

    public int getId() {
        return id;
    }

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public String getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(String horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

    public int getBajan() {
        return bajan;
    }

    public void setBajan(int bajan) {
        this.bajan = bajan;
    }

    public int getSuban() {
        return suban;
    }

    public void setSuban(int suban) {
        this.suban = suban;
    }

    public int getQuedan() {
        return quedan;
    }

    public void setQuedan(int quedan) {
        this.quedan = quedan;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }
}
