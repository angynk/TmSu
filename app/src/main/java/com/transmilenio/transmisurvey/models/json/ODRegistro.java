package com.transmilenio.transmisurvey.models.json;

import java.util.List;



public class ODRegistro {

    private String estacion_origen;
    private String estacion_destino;
    private String servicio_origen;
    private String hora_encuesta;
    private List<ODTransbordo> transbordos;

    public ODRegistro() {
    }

    public String getEstacion_origen() {
        return estacion_origen;
    }

    public void setEstacion_origen(String estacion_origen) {
        this.estacion_origen = estacion_origen;
    }

    public String getEstacion_destino() {
        return estacion_destino;
    }

    public void setEstacion_destino(String estacion_destino) {
        this.estacion_destino = estacion_destino;
    }

    public String getServicio_origen() {
        return servicio_origen;
    }

    public void setServicio_origen(String servicio_origen) {
        this.servicio_origen = servicio_origen;
    }

    public String getHora_encuesta() {
        return hora_encuesta;
    }

    public void setHora_encuesta(String hora_encuesta) {
        this.hora_encuesta = hora_encuesta;
    }

    public List<ODTransbordo> getTransbordos() {
        return transbordos;
    }

    public void setTransbordos(List<ODTransbordo> transbordos) {
        this.transbordos = transbordos;
    }
}
