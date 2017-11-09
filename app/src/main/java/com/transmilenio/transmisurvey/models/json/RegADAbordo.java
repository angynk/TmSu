package com.transmilenio.transmisurvey.models.json;

/**
 * Created by nataly on 08/11/2017.
 */

public class RegADAbordo {


    private String hora_llegada;
    private int bajan;
    private int suben;
    private int quedan;
    private String hora_salida;
    private String observacion;


    public RegADAbordo() {
    }

    public String getHora_llegada() {
        return hora_llegada;
    }

    public void setHora_llegada(String hora_llegada) {
        this.hora_llegada = hora_llegada;
    }

    public int getBajan() {
        return bajan;
    }

    public void setBajan(int bajan) {
        this.bajan = bajan;
    }

    public int getSuben() {
        return suben;
    }

    public void setSuben(int suben) {
        this.suben = suben;
    }

    public int getQuedan() {
        return quedan;
    }

    public void setQuedan(int quedan) {
        this.quedan = quedan;
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
}
