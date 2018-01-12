package com.transmilenio.transmisurvey.models.json;

import java.util.List;

/**
 * Created by nataly on 11/01/2018.
 */

public class FR_Bus {

    private String estacion;
    private String sentido;
    private List<RegFROcupacionBus> registros;

    public FR_Bus() {
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

    public List<RegFROcupacionBus> getRegistros() {
        return registros;
    }

    public void setRegistros(List<RegFROcupacionBus> registros) {
        this.registros = registros;
    }
}
