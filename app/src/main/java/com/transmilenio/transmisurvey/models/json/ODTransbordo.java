package com.transmilenio.transmisurvey.models.json;



public class ODTransbordo {

    private String estacion;
    private String servicio;

    public ODTransbordo() {
    }

    public ODTransbordo(String estacion, String servicio) {
        this.estacion = estacion;
        this.servicio = servicio;
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
}
