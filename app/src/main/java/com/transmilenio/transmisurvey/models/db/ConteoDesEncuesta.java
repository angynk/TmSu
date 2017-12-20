package com.transmilenio.transmisurvey.models.db;

import com.transmilenio.transmisurvey.app.MyApplication;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;



public class ConteoDesEncuesta extends RealmObject {

    @PrimaryKey
    private int id;
    private String estacion;
    private String servicio;

    private RealmList<RegistroConteo> registros;

    public ConteoDesEncuesta() {
        this.id = MyApplication.conteoDespaID.incrementAndGet();
        this.registros = new RealmList<RegistroConteo>();
    }

    public ConteoDesEncuesta(String estacion, String servicio) {
        this.estacion = estacion;
        this.servicio = servicio;
        this.id = MyApplication.conteoDespaID.incrementAndGet();
        this.registros = new RealmList<RegistroConteo>();
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

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public RealmList<RegistroConteo> getRegistros() {
        return registros;
    }

    public void setRegistros(RealmList<RegistroConteo> registros) {
        this.registros = registros;
    }
}
