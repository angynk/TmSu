package com.transmilenio.transmisurvey.models.json;

public class RegistroEncuesta {

    private String estacion;
    private String hora_llegada;
    private int bajan;
    private int suban;
    private int quedan;
    private String hora_salida;
    private String observacion;

    public RegistroEncuesta() {
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
