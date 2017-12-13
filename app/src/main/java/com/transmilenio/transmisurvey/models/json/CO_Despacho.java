package com.transmilenio.transmisurvey.models.json;

import java.util.List;

/**
 * Created by nataly on 12/12/2017.
 */

public class CO_Despacho {

    private String estacion;
    private String servicio;
    private List<RegCoDespachos> registros;

    public CO_Despacho() {
    }

    public CO_Despacho(String estacion, String servicio, List<RegCoDespachos> registros) {
        this.estacion = estacion;
        this.servicio = servicio;
        this.registros = registros;
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

    public List<RegCoDespachos> getRegistros() {
        return registros;
    }

    public void setRegistros(List<RegCoDespachos> registros) {
        this.registros = registros;
    }
}
