package com.transmilenio.transmisurvey.models.json;

import java.util.List;

/**
 * Created by nataly on 14/11/2017.
 */

public class FR_Ocupacion {

    private String estacion;
    private String sentido;
    private List<RegFROcupacion> registros;

    public FR_Ocupacion() {
    }

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public String getSentido() {
        return sentido;
    }

    public void setSentido(String sentido) {
        this.sentido = sentido;
    }

    public List<RegFROcupacion> getRegistros() {
        return registros;
    }

    public void setRegistros(List<RegFROcupacion> registros) {
        this.registros = registros;
    }
}
