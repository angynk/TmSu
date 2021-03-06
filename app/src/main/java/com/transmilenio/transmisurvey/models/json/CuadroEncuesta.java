package com.transmilenio.transmisurvey.models.json;

import com.transmilenio.transmisurvey.app.MyApplication;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CuadroEncuesta extends RealmObject {

    @PrimaryKey
    private int id;

    private String dia_semana;
    private String servicio;
    private String num_bus;
    private int recorrido;
    private int num_puerta;

    private RealmList<RegistroEncuesta> registros;

    public CuadroEncuesta() {
        this.id = MyApplication.encAsDsAbordoID.incrementAndGet();
        this.registros = new RealmList<RegistroEncuesta>();
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

    public int getRecorrido() {
        return recorrido;
    }

    public void setRecorrido(int recorrido) {
        this.recorrido = recorrido;
    }

    public int getNum_puerta() {
        return num_puerta;
    }

    public void setNum_puerta(int num_puerta) {
        this.num_puerta = num_puerta;
    }

    public RealmList<RegistroEncuesta> getRegistros() {
        return registros;
    }

    public void setRegistros(RealmList<RegistroEncuesta> registros) {
        this.registros = registros;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
