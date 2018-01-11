package com.transmilenio.transmisurvey.models.json;

import java.util.List;



public class ODEncuesta {

    private String estacion;
    private String tipo;
    private List<ODRegistro> registros;

    public ODEncuesta() {
    }

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<ODRegistro> getRegistros() {
        return registros;
    }

    public void setRegistros(List<ODRegistro> registros) {
        this.registros = registros;
    }
}
