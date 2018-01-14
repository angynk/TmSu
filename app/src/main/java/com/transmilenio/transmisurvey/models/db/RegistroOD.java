package com.transmilenio.transmisurvey.models.db;

import com.transmilenio.transmisurvey.app.MyApplication;

import io.realm.RealmList;
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
    private String hora;
    private String estacionDestino;
    private String modoLlegada;
    private Integer numVeces;

    private RealmList<TransbordoOD> transbordos;

    public RegistroOD() {
        this.id = MyApplication.orDesRegID.incrementAndGet();
    }

    public RegistroOD(String servicioOrigen, String estacionOrigen, String estacionDestino) {
        this.servicioOrigen = servicioOrigen;
        this.estacionOrigen = estacionOrigen;
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

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEstacionDestino() {
        return estacionDestino;
    }

    public void setEstacionDestino(String estacionDestino) {
        this.estacionDestino = estacionDestino;
    }

    public RealmList<TransbordoOD> getTransbordos() {
        return transbordos;
    }

    public void setTransbordos(RealmList<TransbordoOD> transbordos) {
        this.transbordos = transbordos;
    }

    public String getModoLlegada() {
        return modoLlegada;
    }

    public void setModoLlegada(String modoLlegada) {
        this.modoLlegada = modoLlegada;
    }

    public Integer getNumVeces() {
        return numVeces;
    }

    public void setNumVeces(Integer numVeces) {
        this.numVeces = numVeces;
    }
}
