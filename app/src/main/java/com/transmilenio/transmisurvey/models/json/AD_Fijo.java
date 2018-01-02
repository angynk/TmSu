package com.transmilenio.transmisurvey.models.json;


import java.util.List;


public class AD_Fijo {

    private String dia_semana;

    private String estacion;

    private List<RegADFijo> registros;

    public AD_Fijo() {
    }

    public AD_Fijo(String dia_semana,  String estacion) {
        this.dia_semana = dia_semana;
        this.estacion = estacion;
    }

    public String getDia_semana() {
        return dia_semana;
    }

    public void setDia_semana(String dia_semana) {
        this.dia_semana = dia_semana;
    }

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public List<RegADFijo> getRegistros() {
        return registros;
    }

    public void setRegistros(List<RegADFijo> registros) {
        this.registros = registros;
    }
}
