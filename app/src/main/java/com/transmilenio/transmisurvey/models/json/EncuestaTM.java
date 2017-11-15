package com.transmilenio.transmisurvey.models.json;

import com.transmilenio.transmisurvey.app.MyApplication;
import com.transmilenio.transmisurvey.models.db.FOcupacionEncuesta;



import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class EncuestaTM extends RealmObject {

    @PrimaryKey
    private int id;

    // Datos Básicos
    private Integer tipo;
    private String nombre_encuesta;
    private String aforador;
    private String identificador;
    private String fecha_encuesta;
    private int id_realm;

    //Datos por encuesta
    private CuadroEncuesta ad_abordo; // ASCENSOS Y DESCENSOS TRONCAL ABORDO
    private FOcupacionEncuesta fr_ocupacion; // FRECUENCIA OCUPACIÓN

    public EncuestaTM() {
        this.id = MyApplication.encuestasTmID.incrementAndGet();
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

    public CuadroEncuesta getAd_abordo() {
        return ad_abordo;
    }

    public void setAd_abordo(CuadroEncuesta ad_abordo) {
        this.ad_abordo = ad_abordo;
    }

    public FOcupacionEncuesta getFr_ocupacion() {
        return fr_ocupacion;
    }

    public void setFr_ocupacion(FOcupacionEncuesta fr_ocupacion) {
        this.fr_ocupacion = fr_ocupacion;
    }

    public int getId_realm() {
        return id_realm;
    }

    public void setId_realm(int id_realm) {
        this.id_realm = id_realm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}