package com.transmilenio.transmisurvey.models.json;


public class EncuestaJSON {

    // Datos Básicos
    private Integer tipo;
    private String nombre_encuesta;
    private String aforador;
    private String identificador;
    private String fecha_encuesta;
    private int id_realm;

    private AD_Abordo ad_abordo;
    private FR_Ocupacion fr_ocupacion;
    private AD_Fijo ad_fijo;
    private CO_Despacho co_despacho;

    public EncuestaJSON() {
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public String getNombre_encuesta() {
        return nombre_encuesta;
    }

    public void setNombre_encuesta(String nombre_encuesta) {
        this.nombre_encuesta = nombre_encuesta;
    }

    public String getAforador() {
        return aforador;
    }

    public void setAforador(String aforador) {
        this.aforador = aforador;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getFecha_encuesta() {
        return fecha_encuesta;
    }

    public void setFecha_encuesta(String fecha_encuesta) {
        this.fecha_encuesta = fecha_encuesta;
    }

    public int getId_realm() {
        return id_realm;
    }

    public void setId_realm(int id_realm) {
        this.id_realm = id_realm;
    }

    public AD_Abordo getAd_abordo() {
        return ad_abordo;
    }

    public void setAd_abordo(AD_Abordo ad_abordo) {
        this.ad_abordo = ad_abordo;
    }

    public FR_Ocupacion getFr_ocupacion() {
        return fr_ocupacion;
    }

    public void setFr_ocupacion(FR_Ocupacion fr_ocupacion) {
        this.fr_ocupacion = fr_ocupacion;
    }

    public AD_Fijo getAd_fijo() {
        return ad_fijo;
    }

    public void setAd_fijo(AD_Fijo ad_fijo) {
        this.ad_fijo = ad_fijo;
    }

    public CO_Despacho getCo_despacho() {
        return co_despacho;
    }

    public void setCo_despacho(CO_Despacho co_despacho) {
        this.co_despacho = co_despacho;
    }
}
