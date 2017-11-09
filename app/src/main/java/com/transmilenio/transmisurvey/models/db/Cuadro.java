package com.transmilenio.transmisurvey.models.db;

import com.transmilenio.transmisurvey.app.MyApplication;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


public class Cuadro extends RealmObject {

    @PrimaryKey
    private int id;
    @Required
    private String fecha;

    //Abordo
    private String diaSemana;
    private String servicio;
    private String numBus;
    private int recorrido;
    private int numPuerta;

    private Date now;
    private String nombreEncuesta;
    private String aforador;

    //En punto fijo
    private String zona;
    private String estacion;
    private String sentido;

    private RealmList<Registro> registros;
    private RealmList<RegistroFrecOcupacion> registrosOcupacion;

    public Cuadro() {
        this.id = MyApplication.cuadroID.incrementAndGet();
    }

    public Cuadro(String fecha, String diaSemana, String servicio, String numBus, int recorrido,String nombreEncuesta) {
        this.fecha = fecha;
        this.diaSemana = diaSemana;
        this.servicio = servicio;
        this.numBus = numBus;
        this.recorrido = recorrido;
        this.id = MyApplication.cuadroID.incrementAndGet();
        this.registros = new RealmList<Registro>();
        this.now = new Date();
        this.nombreEncuesta = nombreEncuesta;
        this.registrosOcupacion = new RealmList<RegistroFrecOcupacion>();
    }

    public int getId() {
        return id;
    }


    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getNumBus() {
        return numBus;
    }

    public void setNumBus(String numBus) {
        this.numBus = numBus;
    }

    public int getRecorrido() {
        return recorrido;
    }

    public void setRecorrido(int recorrido) {
        this.recorrido = recorrido;
    }


    public RealmList<Registro> getRegistros() {
        return registros;
    }

    public void setRegistros(RealmList<Registro> registros) {
        this.registros = registros;
    }

    public Date getNow() {
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    public String getNombreEncuesta() {
        return nombreEncuesta;
    }

    public void setNombreEncuesta(String nombreEncuesta) {
        this.nombreEncuesta = nombreEncuesta;
    }

    public int getNumPuerta() {
        return numPuerta;
    }

    public void setNumPuerta(int numPuerta) {
        this.numPuerta = numPuerta;
    }

    public String getAforador() {
        return aforador;
    }

    public void setAforador(String aforador) {
        this.aforador = aforador;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
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

    public RealmList<RegistroFrecOcupacion> getRegistrosOcupacion() {
        return registrosOcupacion;
    }

    public void setRegistrosOcupacion(RealmList<RegistroFrecOcupacion> registrosOcupacion) {
        this.registrosOcupacion = registrosOcupacion;
    }
}
