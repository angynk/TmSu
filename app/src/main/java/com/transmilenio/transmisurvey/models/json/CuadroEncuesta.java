package com.transmilenio.transmisurvey.models.json;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CuadroEncuesta implements Serializable {

    private String nombre_encuesta;
    private String fecha_encuesta;
    private String dia_semana;
    private String servicio;
    private String num_bus;
    private String aforador;
    private int recorrido;
    private int num_puerta;
    private int id_realm;

    private List<RegistroEncuesta> registros;

    public CuadroEncuesta() {
    }

    public String getNombre_encuesta() {
        return nombre_encuesta;
    }

    public void setNombre_encuesta(String nombre_encuesta) {
        this.nombre_encuesta = nombre_encuesta;
    }

    public String getFecha_encuesta() {
        return fecha_encuesta;
    }

    public void setFecha_encuesta(String fecha_encuesta) {
        this.fecha_encuesta = fecha_encuesta;
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

    public List<RegistroEncuesta> getRegistros() {
        return registros;
    }

    public void setRegistros(List<RegistroEncuesta> registros) {
        this.registros = registros;
    }

    public int getId_realm() {
        return id_realm;
    }

    public void setId_realm(int id_realm) {
        this.id_realm = id_realm;
    }

    public String getAforador() {
        return aforador;
    }

    public void setAforador(String aforador) {
        this.aforador = aforador;
    }
}
