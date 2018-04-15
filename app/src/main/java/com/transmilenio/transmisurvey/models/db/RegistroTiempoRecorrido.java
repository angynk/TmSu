package com.transmilenio.transmisurvey.models.db;

import com.transmilenio.transmisurvey.app.MyApplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;



public class RegistroTiempoRecorrido extends RealmObject {

    @PrimaryKey
    private int id;

    private String estacion;
    private String hora_llegada;
    private String hora_salida;
    private String observacion;
    private boolean primera_zon_destino;

    public RegistroTiempoRecorrido() {
        this.id = MyApplication.regTRecorridoID.incrementAndGet();
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

    public String getHora_llegada() {
        return hora_llegada;
    }

    public void setHora_llegada(String hora_llegada) {
        this.hora_llegada = hora_llegada;
    }

    public String getHora_salida() {
        return hora_salida;
    }

    public void setHora_salida(String hora_salida) {
        this.hora_salida = hora_salida;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public boolean isPrimera_zon_destino() {
        return primera_zon_destino;
    }

    public void setPrimera_zon_destino(boolean primera_zon_destino) {
        this.primera_zon_destino = primera_zon_destino;
    }
}
