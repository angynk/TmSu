package com.transmilenio.transmisurvey.models.json;

import java.util.List;



public class AD_Abordo {

    private String dia_semana;
    private String servicio;
    private String num_bus;
    private int recorrido;
    private int num_puerta;


    private List<RegADAbordo> registros;

    public AD_Abordo() {
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

    public int getNum_puerta() {
        return num_puerta;
    }

    public void setNum_puerta(int num_puerta) {
        this.num_puerta = num_puerta;
    }

    public List<RegADAbordo> getRegistros() {
        return registros;
    }

    public void setRegistros(List<RegADAbordo> registros) {
        this.registros = registros;
    }
}
