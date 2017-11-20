package com.transmilenio.transmisurvey.models.db;

import com.transmilenio.transmisurvey.app.MyApplication;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;



public class AdPuntoEncuesta extends RealmObject {

    @PrimaryKey
    private int id;

    //Abordo
    private String diaSemana;
    private String servicio;
    private String estacion;

    private RealmList<RegistroAdPunto> registros;


    public AdPuntoEncuesta() {
        this.id = MyApplication.encAsDsPuntoID.incrementAndGet();
    }

    public AdPuntoEncuesta(String diaSemana, String servicio, String estacion, String sentido) {
        this.diaSemana = diaSemana;
        this.servicio = servicio;
        this.estacion = estacion;
        this.id = MyApplication.encAsDsPuntoID.incrementAndGet();
        this.registros = new RealmList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public RealmList<RegistroAdPunto> getRegistros() {
        return registros;
    }

    public void setRegistros(RealmList<RegistroAdPunto> registros) {
        this.registros = registros;
    }
}
