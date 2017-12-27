package com.transmilenio.transmisurvey.models.db;

import com.transmilenio.transmisurvey.app.MyApplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nataly on 26/12/2017.
 */

public class RegistroOD extends RealmObject {

    @PrimaryKey
    private int id;
    private String servicioOrigen;
    private String estacionOrigen;
    private String servicioDestino;
    private String estacionDestino;

    public RegistroOD() {
        this.id = MyApplication.orDesRegID.incrementAndGet();
    }

    public RegistroOD(String servicioOrigen, String estacionOrigen, String servicioDestino, String estacionDestino) {
        this.servicioOrigen = servicioOrigen;
        this.estacionOrigen = estacionOrigen;
        this.servicioDestino = servicioDestino;
        this.estacionDestino = estacionDestino;
        this.id = MyApplication.orDesRegID.incrementAndGet();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServicioOrigen() {
        return servicioOrigen;
    }

    public void setServicioOrigen(String servicioOrigen) {
        this.servicioOrigen = servicioOrigen;
    }

    public String getEstacionOrigen() {
        return estacionOrigen;
    }

    public void setEstacionOrigen(String estacionOrigen) {
        this.estacionOrigen = estacionOrigen;
    }

    public String getServicioDestino() {
        return servicioDestino;
    }

    public void setServicioDestino(String servicioDestino) {
        this.servicioDestino = servicioDestino;
    }

    public String getEstacionDestino() {
        return estacionDestino;
    }

    public void setEstacionDestino(String estacionDestino) {
        this.estacionDestino = estacionDestino;
    }
}
