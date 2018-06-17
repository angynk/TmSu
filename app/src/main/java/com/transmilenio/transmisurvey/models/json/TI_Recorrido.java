package com.transmilenio.transmisurvey.models.json;

import java.util.List;



public class TI_Recorrido {

    private String dia_semana;
    private String servicio;
    private String num_bus;
    private String observacion;
    private int recorrido;
    private List<RegRecorrido> registros;

    public TI_Recorrido() {
    }

    public String getDia_semana() {
        return dia_semana;
    }

    public void setDia_semana(String dia_semana) {
        this.dia_semana = dia_semana;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getNum_bus() {
        return num_bus;
    }

    public void setNum_bus(String num_bus) {
        this.num_bus = num_bus;
    }

    public int getRecorrido() {
        return recorrido;
    }

    public void setRecorrido(int recorrido) {
        this.recorrido = recorrido;
    }

    public List<RegRecorrido> getRegistros() {
        return registros;
    }

    public void setRegistros(List<RegRecorrido> registros) {
        this.registros = registros;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
