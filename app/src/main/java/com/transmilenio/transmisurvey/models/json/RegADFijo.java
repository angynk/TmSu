package com.transmilenio.transmisurvey.models.json;



public class RegADFijo {

    private String num_bus;
    private String hora_llegada;
    private String hora_salida;
    private int pas_bajan;
    private int pas_suben;
    private int pas_quedan;
    private String observacion;

    public RegADFijo() {
    }

    public RegADFijo(String num_bus, String hora_llegada, String hora_salida, int pas_bajan, int pas_suben) {
        this.num_bus = num_bus;
        this.hora_llegada = hora_llegada;
        this.hora_salida = hora_salida;
        this.pas_bajan = pas_bajan;
        this.pas_suben = pas_suben;
    }

    public String getNum_bus() {
        return num_bus;
    }

    public void setNum_bus(String num_bus) {
        this.num_bus = num_bus;
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

    public int getPas_bajan() {
        return pas_bajan;
    }

    public void setPas_bajan(int pas_bajan) {
        this.pas_bajan = pas_bajan;
    }

    public int getPas_suben() {
        return pas_suben;
    }

    public void setPas_suben(int pas_suben) {
        this.pas_suben = pas_suben;
    }

    public int getPas_quedan() {
        return pas_quedan;
    }

    public void setPas_quedan(int pas_quedan) {
        this.pas_quedan = pas_quedan;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
